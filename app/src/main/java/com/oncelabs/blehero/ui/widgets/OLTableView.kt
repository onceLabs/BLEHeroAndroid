package com.oncelabs.blehero.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.oncelabs.blehero.R


class OLTableView : LinearLayout {

    constructor (context: Context) : super(context)
    constructor (context: Context?, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        this.orientation = VERTICAL
        this.showDividers = SHOW_DIVIDER_MIDDLE
        this.dividerPadding = 32

        this.dividerDrawable = ContextCompat.getDrawable(context, R.drawable.ic_divider)

        this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                handleChildTouch(event.y, true, false)
//                return true
            }
            MotionEvent.ACTION_UP -> {
                handleChildTouch(event.y, false, true)
//                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                handleChildTouch(event.y, false, false)
//                return true
            }
        }
        return true
    }

    var touchedChild: View? = null
    private fun handleChildTouch(touchY: Float, show: Boolean, click: Boolean){
        val childHeight = this.height / childCount
        var touchedWindow = 0

        if(show && touchedChild == null){
            children.forEach {
                touchedWindow += childHeight
                if(touchY < touchedWindow && touchedChild == null){
                    touchedChild = it
                    it.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonHighlighted))
                }
            }
        }
        else{
            touchedChild?.let{
                it.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
                if(click){
                    it.performClick()
                }
                touchedChild = null
            }
        }

    }

    private var rectF: RectF? = null
    private val path = Path()
    private val cornerRadius = 50f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()
    }

    override fun draw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(save)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    private fun resetPath() {
        path.reset()
        rectF?.let{
            path.addRoundRect(it, cornerRadius, cornerRadius, Path.Direction.CW)
        }
        path.close()
    }

}