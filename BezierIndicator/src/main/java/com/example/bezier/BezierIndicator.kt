package com.example.bezier

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Scroller


@Suppress("UseExpressionBody", "PrivatePropertyName")
/**
 * @author WH
 * @date 2017/11/14
 * @description
 */
class BezierIndicator(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    /**
     * 滑动第一阶段
     */
    private val MODE_ONE = 0

    /**
     * 滑动第二阶段
     */
    private val MODE_TWO = 1

    /**
     * 当前所处滑动阶段
     */
    private var currentMode = MODE_ONE

    /**
     * 向左滑动
     */
    private val moveLeft = -1

    /**
     * 向右滑动
     */
    private val moveRight = 1

    /**
     * 当前滚动方向
     */
    private var mDrection: Int = moveRight

    /**
     * 指示器的数量
     */
    private var mCount = 0

    /**
     * 当前指示器的位置
     */
    private var mPosition = 0

    /**
     * 选中圆的初始半径
     */
    private val mSelDefRadius = 30f

    /**
     * 选中圆的半径
     */
    private var mSelRadius = mSelDefRadius

    /**
     * 未选中圆半径(占位圆,类似于木桩)
     */
    private var mUnselRadius = 20f

    /**
     * 选中圆颜色
     */
    private var mSelectedColor: Int = 0xFFff6633.toInt()

    /**
     * 占位圆颜色
     */
    private var mUnSelectedColor: Int = 0xFF808080.toInt()

    /**
     * 占位圆之间的间距
     */
    private var mSpace = 60

    /**
     * 选中圆的画笔
     */
    private val mSelectedPaint: Paint by lazy { Paint() }

    /**
     * 未选中圆的画笔
     */
    private val mUnSelectedPaint: Paint by lazy { Paint() }

    /**
     * 选中圆滑动偏移量
     */
    private var mOffset = 0f

    /**
     * 迁移圆的动态半径
     */
    private var mTransCirRadius = 0f

    /**
     * 迁移圆总偏移量
     */
    private val mTransDistance = mSpace + 2 * mUnselRadius - 2 * mSelDefRadius

    /**
     * 指示器的滑动比例
     */
    private var mScale: Float = 0f

    /**
     * 贝塞尔曲线path
     */
    private val mBezierPath: Path by lazy { Path() }

    /**
     * 两边减速中间加速的插值器
     */
    private val interpolator: Interpolator by lazy { AccelerateDecelerateInterpolator() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        mSelectedPaint.style = Paint.Style.FILL
        mSelectedPaint.color = mSelectedColor
        mUnSelectedPaint.style = Paint.Style.FILL
        mUnSelectedPaint.color = mUnSelectedColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = (mUnselRadius * 2 * (mCount - 1) + mSpace * (mCount - 1)
                + mSelDefRadius * 2).toInt()
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        //绘制占位圆
        (0 until mCount)
                .map {
                    getPointX(it)
                }
                .forEach { canvas?.drawCircle(it, y, mUnselRadius, mUnSelectedPaint) }
        //绘制选中圆
        canvas?.drawCircle(getSelectedX(), y, mSelRadius, mSelectedPaint)
        //绘制迁移圆
        canvas?.drawCircle(getTransX(), y, mTransCirRadius, mSelectedPaint)
        //绘制闭合的贝塞尔曲线区域
        canvas?.drawPath(mBezierPath, mSelectedPaint)
    }

    fun attachToPager(viewPager: ViewPager) {
        setViewPagerScrollSpeed(viewPager)
        viewPager.addOnPageChangeListener(this)
        mCount = viewPager.adapter.count
        mPosition = viewPager.currentItem
    }

    /**
     * 反射改变ViewPager的内置Scroller
     */
    private fun setViewPagerScrollSpeed(viewPager: ViewPager) {
        try {
            val field = ViewPager::class.java.getDeclaredField("mScroller")
            field.isAccessible = true
            //ViewPager内置的插值器
//            val scroller = Scroller(viewPager.context, Interpolator { input ->
//                var t = input
//                t -= 1.0f
//                t * t * t * t * t + 1.0f
//            })
            //先加速后减速的插值器
            val scroller = Scroller(viewPager.context, DecelerateInterpolator())
            field.set(viewPager, scroller)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (positionOffset == 0f) {
            mPosition = position
        }
        mDrection = judgeLeftRight(position, positionOffset)
        setCurrentMode(positionOffset)
        setScale(positionOffset)
        when (mDrection) {
            moveRight -> {
                if (position + positionOffset > mPosition + 1) {
                    mPosition = position
                }
                else {
                    handlePositionOffset()
                }
            }
            moveLeft -> {
                if (position + positionOffset < mPosition - 1) {
                    mPosition = position
                }
                else {
                    handlePositionOffset()
                }
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    /**
     * 判断左右移动方向
     */
    private fun judgeLeftRight(position: Int, positionOffset: Float): Int {
        return if (position + positionOffset - mPosition >= 0) moveRight else moveLeft
    }

    /**
     * 设置当前所处滑动阶段
     */
    private fun setCurrentMode(positionOffset: Float) {
        when (mDrection) {
            moveLeft -> {
                currentMode = if (positionOffset >= 0.4) MODE_ONE else MODE_TWO
            }
            moveRight -> {
                currentMode = if (positionOffset <= 0.6) MODE_ONE else MODE_TWO
            }
        }
    }

    /**
     * 设置控制点的滑动比例
     */
    private fun setScale(positionOffset: Float) {
        if (currentMode == MODE_ONE) {
            mScale = if (mDrection == moveRight) {
                positionOffset * 5 / 6
            }
            else {
                (1 - positionOffset) * 5 / 6
            }
        }
        else {
            mScale = if (mDrection == moveRight) {
                0.5f + (positionOffset - 0.6f) * 5 / 4
            }
            else {
                0.5f + (0.4f - positionOffset) * 5 / 4
            }
        }
    }

    /**
     * 获取占位圆的圆心X坐标
     */
    private fun getPointX(index: Int): Float {
        return if (index == 0) {
            mSelDefRadius
        }
        else {
            index * mSpace + 2 * index * mUnselRadius + mSelDefRadius
        }
    }

    /**
     * 获取选中圆的圆心坐标
     */
    private fun getSelectedX(): Float {
        return if (currentMode == MODE_ONE) {
            getPointX(mPosition)
        }
        else {
            getPointX(mPosition) + mDrection * mSelDefRadius + mDrection * mOffset -
                    mDrection * (mTransDistance * (1 - mScale) + mSelDefRadius) *
                            mSelRadius / mTransCirRadius
        }
    }

    /**
     * 获取迁移圆的圆心X坐标
     */
    private fun getTransX(): Float {
        return if (currentMode == MODE_ONE) {
            getPointX(mPosition) + mDrection * mSelDefRadius +
                    mDrection * mOffset +
                    mDrection * (mSelDefRadius + mOffset) * mTransCirRadius / mSelRadius
        }
        else {
            getPointX(mPosition + mDrection)
        }
    }

    /**
     * 处理滑动进度
     */
    private fun handlePositionOffset() {
        when (currentMode) {
            MODE_ONE -> {
                mSelRadius = mSelDefRadius - (mSelDefRadius - mUnselRadius) * 2 * mScale
                mTransCirRadius = 2 * mUnselRadius * interpolator.getInterpolation(mScale)
                mOffset = mTransDistance * mScale
            }
            MODE_TWO -> {
                mSelRadius = mUnselRadius * 2 * (1 - interpolator.getInterpolation(mScale))
                mTransCirRadius = mUnselRadius +
                        (mSelDefRadius - mUnselRadius) * 2 * (mScale - 0.5f)
                mOffset = mTransDistance * mScale
            }
        }
        setPaths()
        invalidate()
    }

    private fun setPaths() {
        mBezierPath.reset()
        val mControlX = getControlX()
        val startX = getStartX()
        val startY = getStartY()
        val endX = getEndX()
        val endY = getEndY()
        mBezierPath.moveTo(startX, y - startY)
        mBezierPath.quadTo(mControlX, y, endX, y - endY)
        mBezierPath.lineTo(endX, y + endY)
        mBezierPath.quadTo(mControlX, y, startX, y + startY)
        mBezierPath.lineTo(startX, y - startY)
    }

    /**
     * 获取控制点X坐标
     */
    private fun getControlX(): Float = getPointX(mPosition) + mDrection * (mSelDefRadius + mOffset)

    /**
     * 获取数据点(开始点)X坐标
     */
    private fun getStartX(): Float {
        return getSelectedX() + mSelRadius * mSelRadius / (getControlX() - getSelectedX())
    }

    /**
     * 获取数据点(开始点)Y距离
     */
    private fun getStartY(): Float {
        return Math.sqrt((mSelRadius * mSelRadius - (getStartX() - getSelectedX()) *
                (getStartX() - getSelectedX())).toDouble()).toFloat()
    }

    /**
     * 获取数据点(结束点)X坐标
     */
    private fun getEndX(): Float {
        return getTransX() - mTransCirRadius * mTransCirRadius / (getTransX() - getControlX())
    }

    /**
     * 获取数据点(结束点)Y距离
     */
    private fun getEndY(): Float {
        return Math.sqrt((mTransCirRadius * mTransCirRadius - (getTransX() - getEndX()) *
                (getTransX() - getEndX())).toDouble()).toFloat()
    }

}
