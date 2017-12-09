package com.darkwh.customview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.layout_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        btn_bezier_indicator.setOnClickListener(this)
        btn_annular_progress_bar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_bezier_indicator -> {
                startActivity<BezierIndicatorActivity>()
            }
            R.id.btn_annular_progress_bar -> {
                startActivity<AnnularPrpgressActivity>()
            }
        }
    }
}