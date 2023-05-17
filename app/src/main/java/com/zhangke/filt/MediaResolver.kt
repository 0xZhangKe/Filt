package com.zhangke.filt

import com.zhangke.filt.annotaions.Filt
import javax.inject.Inject

interface MediaResolver {

    fun resolve(mediaId: String): String
}

@Filt
class VideoMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(mediaId: String): String {
        return "https://example.com/video/$mediaId"
    }
}

@Filt
class VoiceMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(mediaId: String): String {
        return "https://example.com/voice/$mediaId"
    }
}

@Filt
class ImageMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(mediaId: String): String {
        return "https://example.com/image/$mediaId"
    }
}