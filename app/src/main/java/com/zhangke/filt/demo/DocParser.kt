package com.zhangke.filt.demo

import java.io.File

interface DocParser {

    fun parse(file: File): String
}
