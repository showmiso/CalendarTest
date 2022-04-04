package app.heymoon.calendartest.calendar.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class SwipeScrollView : ScrollView {

    private var swipeTouchListener: OnSwipeTouchListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        swipeTouchListener?.onTouch(this, ev).let {
            return it ?: false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        super.onTouchEvent(ev)
        return true
    }

    fun setListener(listener: OnSwipeTouchListener) {
        swipeTouchListener = listener
    }
}