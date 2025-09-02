package com.github.xs93.framework.utils

import java.lang.reflect.ParameterizedType

/**
 * 泛型相关工具类
 *
 *
 * @author xushuai
 * @date   2022/5/12-22:02
 * @email  466911254@qq.com
 */
@Suppress("unused")
class ClassUtils {


    companion object {

        /**
         * 获取对象对应index的泛型对象
         * @param any Any 对象
         * @param index Int 泛型所在的位置
         * @return Class<*> 泛型具体class对象
         */
        @JvmStatic
        fun getGenericClassByIndex(any: Any, index: Int): Class<*>? {
            try {
                val currentClass: Class<*> = any::class.java
                val parameterizedType = getParameterizedType(currentClass) ?: return null
                val typeArguments = parameterizedType.actualTypeArguments
                if (typeArguments.size <= index) {
                    return null
                }
                return typeArguments[index] as Class<*>
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 查找对象泛型中继承了 filterClass类的泛型类对象，如果有多个，则返回第一个
         * @param any Any  查找对象
         * @param filterClass Class<*>  目标继承对象类
         * @return Class<*>? 查找结果对象
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> getGenericClassByClass(any: Any, filterClass: Class<*>): Class<T>? {
            try {
                val currentClass: Class<*> = any::class.java
                val parameterizedType = getParameterizedType(currentClass) ?: return null
                val typeArguments = parameterizedType.actualTypeArguments
                for (typeArgument in typeArguments) {
                    val clazz: Class<*>? = typeArgument as? Class<*>
                    if (clazz != null) {
                        if (filterClass.isAssignableFrom(clazz)) {
                            return clazz as? Class<T>
                        }
                    }
                }
                return null
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }


        private fun getParameterizedType(clazz: Class<*>): ParameterizedType? {
            // 获取超类并包含泛型类型,superclass 只返回超类，不包含泛型
            val type = clazz.genericSuperclass
            if (type is ParameterizedType) {
                return type
            }
            return null
        }
    }

}