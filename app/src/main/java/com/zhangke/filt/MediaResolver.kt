package com.zhangke.filt

import com.zhangke.filt.annotaions.Filt
import javax.inject.Inject

open class Media {

    open val title: String = ""
}

interface MediaResolver {

    fun resolve(uri: String): Media
}

class VideoMedia(private val uri: String) : Media() {

    override val title: String
        get() = "VideoMedia"
}

@Filt
class VideoMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(uri: String): Media {
        return VideoMedia(uri)
    }
}

class VoiceMedia(private val uri: String) : Media() {

    override val title: String
        get() = "VoiceMedia"
}

@Filt
class VoiceMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(uri: String): Media {
        return VoiceMedia(uri)
    }
}
