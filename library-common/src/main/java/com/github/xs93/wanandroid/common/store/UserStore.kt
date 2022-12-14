package com.github.xs93.wanandroid.common.store

import com.github.xs93.mmkv.*
import com.github.xs93.wanandroid.common.model.UserInfo
import com.tencent.mmkv.MMKV

/**
 * 用户信息存储
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/9/27 17:03
 * @email 466911254@qq.com
 */
object UserStore : MMKVOwner {
    override val mmkv: MMKV
        get() = MMKV.mmkvWithID("UserStoreMMKV")

    /* 用户是否登录 */
    var login: Boolean by mmkvBoolean("logged", false)

    /* 账号信息 */
    var userInfo: UserInfo? by mmkvParcelableWithNull("userInfo", null)
}