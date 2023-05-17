package com.zhangke.filt

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var resolverList: Set<@JvmSuppressWildcards MediaResolver>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infoText = findViewById<TextView>(R.id.info)
        findViewById<View>(R.id.btn).setOnClickListener {
            infoText.text = resolverList.joinToString("\n") { it.resolve("42") }
        }
    }
}
