package ru.xfit.misc.adapters;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import android.widget.SearchView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import com.molo17.customizablecalendar.library.components.CustomizableCalendar;
import com.molo17.customizablecalendar.library.interactors.AUCalendar;
import com.molo17.customizablecalendar.library.model.Calendar;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import ru.xfit.R;
import ru.xfit.databinding.PopupRecyclerBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.CircleTransform;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.misc.utils.validation.*;
import ru.xfit.misc.views.*;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.common.Image;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.screens.DateChangeListener;
import ru.xfit.screens.FeedbackController;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.clubs.AboutClubController;
import ru.xfit.screens.clubs.ClubsFilter;
import ru.xfit.screens.clubs.SuspendCardController;
import ru.xfit.screens.contacts.ContactVM;
import ru.xfit.screens.contacts.OnClubClickListener;
import ru.xfit.screens.contacts.PopupController;
import ru.xfit.screens.filter.FilterController;
import ru.xfit.screens.filter.FilterVM;
import ru.xfit.screens.filter.HeaderFilterVM;
import ru.xfit.screens.schedule.MyScheduleController;

import java.util.ArrayList;
import java.util.List;

public abstract class BindingAdapters {

    private static final String TAG = BindingAdapters.class.getSimpleName();

    @BindingAdapter("itemSelectedListener")
    public static void bindItemSelected(BottomNavigationView view,
                                        BottomNavigationView.OnNavigationItemSelectedListener l) {
        view.setOnNavigationItemSelectedListener(l);
    }

    @BindingAdapter("tabSelectedListener")
    public static void bindItemSelected(TabLayout view,
                                        TabLayout.OnTabSelectedListener listener) {
        view.addOnTabSelectedListener(listener);
    }


    @BindingAdapter(value = {"menu", "itemClickListener"}, requireAll = false)
    public static void bindMenu(Toolbar toolbar, @MenuRes Integer menuRes, View.OnClickListener listener) {
        if (menuRes != null && menuRes != 0) {
            toolbar.inflateMenu(menuRes);
            Menu menu = toolbar.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                View itemChooser = item.getActionView();
                if (itemChooser != null) {
                    itemChooser.setOnClickListener(listener);
                }
            }
        }
    }

    @BindingAdapter("oldItemClickListener")
    public static void bindMenu(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        if (toolbar != null && listener != null) {
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    @BindingAdapter(value = {"menu", "itemClickListener", "searchListener", "onCancelListener"}, requireAll = false)
    public static void bindMenu(Toolbar toolbar, @MenuRes Integer menuRes, View.OnClickListener listener, android.support.v7.widget.SearchView.OnQueryTextListener textListener, OnCancelSearchListener onCancelSearchListener) {
        if (menuRes != null && menuRes != 0) {
            toolbar.inflateMenu(menuRes);
            Menu menu = toolbar.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                View itemChooser = item.getActionView();
                if (itemChooser != null) {
                    itemChooser.setOnClickListener(listener);
                }
                if (item.getItemId() == R.id.action_search) {
                    android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) item.getActionView();
                    searchView.setOnQueryTextListener(textListener);

                    ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
                    closeButton.setOnClickListener(view -> {
                        searchView.setQuery("", true);
                        searchView.setIconified(true);
                        onCancelSearchListener.onCancel();
                    });
                }
            }
        }
    }


    @BindingAdapter(value = {"adapter", "scrollToBottom", "layoutManager"}, requireAll = false)
    public static void bindAdapter(HackyRecyclerView recycler, RecyclerView.Adapter adapter,
                                   Boolean scrollToBottom,
                                   Integer layoutManager) {
        recycler.setAdapter(adapter);

        if (layoutManager == null) throw new IllegalStateException("Forgot to bind LayoutManager");

        RecyclerView.LayoutManager lm;
        switch (layoutManager) {
            case LayoutManagers.LINEAR_VERTICAL:
                lm = new LinearLayoutManager(
                        recycler.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
                break;
            case LayoutManagers.LINEAR_HORIZONTAL:
                lm = new LinearLayoutManager(
                        recycler.getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                break;
            case LayoutManagers.LINEAR_HORIZONTAL_END:
                LinearLayoutManager manager = new LinearLayoutManager(
                        recycler.getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                manager.setStackFromEnd(true);
                lm = manager;
                break;
            case LayoutManagers.GRID_2:
                lm = new GridLayoutManager(recycler.getContext(), 2);
                break;
            case LayoutManagers.VARIABLE_GRID:
                if (!(adapter instanceof BaseAdapter)) {
                    throw new IllegalStateException("Variable span grid can only be used with BaseAdapter");
                }
                GridLayoutManager glm = new GridLayoutManager(recycler.getContext(), SpannedVM.MAX_SPAN_SIZE);
                glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override public int getSpanSize(int position) {
                        BaseVM vm = ((BaseAdapter) adapter).getItem(position);
                        if (!(vm instanceof SpannedVM)) {
                            throw new IllegalStateException("VMs inside variable span grid should implement SpannedVM");
                        }
                        return ((SpannedVM) vm).getSpanSize();
                    }
                });
                lm = glm;
                break;
            default:
                throw new IllegalStateException();
        }

        recycler.setLayoutManager(lm);

        if (scrollToBottom != null && scrollToBottom) {
            recycler.post(() -> {
                if (adapter != null) recycler.scrollToPosition(adapter.getItemCount() - 1);
            });
        }
    }

    @BindingAdapter("layoutChangeListener")
    public static void bindLayoutChangeListener(RecyclerView recyclerView, View.OnLayoutChangeListener listener) {
        if (listener != null) {
            recyclerView.addOnLayoutChangeListener(listener);
        }
    }

    @BindingAdapter("android:background")
    public static void bindBackground(View view, @ColorRes Integer colorRes) {
        if (colorRes != null) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), colorRes));
        }
    }

    @BindingAdapter("android:visibility")
    public static void bindVisibility(View view, Boolean visibility) {
        if (visibility != null) {
            view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("showBackButton")
    public static void bindBackButton(Toolbar toolbar, boolean show) {
        if (show) {
            toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        } else {
            toolbar.setNavigationIcon(null);
        }
    }

    @BindingAdapter("itemClickListener")
    public static void bindItemClickListener(RecyclerView recyclerView, RecyclerItemClickListener.OnItemClickListener listener) {
        if (listener != null) {
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), listener));
        }
    }

    @BindingAdapter("textWatcher")
    public static void bindTextWatcher(EditText editText, TextWatcher textWatcher) {
        if (editText != null) {
            editText.addTextChangedListener(textWatcher);
        }
    }

    @BindingAdapter("android:src")
    public static void bindSrc(ImageView iv, @DrawableRes Integer res) {
        if (res != null) {
            iv.setImageResource(res);
        } else {
            iv.setImageDrawable(null);
        }
    }

    @BindingAdapter("starColor")
    public static void bindStarColor(RatingBar ratingBar, int color) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @BindingAdapter("progress")
    public static void bindProgress(View pv, int progress) {
        if (pv instanceof ProgressBar) {
            ((ProgressBar) pv).setProgress(progress);
        }
    }

    @BindingAdapter("onRefresh")
    public static void bindRefresh(SwipeRefreshLayout refreshLayout, RefreshListener listener) {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> listener.onRefresh(refreshLayout));
    }

    @BindingAdapter("enabled")
    public static void bindEnabled(SwipeRefreshLayout srl, Boolean enabled) {
        srl.setEnabled(enabled != null && enabled);
    }

    @BindingAdapter("android:minWidth")
    public static void bindWidth(View view, int width) {
        view.setMinimumWidth(width);
    }

    @BindingAdapter("tabClickListener")
    public static void bindTabClickListener(TabLayout view, TabLayout.OnTabSelectedListener listener) {
        view.addOnTabSelectedListener(listener);
    }

    @BindingAdapter("backgroundTint")
    public static void bindBgrTint(ImageView view, @ColorRes int colorRes) {
        view.getBackground().mutate().setColorFilter(ContextCompat.getColor(view.getContext(), colorRes), PorterDuff.Mode.MULTIPLY);
    }

    @BindingAdapter("nestedScrollingEnabled")
    public static void bindNestScrollEnabled(RecyclerView rv, Boolean enabled) {
        // Horizontal RW prevents CollapsingToolbarLayout from being collapsed
        // https://code.google.com/p/android/issues/detail?id=177613
        // http://stackoverflow.com/questions/30894334/android-design-lib-collapsingtoolbarlayout-stops-interact-when-horizontal-recycl
        rv.setNestedScrollingEnabled(enabled);
    }

    @BindingAdapter("focusChangeListener")
    public static void bindFocusChangeListener(View view, View.OnFocusChangeListener listener) {
        view.setOnFocusChangeListener(listener);
    }

    @BindingAdapter("editorActionListener")
    public static void bindEditorActionListener(TextView textView, TextView.OnEditorActionListener listener) {
        textView.setOnEditorActionListener(listener);
    }

    @BindingAdapter("menu")
    public static void bindMenu(BottomNavigationView bottomNavigationView, Integer menuRes) {
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(menuRes);
    }

    @BindingAdapter("shiftMode")
    public static void bindShiftMode(BottomNavigationView bottomNavigationView, Boolean enabled) {
        if (!enabled) {
            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        }
    }

    @BindingAdapter("pageChangeListener")
    public static void bindPageChangeListener(ViewPager pager, ViewPager.OnPageChangeListener pageChangeListener) {
        pager.addOnPageChangeListener(pageChangeListener);
    }

    public interface UrlListener {
        void onUrlChanged(WebView webView, String url);
    }

    @BindingAdapter(value = {"url", "urlListener"}, requireAll = false)
    public static void bindUrl(WebView webView, String url, UrlListener urlListener) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }

            @Override public void onPageFinished(WebView view, String url) {
                if (urlListener != null) {
                    urlListener.onUrlChanged(view, url);
                }
            }
        });
        webView.loadUrl(url);
    }

    @BindingAdapter("html")
    public static void bindHtml(TextView textView, String html) {
        textView.setText(Html.fromHtml(html));
    }

    public interface ItemSwipeListener {
        void onItemSwiped(int index);
    }

    @BindingAdapter("itemSwipeListener")
    public static void bindItemSwipeListener(RecyclerView recyclerView, ItemSwipeListener swipeListener) {
        if (swipeListener == null) return;

        ItemTouchHelper.SimpleCallback cb = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                swipeListener.onItemSwiped(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(cb);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @BindingAdapter("onMaskedTextChanged")
    public static void bindOnMaskedTextChangedListener(MaskedEditText maskedEditText, ObservableField<String> observableField) {
        maskedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                observableField.set(maskedEditText.getText(true).toString());
            }
        });
    }

    @BindingAdapter("focusOnView")
    public static void bindFocusOnView(View view, ObservableBoolean focus) {
        view.setOnFocusChangeListener((view1, b) -> focus.set(b));
    }

    @BindingAdapter("onFocusChanged")
    public static void bindOnViewFocusChanged(View view, ObservableBoolean focus) {
        if (focus.get()) {
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
        } else {
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
        }
    }

    @BindingAdapter(value = {"valid", "checkValue"})
    public static void addRePasswordValidation(TextInputLayout textInputLayout, ObservableBoolean isValid, ObservableField<String> checkValue) {
        if (checkValue == null)
            return;
        PasswordEqualValidator validator = new PasswordEqualValidator();
        textInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {

            int trig = 0;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (trig == 1) {
                    String s = validator.validate(textInputLayout.getEditText().getText().toString(), checkValue.get());
                    if (s == null) {
                        textInputLayout.setErrorEnabled(false);
                        isValid.set(true);
                    } else {
                        isValid.set(false);
                    }
                    textInputLayout.setError(s);
                    textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String valid = validator.validate(s.toString(), checkValue.get());
                            if (valid == null) {
                                textInputLayout.setErrorEnabled(false);
                                isValid.set(true);
                            } else {
                                isValid.set(false);
                            }
                            textInputLayout.setError(valid);
                        }
                    });
                }
                trig++;
            }
        });
    }

    @BindingAdapter({"valid", "validationType"})
    public static void addValidation(TextInputLayout textInputLayout, ObservableBoolean isValid, ValidationType validationType) {
        if (validationType != null) {
            EmptyValidator validator;
            switch (validationType) {
                case TEXT:
                    validator = new StringValidator();
                    break;
                case EMAIL:
                    validator = new EmailValidator();
                    break;
                case PASSWORD:
                    validator = new PasswordValidator();
                    break;
                case PHONE:
                    validator = new PhoneValidator();
                    break;
                case DATE:
                    validator = new AgeValidator();
                    break;
                default:
                    validator = new EmptyValidator();
            }
            textInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                int trig = 0;

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (trig == 1) {
                        String s = validator.validate(textInputLayout.getEditText().getText().toString());
                        if (s == null) {
                            textInputLayout.setErrorEnabled(false);
                            isValid.set(true);
                        } else {
                            isValid.set(false);
                        }
                        textInputLayout.setError(s);
                        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String valid = validator.validate(s.toString());
                                if (valid == null) {
                                    textInputLayout.setErrorEnabled(false);
                                    isValid.set(true);
                                } else {
                                    isValid.set(false);
                                }
                                textInputLayout.setError(valid);

                            }
                        });
                    }
                    trig++;
                }
            });

        }
    }

    @BindingAdapter("onCheckedChange")
    public static void setOnCheckedChangeListener(RadioGroup radioGroup, ObservableInt observableInt) {
        radioGroup.check(observableInt.get());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            observableInt.set(checkedId);
        });
    }

    @BindingAdapter("drawerPrepare")
    public static void prepareDrawer(NavigationView navigationView, XFitController controller) {
        navigationView.setItemIconTintList(null);
    }

    @BindingAdapter("onViewReadyListener")
    public static void bindOnViewReadyListener(View view, OnViewReadyListener onViewReadyListener) {
        onViewReadyListener.onReady(view);
    }

    @BindingAdapter(value = {"initCalendarSchedule", "multipleSelect", "canSuspendDays"}, requireAll = false)
    public static void bindCalendar(CustomizableCalendar customizableCalendar,
                                    DateChangeListener dateChangeListener,
                                    boolean multipleSelect,
                                    int canSuspendDays) {
        DateTime today = new DateTime();
        DateTime firstMonth = today.withDayOfMonth(1);
        DateTime lastMonth = today.plusMonths(3).withDayOfMonth(1);

        CompositeDisposable subscriptions = new CompositeDisposable();

        final Calendar calendar = new Calendar(firstMonth, lastMonth);
        if (canSuspendDays > 0)
            calendar.setMaxDaysSelection(canSuspendDays);
        else
            calendar.setMaxDaysSelection(90);

        calendar.setMinDaysSelection(7);

        final CalendarViewInteractor calendarViewInteractor = new CalendarViewInteractor(customizableCalendar.getContext());

        AUCalendar auCalendar = AUCalendar.getInstance(calendar);
        auCalendar.setMultipleSelection(multipleSelect);
        auCalendar.setToday(today);
        calendarViewInteractor.updateCalendar(calendar);

        customizableCalendar.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                auCalendar.observeChangesOnCalendar().subscribe(changeSet -> {
//                    calendarViewInteractor.updateCalendar(calendar);
                    if (calendar.getFirstSelectedDay() != null)
                        dateChangeListener.onDateChange(calendar.getFirstSelectedDay());

                    if (auCalendar.getCalendar().getFirstSelectedDay() != null && auCalendar.getCalendar().getLastSelectedDay() != null) {
                        dateChangeListener.onDatePeriodChanged(auCalendar.getCalendar().getFirstSelectedDay(), auCalendar.getCalendar().getLastSelectedDay());
                    }
                });
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                auCalendar.observeChangesOnCalendar().unsubscribeOn(Schedulers.newThread());
            }
        });

        customizableCalendar.injectViewInteractor(calendarViewInteractor);
    }

    @BindingAdapter("setContract")
    public static void bindContract(CustomizableCalendar customizableCalendar, Contract contract) {
        DateTime endDate = DateTime.parse(contract.suspension.endDate,
                DateTimeFormat.forPattern("yyyy-M-d"));
        DateTime startDate = DateTime.parse(contract.suspension.startDate,
                DateTimeFormat.forPattern("yyyy-M-d"));
        customizableCalendar.setPreselectDates(startDate, endDate);
    }

    @BindingAdapter("setColor")
    public static void bindViewColor(View view, Integer color) {
        view.setBackgroundColor(color);
    }

    @BindingAdapter("navigationClickListener")
    public static void bindNavClickListener(Toolbar toolbar, NavigationClickListener l) {
        if (l == null) {
            toolbar.setNavigationOnClickListener(null);
        } else {
            toolbar.setNavigationOnClickListener(v -> l.onNavigationClick());
        }
    }

    @BindingAdapter("onRadioChecked")
    public static void bindOnRadioChecked(RadioButton radioButton, FilterController filterController ) {
        radioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                filterController.checkedIncome(compoundButton.getId());
            }
        });
    }

    @BindingAdapter(value = {"android:src", "circleTransform"}, requireAll = false)
    public static void bindSrc(ImageView iv, Object o, boolean circleTransform) {
        RequestManager requestManager = Glide.with(iv.getContext());
        DrawableTypeRequest rc;

        if (o == null) {
            iv.setImageDrawable(null);
            return;
        } else if (o instanceof String) {
            rc = requestManager.load((String) o);
            rc.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        } else if (o instanceof Integer) {
            if (!circleTransform) {
                iv.setImageResource((Integer) o);
                return;
            }
            rc = requestManager.load((Integer) o);
            Log.w(TAG, "bindSrc: vectorDrawables would not work with circle transformation");
        } else if (o instanceof Uri) {
            rc = requestManager.load((Uri) o);
        } else if (o instanceof Bitmap) {
            iv.setImageBitmap((Bitmap) o);
            if (circleTransform) {
                // TODO: ?
            }
            return;
        } else {
            throw new IllegalArgumentException("Can't set source" + o);
        }

        if (circleTransform) {
            rc.transform(new CircleTransform(iv.getContext()));
        }
        rc.into(iv);
    }

    @BindingAdapter("onCheckBoxChecked")
    public static void bindOnCheckBoxChecked(CheckBox checkBox, FilterVM filterVM ) {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(filterVM.isChecked.get());
        checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            filterVM.isChecked.set(checked);
        });
    }

    @BindingAdapter("highlightedDays")
    public static void bindHighlightedDates(CustomizableCalendar v,
                                            List<DateTime> dates) {
        AUCalendar.getInstance().highLightDates(dates);
    }

    @BindingAdapter("headerFilterVM")
    public static void bindOnHeaderChekcBoxState(CheckBox checkBox, FilterController filterController) {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            filterController.setStateAllCheckboxes(checked);
        });
    }

    @BindingAdapter("digitMaskedKeyboard")
    public static void bindMaskedKeyboard(MaskedEditText maskedEditText, boolean bind) {
        maskedEditText.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @BindingAdapter("banners")
    public static void bindSlider(SliderLayout sliderLayout, AboutClubController clubController) {
        if (sliderLayout == null || clubController == null) return;

        sliderLayout.stopAutoCycle();
        for (Image image : clubController.club.media) {
            BannerSliderView sliderView = new BannerSliderView(App.getContext(), image);
            sliderView.image(image.url);
            sliderLayout.addSlider(sliderView);
        }

//        PagerIndicator indicator = new PagerIndicator(sliderLayout.getContext());
//        indicator.setDefaultIndicatorColor(sliderLayout.getContext().getResources().getColor(R.color.colorAccent),
//                                            sliderLayout.getContext().getResources().getColor(R.color.white));
//        indicator.setDefaultIndicatorShape(PagerIndicator.Shape.Oval);
//        sliderLayout.setCustomIndicator(indicator);

//        sliderLayout.setCustomIndicator((PagerIndicator) sliderLayout.findViewById(R.id.custom_indicator));
    }

    @BindingAdapter("hackyPopUp")
    public static void bindPopup(EditText editText, FeedbackController controller) {
        PopupRecyclerBinding v = DataBindingUtil.inflate(LayoutInflater.from(editText.getContext()), R.layout.popup_recycler, null, false);

        PopupWindow popupWindow = new PopupWindow(v.getRoot(), WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        PopupController popupController = new PopupController(controller.clubs, new OnClubClickListener() {
            @Override
            public void onClick(ClubItem clubItem) {
                controller.selectedClub.set(clubItem);
                popupWindow.dismiss();
            }
        });

        v.setController(popupController);

        ObservableBoolean isVisible = new ObservableBoolean();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(editText);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                clubsFilter.setClubSearch(editable.toString());
            }
        });
    }

}
