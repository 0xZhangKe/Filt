package com.zhangke.filt.demo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zhangke.filt.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var parser: Set<@JvmSuppressWildcards DocParser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infoText = findViewById<TextView>(R.id.info)
        findViewById<View>(R.id.btn).setOnClickListener {
            infoText.text = parser.joinToString("\n") { it.parse(File("")) }
        }
    }
}
