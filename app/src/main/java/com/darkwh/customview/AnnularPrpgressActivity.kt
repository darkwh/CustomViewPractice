package com.darkwh.customview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_annular.*
import java.util.*

class AnnularPrpgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_annular)
        annular_bar.setOnClickListener {
            annular_bar.setProgressWithAnimation(Random().nextInt(100))
        }
    }
}
