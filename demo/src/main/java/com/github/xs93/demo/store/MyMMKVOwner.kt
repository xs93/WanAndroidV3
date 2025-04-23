package com.github.xs93.demo.store

import com.github.xs93.demo.entity.Student
import com.github.xs93.demo.entity.Teacher
import com.github.xs93.kv.mmkv.MMKVOwner
import com.github.xs93.kv.parcelable
import com.github.xs93.kv.serializable
import com.github.xs93.kv.stringSet

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/23 16:30
 * @description
 *
 */
object MyMMKVOwner : MMKVOwner("test_mmkv") {

    var teacher by serializable("teacher", defaultValue = Teacher().apply {
        name = "xs93"
        age = 18
        grade = "1"
        like = listOf()
    })

    var student by parcelable("student", defaultValue = Student("xs93", 18, "1"))

    var testStringSet by stringSet("testStringSet")
}