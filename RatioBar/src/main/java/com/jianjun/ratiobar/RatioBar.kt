package com.jianjun.ratiobar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jianjun.base.dp
import com.jianjun.base.sp
import java.util.*

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

    private val leftTextPointF = PointF()
    private val rightTextPointF = PointF()

    private val leftDesTextPointF = PointF()
    private val rightDesTextPointF = PointF()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val leftDesText = "left"
    private val rightDesText = "right"

    //比率
    var ratio = 0.5f
        set(value) {
            field = value
            updatePath()
            invalidate()
        }

    private var displayRatio = 0f

    //突出的长度
    private val overlapSize = 10f.dp

    //相距的距离
    private val pathDis = 5f.dp

    companion object {
        //for pos calculate
        private const val TEMP_TEXT = "10%"
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        paint.color = Color.BLACK
        paint.pathEffect = CornerPathEffect(3f.dp)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        textPaint.textSize = 14f.sp
        textPaint.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updatePath()
    }

    /**
     * 设置进度条的 path
     *
     */
    private fun updatePath() {
        val triangleSize = 11f.dp
        val triangleHeight = 6f.dp

        val desTextBound = Rect()
        textPaint.getTextBounds(leftDesText, 0, leftDesText.length, desTextBound)

        val desTextHeight = textPaint.fontMetrics.run {
            return@run bottom - top
        }

        displayRatio = ratio
        //设置最小显示范围
        //todo 根据具体宽度设置显示的比率
        if (ratio < 0.15f) {
            displayRatio = 0.15f
        }

        if (ratio > 0.75f) {
            displayRatio = 0.75f
        }

        pathArcRectF.set(
            borderWidth,
            borderWidth,
            height.toFloat() - borderWidth - triangleHeight - desTextHeight,
            height.toFloat() - borderWidth - triangleHeight - desTextHeight
        )
        leftPath.reset()
        //from left top , CW
        leftPath.moveTo(height.toFloat() / 2, pathArcRectF.top)
        leftPath.lineTo(width.toFloat() * displayRatio - pathDis / 2, pathArcRectF.top)
        leftPath.lineTo(
            width * displayRatio - overlapSize - pathDis / 2,
            pathArcRectF.bottom
        )
        val leftCenter = (width * displayRatio - pathDis / 2f) / 2f
        //add triangle
        leftPath.lineTo(leftCenter + triangleSize / 2, pathArcRectF.bottom)
        leftPath.lineTo(leftCenter, pathArcRectF.bottom + triangleHeight)
        leftPath.lineTo(leftCenter - triangleSize / 2, pathArcRectF.bottom)
        leftPath.lineTo(height.toFloat() / 2, pathArcRectF.bottom)
        leftPath.arcTo(
            pathArcRectF, 90f, 180f, false
        )
        leftPath.close()

        //left text
        val textBound = Rect()
        val lt = String.format(Locale.ENGLISH, "%.0f%%", ratio * 100)
        textPaint.getTextBounds(lt, 0, lt.length, textBound)
        leftTextPointF.set(
            leftCenter - textBound.width() / 2f,
            pathArcRectF.centerY() + textBound.height() / 2f
        )
        leftDesTextPointF.set(
            leftCenter - desTextBound.width() / 2f,
            height - (desTextHeight / 3)
        )
        if (leftDesTextPointF.x < 0) {
            leftDesTextPointF.x = 0f
        }

        pathArcRectF.offsetTo(width - pathArcRectF.width() - borderWidth, pathArcRectF.top)
        rightPath.reset()
        rightPath.moveTo(pathArcRectF.left, pathArcRectF.top)
        rightPath.lineTo(width * displayRatio + pathDis / 2, pathArcRectF.top)
        rightPath.lineTo(width * displayRatio - overlapSize + pathDis / 2, pathArcRectF.bottom)
        val rightCenter = width - (width - (width * displayRatio - overlapSize + pathDis / 2)) / 2f
        rightPath.lineTo(rightCenter - triangleSize / 2, pathArcRectF.bottom)
        rightPath.lineTo(rightCenter, pathArcRectF.bottom + triangleHeight)
        rightPath.lineTo(rightCenter + triangleSize / 2, pathArcRectF.bottom)
        rightPath.lineTo(pathArcRectF.left, pathArcRectF.bottom)
        rightPath.arcTo(pathArcRectF, 90f, -180f, false)
        rightPath.close()

        //right text
        val rt = String.format(Locale.ENGLISH, "%.0f%%", 100 - ratio * 100)
        textPaint.getTextBounds(rt, 0, rt.length, textBound)
        rightTextPointF.set(
            rightCenter - textBound.width() / 2f,
            pathArcRectF.centerY() + textBound.height() / 2f
        )
        textPaint.getTextBounds(rightDesText, 0, rightDesText.length, desTextBound)
        rightDesTextPointF.set(
            rightCenter - desTextBound.width() / 2f,
            height - (desTextHeight / 3)
        )
        if (rightDesTextPointF.x + desTextBound.width() > width) {
            rightDesTextPointF.offset(width - rightDesTextPointF.x - desTextBound.width(), 0f)
        }
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

        //text
        textPaint.color = leftTextColor
        canvas?.drawText(
            String.format(Locale.ENGLISH, "%.0f%%", ratio * 100),
            leftTextPointF.x,
            leftTextPointF.y,
            textPaint
        )
        textPaint.color = rightTextColor
        canvas?.drawText(
            String.format(Locale.ENGLISH, "%.0f%%", 100 - ratio * 100),
            rightTextPointF.x,
            rightTextPointF.y,
            textPaint
        )

        //des text
        textPaint.color = Color.BLACK
        canvas?.drawText(
            leftDesText,
            leftDesTextPointF.x,
            leftDesTextPointF.y,
            textPaint
        )
        canvas?.drawText(
            rightDesText,
            rightDesTextPointF.x,
            rightDesTextPointF.y,
            textPaint
        )
    }
}