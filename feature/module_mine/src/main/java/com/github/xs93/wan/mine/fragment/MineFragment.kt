package com.github.xs93.wan.mine.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.xs93.core.ktx.setSingleClickListener
import com.github.xs93.core.log.logD
import com.github.xs93.ui.base.ui.viewbinding.BaseVBFragment
import com.github.xs93.wan.common.service.IMusicService
import com.github.xs93.wan.mine.databinding.FragmentMineBinding
import com.github.xs93.wan.router.PageRouterPath
import com.therouter.TheRouter
import com.therouter.router.Route
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:25
 * @email 466911254@qq.com
 */
@Route(path = PageRouterPath.FRAGMENT_MINE)
class MineFragment : BaseVBFragment<FragmentMineBinding>(
    FragmentMineBinding::inflate
) {

    companion object {
        fun newInstance(): MineFragment {
            val args = Bundle()
            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val musicService: IMusicService? by lazy {
        TheRouter.get(IMusicService::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
//            val path = "https://music.163.com/song/media/outer/url?id=3322380723.mp3"
        val path =
            "https://m801.music.126.net/20260331112040/714ce5111df85f2797dba8d8cd33fe5b/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/76762438505/ef58/e50d/d6f8/76c225aaf83dd0c77461a55c6b987239.mp3?vuutv=aMytRYImRIsmTJ0j0QsWOsQyS6E6HZA1rCnDP9DXlqWadS7erDMPaka2sf+Z69qSOthWaRxP8cnYSxoAX9Ig90JqKD4Ay2XHrqViHQPZgah1kKUKXf20gXlc2a9V1snt0HHyyi+XOnCAQCE/RWnAgah1quz5WoxlKaPAoceJERm7RSRcObLV0jMtOP9XtaFIanXkdQux4rxl/6G/d9UshYbjPt1Mk2kYLmtIY7pau9O9w1Z5AFqIIy2lswExSBqUQVRXEzsv8It1VMV/2Qc/vJVBZwAsZy9be6so+9caOrJZ9SA19ufqQmmJA79GOKieY++YM2WXexcyZoCD4u/YFOOVg4kSJxZG9g2nmBQMo8H8V0HEjgXUf29l0PtU+QeI&cdntag=bWFyaz1vc193ZWIscXVhbGl0eV9zdGFuZGFyZA"

        val musicInfo = IMusicService.MusicInfo(path)
        musicService?.setMusicList(listOf(musicInfo))

        vBinding.btnStart.setSingleClickListener {
            musicService?.play()
        }
        vBinding.btnPause.setSingleClickListener {
            musicService?.pause()
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        lifecycleScope.launch {
            musicService?.playState?.collect {
                logD("MusicService", "$it")
            }
        }
    }
}