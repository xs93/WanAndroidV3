package com.github.xs93.wan.music

import android.content.ComponentName
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.github.xs93.core.AppInject
import com.github.xs93.wan.common.service.IMusicService
import com.github.xs93.wan.music.service.MusicPlayService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.therouter.inject.ServiceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/3/27 16:40
 * @description
 * 
 */
@ServiceProvider
class MusicServiceImpl : IMusicService {

    private val _playState = MutableStateFlow(IMusicService.PlayState())
    override val playState: StateFlow<IMusicService.PlayState> = _playState.asStateFlow()

    private var _controllerFuture: ListenableFuture<MediaController>? = null
    private var _controller: MediaController? = null
    private val _musicList = Collections.synchronizedList(mutableListOf<IMusicService.MusicInfo>())
    private var progressUpdateJob: Job? = null

    init {
        initController()
    }

    private fun initController() {
        val context = AppInject.getApp()
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayService::class.java))
        _controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        _controllerFuture?.addListener({
            try {
                _controller = _controllerFuture?.get()
                _controller?.addListener(PlayerListener())
                handlePendingOperations()
                startProgressUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
                updateState {
                    copy(status = IMusicService.PlayStatus.Error(e.message ?: "Unknown error", -1))
                }
            }
        }, MoreExecutors.directExecutor())
    }


    private fun handlePendingOperations() {
        AppInject.mainScope.launch {
            if (_musicList.isNotEmpty() && !isMediaItemsSet()) {
                setMediaItemsInternal(_musicList)
            }
        }
    }

    private fun isMediaItemsSet(): Boolean {
        return (_controller?.mediaItemCount ?: 0) > 0
    }

    private fun startProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = AppInject.mainScope.launch {
            while (true) {
                delay(500)
                _controller?.let { controller ->
                    if (controller.isPlaying) {
                        updateState {
                            copy(
                                currentPosition = controller.currentPosition,
                                duration = controller.duration
                            )
                        }
                    }
                }
            }
        }
    }

    override fun setMusicList(musicList: List<IMusicService.MusicInfo>) {
        if (musicList.isEmpty()) {
            return
        }

        _musicList.clear()
        _musicList.addAll(musicList)

        setMediaItemsInternal(musicList)

        val firstMusic = _musicList.firstOrNull()
        updateState {
            copy(
                playlist = _musicList.toList(),
                currentMusicInfo = firstMusic,
                currentMediaIndex = 0,
                duration = firstMusic?.duration ?: 0L
            )
        }
    }

    override fun play() {
        _controller?.play()
    }

    override fun playSingle(
        musicInfo: IMusicService.MusicInfo,
        strategy: IMusicService.PlayStrategy
    ) {
        when (strategy) {
            is IMusicService.PlayStrategy.ClearAndPlay -> {
                // 清空列表，只播放这一个
                _musicList.clear()
                _musicList.add(musicInfo)
                setMediaItemsInternal(listOf(musicInfo))
                updateState {
                    copy(
                        playlist = listOf(musicInfo),
                        currentMusicInfo = musicInfo,
                        currentMediaIndex = 0,
                        currentPosition = 0L,
                        duration = musicInfo.duration
                    )
                }
                _controller?.play()
            }

            is IMusicService.PlayStrategy.AddToHead -> {
                // 检查是否已存在
                val existingIndex = _musicList.indexOfFirst { it.uri == musicInfo.uri }

                if (existingIndex >= 0) {
                    // 已存在，直接播放
                    playAt(existingIndex)
                } else {
                    // 添加到第一个位置
                    _musicList.add(0, musicInfo)
                    setMediaItemsInternal(_musicList)

                    updateState {
                        copy(
                            playlist = _musicList.toList(),
                            currentMusicInfo = musicInfo,
                            currentMediaIndex = 0,
                            currentPosition = 0L,
                            duration = musicInfo.duration
                        )
                    }
                    _controller?.seekToDefaultPosition(0)
                    _controller?.play()
                }
            }

            is IMusicService.PlayStrategy.AddToTail -> {
                // 检查是否已存在
                val existingIndex = _musicList.indexOfFirst { it.uri == musicInfo.uri }

                if (existingIndex >= 0) {
                    // 已存在，直接播放
                    playAt(existingIndex)
                } else {
                    // 添加到末尾
                    val newIndex = _musicList.size
                    _musicList.add(musicInfo)
                    setMediaItemsInternal(_musicList)

                    updateState {
                        copy(
                            playlist = _musicList.toList(),
                            currentMusicInfo = musicInfo,
                            currentMediaIndex = newIndex,
                            currentPosition = 0L,
                            duration = musicInfo.duration
                        )
                    }
                    _controller?.seekToDefaultPosition(newIndex)
                    _controller?.play()
                }
            }

            is IMusicService.PlayStrategy.SkipIfNotExist -> {
                // 检查是否存在，不存在则跳过
                val existingIndex = _musicList.indexOfFirst { it.uri == musicInfo.uri }

                if (existingIndex >= 0) {
                    playAt(existingIndex)
                } else {
                    // 不在列表中，跳过不播放
                    return
                }
            }

            is IMusicService.PlayStrategy.ReplaceAt -> {
                val index = strategy.index

                if (index in _musicList.indices) {
                    // 替换指定位置的音乐
                    _musicList[index] = musicInfo
                    setMediaItemsInternal(_musicList)

                    updateState {
                        copy(
                            playlist = _musicList.toList(),
                            currentMusicInfo = musicInfo,
                            currentMediaIndex = index,
                            currentPosition = 0L,
                            duration = musicInfo.duration
                        )
                    }
                    _controller?.seekToDefaultPosition(index)
                    _controller?.play()
                } else {
                    // 索引无效，回退到 ClearAndPlay
                    playSingle(musicInfo, IMusicService.PlayStrategy.ClearAndPlay)
                }
            }

            is IMusicService.PlayStrategy.PlayNext -> {
                // 检查是否已存在
                val existingIndex = _musicList.indexOfFirst { it.uri == musicInfo.uri }

                if (existingIndex >= 0) {
                    // 已存在，直接播放
                    playAt(existingIndex)
                } else {
                    // 在当前播放位置之后插入
                    val currentIndex = getCurrentMediaIndex()
                    val insertIndex = currentIndex + 1

                    if (insertIndex <= _musicList.size) {
                        _musicList.add(insertIndex, musicInfo)
                        setMediaItemsInternal(_musicList)

                        updateState {
                            copy(
                                playlist = _musicList.toList(),
                                currentMusicInfo = musicInfo,
                                currentMediaIndex = insertIndex,
                                currentPosition = 0L,
                                duration = musicInfo.duration
                            )
                        }
                        _controller?.seekToDefaultPosition(insertIndex)
                        _controller?.play()
                    } else {
                        // 如果当前是最后一首，添加到末尾
                        playSingle(musicInfo, IMusicService.PlayStrategy.AddToTail)
                    }
                }
            }
        }
    }

    override fun playAt(index: Int) {
        if (index < 0 || index >= _musicList.size) {
            return
        }
        _controller?.seekToDefaultPosition(index)
        _controller?.play()
        val musicInfo = getMusicInfoAt(index)
        updateState {
            copy(
                currentMediaIndex = index,
                currentMusicInfo = musicInfo,
                currentPosition = 0L,
                duration = musicInfo?.duration ?: _controller?.duration ?: 0L
            )
        }
    }

    override fun pause() {
        _controller?.pause()
    }

    override fun playNext() {
        _controller?.seekToNext()
        _controller?.play()
    }

    override fun playPrevious() {
        _controller?.seekToPrevious()
        _controller?.play()
    }

    override fun setRepeatMode(mode: IMusicService.RepeatMode) {
        _controller?.repeatMode = mode.value
        updateState { copy(repeatMode = mode) }
    }

    override fun getRepeatMode(): IMusicService.RepeatMode {
        val modeValue = _controller?.repeatMode ?: Player.REPEAT_MODE_OFF
        return IMusicService.RepeatMode.fromValue(modeValue)
    }

    override fun setShuffleEnabled(enabled: Boolean) {
        _controller?.shuffleModeEnabled = enabled
        updateState { copy(isShuffleEnabled = enabled) }
    }

    override fun isShuffleEnabled(): Boolean {
        return _controller?.shuffleModeEnabled ?: false
    }

    override fun seekTo(positionMs: Long) {
        _controller?.seekTo(positionMs)
        updateState { copy(currentPosition = positionMs) }
    }

    override fun getCurrentPosition(): Long {
        return _controller?.currentPosition ?: 0L
    }

    override fun getDuration(): Long {
        return _controller?.duration ?: 0L
    }

    override fun getCurrentMediaIndex(): Int {
        return _controller?.currentMediaItemIndex ?: 0
    }

    override fun getCurrentMusicInfo(): IMusicService.MusicInfo? {
        val index = getCurrentMediaIndex()
        return getMusicInfoAt(index)
    }

    override fun getMusicInfoAt(index: Int): IMusicService.MusicInfo? {
        return if (index in _musicList.indices) {
            _musicList[index]
        } else {
            null
        }
    }

    override fun getMusicListSize(): Int {
        return _musicList.size
    }

    override fun clear() {
        pause()
        seekTo(0L)

        _musicList.clear()

        updateState {
            IMusicService.PlayState()
        }

        _controller?.clearMediaItems()
    }

    fun release() {
        try {
            progressUpdateJob?.cancel()
            _controller?.removeListener(PlayerListener())
            _controllerFuture?.run {
                MediaController.releaseFuture(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _controller = null
            _controllerFuture = null
            _musicList.clear()
            _playState.value = IMusicService.PlayState()
        }
    }

    private fun setMediaItemsInternal(musicList: List<IMusicService.MusicInfo>) {
        if (_controller == null) {
            return
        }
        val mediaItems = musicList.mapIndexed { index, musicInfo ->
            MediaItem.Builder()
                .setUri(musicInfo.uri)
                .setMediaId("music_$index")
                .build()
        }
        _controller?.setMediaItems(mediaItems)
    }

    private inline fun updateState(crossinline block: IMusicService.PlayState.() -> IMusicService.PlayState) {
        _playState.update { currentState ->
            currentState.block()
        }
    }

    private fun mapPlayerState(playerState: Int): IMusicService.PlayStatus {
        return when (playerState) {
            Player.STATE_IDLE -> IMusicService.PlayStatus.Idle
            Player.STATE_BUFFERING -> IMusicService.PlayStatus.Buffering()
            Player.STATE_READY -> IMusicService.PlayStatus.Ready
            Player.STATE_ENDED -> IMusicService.PlayStatus.Ended
            else -> IMusicService.PlayStatus.Error("Unknown player state", -2)
        }
    }

    private inner class PlayerListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val status = mapPlayerState(playbackState)
            updateState {
                copy(status = status)
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateState { copy(isPlaying = isPlaying) }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val newIndex = _controller?.currentMediaItemIndex ?: 0
            val newMusicInfo = getMusicInfoAt(newIndex)
            updateState {
                copy(
                    currentMediaIndex = newIndex,
                    currentPosition = 0L,
                    currentMusicInfo = newMusicInfo,
                    duration = newMusicInfo?.duration ?: _controller?.duration ?: 0L
                )
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            updateState {
                copy(
                    status = IMusicService.PlayStatus.Error(
                        error.message ?: "Playback error",
                        error.errorCode
                    )
                )
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            // 处理来自通知栏等外部事件源的进度变化
            if (events.containsAny(Player.EVENT_POSITION_DISCONTINUITY)) {
                updateState {
                    copy(
                        currentPosition = player.currentPosition,
                        duration = player.duration
                    )
                }
            }

            // 如果播放列表或重复模式发生变化，也需要同步状态
            if (events.containsAny(
                    Player.EVENT_MEDIA_ITEM_TRANSITION,
                    Player.EVENT_TIMELINE_CHANGED,
                    Player.EVENT_PLAYLIST_METADATA_CHANGED
                )
            ) {
                val currentIndex = player.currentMediaItemIndex
                val currentMusic = getMusicInfoAt(currentIndex)
                updateState {
                    copy(
                        currentMediaIndex = currentIndex,
                        currentMusicInfo = currentMusic,
                        currentPosition = player.currentPosition,
                        duration = player.duration
                    )
                }
            }

            // 同步播放模式状态
            if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) {
                updateState {
                    copy(repeatMode = IMusicService.RepeatMode.fromValue(player.repeatMode))
                }
            }

            // 同步随机播放状态
            if (events.contains(Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED)) {
                updateState {
                    copy(isShuffleEnabled = player.shuffleModeEnabled)
                }
            }
        }
    }
}