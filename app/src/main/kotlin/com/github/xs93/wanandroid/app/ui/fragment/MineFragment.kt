package com.github.xs93.wanandroid.app.ui.fragment

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.github.xs93.framework.base.ui.viewbinding.BaseVBFragment
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.wan.router.PageRouterPath
import com.github.xs93.wanandroid.app.databinding.FragmentMineBinding
import com.github.xs93.wanandroid.music.service.MusicPlayService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.therouter.router.Route

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

    private var mController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        context?.let {
            val sessionToken = SessionToken(it, ComponentName(it, MusicPlayService::class.java))
            controllerFuture = MediaController.Builder(it, sessionToken).buildAsync()
            controllerFuture?.addListener({
                mController = controllerFuture?.get()
                val mediaItem = MediaItem.Builder()
                    .setUri("https://m701.music.126.net/20250731152027/8bcfe4946e2e14b6ed4f965beff68a8b/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/14096444542/bafc/a068/39f8/9a9e06e5634410b5e7e81df24749e656.mp3?vuutv=dRet6d1l9EmVIFZ1GvU7Zfl6RIuCUOZrlgo/LlQj4c1nuUIJWXuN7ReeGGfy1oy24LHgwc3xtv5fHdZNVnW+Z9NXWrFaVsgXBR6RGyCxb4xLOeLFa0nEOAi1/OHbS4NtnUBUI+71ZbYO5UJiEwIcFA==")
                    .build()
                mController?.setMediaItem(mediaItem)
            }, MoreExecutors.directExecutor())
        }

        vBinding.btnStart.setSingleClickListener {
            mController?.play()
        }
        vBinding.btnPause.setSingleClickListener {
            mController?.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controllerFuture?.run {
            MediaController.releaseFuture(this)
        }
    }
}