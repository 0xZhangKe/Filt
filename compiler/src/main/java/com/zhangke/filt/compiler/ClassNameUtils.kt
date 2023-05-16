package com.zhangke.filt.compiler

object ClassNameUtils {

    fun splitPackageAndName(
        qualifiedName: String
    ): Pair<String, String> {
        val namePrePointIndex = qualifiedName.lastIndexOf('.')
        if (namePrePointIndex <= 0) return "" to qualifiedName
        val packageName = qualifiedName.substring(0, namePrePointIndex)
        val className = qualifiedName.substring(namePrePointIndex, qualifiedName.length)
        return packageName to className
    }
}
