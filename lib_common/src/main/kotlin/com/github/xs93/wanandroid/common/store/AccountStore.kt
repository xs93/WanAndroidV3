package com.github.xs93.wanandroid.common.store

import com.github.xs93.kv.mmkv.MMKVOwner
import com.github.xs93.kv.parcelable
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
object AccountStore : MMKVOwner("account_info") {

    override val mmkv: MMKV
        get() = MMKV.mmkvWithID("account_info")

    // 用户信息
    var userInfo: User? by parcelable("user_info")

    // 用户详细信息
    var userDetailInfo: UserDetailInfo? by parcelable("user_detail_info", null)
}