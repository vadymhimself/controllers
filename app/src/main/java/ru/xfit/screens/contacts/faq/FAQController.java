package ru.xfit.screens.contacts.faq;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutFaqBinding;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.screens.XFitController;

/**
 * Created by aleks on 28.11.2017.
 */

public class FAQController extends XFitController<LayoutFaqBinding> implements NavigationClickListener {

    @Override
    public String getTitle() {
        return getActivity() == null ? "" : getActivity().getString(R.string.contacts_faq);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_faq;
    }

    @Override
    public void onNavigationClick() {
        back();
    }

    public void onGeneralQuestionsClick(View view) {

    }

    public void onCardQuestionsClick(View view) {

    }

    public void onPoolQuestionsClick(View view) {

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
