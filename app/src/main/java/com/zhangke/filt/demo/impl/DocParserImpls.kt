package com.zhangke.filt.demo.impl

import com.zhangke.filt.annotaions.Filt
import com.zhangke.filt.demo.DocParser
import dagger.hilt.android.components.ActivityComponent
import java.io.File
import javax.inject.Inject


@Filt(installIn = ActivityComponent::class)
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
