package ru.xfit.screens.contacts.faq;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutFaqGeneralBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.views.OnExpandedListener;
import ru.xfit.screens.XFitController;

/**
 * Created by aleks on 28.11.2017.
 */

public class FAQQuestionsController extends XFitController<LayoutFaqGeneralBinding> implements OnExpandedListener<QuestionVM>, Transition.TransitionListener {

    @Bindable
    public BaseAdapter<BaseVM> adapter;
    private QuestionVM expandedViewModel;
    private String title;

    public FAQQuestionsController(String[] questions, String[] answers, String title) {
        this.title = title;
        List<BaseVM> items = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            items.add(new QuestionVM(questions[i], answers[i], this, this));
        }
        adapter = new BaseAdapter<>(items);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_faq_general;
    }

    @Override
    public void onExpanded(QuestionVM questionVM) {
        if (expandedViewModel != null)
            expandedViewModel.isAnswerVisible.set(false);
        expandedViewModel = questionVM;
    }

    @Override
    public void onTransitionStart(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionEnd(@NonNull Transition transition) {
        int position = adapter.indexOf(expandedViewModel);
        if (getBinding() != null)
            getBinding().recyclerView.smoothScrollToPosition(position);

    }

    @Override
    public void onTransitionCancel(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionPause(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionResume(@NonNull Transition transition) {

    }
}
