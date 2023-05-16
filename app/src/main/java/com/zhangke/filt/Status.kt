package com.zhangke.filt

import com.zhangke.filt.annotaions.Filt
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface Status {

    fun getName(): String
}

@Filt(Status::class, ActivityComponent::class)
class NewStatus @Inject constructor() : Status {

    override fun getName(): String {
        return "New"
    }
}

@Filt(installIn = SingletonComponent::class)
class LikeStatus @Inject constructor() : Status {

    override fun getName(): String {
        return "Like"
    }
}

@Filt
class LikeSingleStatus @Inject constructor() : Status {

    override fun getName(): String {
        return "LikeSingle"
    }
}
