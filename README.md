# Filt
Named Fill+Hilt, it's make Hilt can inject interface implementions.

## Usage
`MediaResolver` is a interface and maybe have multiple implementation on anywhere, like this:

```kotlin
import com.zhangke.filt.annotaions.Filt
import javax.inject.Inject

open class Media

interface MediaResolver {

    fun resolve(uri: String): Media
}

class VideoMedia(private val uri: String) : Media()

@Filt
class VideoMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(uri: String): Media {
        return VideoMedia(uri)
    }
}

class VoiceMedia(private val uri: String) : Media()

@Filt
class VoiceMediaResolver @Inject constructor() : MediaResolver {

    override fun resolve(uri: String): Media {
        return VoiceMedia(uri)
    }
}
```
Now, if some class dependence all MediaResolvers, you can use `Filt` annotation to implementation, and inject Set<MediaResolver> in you class.
```kotlin
  
@HiltViewModel
class MediaViewModel(
    private val resolvers: Set<@JvmSuppressWildcards MediaResolver>
): ViewModel()
  
```
