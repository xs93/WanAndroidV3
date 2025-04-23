package com.github.xs93.wanandroid.common.store

import com.github.xs93.kv.bool
import com.github.xs93.kv.long
import com.github.xs93.kv.mmkv.MMKVOwner
import com.github.xs93.kv.string
import com.tencent.mmkv.MMKV

/**
 * App相关存储
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/14 19:58
 * @email 466911254@qq.com
 */
object AppCommonStore : MMKVOwner("app_common_store", MMKV.MULTI_PROCESS_MODE) {

    // app安装版本信息
    var appInstallVersionCode by long("appInstallVersionCode", -1)

    // 是否同意隐私协议
    var agreePrivacyPolicy by bool("agreePrivacyPolicy", false)

    // 当获取appsFlyer id为空时，使用此Id
    var appsFlyerUUID by string("appsFlyerUUID", null)

    // 是否使用户设置黑夜模式,当使用用户黑夜模式,则不随系统模式变化
    var userCustomNightMode by bool("use_custom_night_mode", false)

    // 当前是否黑夜模式
    var isNightMode by bool("app_night_mode", false)
}