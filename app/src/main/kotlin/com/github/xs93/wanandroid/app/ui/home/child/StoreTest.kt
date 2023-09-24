package com.github.xs93.wanandroid.app.ui.home.child

import com.github.xs93.persistent.mmkv.mmkvBoolean
import com.github.xs93.persistent.store.MMKVStore

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/14 8:31
 * @email 466911254@qq.com
 */
object StoreTes : MMKVStore {

    var p1 by mmkvBoolean("aaaa", false)
}