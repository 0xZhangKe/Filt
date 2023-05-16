package com.zhangke.filt.annotaions

import kotlin.reflect.KClass

/**
 * Use this annotation will auto generate a Hilt Module for binds [type].
 * @param type witch interface that need bind
 * @param installIn generated Hilt Module's InstallIn params
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Filt(
    val type: KClass<*> = Unit::class,
    val installIn: KClass<*> = Unit::class,
)
