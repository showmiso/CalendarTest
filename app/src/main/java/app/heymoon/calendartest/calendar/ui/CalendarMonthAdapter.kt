package app.heymoon.calendartest.calendar.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

        override fun onClick(p0: View?) {
            onClickListener.onClickListener(adapterPosition)
        }
    }

    interface OnMonthCalendarListener {
        fun onClickListener(position: Int)
    }
}