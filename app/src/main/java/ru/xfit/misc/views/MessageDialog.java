package ru.xfit.misc.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import javax.inject.Inject;

import ru.xfit.R;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 20.11.2017.
 */

public class MessageDialog extends DialogFragment {
    private static final String POSITIVE_EXTRA = "POSITIVE_EXTRA";
    private static final String NEGATIVE_EXTRA = "NEGATIVE_EXTRA";
    private static final String MESSAGE_EXTRA = "MESSAGE_EXTRA";

    private boolean isPositivePressed;
    private XFitController controller;

    public MessageDialog() {
    }

    public static class Builder {
        private String mMessage;

        private String mPositiveText;
        private String mNegativeText;

        Context context;

        public Builder() {
            context = App.getContext();
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        public Builder setMessage(@StringRes int messageResId) {
            return this.setMessage(context.getResources().getString(messageResId));
        }

        public Builder setPositiveText(@StringRes int positiveResId) {
            mPositiveText = context.getResources().getString(positiveResId);
            return this;
        }

        public Builder setNegativeText(@StringRes int negativeResId) {
            mNegativeText = context.getResources().getString(negativeResId);
            return this;
        }


        public MessageDialog build() {
            MessageDialog fragment = new MessageDialog();
            Bundle bundle = new Bundle();
            bundle.putString(MESSAGE_EXTRA, mMessage);
            bundle.putString(POSITIVE_EXTRA, mPositiveText);
            bundle.putString(NEGATIVE_EXTRA, mNegativeText);

            fragment.setArguments(bundle);

            return fragment;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_dialog, null);

        TextView mDialogMessage = (TextView) view.findViewById(R.id.dialog_msg);
        TextView mDialogOk = (TextView) view.findViewById(R.id.dialog_ok);
        TextView mDialogPositive = (TextView) view.findViewById(R.id.dialog_positive);
        TextView mDialogNegative = (TextView) view.findViewById(R.id.dialog_negative);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();

        Bundle args = getArguments();
        String positiveRes = "";
        String negativeRes = "";

        if (args != null) {
            mDialogMessage.setText(args.getString(MESSAGE_EXTRA, ""));
            positiveRes = args.getString(POSITIVE_EXTRA, "");
            negativeRes = args.getString(NEGATIVE_EXTRA, "");
        }

        if (!TextUtils.isEmpty(positiveRes)) {
            mDialogPositive.setVisibility(View.VISIBLE);
            mDialogPositive.setText(positiveRes);
            mDialogPositive.setOnClickListener(view13 -> onResult(true));
        }

        if (!TextUtils.isEmpty(negativeRes)) {
            mDialogNegative.setVisibility(View.VISIBLE);
            mDialogNegative.setText(negativeRes);
            mDialogNegative.setOnClickListener(view12 -> dismiss());
        }

        if (TextUtils.isEmpty(positiveRes) && TextUtils.isEmpty(negativeRes)) {
            mDialogOk.setVisibility(View.VISIBLE);
            mDialogOk.setOnClickListener(view1 -> onResult(false));
        }


        dialog.setView(view);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.color.transparent);
        }

        return dialog;
    }

    private void onResult(boolean ok) {
        if (controller != null && controller instanceof DialogResultListener) {
            onResult((DialogResultListener) controller, ok);
        }
    }

    public void setController(XFitController controller) {
        this.controller = controller;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isPositivePressed) {
            isPositivePressed = false;
            onResult(false);
        }
    }

    private void onResult(DialogResultListener result, boolean ok) {
        final String tag = getTag();
        if (ok) {
            isPositivePressed = true;
            result.onPositive(tag);
        } else {
            result.onNegative(tag);
        }

        dismiss();
    }

    public interface DialogResultListener extends Serializable {
        void onPositive(@NonNull String tag);

        void onNegative(@NonNull String tag);
    }
}
