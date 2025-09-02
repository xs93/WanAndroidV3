package com.github.xs93.demo.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 11:43
 * @description
 *
 */
@Parcelize
data class Student(val name: String, val age: Int, val grade: String) : Parcelable
