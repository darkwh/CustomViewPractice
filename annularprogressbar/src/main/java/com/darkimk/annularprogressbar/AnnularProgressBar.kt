package com.darkimk.annularprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
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
     * 当前进度的文字颜色
     */
    private var progressTextColor = 0

    /**
     * 总进度的文字颜色
     */
    private var totalTextColor = 0

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
    private var fillWidth = 0f

    /**
     * 外环半径
     */
    private var borderRadius = 0f

    /**
     * 进度条所在圆半径
     */
    private var progressRadius = 0f

    /**
     * 填充环半径
     */
    private var fillRadius = 0f

    /**
     * 内圆半径
     */
    private var circleRadius = 0f

    /**
     * 当前进度
     */
    private var progress = 0

    /**
     * 最大进度(默认为100)
     */
    private var max = 100

    /**
     * 动画持续时间
     */
    private val mDuration = 0f

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

    /**
     * 当前进度的文字画笔
     */
    private val progressTextPaint by lazy { Paint() }

    /**
     * 总进度的文字画笔
     */
    private val totalTextPaint by lazy { Paint() }

    /**
     * 画圆弧需要的RectF对象
     */
    private val oval by lazy { RectF() }

    /**
     * 进度条圆弧角度
     */
    private var angle = 0f

    /**
     * 当前进度文字的宽度
     */
    private var progressTextWidth = 0f

    /**
     * 总进度文字的宽度
     */
    private var totalTextWidth = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        //自定义属性的默认值都在此处修改
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.annular)
        borderWidth = typedArray.getDimension(R.styleable.annular_borderWidth, 5f)
        progressWidth = typedArray.getDimension(R.styleable.annular_progressbarWidth, 10f)
        progressTextSize = typedArray.getDimension(R.styleable.annular_progressTextSize, 18f)
        totalTextSize = typedArray.getDimension(R.styleable.annular_totalTextSize, 16f)
        borderColor = typedArray.getColor(R.styleable.annular_borderColor, 0xFF000000.toInt())
        progressColor = typedArray.getColor(R.styleable.annular_progressbarColor,
                0xFFFFFFFF.toInt())
        circleColor = typedArray.getColor(R.styleable.annular_circleColor, 0xFF000000.toInt())
        @Suppress("RemoveRedundantCallsOfConversionMethods")
        fillColor = typedArray.getColor(R.styleable.annular_fillColor, 0x00000000.toInt())
        progressTextColor = typedArray.getColor(R.styleable.annular_progressTextColor,
                0xFFFFFFFF.toInt())
        totalTextColor = typedArray.getColor(R.styleable.annular_totalTextColor, 0xFFFFFFFF.toInt())
        fillWidth = typedArray.getDimension(R.styleable.annular_fillWidth, 20f)
        progress = typedArray.getInt(R.styleable.annular_progress, 0)
        max = typedArray.getInt(R.styleable.annular_max, 100)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        calculateParams()
        canvas?.drawCircle(centerX, centerY, circleRadius, circlePaint)
        canvas?.drawCircle(centerX, centerY, borderRadius, borderPaint)
        canvas?.drawCircle(centerX, centerY, fillRadius, fillPaint)
        canvas?.drawArc(oval, -90f, angle, false, progressPaint)
        canvas?.drawText(progress.toString(),
                centerX - (width - progressTextWidth - totalTextWidth) / 2, centerY, progressPaint)
        canvas?.drawText("/" + max, centerX + progressTextWidth / 2, centerY, totalTextPaint)
    }

    /**
     * 计算各属性值
     */
    private fun calculateParams() {
        centerX = width.toFloat() / 2
        centerY = height.toFloat() / 2
        borderRadius = (width - borderWidth) / 2
        progressRadius = (width - progressWidth) / 2
        fillRadius = borderRadius - borderWidth / 2
        circleRadius = fillRadius - fillWidth / 2
        oval.set(progressWidth / 2, progressWidth / 2,
                width - progressWidth / 2, height - progressWidth / 2)
        angle = 360f * progress / max
        progressTextWidth = progressPaint.measureText(progress.toString())
        totalTextWidth = totalTextPaint.measureText("/" + max)
        setPaints()
    }

    /**
     * 设置画笔
     */
    private fun setPaints() {
        //内圆
        circlePaint.color = circleColor
        //外环
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.color = borderColor
        //填充环
        fillPaint.style = Paint.Style.STROKE
        fillPaint.strokeWidth = fillWidth
        fillPaint.color = fillColor
        //进度条
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = progressWidth
        progressPaint.color = progressColor
        //当前进度
        progressTextPaint.color = progressTextColor
        progressTextPaint.textSize = progressTextSize
        progressTextPaint.textAlign = Paint.Align.CENTER
        //总进度
        totalTextPaint.color = totalTextColor
        totalTextPaint.textSize = totalTextSize
    }

    /**
     * 设置进度
     */
    fun setProgress() {

    }

    /**
     * 设置进度并开始动画
     */
    fun setProgressWithAnimation() {

    }

}