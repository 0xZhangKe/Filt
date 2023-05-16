package com.zhangke.filt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var resolverList: List<SourceResolver>
//    lateinit var resolverList: SourceResolver

    @Inject
    lateinit var status: Set<@JvmSuppressWildcards Status>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn).setOnClickListener {
            Log.d("MB_TEST", status.joinToString(", ") { it.getName() })
//            Log.d("MB_TEST", resolverList.resolve())
        }
    }
}
