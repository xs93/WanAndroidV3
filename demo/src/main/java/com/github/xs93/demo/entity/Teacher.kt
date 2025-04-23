package com.github.xs93.demo.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 11:46
 * @description
 *
 */
@Parcelize
class Teacher : Serializable, Parcelable {

    companion object {
        const val serialVersionUID = 1L
    }

    var name: String? = null
    var age: Int? = null
    var grade: String? = null
    var like: List<String>? = null
    override fun toString(): String {
        return "Teacher(name=$name, age=$age, grade=$grade, like=$like)"
    }
}