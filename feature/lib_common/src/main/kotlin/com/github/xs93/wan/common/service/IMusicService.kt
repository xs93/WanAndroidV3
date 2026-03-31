package com.github.xs93.wan.common.service

import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Singleton

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/3/27 16:32
 * @description 音乐服务
 * 
 */
@Singleton
interface IMusicService {

    /**
     * 播放状态流 - 用于 UI 观察
     */
    val playState: StateFlow<PlayState>


    /**
     * 设置音乐列表
     * @param musicList 音乐信息列表
     */
    fun setMusicList(musicList: List<MusicInfo>)

    /**
     * 开始播放音乐
     */
    fun play()

    /**
     * 播放单个音乐
     * @param musicInfo 要播放的音乐信息
     * @param strategy 播放策略，默认为 ClearAndPlay
     */
    fun playSingle(musicInfo: MusicInfo, strategy: PlayStrategy = PlayStrategy.ClearAndPlay)

    /**
     * 播放播放列表中的指定音乐
     * @param index 播放列表索引
     */
    fun playAt(index: Int)

    /**
     * 暂停音乐
     */
    fun pause()

    /**
     * 播放下一首
     */
    fun playNext()

    /**
     * 播放上一首
     */
    fun playPrevious()

    /**
     * 设置重复模式
     * @param mode 重复模式：REPEAT_MODE_OFF, REPEAT_MODE_ONE, REPEAT_MODE_ALL
     */
    fun setRepeatMode(mode: RepeatMode)

    /**
     * 获取当前重复模式
     */
    fun getRepeatMode(): RepeatMode

    /**
     * 设置随机播放模式
     * @param enabled 是否启用随机播放
     */
    fun setShuffleEnabled(enabled: Boolean)

    /**
     * 是否启用了随机播放
     */
    fun isShuffleEnabled(): Boolean

    /**
     * 跳转到指定位置
     * @param positionMs 位置（毫秒）
     */
    fun seekTo(positionMs: Long)

    /**
     * 获取当前播放位置（毫秒）
     */
    fun getCurrentPosition(): Long

    /**
     * 获取音频总时长（毫秒）
     */
    fun getDuration(): Long

    /**
     * 获取当前播放索引
     */
    fun getCurrentMediaIndex(): Int

    /**
     * 获取当前播放的音乐信息
     */
    fun getCurrentMusicInfo(): MusicInfo?

    /**
     * 获取播放列表中的指定项
     * @param index 索引
     */
    fun getMusicInfoAt(index: Int): MusicInfo?

    /**
     * 获取播放列表总数
     */
    fun getMusicListSize(): Int

    /**
     * 清空播放列表并释放资源
     */
    fun clear()


    /**
     * 音乐信息数据类
     */
    data class MusicInfo(
        val uri: String, // 音频 URI
        val title: String = "", // 音频标题
        val artist: String = "", // 音频艺术家
        val album: String = "", // 音频专辑
        val duration: Long = 0L, // 音频时长
        val coverArtUri: String? = null, // 封面图片 URI
        val extraData: Map<String, Any> = emptyMap() // 额外的数据
    ) {
        /**
         * 是否有有效的 URI
         */
        fun hasValidUri(): Boolean = uri.isNotEmpty()

        /**
         * 显示标题（优先使用 title，否则使用 URI 的文件名部分）
         */
        fun getDisplayTitle(): String {
            return when {
                title.isNotEmpty() -> title
                uri.isNotEmpty() -> uri.substringAfterLast('/').substringBeforeLast('.')
                else -> "未知曲目"
            }
        }

        /**
         * 显示艺术家（如果没有则返回空字符串）
         */
        fun getDisplayArtist(): String {
            return artist.ifEmpty { "未知艺术家" }
        }

        /**
         * 显示专辑（如果没有则返回空字符串）
         */
        fun getDisplayAlbum(): String {
            return album.ifEmpty { "未知专辑" }
        }

        /**
         * 格式化时长
         */
        fun getFormattedDuration(): String {
            return formatTime(duration)
        }

        /**
         * 是否有封面图片
         */
        fun hasCoverArt(): Boolean = !coverArtUri.isNullOrEmpty()

        private fun formatTime(timeMs: Long): String {
            if (timeMs <= 0) return "00:00"
            val totalSeconds = timeMs / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }

        override fun toString(): String {
            return "MusicInfo(title='$title', artist='$artist', duration=${getFormattedDuration()})"
        }
    }


    /**
     * 播放状态 - 使用 sealed class 提供更丰富的状态信息
     */
    sealed class PlayStatus {
        /** 空闲状态，未初始化或已释放 */
        object Idle : PlayStatus()

        /** 缓冲中，正在加载或准备播放 */
        data class Buffering(val progress: Int = 0) : PlayStatus()

        /** 就绪状态，可以播放 */
        object Ready : PlayStatus()

        /** 播放结束 */
        object Ended : PlayStatus()

        /** 错误状态 */
        data class Error(val message: String, val errorCode: Int = -1) : PlayStatus()

        /**
         * 是否为可播放状态
         */
        fun canPlay(): Boolean = this is Ready

        /**
         * 是否为活跃状态（非空闲和结束）
         */
        fun isActive(): Boolean = when (this) {
            is Buffering, Ready -> true
            else -> false
        }

        /**
         * 是否有错误
         */
        fun isError(): Boolean = this is Error

        /**
         * 获取错误信息（如果有）
         */
        fun getErrorMessage(): String? = when (this) {
            is Error -> message
            else -> null
        }

        /**
         * 状态文本描述
         */
        fun getStatusText(): String = when (this) {
            is Idle -> "空闲"
            is Buffering -> "缓冲中 ${if (progress > 0) "$progress%" else ""}"
            is Ready -> "就绪"
            is Ended -> "结束"
            is Error -> "错误：$message"
        }
    }

    /**
     * 重复播放模式 - 使用 sealed class
     */
    sealed class RepeatMode(val value: Int) {
        /** 不重复 */
        object Off : RepeatMode(0)

        /** 单曲循环 */
        object One : RepeatMode(1)

        /** 全部循环 */
        object All : RepeatMode(2)

        companion object {
            /**
             * 从整数值转换为 RepeatMode
             * @param value 整数值
             * @return 对应的 RepeatMode
             */
            fun fromValue(value: Int): RepeatMode {
                return when (value) {
                    1 -> One
                    2 -> All
                    else -> Off
                }
            }

            /**
             * 下一个模式
             */
            fun RepeatMode.next(): RepeatMode {
                return when (this) {
                    is Off -> One
                    is One -> All
                    is All -> Off
                }
            }
        }
    }

    /**
     * 播放策略 - 当要播放的音乐不在当前列表中时的处理方式
     * 使用 sealed class 提供更丰富的策略选项
     */
    sealed class PlayStrategy {
        /** 清空当前列表，只播放这一个音乐 */
        object ClearAndPlay : PlayStrategy()

        /** 添加到列表第一个位置并播放 */
        object AddToHead : PlayStrategy()

        /** 添加到列表末尾并播放 */
        object AddToTail : PlayStrategy()

        /** 如果不在列表中则不播放 */
        object SkipIfNotExist : PlayStrategy()

        /**
         * 替换指定索引位置的音乐并播放
         * @param index 要替换的位置
         */
        data class ReplaceAt(val index: Int) : PlayStrategy()

        /**
         * 在当前播放位置之后插入并播放
         */
        object PlayNext : PlayStrategy()

        companion object {
            /**
             * 默认策略：清空并播放
             */
            fun default(): PlayStrategy = ClearAndPlay
        }
    }

    /**
     * 播放状态数据类
     */
    data class PlayState(
        val isPlaying: Boolean = false,
        val status: PlayStatus = PlayStatus.Idle,
        val currentMediaIndex: Int = 0,
        val currentPosition: Long = 0L,
        val duration: Long = 0L,
        val repeatMode: RepeatMode = RepeatMode.Off,
        val isShuffleEnabled: Boolean = false,
        val playlist: List<MusicInfo> = emptyList(),
        val currentMusicInfo: MusicInfo? = null
    ) {
        /**
         * 是否正在播放
         */
        fun isActuallyPlaying(): Boolean = isPlaying && status is PlayStatus.Ready

        /**
         * 获取播放列表大小
         */
        fun getPlaylistSize(): Int = playlist.size

        /**
         * 是否为空播放列表
         */
        fun isPlaylistEmpty(): Boolean = playlist.isEmpty()

        /**
         * 是否有当前播放的音乐
         */
        fun hasCurrentMusic(): Boolean = currentMusicInfo != null

        /**
         * 获取当前播放音乐的显示标题
         */
        fun getCurrentMusicTitle(): String {
            return currentMusicInfo?.getDisplayTitle() ?: "未知曲目"
        }

        /**
         * 获取当前播放音乐的艺术家
         */
        fun getCurrentMusicArtist(): String {
            return currentMusicInfo?.getDisplayArtist() ?: ""
        }

        /**
         * 获取当前播放音乐的专辑
         */
        fun getCurrentMusicAlbum(): String {
            return currentMusicInfo?.getDisplayAlbum() ?: ""
        }

        /**
         * 获取进度百分比 (0-100)
         */
        fun getProgressPercent(): Float {
            return if (duration > 0) {
                (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }

        /**
         * 格式化当前播放时间
         */
        fun getCurrentTimeText(): String {
            return formatTime(currentPosition)
        }

        /**
         * 格式化总时长
         */
        fun getDurationText(): String {
            return formatTime(duration)
        }

        /**
         * 获取状态文本描述
         */
        fun getStatusText(): String = status.getStatusText()

        /**
         * 检查是否应该显示错误
         */
        fun shouldShowError(): Boolean = status is PlayStatus.Error

        /**
         * 获取错误信息
         */
        fun getErrorMessage(): String? = status.getErrorMessage()

        private fun formatTime(timeMs: Long): String {
            if (timeMs <= 0) return "00:00"
            val totalSeconds = timeMs / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
        }

        override fun toString(): String {
            return buildString {
                append("PlaybackState(")
                append("status=$status, ")
                append("isPlaying=$isPlaying, ")
                append("currentMusic=${currentMusicInfo?.getDisplayTitle()}, ")
                append("position=${getCurrentTimeText()}, ")
                append("duration=${getDurationText()}, ")
                append("repeatMode=$repeatMode, ")
                append("shuffle=$isShuffleEnabled")
                append(")")
            }
        }
    }
}