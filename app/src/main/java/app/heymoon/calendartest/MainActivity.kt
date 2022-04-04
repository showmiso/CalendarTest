package app.heymoon.calendartest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.heymoon.calendartest.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_calendar, TodayCalendarFragment())
                .addToBackStack(null)
                .commit()
        }
//        binding.viewFragmentBackground.setOnClickListener {
//            Timber.d("background click")
//        }
    }

}