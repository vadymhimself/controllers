package com.controllers.misc;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ImageViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.controllers.misc.views.BottomNavigationViewHelper;
import com.controllers.misc.views.RecyclerItemClickListener;

public abstract class BindingAdapters {

    @BindingAdapter("android:visibility")
    public static void bindVisibility(View view, Boolean visibility) {
        if (visibility != null) {
            view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

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

    @BindingAdapter("setupWithViewPager")
    public static void bindItemSelected(TabLayout view,
                                        @IdRes int viewPagerId) {
        view.setupWithViewPager((ViewPager) view.getRootView().findViewById(viewPagerId));
    }

    @BindingAdapter("etSearchListener")
    public static void bindSearchListener(EditText view,
                                          final OnSearchListener onSearchListener) {
        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchListener.onSearchQuery(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @BindingAdapter("navigationListener")
    public static void bindNavigationListener(Toolbar toolbar, View.OnClickListener navListener) {
        toolbar.setNavigationOnClickListener(navListener);
    }

    @BindingAdapter("menu")
    public static void bindMenu(Toolbar toolbar, @MenuRes Integer menuRes) {
        if (menuRes != null && menuRes != 0) {
            toolbar.inflateMenu(menuRes);
        }
    }

    @BindingAdapter(value = {"menu", "searchListener"}, requireAll = false)
    public static void bindMenu(Toolbar toolbar, @MenuRes Integer menuRes,
                                SearchView.OnQueryTextListener searchListener) {
        if (menuRes != null && menuRes != 0) {
            toolbar.inflateMenu(menuRes);
            Menu menu = toolbar.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                View itemChooser = item.getActionView();
                if (itemChooser instanceof SearchView && searchListener != null) {
                    ((SearchView) itemChooser).setOnQueryTextListener(searchListener);
                }
            }
        }
    }

    @BindingAdapter("itemClickListener")
    public static void bindMenu(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, CharSequence text) {
        final CharSequence oldText = view.getText();
        if (text == oldText || (text == null && oldText.length() == 0)) {
            return;
        }
        if (text instanceof Spanned) {
            if (text.equals(oldText)) {
                return; // No change in the spans, so don't set anything.
            }
        } else if (!haveContentsChanged(text, oldText)) {
            return; // No content changes, so don't set anything.
        }

        int selectionEnd = view.getSelectionEnd();
        view.setText(text);

        if (view instanceof EditText && selectionEnd != -1 && text != null) {
            if (selectionEnd > text.length() || selectionEnd == oldText.length()) {
                // selection was in the end, let it remain in the end
                selectionEnd = text.length();
            }
            ((EditText) view).setSelection(selectionEnd);
        }
    }

    private static boolean haveContentsChanged(CharSequence str1, CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            return true;
        } else if (str1 == null) {
            return false;
        }
        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @BindingAdapter("android:enabled")
    public static void bindEnabled(View view, Boolean enabled) {
        view.setEnabled(enabled);
    }

    @BindingAdapter("layoutChangeListener")
    public static void bindLayoutChangeListener(RecyclerView recyclerView, View.OnLayoutChangeListener listener) {
        if (listener != null) {
            recyclerView.addOnLayoutChangeListener(listener);
        }
    }

    @BindingAdapter("android:background")
    public static void bindBackground(View view, @DrawableRes Integer res) {
        if (res != null) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), res));
        }
    }

    @BindingAdapter(value = {"android:visibility", "goneType"}, requireAll = false)
    public static void bindVisibility(View view, Boolean visibility, int goneType) {
        if (visibility != null) {
            if (goneType != 0) {
                view.setVisibility(visibility ? View.VISIBLE : goneType);
            } else {
                view.setVisibility(visibility ? View.VISIBLE : View.GONE);
            }
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

    public interface SimpleTextWatcher {
        void onTextChanged(String text);
    }

    @BindingAdapter("simpleTextWatcher")
    public static void bindSimpleTextWatcher(EditText editText,
                                             final SimpleTextWatcher textWatcher) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textWatcher != null) {
                    textWatcher.onTextChanged(s.toString());
                }
            }
        });
    }

    @BindingAdapter("android:src")
    public static void bindSrc(ImageView iv, @DrawableRes Integer res) {
        if (res != null) {
            iv.setImageResource(res);
        } else {
            iv.setImageDrawable(null);
        }
    }

    @BindingAdapter(value = {"android:src", "circleTransform", "placeholder"}, requireAll = false)
    public static void bindSrc(ImageView iv, Object uri, boolean circleTransform, Integer placeholder) {
        RequestOptions options = new RequestOptions();

        if (circleTransform) {
            options = options.circleCrop();
        }

        if (placeholder != null) {
            options = options.placeholder(placeholder);
        }

        Glide.with(iv).load(uri).apply(options).into(iv);
    }

    @BindingAdapter("circleSrc")
    public static void bindCircleSrc(ImageView iv, @DrawableRes Integer src) {
        Glide.with(iv)
                .load(src)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(iv);
    }

    @BindingAdapter("onSeekBarChangeListener")
    public static void bindOnProgressChangeListener(SeekBar sb, SeekBar.OnSeekBarChangeListener l) {
        sb.setOnSeekBarChangeListener(l);
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

    @BindingAdapter("enabled")
    public static void bindEnabled(SwipeRefreshLayout srl, Boolean enabled) {
        srl.setEnabled(enabled != null && enabled);
    }

    @BindingAdapter("android:scaleType")
    public static void bindScaleImageView(ImageView view, ImageView.ScaleType type) {
        view.setScaleType(type);
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

    @BindingAdapter("html")
    public static void bindHtml(TextView textView, String html) {
        textView.setText(Html.fromHtml(html));
    }

    @BindingAdapter(value = {"url", "webClient"})
    public static void _bindUrl(WebView webView, String uri, WebViewClient webViewClient) {
        if (webViewClient == null) {
            webView.setWebViewClient(new WebViewClient());
        } else {
            webView.setWebViewClient(webViewClient);
        }
        webView.loadUrl(uri);
    }

    @BindingAdapter("html")
    public static void bindHtml(WebView wv, String html) {
        wv.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("jsEnabled")
    public static void bindJsEnabled(WebView wv, Boolean enabled) {
        wv.getSettings().setJavaScriptEnabled(enabled);
        wv.getSettings().setLoadWithOverviewMode(enabled);
        wv.getSettings().setUseWideViewPort(enabled);
    }


    @BindingAdapter("android:visibility")
    public static void bindProgressVisibility(ProgressBar pb, boolean visible) {
        if (!pb.isIndeterminate()) {
            pb.setVisibility(visible ? View.VISIBLE : View.GONE);
            return;
        }

        if (visible) {
            pb.setScaleY(0);
            pb.animate()
                    .scaleY(1)
                    .withStartAction(() -> pb.setVisibility(View.VISIBLE))
                    .setDuration(200)
                    .start();
        } else {
            pb.setScaleY(1);
            pb.animate()
                    .scaleY(0)
                    .setDuration(200)
                    .withStartAction(() -> pb.setVisibility(View.VISIBLE))
                    .withEndAction(() -> pb.setVisibility(View.GONE))
                    .start();
        }
    }

    @BindingAdapter("itemSwipeListener")
    public static void bindItemSwipeListener(RecyclerView recyclerView, final ItemSwipeListener swipeListener) {
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

    @BindingAdapter("checkedChangedListener")
    public static void bindCheckedChangedListener(CompoundButton cb,
                                                  CompoundButton.OnCheckedChangeListener listener) {
        cb.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("android:layout_marginTop")
    public static void bindMarginTop(View v, int marginTop) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = marginTop;
    }

    @BindingAdapter("android:progressBackgroundTint")
    public static void bindProgressTint(ProgressBar progressBar, @ColorInt int color) {
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void bindMarginLeft(View v, float margin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin = (int) margin;
    }

    @BindingAdapter("android:layout_marginRight")
    public static void bindMarginRight(View v, float margin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin = (int) margin;
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void bindMarginBottom(View v, int marginTop) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin = marginTop;
    }

    @BindingAdapter("android:minHeight")
    public static void bindLayoutHeight(View v, int height) {
        v.setMinimumHeight(height);
    }

    @BindingAdapter("android:paddingTop")
    public static void bindPaddingTop(View v, int top) {
        v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), v.getPaddingBottom());
    }

    @BindingAdapter("onViewReady")
    public static void bindViewReady(View v, OnViewReadyListener l) {
        v.post(() -> l.onReady(v));
    }

    @BindingAdapter("android:scaleX")
    public static void bindScaleX(View v, float scale) {
        v.setScaleX(scale);
    }

    @BindingAdapter("android:scaleY")
    public static void bindScaleY(View v, float scale) {
        v.setScaleY(scale);
    }

    @BindingAdapter("headerLayout")
    public static void bindHeaderLayout(NavigationView view, int layoutId) {
        ViewDataBinding b = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()),
                layoutId, null, false);
        view.addHeaderView(b.getRoot());
    }

    @BindingAdapter(value = {"android:drawableRight", "android:drawableTint"}, requireAll = true)
    public static void bindDrawableTint(TextView tv, @DrawableRes int drawableRes, @ColorRes Integer colorRes) {
        Drawable d = VectorDrawableCompat.create(tv.getResources(), drawableRes, null);
        d = d.mutate();
        DrawableCompat.setTint(d, ContextCompat.getColor(tv.getContext(), colorRes));
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
    }

    @BindingAdapter(value = {"android:drawableLeft", "android:drawableTint"}, requireAll = true)
    public static void bindDrawableLeftTint(TextView tv, @DrawableRes int drawableRes, @ColorRes Integer colorRes) {
        Drawable d = VectorDrawableCompat.create(tv.getResources(), drawableRes, null);
        d = d.mutate();
        DrawableCompat.setTint(DrawableCompat.unwrap(d), ContextCompat.getColor(tv.getContext(), colorRes));
        tv.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    }

    @BindingAdapter("srcCompat")
    public static void bindSrcCompat(ImageView iv, @DrawableRes int res) {
        iv.setImageResource(res);
    }

    @BindingAdapter("android:layout_weight")
    public static void bindLayoutWeight(View v, int weight) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
        lp.weight = weight;
    }

    @BindingAdapter("tint")
    public static void bindTint(ImageView iv, @ColorRes int res) {
        Drawable d = DrawableCompat.wrap(iv.getDrawable());
        DrawableCompat.setTint(d, ContextCompat.getColor(iv.getContext(), res));
        iv.setImageDrawable(d);
    }

    @BindingAdapter("offsetChangedListener")
    public static void bindOnOffsetChangeListener(AppBarLayout abl,
                                                  AppBarLayout.OnOffsetChangedListener listener) {
        abl.addOnOffsetChangedListener(listener);
    }

    @BindingAdapter("android:backgroundTint")
    public static void bindBgrTintProgress(ProgressBar pb, @ColorRes int c) {
        if (pb.isIndeterminate()) {
            int color = ContextCompat.getColor(pb.getContext(), c);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable wrapDrawable = DrawableCompat.wrap(pb.getIndeterminateDrawable());
                DrawableCompat.setTint(wrapDrawable, color);
                pb.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
            } else {
                pb.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @BindingAdapter("autoMarginMaxWidth")
    public static void bindMaxWidth(View view, float width) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        View rootView = view.getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getMeasuredWidth() > width) {
                    float dif = view.getMeasuredWidth() - width;
                    lp.leftMargin += dif / 2;
                    lp.rightMargin += dif / 2;
                    view.requestLayout();
                }
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @BindingAdapter("width")
    public static void bindDamnWidth(View v, int width) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = width;
    }

    @BindingAdapter("tintColor")
    public static void bindTintColor(ImageView iv, @ColorInt int colorInt) {
        ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(colorInt));
    }

    @BindingAdapter("errorText")
    public static void bindErrorText(TextInputLayout til, String text) {
        til.post(() -> til.setError(text));
    }

    @BindingAdapter("android:onCheckedChanged")
    public static void bindOnCheckedChangedListener(Switch v, CompoundButton.OnCheckedChangeListener l) {
        v.setOnCheckedChangeListener(l);
    }

    public interface UrlListener {
        void onUrlChanged(WebView webView, String url);
    }

    public interface OnViewReadyListener {
        void onReady(View view);
    }

    public interface ItemSwipeListener {
        void onItemSwiped(int index);
    }


    @BindingAdapter("android:orientation")
    public static void defineOrientationLayout(LinearLayout iv, boolean isHorizontal) {
        iv.setOrientation(isHorizontal ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
    }
}
