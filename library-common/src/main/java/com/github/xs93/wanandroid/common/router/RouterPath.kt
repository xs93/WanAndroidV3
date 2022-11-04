package com.github.xs93.wanandroid.common.router

/**
 *
 * ARouter 路由路径
 *
 * @author xushuai
 * @date   2022/9/7-17:05
 * @email  466911254@qq.com
 */
class RouterPath {

    class Login {
        companion object {
            private const val LoginGroup = "/login"

            const val LoginActivity = "$LoginGroup/LoginActivity"
        }
    }

    class Home {
        companion object {
            private const val HomeGroup = "/home"

            const val HomeFragment = "${HomeGroup}/HomeFragment"
        }
    }

    class Web {
        companion object {
            private const val WebGroup = "/web"

            const val WebActivity = "$WebGroup/WebActivity"
        }
    }

}