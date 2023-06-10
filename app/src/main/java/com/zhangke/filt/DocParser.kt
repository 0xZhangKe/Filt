package com.zhangke.filt

import com.zhangke.filt.annotaions.Filt
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Inject

interface DocParser {

    fun parse(file: File): String
}

@Filt(installIn = SingletonComponent::class)
class HtmlParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "html parsed data"
    }
}

@Filt
class PdfParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "pdf parsed data"
    }
}

@Filt
class XmlParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "xml parsed data"
    }
}
