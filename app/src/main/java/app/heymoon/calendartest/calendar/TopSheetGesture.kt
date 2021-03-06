package app.heymoon.calendartest.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GestureDetectorCompat
import app.heymoon.calendartest.calendar.ui.OnSwipeGestureListener
import timber.log.Timber

class TopSheetGesture(
    private val context: Context,
    private val layoutFragment: View,
) : View.OnTouchListener, OnSwipeGestureListener() {

    private val detector by lazy {
        GestureDetectorCompat(context, this)
    }
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
                // v 가 아니라 Fragment 전체를 움직여야 한다.
                layoutFragment.animate()
                    .y(targetY)
                    .setDuration(0)
                    .start()
            }
            MotionEvent.ACTION_UP -> {
                eventRawY = event.rawY
                val dy = eventRawY - lastY


            }
        }
        return detector.onTouchEvent(event)
    }

    override fun onSwipeTop() {
        Timber.d("onSwipeTop")
        Log.d("test","onSwipeTop")
        // 월간 -> 주간
    }

    override fun onSwipeBottom() {
        Timber.d("onSwipeBottom")
        Log.d("test","onSwipeBottom")
        // 주간 -> 월간
        /**
         * 주간 캘린더 Gone 시키고 animation 시작하기?
         * 또는 해당 위치로 이동한 다음에 Gone 시키고 위치 복구
         */
    }
}