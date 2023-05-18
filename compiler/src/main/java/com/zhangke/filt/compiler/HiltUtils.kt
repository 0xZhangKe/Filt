package com.zhangke.filt.compiler

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import dagger.hilt.InstallIn

fun installInAnnotations(installInName: String): AnnotationSpec {
    return AnnotationSpec.builder(InstallIn::class)
        .addMember(CodeBlock.of("$installInName::class"))
        .build()
}
