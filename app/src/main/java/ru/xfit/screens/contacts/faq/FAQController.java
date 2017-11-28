package ru.xfit.screens.contacts.faq;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutFaqBinding;
import ru.xfit.screens.XFitController;

/**
 * Created by aleks on 28.11.2017.
 */

public class FAQController extends XFitController<LayoutFaqBinding> {

    @Override
    public String getTitle() {
        return getActivity() == null ? "" : getActivity().getString(R.string.contacts_faq);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_faq;
    }

    public void onGeneralQuestionsClick(View view) {
        show(new FAQQuestionsController(getActivity().getResources().getStringArray(R.array.faq_questions_general)
                , getActivity().getResources().getStringArray(R.array.faq_answers_general)
                , getActivity().getString(R.string.faq_general)));
    }

    public void onTrainingsQuestionClick(View view) {
        show(new FAQQuestionsController(getActivity().getResources().getStringArray(R.array.faq_questions_trainings)
                , getActivity().getResources().getStringArray(R.array.faq_answers_trainings)
                , getActivity().getString(R.string.faq_trainings)));
    }

    public void onCardQuestionsClick(View view) {
        show(new FAQQuestionsController(getActivity().getResources().getStringArray(R.array.faq_questions_card)
                , getActivity().getResources().getStringArray(R.array.faq_answers_card)
                , getActivity().getString(R.string.faq_cards)));
    }

    public void onPoolQuestionsClick(View view) {
        show(new FAQQuestionsController(getActivity().getResources().getStringArray(R.array.faq_questions_pool)
                , getActivity().getResources().getStringArray(R.array.faq_answers_pool)
                , getActivity().getString(R.string.faq_pools)));
    }

    public void onAskQuestionCLicked(View view) {

    }

    public void onTelegramClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://telegram.me/XFitBot"));
        if (getActivity() != null)
            getActivity().startActivity(intent);

    }


}
