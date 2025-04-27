package com.github.xs93.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.xs93.datastore.DataStoreOwner
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 10:52
 * @description
 *
 */
@RunWith(AndroidJUnit4::class)
class DataStoreTest : DataStoreOwner("Test") {

    private val i1 by intPreference("test1")

    @Test
    fun testInt1() = runTest {
        Assert.assertEquals(null, i1.get())
        Assert.assertEquals(0, i1.get())
        i1.set(1)
        Assert.assertEquals(1, i1.get())
    }
}