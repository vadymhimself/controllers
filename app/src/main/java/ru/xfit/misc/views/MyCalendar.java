package ru.xfit.misc.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.xfit.R;
import ru.xfit.misc.utils.CalendarUtils;

/**
 * Created by TESLA on 31.10.2017.
 */

public class MyCalendar extends LinearLayout {
    private static final int SUNDAY = 1;
    private static final int MONDAY = 2;
    private static final int TUESDAY = 4;
    private static final int WEDNESDAY = 8;
    private static final int THURSDAY = 16;
    private static final int FRIDAY = 32;
    private static final int SATURDAY = 64;

    private static final int[] FLAGS = new int[]{
            SUNDAY,
            MONDAY,
            TUESDAY,
            WEDNESDAY,
            THURSDAY,
            FRIDAY,
            SATURDAY
    };

    private static final int[] WEEK_DAYS = new int[]{
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };

    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
    int SCROLL_STATE_SETTLING = 2;
    boolean USE_CACHE = false;

    int MIN_DISTANCE_FOR_FLING = 25; // dips
    int DEFAULT_GUTTER_SIZE = 16; // dips
    int MIN_FLING_VELOCITY = 400; // dips
    int CLOSE_ENOUGH = 2; // dp

    int INVALID_POINTER = -1;

    private Context context;
    private Calendar calendar;
    private Calendar prevCalendar;
    private View currentView;
    private RelativeLayout prevMonth;
    private RelativeLayout nextMonth;

    private boolean isCommonDay;
    private boolean isOverflowDateVisible = true;
    private int[] totalDayOfWeekend;

    private LinearLayout daysView;
    private TextView monthTv;

    private int currentMonthIndex;
    private int firstDayOfWeek;
    private int weekendDays;

    private float lastMotionX;
    private float lastMotionY;
    private float initialMotionX;
    private float initialMotionY;

    private Scroller scroller;

    private boolean scrollingCacheEnabled;
    private boolean isBeingDragged;
    private boolean isUnableToDrag;
    private GestureDetectorCompat gestureDetector;
    private int minimumVelocity;
    private int maximumVelocity;
    private int flingDistance;
    private int closeEnough;
    private int defaultGutterSize;
    private int touchSlop;
    private VelocityTracker velocityTracker;

    private int scrollState = SCROLL_STATE_IDLE;

    private final Runnable endScrollRunnable = () -> setScrollState(SCROLL_STATE_IDLE);

    private int activePointerId = INVALID_POINTER;

    @Nullable
    private OnMonthTitleClickListener onMonthTitleClickListener;

    @Nullable
    private OnMonthChangeListener onMonthChangeListener;

    public MyCalendar(Context context) {
        super(context);
        init(context);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.calendar = Calendar.getInstance(Locale.getDefault());
        this.prevCalendar = Calendar.getInstance(Locale.getDefault());

        currentView = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
        daysView = (LinearLayout) currentView.findViewById(R.id.days_view);
        monthTv = (TextView) currentView.findViewById(R.id.month);

        prevMonth = (RelativeLayout) currentView.findViewById(R.id.prev_month);
        prevMonth.setOnClickListener(this::onBackButtonClick);

        nextMonth = (RelativeLayout) currentView.findViewById(R.id.next_month);
        nextMonth.setOnClickListener(this::onNextButtonClick);

        firstDayOfWeek = Calendar.MONDAY;
        this.weekendDays = 65;

        initTouchVariables();

        refreshCalendar(Calendar.getInstance(getLocale()));
    }

    private void initTouchVariables() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        final float density = getContext().getResources().getDisplayMetrics().density;

        gestureDetector = new GestureDetectorCompat(getContext(), new CalendarGestureDetector());
        scroller = new Scroller(getContext(), null);

        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        minimumVelocity = (int) (MIN_FLING_VELOCITY * density);
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
        flingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
        closeEnough = (int) (CLOSE_ENOUGH * density);
        defaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
    }

    public void onTitleClick() {
        if (onMonthTitleClickListener != null) {
            onMonthTitleClickListener.onMonthTitleClick(calendar.getTime());
        }
    }

    public void onNextButtonClick(@NonNull View v) {
        currentMonthIndex++;
        updateCalendarOnTouch();
    }

    public void onBackButtonClick(@NonNull View v) {
        currentMonthIndex--;
        updateCalendarOnTouch();
    }

    private void updateCalendarOnTouch() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, currentMonthIndex);

        update(calendar);

        if (onMonthChangeListener != null) {
            onMonthChangeListener.onMonthChange(calendar.getTime());
        }
    }

    private void drawAdapterView() {
        final List<Day> days = CalendarUtils.obtainDays(calendar, currentMonthIndex);

        daysView.removeAllViews();

        int size = days.size();
        DayView dayView;
        LinearLayout weekContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekContainer.setLayoutParams(lp);

        for (int i = 0; i < size; i++) {
            Day day = days.get(i);

            if (i == 7 || i == 14 || i == 21 || i == 28 || i == 35 || i == 42) {
                daysView.addView(weekContainer);
                weekContainer = new LinearLayout(context);
                weekContainer.setLayoutParams(lp);
            }

            dayView = new DayView(context);
            weekContainer.addView(dayView);

            dayView.bind(day.toDate());
            dayView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams dlp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            dlp.weight = 7.0f;
            dlp.gravity = Gravity.CENTER_HORIZONTAL;
            dayView.setLayoutParams(dlp);

            if (day.isCurrentMonth()) {
                isCommonDay = true;

                if (totalDayOfWeekend.length != 0) {
                    final Calendar calendar = day.toCalendar(Locale.getDefault());

                    for (int weekend : totalDayOfWeekend) {
                        if (weekend == calendar.get(Calendar.DAY_OF_WEEK)) {
                            dayView.setTextColor(context.getResources().getColor(R.color.calendarWeekend));
                            dayView.setBold();
                            isCommonDay = false;
                        }
                    }
                }

                if(isCommonDay) {
                    dayView.setTextColor(context.getResources().getColor(R.color.calendarCommonDay));
                }

                if (day.isCurrentDay()) {
                    if (CalendarUtils.isToday(day.toCalendar())) {
                        dayView.setTextColor(context.getResources().getColor(R.color.calendarCurrentDay));
                        dayView.setTextBg(R.drawable.calendar_current_day_bg);
                    }
                }

            } else {
                dayView.setTextColor(context.getResources().getColor(R.color.calendarDisableDay));

                if (!isOverflowDateVisible)
                    dayView.setVisibility(View.INVISIBLE);
                else {
                    dayView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public Locale getLocale() {
        return this.context.getResources().getConfiguration().locale;
    }

    public void refreshCalendar(Calendar calendar) {
        this.calendar = calendar;
        this.calendar.setFirstDayOfWeek(Calendar.MONDAY);

        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);

        currentMonthIndex = (prevCalendar.get(Calendar.YEAR) - y) * 12 + (prevCalendar.get(Calendar.MONTH) - m);

        Calendar calendarUpdate = Calendar.getInstance(Locale.getDefault());
        calendarUpdate.add(Calendar.MONTH, currentMonthIndex);

        update(calendarUpdate);

        prevCalendar = calendar;
    }

    public void update(@NonNull Calendar calender) {
        calendar = calender;
        calendar.setFirstDayOfWeek(firstDayOfWeek);

        calculateWeekEnds();

        final SimpleDateFormat format = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        String date = format.format(calendar.getTime());
        monthTv.setText(date);
        monthTv.setAllCaps(true);

        drawAdapterView();
    }

    private void calculateWeekEnds() {
        totalDayOfWeekend = new int[2];
        int weekendIndex = 0;

        for (int i = 0; i < FLAGS.length; i++) {
            boolean isContained = containsFlag(this.weekendDays, FLAGS[i]);

            if (isContained) {
                totalDayOfWeekend[weekendIndex] = WEEK_DAYS[i];
                weekendIndex++;
            }
        }
    }

    private boolean containsFlag(int flagSet, int flag) {
        return (flagSet | flag) == flagSet;
    }

    public class CalendarGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > touchSlop && Math.abs(velocityX) > minimumVelocity && Math.abs(velocityX) < maximumVelocity) {
                        if (e2.getX() - e1.getX() > flingDistance) {
                            currentMonthIndex--;
                            updateCalendarOnTouch();

                        } else if (e1.getX() - e2.getX() > flingDistance) {
                            currentMonthIndex++;
                            updateCalendarOnTouch();
                        }
                    }
                }

                return true;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < defaultGutterSize && dx > 0) || (x > getWidth() - defaultGutterSize && dx < 0);
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dx, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (scrollingCacheEnabled != enabled) {
            scrollingCacheEnabled = enabled;

            if (USE_CACHE) {
                final int size = getChildCount();
                for (int i = 0; i < size; ++i) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        child.setDrawingCacheEnabled(enabled);
                    }
                }
            }
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = scrollState == SCROLL_STATE_SETTLING;
        if (needPopulate) {
            // Done with scroll, no longer want to cache view drawing.
            setScrollingCacheEnabled(false);
            scroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
        }

        if (needPopulate) {
            if (postEvents) {
                ViewCompat.postOnAnimation(this, endScrollRunnable);
            } else {
                endScrollRunnable.run();
            }
        }
    }

    private void setScrollState(int newState) {
        if (scrollState == newState) {
            return;
        }

        scrollState = newState;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();

        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            isBeingDragged = false;
            isUnableToDrag = false;
            activePointerId = INVALID_POINTER;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                velocityTracker = null;
            }
            return false;
        }

        // Nothing more to do here if we have decided whether or not we
        // are dragging.
        if (action != MotionEvent.ACTION_DOWN) {
            if (isBeingDragged) {
                return true;
            }

            if (isUnableToDrag) {
                return false;
            }
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                /*
                 * isBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. lastMotionY is set to the y value
                * of the down event.
                */
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float dx = x - lastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = Math.abs(y - initialMotionY);

                if (dx != 0 && !isGutterDrag(lastMotionX, dx) &&
                        canScroll(this, false, (int) dx, (int) x, (int) y)) {
                    // Nested view has scrollable area under this point. Let it be handled there.
                    lastMotionX = x;
                    lastMotionY = y;
                    isUnableToDrag = true;
                    return false;
                }
                if (xDiff > touchSlop && xDiff * 0.5f > yDiff) {
                    isBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                    lastMotionX = dx > 0 ? initialMotionX + touchSlop :
                            initialMotionX - touchSlop;
                    lastMotionY = y;
                    setScrollingCacheEnabled(true);
                } else if (yDiff > touchSlop) {
                    // The finger has moved enough in the vertical
                    // direction to be counted as a drag...  abort
                    // any attempt to drag horizontally, to work correctly
                    // with children that have scrolling containers.
                    isUnableToDrag = true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                lastMotionX = initialMotionX = ev.getX();
                lastMotionY = initialMotionY = ev.getY();
                activePointerId = MotionEventCompat.getPointerId(ev, 0);
                isUnableToDrag = false;

                scroller.computeScrollOffset();
                if (scrollState == SCROLL_STATE_SETTLING &&
                        Math.abs(scroller.getFinalX() - scroller.getCurrX()) > closeEnough) {
                    isBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                } else {
                    completeScroll(false);
                    isBeingDragged = false;
                }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
//                onSecondaryPointerUp(ev);
                break;
        }

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(ev);

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return isBeingDragged;
    }
}
