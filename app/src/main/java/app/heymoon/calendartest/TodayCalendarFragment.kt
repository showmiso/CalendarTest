package app.heymoon.calendartest

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import app.heymoon.calendartest.calendar.TopSheetGesture
import app.heymoon.calendartest.calendar.model.CalendarDay
import app.heymoon.calendartest.calendar.model.DayOwner
import app.heymoon.calendartest.calendar.ui.CalendarAdapter
import app.heymoon.calendartest.calendar.ui.CalendarMonthAdapter
import app.heymoon.calendartest.calendar.ui.CalendarWeekAdapter
import app.heymoon.calendartest.calendar.ui.OnSwipeTouchListener
import app.heymoon.calendartest.databinding.FragmentTodayCalendarBinding
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields

class TodayCalendarFragment : Fragment() {

    private var _binding: FragmentTodayCalendarBinding? = null
    private val binding get() = _binding!!

    private val detectorCompat by lazy {
        GestureDetectorCompat(requireContext(), onGestureListener)
    }
//    private val topSheetGesture by lazy {
//        TopSheetGesture(requireContext(), binding.layoutFragmentTodayCalendar)
//    }

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
        binding.switchAnimationTest.setOnCheckedChangeListener { compoundButton, isChecked ->
//            updateRecyclerViewHeight(!isChecked)
            updateViewAnimation()
        }
//        binding.viewCardAll.setOnTouchListener { view, motionEvent ->
//            return@setOnTouchListener detectorCompat.onTouchEvent(motionEvent)
//        }
        // recyclerview
//        val adapter = CalendarAdapter(onCalendarListener)
        val monthAdapter = CalendarMonthAdapter(getSwipe(requireContext()), onMonthClickListener)
        binding.rcvCalendar.adapter = monthAdapter
        binding.rcvCalendar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            GridLayoutManager(requireContext(), 6, GridLayoutManager.HORIZONTAL, false)
        // StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL)
        // week
        val weekAdapter = CalendarWeekAdapter()
        binding.rcvWeekCalendar.adapter = weekAdapter
        binding.rcvWeekCalendar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rcvCalendar)
        val snapHelper2 = PagerSnapHelper()
        snapHelper2.attachToRecyclerView(binding.rcvWeekCalendar)
        // recyclerview 에 gesture 를 붙여봄
        binding.rcvCalendar.addOnItemTouchListener(onItemTouchListener)
        binding.rcvWeekCalendar.addOnItemTouchListener(onItemTouchListener)
//        binding.rcvCalendar.setOnTouchListener(topSheetGesture)
//        createCalendar()

        // all touch listener
//        binding.layoutFragmentTodayCalendar.apply {
//            setOnTouchListener(topSheetGesture)
//            setOnClickListener {  }
//        }
    }

    private fun getSwipe(context: Context) : OnSwipeTouchListener {
        return object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                Timber.d("onSwipeLeft")
                Log.d("test","onSwipeLeft")
            }

            override fun onSwipeRight() {
                Timber.d("onSwipeRight")
                Log.d("test","onSwipeRight")
            }

            override fun onSwipeTop() {
                Timber.d("onSwipeTop")
                Log.d("test","onSwipeTop")
            }

            override fun onSwipeBottom() {
                Timber.d("onSwipeBottom")
                Log.d("test","onSwipeBottom")
            }
        }
    }

    private val onMonthClickListener = object : CalendarMonthAdapter.OnMonthCalendarListener {
        override fun onClickListener(index: Int, position: Int) {
            val translateToWeekIndex = position * 6 + index
            binding.rcvWeekCalendar.scrollToPosition(translateToWeekIndex)
        }

        override fun onFinishedCollapse() {
            Log.d("test","onFinishedCollapse")
        }

        override fun onFinishedExpand() {
            Log.d("test","onFinishedExpand")
        }
    }

    private fun updateRecyclerViewHeight(isChecked: Boolean) {
        // change height
        val itemHeight = resources.getDimension(R.dimen.height_week_item)
        val startValue = itemHeight.toInt()
        val endValue = itemHeight.toInt() * 6
        val animator = if (isChecked) {
            ValueAnimator.ofInt(startValue, endValue)
        } else {
            ValueAnimator.ofInt(endValue, startValue)
        }
        animator.addUpdateListener { animator ->
            binding.layoutOverRecyclerview.updateLayoutParams {
                height = animator.animatedValue as Int
            }
        }
        animator.doOnEnd {
            val animator2 = ObjectAnimator.ofFloat(binding.rcvCalendar, "translationY", 0f)
            animator2.duration = 0
            animator2.start()
            endOfAnimation(isChecked)
        }
        animator.doOnStart {
            if (isChecked) {
                endOfAnimation(isChecked)
            }
        }
        animator.duration = 1000
        animator.start()

        // move animation
        val value = if (isChecked) { 0f } else { itemHeight * -1 }
        val animator1 = ObjectAnimator.ofFloat(binding.rcvCalendar, "translationY", value)
        animator1.doOnEnd {
//            endOfAnimation(isChecked)
        }
        animator1.duration = 1000
        animator1.start()

//        // animation merge test
//        val translateY = PropertyValuesHolder.ofFloat(
//            View.TRANSLATION_Y, value
//        )
//        val animator2 = ObjectAnimator.ofPropertyValuesHolder(binding.rcvCalendar, translateY)
//        animator2.addUpdateListener {
//            binding.rcvCalendar.updateLayoutParams {
//                height = animator.animatedValue as Int
//            }
//        }
    }

    private fun updateViewAnimation() {
//        val animation = TranslateAnimation(0f, 0f, 100f, 200f)
//        animation.duration = 1000
//        binding.rcvCalendar.startAnimation(animation)
    }


    // recyclerview 에 gesture 를 붙임
    private val onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(v: RecyclerView, event: MotionEvent): Boolean {
            detectorCompat.onTouchEvent(event)
            return false
        }
    }

    private fun endOfAnimation(isChecked: Boolean) {
        if (isChecked) {
            binding.rcvCalendar.layoutManager = GridLayoutManager(requireContext(), 6, GridLayoutManager.HORIZONTAL, false)
        // StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL)
        } else {
            binding.rcvCalendar.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private val onCalendarListener = object : CalendarAdapter.OnItemClickListener {
        override fun onItemClick(toggle: Boolean) {
//            if (toggle) {
//                binding.rcvCalendar.layoutManager = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL)
//            } else {
//                binding.rcvCalendar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            }
        }

        override fun onItemCheckedChanged(checkedPosition: Int) {
        }
    }

    private val onGestureListener = object : GestureDetector.SimpleOnGestureListener() {
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