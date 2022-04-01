package app.heymoon.calendartest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.heymoon.calendartest.calendar.model.CalendarDay
import app.heymoon.calendartest.calendar.model.DayOwner
import app.heymoon.calendartest.calendar.ui.CalendarAdapter
import app.heymoon.calendartest.databinding.FragmentTodayCalendarBinding
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields

class TodayCalendarFragment : Fragment(), CalendarAdapter.OnItemClickListener {

    private var _binding: FragmentTodayCalendarBinding? = null
    private val binding get() = _binding!!

    private val detectorCompat by lazy {
        GestureDetectorCompat(requireContext(), onGestureListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTodayCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUi() {
        binding.tvCalendar.setOnClickListener {
            binding.rcvCalendar.smoothScrollToPosition(5)
        }
//        binding.viewCardAll.setOnTouchListener { view, motionEvent ->
//            return@setOnTouchListener detectorCompat.onTouchEvent(motionEvent)
//        }
        // recyclerview
        val adapter = CalendarAdapter(this)
        binding.rcvCalendar.adapter = adapter
        binding.rcvCalendar.layoutManager =
            StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rcvCalendar)
        // recyclerview 에 gesture 를 붙여봄
        binding.rcvCalendar.addOnItemTouchListener(onItemTouchListener)
//        createCalendar()
    }

    override fun onItemClick(toggle: Boolean) {
//        if (toggle) {
//            binding.rcvCalendar.layoutManager = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL)
//        } else {
//            binding.rcvCalendar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
    }

    private val onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            detectorCompat.onTouchEvent(e)
            return false
        }
    }

    private val onGestureListener = object : GestureDetector.SimpleOnGestureListener() {
//        override fun onDown(p0: MotionEvent?): Boolean {
//            Timber.i("onGestureListener onDown")
//            return true
//        }
//
//        override fun onShowPress(p0: MotionEvent?) {
//            Timber.i("onGestureListener onShowPress")
//        }
//
//        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
//            Timber.i("onGestureListener onSingleTapUp")
//            return true
//        }
//
//        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
//            Timber.i("onGestureListener onScroll")
//            return true
//        }
//
//        override fun onLongPress(p0: MotionEvent?) {
//            Timber.i("onGestureListener onScroll")
//        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Timber.i("onGestureListener onFling")
            Log.d("test", "onGestureListener onFling $velocityX $velocityY")
            return true
        }
    }

    private fun createCalendar() {
        val yearMonth = YearMonth.of(2022, 3)

    }

    private fun getOneMonth(yearMonth: YearMonth): List<List<CalendarDay>> {
        val year = yearMonth.year
        val month = yearMonth.monthValue
        val thisMonthDays = (1..yearMonth.lengthOfMonth()).map { day ->
            val localDate = LocalDate.of(year, month, day)
            CalendarDay(localDate, DayOwner.THIS_MONTH)
        }
        // 한 주의 시작은 일요일이고, 한 주를 구성하기 위해 최소 1일 필요하다.
        val weekdayOfMonthField = WeekFields.of(DayOfWeek.SUNDAY, 1).weekOfMonth()
        val weekOfMonth = thisMonthDays.groupBy {
            it.date.get(weekdayOfMonthField)
        }.values.toMutableList()
        // previous month
        val firstWeek = weekOfMonth.first()
        if (firstWeek.size < 7) {
            val previousMonth = yearMonth.minusMonths(1)
            val previousMonthDatesForFirstWeek = (1..previousMonth.lengthOfMonth()).toList()
                .takeLast(7 - firstWeek.size).map { day ->
                    val localDate = LocalDate.of(previousMonth.year, previousMonth.month, day)
                    CalendarDay(localDate, DayOwner.PREVIOUS_MONTH)
                }
            weekOfMonth[0] = previousMonthDatesForFirstWeek + firstWeek
        }
        // next month
        val lastWeek = weekOfMonth.last()
        if (lastWeek.size < 7) {
            val lastDay = lastWeek.last()
            val nextMonthDatesForLastWeek = (1..(7 - lastWeek.size)).map { index ->
                val localDate = lastDay.date.plusDays(index.toLong())
                CalendarDay(localDate, DayOwner.NEXT_MONTH)
            }
            weekOfMonth[weekOfMonth.lastIndex] = lastWeek + nextMonthDatesForLastWeek
        }
        // week 높이를 6으로 맞춘다.
        if (weekOfMonth.size < 6) {
            val lastDay = weekOfMonth.last().last()
            val lastWeekDates = (1..7).map { index ->
                val localDate = lastDay.date.plusDays(index.toLong())
                CalendarDay(localDate, DayOwner.NEXT_MONTH)
            }
            weekOfMonth.add(lastWeekDates)
        }
        return weekOfMonth.toList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}