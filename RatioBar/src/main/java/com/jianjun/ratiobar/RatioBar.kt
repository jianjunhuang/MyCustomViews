package com.jianjun.ratiobar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jianjun.base.dp

class RatioBar : View {

    private val borderWidth = 6f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val leftPath = Path()
    private val rightPath = Path()
    private val pathArcRectF = RectF()

    private val leftColor = Color.parseColor("#CCCCCC")
    private val rightColor = Color.parseColor("#808080")

    private val leftTextColor = Color.BLACK
    private val rightTextColor = Color.WHITE

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //比率
    var ratio = 0.2f
        set(value) {
            field = value
            updatePath()
            invalidate()
        }

    //突出的长度
    private val overlapSize = 20f.dp

    //相距地距离
    private val pathDis = 10f.dp

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        paint.color = Color.BLACK
        paint.pathEffect = CornerPathEffect(5f.dp)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updatePath()
    }

    private fun updatePath() {
        val triangleSize = 11f.dp
        val triangleHeight = 8f.dp

        pathArcRectF.set(
            borderWidth,
            borderWidth,
            height.toFloat() - borderWidth - triangleHeight,
            height.toFloat() - borderWidth - triangleHeight
        )
        leftPath.reset()
        //from left top , CW
        leftPath.moveTo(height.toFloat() / 2, pathArcRectF.top)
        leftPath.lineTo(width.toFloat() * ratio - pathDis / 2, pathArcRectF.top)
        leftPath.lineTo(
            width * ratio - overlapSize - pathDis / 2,
            pathArcRectF.bottom
        )
        val leftCenter = (width * ratio - pathDis / 2f) / 2f
        //add triangle
        leftPath.lineTo(leftCenter + triangleSize / 2, pathArcRectF.bottom)
        leftPath.lineTo(leftCenter, height - borderWidth)
        leftPath.lineTo(leftCenter - triangleSize / 2, pathArcRectF.bottom)
        leftPath.lineTo(height.toFloat() / 2, pathArcRectF.bottom)
        leftPath.arcTo(
            pathArcRectF, 90f, 180f, false
        )
        leftPath.close()

        pathArcRectF.offsetTo(width - pathArcRectF.width() - borderWidth, pathArcRectF.top)
        rightPath.reset()
        rightPath.moveTo(pathArcRectF.left, pathArcRectF.top)
        rightPath.lineTo(width * ratio + pathDis / 2, pathArcRectF.top)
        rightPath.lineTo(width * ratio - overlapSize + pathDis / 2, pathArcRectF.bottom)
        val rightCenter = width - (width - (width * ratio - overlapSize + pathDis / 2)) / 2f
        rightPath.lineTo(rightCenter - triangleSize / 2, pathArcRectF.bottom)
        rightPath.lineTo(rightCenter, height - borderWidth)
        rightPath.lineTo(rightCenter + triangleSize / 2, pathArcRectF.bottom)
        rightPath.lineTo(pathArcRectF.left, pathArcRectF.bottom)
        rightPath.arcTo(pathArcRectF, 90f, -180f, false)
        rightPath.close()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        paint.color = leftColor
        canvas?.drawPath(leftPath, paint)
        paint.color = rightColor
        canvas?.drawPath(rightPath, paint)

        //border
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        canvas?.drawPath(leftPath, paint)
        canvas?.drawPath(rightPath, paint)
    }
}