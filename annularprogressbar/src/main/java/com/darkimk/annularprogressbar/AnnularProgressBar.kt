package com.darkimk.annularprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author DarkWH
 * @date 2017/12/9
 * @description 环形进度条控件
 */
class AnnularProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        View(context, attrs, defStyleAttr) {

    /**
     * 圆心坐标-X
     */
    private var centerX = 0f

    /**
     * 圆心坐标-Y
     */
    private var centerY = 0f

    /**
     * 外环宽度
     */
    private var borderWidth = 0f

    /**
     * 进度条宽度
     */
    private var progressWidth = 0f

    /**
     * 外环颜色
     */
    private var borderColor = 0

    /**
     * 进度条颜色
     */
    private var progressColor = 0

    /**
     * 内圆颜色
     */
    private var circleColor = 0

    /**
     * 两圆之间的填充色
     */
    private var fillColor = 0

    /**
     * 总进度的文字颜色
     */
    private var totalTextColor = 0

    /**
     * 当前进度的文字颜色
     */
    private var progressTextColor = 0

    /**
     * 总进度的文字大小
     */
    private var totalTextSize = 0f

    /**
     * 当前进度的文字大小
     */
    private var progressTextSize = 0f

    /**
     * 两圆之间的间距
     */
    private var space = 0f

    /**
     * 外环半径
     */
    private var borderRadius = 0f

    /**
     * 进度条所在圆半径
     */
    private var progressRadius = 0f

    /**
     * 内圆半径
     */
    private var circleRadius = 0f

    /**
     * 填充环半径
     */
    private var fillRadius = 0f

    /**
     * 当前进度
     */
    private val progress = 0f

    /**
     * 最大进度(默认为100)
     */
    private val max = 100f

    /**
     * 内圆画笔
     */
    private val circlePaint by lazy { Paint() }

    /**
     * 外环画笔
     */
    private val borderPaint by lazy { Paint() }

    /**
     * 进度条画笔
     */
    private val progressPaint by lazy { Paint() }

    /**
     * 两圆之间的填充环画笔
     */
    private val fillPaint by lazy { Paint() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        //自定义属性的默认值都在此处修改
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.annular)
        borderWidth = typedArray.getDimension(R.styleable.annular_borderWidth, 5f)
        progressWidth = typedArray.getDimension(R.styleable.annular_progressbarWidth, 10f)
        borderColor = typedArray.getColor(R.styleable.annular_borderColor, 0xFF000000.toInt())
        progressColor = typedArray.getColor(R.styleable.annular_progressbarColor,
                0xFFFFFFFF.toInt())
        circleColor = typedArray.getColor(R.styleable.annular_circleColor, 0xFF000000.toInt())
        @Suppress("RemoveRedundantCallsOfConversionMethods")
        fillColor = typedArray.getColor(R.styleable.annular_fillColor, 0x00000000.toInt())
        space = typedArray.getDimension(R.styleable.annular_space, 20f)
        typedArray.recycle()
    }

    //onMeasure无自定义逻辑
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    fun updateParams() {

    }


}