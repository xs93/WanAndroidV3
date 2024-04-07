package com.github.xs93.wanandroid.common.store

import com.github.xs93.persistent.mmkv.mmkvParcelableWithNull
import com.github.xs93.persistent.store.MMKVStore
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.tencent.mmkv.MMKV

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 11:09
 * @email 466911254@qq.com
 */
object AccountStore : MMKVStore {

    override val mmkv: MMKV
        get() = MMKV.mmkvWithID("account_info")

    // 用户信息
    var userInfo: User? by mmkvParcelableWithNull("user_info", null)

    // 用户详细信息
    var userDetailInfo: UserDetailInfo? by mmkvParcelableWithNull("user_detail_info", null)
}