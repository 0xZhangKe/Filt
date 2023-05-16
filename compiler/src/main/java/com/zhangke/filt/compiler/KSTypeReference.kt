package com.zhangke.filt.compiler

import com.google.devtools.ksp.symbol.KSTypeReference

val KSTypeReference.typeQualifiedName: String?
    get() = resolve()
        .declaration
        .qualifiedName
        ?.asString()
