@file:Suppress("unused")

import Versions.kotlin
import Versions.kotlinCoroutines
import Versions.smartRefreshLayout

/**
 *
 * 依赖管理
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/11 9:50
 * @email 466911254@qq.com
 */

object Versions {
    //apg版本号
    const val agp = "7.2.2"

    //kotlin版本号
    const val kotlin = "1.7.10"
    const val kotlinCoroutines = "1.6.3"

    const val lifecycle = "2.5.1"

    const val room = "2.4.3"

    const val arouter = "1.5.2"

    const val moshi = "1.13.0"

    const val okhttp = "4.10.0"

    const val retrofit = "2.9.0"

    const val immersionBar = "3.2.2"

    const val smartRefreshLayout = "2.0.5"
}


object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.5.0"
    const val activityKtx = "androidx.activity:activity-ktx:1.5.1"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.2"
    const val annotation = "androidx.annotation:annotation:1.4.0"
    const val material = "com.google.android.material:material:1.6.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"

    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_liveData_Ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycle_process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val lifecycle_service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"
}

object Kotlin {
    const val plugin_kotlin_Android = "org.jetbrains.kotlin.android:$kotlin"
    const val plugin_kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:$kotlin"
    const val kotlinx_serialization_json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"

    const val plugin_kotlin_parcelize = "org.jetbrains.kotlin:kotlin-parcelize-runtime:$kotlin"

    //协程
    const val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines"
    const val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutines"
}

object ThirdPart {

    //基础类
    const val androidBase = "com.github.xs93:AndroidBase:1.1.3"

    //网络封装
    const val easyRetrofit = "com.github.xs93:EasyRetrofit:1.0.3"

    //mmkv ktx封装扩展
    const val mmkvKtx = "com.github.xs93:mmkv-ktx:1.0.3"

    //可以选中的组件
    const val checkableView = "com.github.xs93:CheckableView:1.0.0"

    //ARouter 路由组件
    const val arouter_plugin = "com.alibaba:arouter-register:1.0.2"
    const val arouter_api = "com.alibaba:arouter-api:${Versions.arouter}"
    const val arouter_compiler = "com.alibaba:arouter-compiler:${Versions.arouter}"

    //moshi json解析库
    const val moshi_kotlin_codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val moshi_kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"

    //网络库
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttp_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val okhttp_profiler = "com.localebro:okhttpprofiler:1.0.8"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_moshi_converter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    //日志库
    const val logger = "com.orhanobut:logger:2.2.0"

    //沉浸式库
    const val immersionBar = "com.geyifeng.immersionbar:immersionbar:${Versions.immersionBar}"
    const val immersionBar_ktx = "com.geyifeng.immersionbar:immersionbar:${Versions.immersionBar}"

    //界面适配库
    const val androidAutoSize = "com.github.JessYanCoding:AndroidAutoSize:v1.2.1"

    //图片加载库
    const val coil = "io.coil-kt:coil:2.1.0"

    //刷新库
    const val smart_refresh_layout_kernel = "io.github.scwang90:refresh-layout-kernel:$smartRefreshLayout"//核心必须依赖
    const val smart_refresh_header_classics = "io.github.scwang90:refresh-header-classics:$smartRefreshLayout"//经典刷新头
    const val smart_refresh_header_radar = "io.github.scwang90:refresh-header-radar:$smartRefreshLayout"//雷达刷新头
    const val smart_refresh_header_falsify = "io.github.scwang90:refresh-header-falsify:$smartRefreshLayout" //虚拟刷新头
    const val smart_refresh_header_material = "io.github.scwang90:refresh-header-material:$smartRefreshLayout"//谷歌刷新头
    const val smart_refresh_header_two_level = "io.github.scwang90:refresh-header-two-level:$smartRefreshLayout" //二级刷新头
    const val smart_refresh_footer_ball = "io.github.scwang90:refresh-footer-ball:$smartRefreshLayout"//球脉冲加载
    const val smart_refresh_footer_classics = "io.github.scwang90:refresh-footer-classics:$smartRefreshLayout"//经典加载

    /** RecyclerView 扩展框架  */
    const val BRVAH = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11"
    const val BRVAH_V4 = "io.github.cymchad:BaseRecyclerViewAdapterHelper:4.0.0-beta04"

    //事件总线库
    const val eventBus = "org.greenrobot:eventbus:3.3.1"

    //Banner滑动控件
    const val bannerViewPager = "com.github.zhpanvip:bannerviewpager:3.5.7"

    //一个多进程的preference的库
    const val preferenceMultiprocess = "dev.rikka.rikkax.preference:multiprocess:1.0.0"

    const val jackson = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.2"

    /** RecyclerView 扩展框架 */
    const val brv = "com.github.liangjingkanji:BRV:1.3.79"


    /** google pay 结算库 */
    const val billing = "com.android.billingclient:billing-ktx:5.0.0"

    /** appsflyer 归因库 */
    const val appsflyer = "com.appsflyer:af-android-sdk:6.3.2"

    /** google admob 广告库 */
    const val admob = "com.google.android.gms:play-services-ads:21.1.0"

    const val errorProneAnnotations = "com.google.errorprone:error_prone_annotations:2.15.0"
}


object Depend {
    const val junit = "junit:junit:4.13.2"
    const val junitExt = "androidx.test.ext:junit:1.1.3"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
}
