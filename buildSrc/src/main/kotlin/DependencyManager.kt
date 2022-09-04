@file:Suppress("unused")

import Versions.kotlin

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
}

object AndroidX {

    object Plugins {
        const val application = "com.android.application:${Versions.agp}"
        const val library = "com.android.library:${Versions.agp}"
    }

    const val coreKtx = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.4.2"
    const val activityKtx = "androidx.activity:activity-ktx:1.4.0"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.4.1"
    const val annotation = "androidx.annotation:annotation:1.4.0"

    const val material = "com.google.android.material:material:1.6.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"

    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"

    object Lifecycle {
        private const val version = "2.4.1"

        const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val livedataKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val process = "androidx.lifecycle:lifecycle-process:$version"
    }


    object Room {

        //room数据库
        private const val version = "2.4.2"

        //roomCompiler 使用ksp依赖
        const val compiler = "androidx.room:room-compiler:${version}"

        const val runtime = "androidx.room:room-runtime:${version}"
        const val ktx = "androidx.room:room-ktx:${version}"
    }
}

object Kotlin {

    object Plugins {
        const val android = "org.jetbrains.kotlin.android:$kotlin"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$kotlin"
        const val parcelize = "org.jetbrains.kotlin:kotlin-parcelize-runtime:$kotlin"
    }

    //协程
    object Coroutines {
        //协程版本
        private const val version = "1.6.3"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${version}"
    }

    object Serialization {
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
    }
}

object ThirdPart {

    object Plugins {
        //google 使用ksp,代替kapt
        const val ksp = "com.google.devtools.ksp:symbol-processing-gradle-plugin:$kotlin-1.0.6"

        //编译golang的插件
        const val golang = "com.github.kr328.golang:gradle-plugin:1.0.4"
    }

    //一个多进程的preference的库
    const val preferenceMultiprocess = "dev.rikka.rikkax.preference:multiprocess:1.0.0"

    //一个自动编译aidl的库
    object KAidl {
        private const val version = "1.15"
        const val runtime = "com.github.kr328.kaidl:kaidl-runtime:$version"
        const val compiler = "com.github.kr328.kaidl:kaidl:$version"
    }

    object Moshi {
        private const val version = "1.13.0"
        const val kotlinPlugin = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
    }


    object Retrofit {
        private const val version = "2.9.0"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
    }

    object Okhttp {
        private const val version = "4.10.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val okhttpProfiler = "com.localebro:okhttpprofiler:1.0.8"
    }

    const val logger = "com.orhanobut:logger:2.2.0"

    const val jackson = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.2"

    object ImmersionBar {
        private const val version = "3.2.2"
        const val immersionBar = "com.geyifeng.immersionbar:immersionbar:$version"
        const val ktx = "com.geyifeng.immersionbar:immersionbar:$version"
    }

    object ARouter {
        private const val version = "1.5.2"
        const val plugin = "com.alibaba:arouter-register:1.0.2"
        const val arouter = "com.alibaba:arouter-api:$version"
        const val compiler = "com.alibaba:arouter-compiler:$version"
    }

    const val androidAutoSize = "com.github.JessYanCoding:AndroidAutoSize:v1.2.1"

    //基础类
    const val androidBase = "com.github.xs93:AndroidBase:1.0.5"

    //网络封装
    const val easyRetrofit = "com.github.xs93:EasyRetrofit:1.0.0"

    //可以选中的组件
    const val checkableView = "com.github.xs93:CheckableView:1.0.0"

    //mmkv ktx封装扩展
    const val mmkvKtx = "com.github.xs93:mmkv-ktx:1.0.1"


    /** RecyclerView 扩展框架 */
    const val brv = "com.github.liangjingkanji:BRV:1.3.79"

    /** RecyclerView 扩展框架  */
    const val BRVAH = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.7"

    const val coil = "io.coil-kt:coil:2.1.0"

    const val eventBus = "org.greenrobot:eventbus:3.3.1"

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
