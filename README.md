# Filt
Filt stands for Fill+Hilt and is used to simplify the injection of all the implementation classes of the interface when using Hilt.

I will first introduce the background of the requirements.
First of all the requirements will do some unified abstraction of a certain class of things and behaviours, these abstractions are placed in a separate module.
The app layer directly depend on this module and use the entities and UseCase, this abstraction layer will contain multiple implementations.
Each implementation is also a separate module, these modules must rely on the abstraction layer module (similar to the plug-in architecture) .
The app layer depends on these concrete implementations by way of `runtimeOnly`, and the abstraction layer does not depend on these implementations.

Then the abstraction layer needs to get all the implementations of the runtime interface by some means, generally we can get it through the ServiceLoader.
But the ServiceLoader can not support dependency injection.
Hilt provides a way to get all the implementations of the interface injection but it is more complex to use.
Given the overall architecture design will lead to a lot of similar work. 
Therefore, we are considering the use of KSP to automatically generate the code required by Hilt to simplify the use of this scenario.

# Use Hilt @IntoSet implement MultiBindings
Let's start by looking at what we need to do to use Hilt.
First define an interface.

```kotlin
interface DocParser {

    fun parse(file: File): String
}
```
We then provide an implementation class in the html module:
```kotlin
class HtmlParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "html parsed data"
    }
}

@InstallIn(ActivityComponent::class)
@Module
abstract class HtmlParserModule {

    @Binds
    @IntoSet
    abstract fun bind(input: HtmlParser): DocParser
}
```
A further implementation class is provided in the pdf module:
```kotlin
class PdfParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "pdf parsed data"
    }
}

@InstallIn(ActivityComponent::class)
@Module
abstract class PdfParserModule {

    @Binds
    @IntoSet
    abstract fun bind(input: PdfParser): DocParser
}
```
Set collections can be injected when using:
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var parsers: Set<@JvmSuppressWildcards DocParser>
}
```
For Kotlin, the @JvmSuppressWildcards annotation will also need to be added.

# Now with Filt
```kotlin
@Filt(installIn = ActivityComponent::class) // Set the installIn parameter manually
class HtmlParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "html parsed data"
    }
}

@Filt // Default if not set SingletonComponent
class PdfParser @Inject constructor() : DocParser {

    override fun parse(file: File): String {
        return "pdf parsed data"
    }
}
```

Then build and open the build/generated/ksp/release/kotlin directory to see the automatically generated files:

```kotlin
// HtmlParserBindModule.kt
@InstallIn(dagger.hilt.android.components.ActivityComponent::class)
@Module
public abstract class HtmlParserBindModule {
    @Binds
    @IntoSet
    public abstract fun bind(input: HtmlParser): DocParser
}

// PdfParserBindModule.kt
@InstallIn(dagger.hilt.components.SingletonComponent::class)
@Module
public abstract class PdfParserBindModule {
    @Binds
    @IntoSet
    public abstract fun bind(input: PdfParser): DocParser
}
```

The generated code is compounded as expected and is consistent with that written manually. 
The usage remains the same and is still used in the same way as before.

# @Filt
The Filt annotation is the only public annotation and contains two parameters.

`type` indicates the interface to be injected, since a class can have multiple SuperTypes, 
if it contains multiple SuperTypes the Filt processor will not be able to determine which interface is to be injected, 
so you need to specify it manually with the type parameter.

The `installIn` parameter is the same as the `dagger.hilt.InstallIn` annotation and will be passed through to the generated BindModule, 
defaulting to `SingletonComponent`.

# Usage
The first thing you need to do is to configure the project's KSP. 
The exact steps are not described here, just follow the official steps given.

https://kotlinlang.org/docs/ksp-quickstart.html

Then just add Filt's KSP and annotation dependencies.
```kotlin
implementation("com.github.0xZhangKe.Filt:annotaions:1.0.3")
ksp("com.github.0xZhangKe.Filt:compiler:1.0.3")
```
It may also be necessary to add the jitpack repository.
```kotlin
repositories {
	maven { setUrl("https://jitpack.io") }
}
```
