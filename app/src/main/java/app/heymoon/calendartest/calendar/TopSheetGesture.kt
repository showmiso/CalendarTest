package app.heymoon.calendartest.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class TopSheetGesture(
    private val context: Context,
    private val layoutFragment: View
) : View.OnTouchListener {

    private var moveY = 0f
    private var eventRawY = 0f
    private var lastY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v : View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moveY = v.y - event.rawY
                lastY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                eventRawY = event.rawY
                val targetY = eventRawY + moveY
                v.animate()
                    .y(targetY)
                    .setDuration(0)
                    .start()
                layoutFragment.animate()
                    .y(targetY)
                    .setDuration(0)
                    .start()
            }
        }
        return true
    }

}