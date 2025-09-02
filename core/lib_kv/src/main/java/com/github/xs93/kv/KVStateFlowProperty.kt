package com.github.xs93.kv

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 9:47
 * @description
 *
 */
class KVStateFlowProperty<T>(private val kvProperty: KVProperty<T>) :
    ReadOnlyProperty<IKV, MutableStateFlow<T>> {

    private var cache: MutableStateFlow<T>? = null

    override fun getValue(thisRef: IKV, property: KProperty<*>): MutableStateFlow<T> {
        return cache ?: KVFlow(
            { kvProperty.getValue(thisRef, property) },
            { kvProperty.setValue(thisRef, property, it) }).also { cache = it }
    }
}

class KVFlow<T>(
    private val getPropertyValue: () -> T,
    private val setPropertyValue: (T) -> Unit,
    private val flow: MutableStateFlow<T> = MutableStateFlow(getPropertyValue())
) : MutableStateFlow<T> by flow {

    override var value: T
        get() = getPropertyValue()
        set(value) {
            val originValue = flow.value
            flow.value = value
            if (originValue != value) {
                setPropertyValue(value)
            }
        }

    override fun compareAndSet(expect: T, update: T): Boolean =
        flow.compareAndSet(expect, update).also { setSuccess ->
            if (setSuccess) {
                setPropertyValue(update)
            }
        }
}