package com.zhangke.filt

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Inject

interface SourceResolver {

    fun resolve(): String
}

class TimelineResolver @Inject constructor() : SourceResolver {

    override fun resolve(): String {
        return "Timeline"
    }
}

class UserResolver @Inject constructor() : SourceResolver {

    override fun resolve(): String {
        return "User"
    }
}

//@InstallIn(SingletonComponent::class)
//@Module
//class CommonModule {
//
//    @Provides
//    fun provideImplementList(
//        timelineResolver: TimelineResolver,
//        userResolver: UserResolver,
//    ): Array<SourceResolver> {
//        return arrayOf(userResolver, timelineResolver)
//    }
//}

//@InstallIn(SingletonComponent::class)
//@Module
//abstract class TimelineSourceResolverModule {
//
//    @Binds
//    @IntoSet
//    abstract fun timelineResolver(timelineResolver: TimelineResolver): SourceResolver
//
//}
//
//@InstallIn(SingletonComponent::class)
//@Module
//abstract class UserSourceResolverModule {
//
//    @Binds
//    @IntoSet
//    abstract fun userResolver(userResolver: UserResolver): SourceResolver
//}
//
//@InstallIn(SingletonComponent::class)
//@Module
//class CommonModule {
//
//    @Provides
//    fun provideResolverList(
//        resolverSet: Set<@JvmSuppressWildcards SourceResolver>
//    ): @JvmSuppressWildcards List<SourceResolver> {
//        return resolverSet.toList()
//    }
//}
