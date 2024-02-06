package com.github.xs93.wanandroid.common.store

import com.github.xs93.persistent.mmkv.mmkvBoolean
import com.github.xs93.persistent.mmkv.mmkvLong
import com.github.xs93.persistent.mmkv.mmkvString
import com.github.xs93.persistent.store.MMKVStore
import com.tencent.mmkv.MMKV

/**
 * App相关存储
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/14 19:58
 * @email 466911254@qq.com
 */
object AppCommonStore : MMKVStore {

    override val mmkv: MMKV = MMKV.mmkvWithID("app_common_store", MMKV.MULTI_PROCESS_MODE)

    // app安装版本信息
    var appInstallVersionCode by mmkvLong("appInstallVersionCode", -1)

    // 是否同意隐私协议
    var agreePrivacyPolicy by mmkvBoolean("agreePrivacyPolicy", false)

    // 当获取appsFlyer id为空时，使用此Id
    var appsFlyerUUID by mmkvString("appsFlyerUUID", null)
}