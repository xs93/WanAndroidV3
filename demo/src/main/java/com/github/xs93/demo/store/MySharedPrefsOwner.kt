package com.github.xs93.demo.store

import com.github.xs93.demo.entity.Teacher
import com.github.xs93.kv.asStateFlow
import com.github.xs93.kv.int
import com.github.xs93.kv.serializable
import com.github.xs93.kv.sp.SharedPrefsKVOwner
import com.github.xs93.kv.string
import com.github.xs93.kv.stringSet

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 13:39
 * @description
 *
 */
object MySharedPrefsOwner : SharedPrefsKVOwner("test_prefs") {


    var teacher by serializable("teacher", defaultValue = Teacher().apply {
        name = "xs93"
        age = 18
        grade = "1"
        like = listOf()
    })

    var testStringSet by stringSet("testStringSet")

    var testString by string("testString")

    var num by int("num")

    val num2Flow by int("num2").asStateFlow()
}