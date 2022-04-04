package app.heymoon.calendartest.calendar.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ScrollView

class SwipeScrollView : ScrollView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )



}