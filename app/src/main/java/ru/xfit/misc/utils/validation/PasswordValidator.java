package ru.xfit.misc.utils.validation;

import java.util.regex.Pattern;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.10.2017.
 */

public class PasswordValidator extends StringValidator {
    public static final Pattern PASSWORD = Pattern.compile("[a-zA-Z0-9!?*@#$%^&()\\-_+=;:,./{}\\[\\]~`|\\\\]{8,}");

    @Override
    public String validate(String... args) {
        String result = super.validate(args[0]);
        if (result != null)
            return result;
        if (!PASSWORD.matcher(args[0]).matches())
            return App.getContext().getString(R.string.incorrect_password);
        if (calculateEnthropy(args[0]) < 42)
            return App.getContext().getString(R.string.easy_password);
        return null;

    }


    private double calculateEnthropy(String string) {
        int enthropy = 0;
        char[] symbols = string.toCharArray();
        for (char symbol : symbols) {
            if (Character.isLowerCase(symbol)) {
                enthropy += 26;
                break;
            }
        }
        for (char symbol : symbols) {
            if (Character.isUpperCase(symbol)) {
                enthropy += 26;
                break;
            }
        }
        for (char symbol : symbols) {
            if (Character.isDigit(symbol)) {
                enthropy += 10;
                break;
            }
        }
        for (char symbol : symbols) {
            if ("\\|¬¦`!\"£$%^&*()_+-=[]{};:'@#~<>,./? ".indexOf(symbol) >= 0) {
                enthropy += 36;
                break;
            }
        }
        return Math.log(enthropy) / Math.log(2) * symbols.length;
    }
}
