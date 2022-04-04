package app.heymoon.calendartest.calendar.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.heymoon.calendartest.R
import app.heymoon.calendartest.databinding.ItemMonthBinding

class CalendarMonthAdapter(
    private val listener: OnSwipeTouchListener,
    private val onClickListener: OnMonthCalendarListener
) : RecyclerView.Adapter<CalendarMonthAdapter.CalendarMonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMonthViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMonthBinding.inflate(layoutInflater, parent, false)
        return CalendarMonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarMonthViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class CalendarMonthViewHolder(
        private val binding: ItemMonthBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var toggle = false

        @SuppressLint("ClickableViewAccessibility")
        fun bind() {
            var count = 0
            binding.tvWeek1.text = "$adapterPosition${++count} week"
            binding.tvWeek2.text = "$adapterPosition${++count} week"
            binding.tvWeek3.text = "$adapterPosition${++count} week"
            binding.tvWeek4.text = "$adapterPosition${++count} week"
            binding.tvWeek5.text = "$adapterPosition${++count} week"
            binding.tvWeek6.text = "$adapterPosition${++count} week"
            binding.swipeScrollView.setOnTouchListener(listener)
            binding.swipeScrollView.setListener(listener)
            binding.tvWeek1.setOnClickListener(this@CalendarMonthViewHolder)
            binding.tvWeek2.setOnClickListener(this@CalendarMonthViewHolder)
            binding.tvWeek3.setOnClickListener(this@CalendarMonthViewHolder)
            binding.tvWeek4.setOnClickListener(this@CalendarMonthViewHolder)
            binding.tvWeek5.setOnClickListener(this@CalendarMonthViewHolder)
            binding.tvWeek6.setOnClickListener(this@CalendarMonthViewHolder)
        }

        override fun onClick(view: View) {
            val index = when (view.id) {
                binding.tvWeek1.id -> 0
                binding.tvWeek2.id -> 1
                binding.tvWeek3.id -> 2
                binding.tvWeek4.id -> 3
                binding.tvWeek5.id -> 4
                binding.tvWeek6.id -> 5
                else -> 0
            }
            onClickListener.onClickListener(adapterPosition)
            toggle = !toggle
            if (toggle) {
                collapse(index)
            } else {
                expand()
            }
        }

        private fun collapse(index: Int) {
            val weekPosition = index
            val itemHeight = binding.root.resources.getDimension(R.dimen.height_week_item)
            val currentHeight = itemHeight * 6
            val targetHeight = itemHeight
            val topHeight = itemHeight * weekPosition
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    val height = if (interpolatedTime == 1f) {
                        targetHeight.toInt()
                    } else {
                        (currentHeight - ((currentHeight - targetHeight) * interpolatedTime)).toInt()
                    }
                    binding.swipeScrollView.layoutParams.height = height
                    binding.swipeScrollView.requestLayout()
                    // 스크롤
//                    if (height < topHeight + targetHeight) {
//                        val position = topHeight + targetHeight - binding.swipeScrollView.measuredHeight
//                        binding.swipeScrollView.smoothScrollTo(0, position.toInt())
//                    }
//                    binding.swipeScrollView.smoothScrollTo(0, topHeight.toInt())
                    if (interpolatedTime == 1f) {
                        // 모든 액션이 종료되었을 때 처리
                        onClickListener.onFinishedCollapse()
                    }
                }
            }
            animation.duration = 1000
            binding.swipeScrollView.startAnimation(animation)

            // scroll animation
            val scrollAnimation = ValueAnimator.ofInt(0, topHeight.toInt())
            scrollAnimation.duration = 1000
            scrollAnimation.addUpdateListener {
                val value = it.animatedValue as Int
                binding.swipeScrollView.scrollTo(0, value)
            }
            scrollAnimation.start()
        }

        private fun expand() {
            val itemHeight = binding.root.resources.getDimension(R.dimen.height_week_item)
            val currentHeight = itemHeight
            val targetHeight = itemHeight * 6
            val topHeight = itemHeight * 4
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    val height = if (interpolatedTime == 1f) {
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    } else {
                        (currentHeight - ((currentHeight - targetHeight) * interpolatedTime)).toInt()
                    }
                    binding.swipeScrollView.layoutParams.height = height
                    binding.swipeScrollView.requestLayout()
                    if (interpolatedTime == 1f) {
                        // 모든 액션이 종료되었을 때 처리
                        onClickListener.onFinishedExpand()
                    }
                }
            }
            animation.duration = 1000
            binding.swipeScrollView.startAnimation(animation)

            // scroll animation
            // 선택한 주를 기반으로 scroll 되도록 하면 위 아래가 같이 올라갔다 온다. 
            val scrollAnimation = ValueAnimator.ofInt(topHeight.toInt(), 0)
            scrollAnimation.duration = 1000
            scrollAnimation.addUpdateListener {
                val value = it.animatedValue as Int
                binding.swipeScrollView.scrollTo(0, value)
            }
            scrollAnimation.start()
        }
    }

    interface OnMonthCalendarListener {
        fun onClickListener(position: Int)
        fun onFinishedCollapse()
        fun onFinishedExpand()
    }
}