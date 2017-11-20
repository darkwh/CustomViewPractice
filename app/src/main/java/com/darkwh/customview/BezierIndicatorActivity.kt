package com.darkwh.customview

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_bezier_indicator.*

class BezierIndicatorActivity : AppCompatActivity() {

    val viewList: MutableList<View> by lazy { mutableListOf<View>() }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bezier_indicator)
        val inflater = layoutInflater
        viewList.add(inflater.inflate(R.layout.layout1, null))
        viewList.add(inflater.inflate(R.layout.layout2, null))
        viewList.add(inflater.inflate(R.layout.layout3, null))
        pager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View?, obj: Any?): Boolean {
                return view == obj
            }

            override fun getCount(): Int {
                return viewList.size
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                container?.addView(viewList[position])
                return viewList[position]
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(viewList.get(position))
            }
        }
        indicator.attachToPager(pager)
    }
}