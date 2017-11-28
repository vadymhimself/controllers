package ru.xfit.screens.contacts.faq;

import android.databinding.ObservableBoolean;
import android.support.transition.Transition;
import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.views.OnExpandedListener;

/**
 * Created by aleks on 28.11.2017.
 */

public class QuestionVM implements BaseVM {

    public String question;
    public String answer;
    public ObservableBoolean isAnswerVisible = new ObservableBoolean();
    public Transition.TransitionListener transitionListener;
    private OnExpandedListener<QuestionVM> onExpandedListener;

    public QuestionVM(String question, String answer, OnExpandedListener<QuestionVM> onExpandedListener, Transition.TransitionListener transitionListener) {
        this.question = question;
        this.answer = answer;
        this.onExpandedListener = onExpandedListener;
        this.transitionListener = transitionListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_question;
    }

    public void onClick(View view) {
        if (!isAnswerVisible.get())
            onExpandedListener.onExpanded(this);
        isAnswerVisible.set(!isAnswerVisible.get());
    }

}
