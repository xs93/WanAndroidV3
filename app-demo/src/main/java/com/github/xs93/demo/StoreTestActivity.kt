package com.github.xs93.demo

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.github.xs93.demo.databinding.ActivityStoreTestBinding
import com.github.xs93.demo.entity.Student
import com.github.xs93.demo.entity.Teacher
import com.github.xs93.demo.entity.TestResponse
import com.github.xs93.demo.store.MyMMKVOwner
import com.github.xs93.demo.store.MySharedPrefsOwner
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity
import kotlinx.coroutines.launch

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 13:19
 * @description
 *
 */
class StoreTestActivity : BaseVBActivity<ActivityStoreTestBinding>(
    ActivityStoreTestBinding::inflate
) {


    private val teacher: Teacher by lazy {
        Teacher().apply {
            name = "XiaoLi"
            age = 25
            grade = "1"
            like = listOf("coding", "sleep")
        }
    }

    private val student: Student by lazy {
        Student("XiaoHong", 18, "1")
    }

    private val teacherList by lazy {
        listOf(
            Teacher().apply {
                name = "XiaoLi"
                age = 25
                grade = "1"
                like = listOf("coding", "sleep")
            },
            Teacher().apply {
                name = "LiHua"
                age = 27
                grade = "0"
                like = listOf("play", "sleep")
            },
            Teacher().apply {
                name = "abb"
                age = 67
                grade = "1"
                like = listOf("tea", "sleep")
            }

        )
    }

    private val studentMap by lazy {
        mapOf(
            1 to Student("XiaoHong", 18, "1"),
            2 to Student("XiaoMing", 18, "2"),
            3 to Student("XiaoHuang", 18, "3")
        )
    }

    private val testResponse: TestResponse<Teacher> by lazy {
        TestResponse(
            code = 200,
            msg = "success",
            data = Teacher().apply {
                name = "DaLi"
                age = 25
                grade = "1"
                like = listOf("coding", "sleep")
            }
        )
    }


    private val testResponse2: TestResponse<List<Teacher>> by lazy {
        TestResponse(
            code = 200,
            msg = "success",
            data = teacherList
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewBinding.tvContent.movementMethod = ScrollingMovementMethod.getInstance()
        viewBinding.btnTestSp.setOnClickListener {
        }

        viewBinding.btnClear.setOnClickListener {
            MySharedPrefsOwner.clear()
            MyMMKVOwner.clear()
            viewBinding.tvContent.text = "clear success"
        }

        viewBinding.btnSaveSerializable.setOnClickListener {
//            MySharedPrefsOwner.teacher = null
            MyMMKVOwner.teacher = teacher
        }

        viewBinding.btnGetSerializable.setOnClickListener {
            viewBinding.tvContent.text = MyMMKVOwner.teacher.toString()
        }
        viewBinding.btnSaveSet.setOnClickListener {
            MyMMKVOwner.testStringSet = setOf("a", "b", "c")
        }

        viewBinding.btnGetSet.setOnClickListener {
            viewBinding.tvContent.text = MyMMKVOwner.testStringSet.toString()
        }

        lifecycleScope.launch {
            MySharedPrefsOwner.num2Flow.collect {
                viewBinding.tvContent.text = it.toString()
            }
        }

        viewBinding.btnSaveFlow.setOnClickListener {
            MySharedPrefsOwner.num2Flow.value = MySharedPrefsOwner.num2Flow.value + 1
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        viewBinding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}