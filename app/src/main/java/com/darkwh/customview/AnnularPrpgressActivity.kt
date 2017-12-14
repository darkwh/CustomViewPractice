package com.darkwh.customview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_annular.*

class AnnularPrpgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_annular)
        annular_bar.setOnClickListener {
//            annular_bar.playAnimation()
            annular_bar.setProgressWithAnimation(80)
        }
    }
}
