package app.heymoon.calendartest.calendar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.heymoon.calendartest.databinding.ItemWeekBinding

class CalendarAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarWeekViewHolder>() {

    private var toggle = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarWeekViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWeekBinding.inflate(layoutInflater, parent, false)
        return CalendarWeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarWeekViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 50
    }

    inner class CalendarWeekViewHolder(
        private val binding: ItemWeekBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvWeekText.text = "$adapterPosition week"
            binding.root.setOnClickListener {
                toggle = !toggle
                listener.onItemClick(toggle)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(toggle: Boolean)
    }
}