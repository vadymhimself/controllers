package com.controllers.misc.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void callNumber(Activity activity, String number) {
        if (activity == null) return;
        Uri numberUri = Uri.parse("tel:" + number);
        Intent dial = new Intent(Intent.ACTION_DIAL, numberUri);
        activity.startActivity(dial);
    }

    public static void openWebsite(Activity activity, String url) {
        if (activity == null) return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void startMailToAction(Context context, Uri uri, @StringRes int stringId) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);//common intent
        intent.setData(uri); // only email apps should handle this

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", uri.getEncodedSchemeSpecificPart());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
        }
    }

    private static List<String> readAsset(String fileName, Context context){
        List<String> data = new LinkedList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                data.add(mLine);
            }
        } catch (IOException e) {
            Log.d("Utils", "readAsset: ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d("Utils", "readAsset: ", e);
                }
            }
        }
        return data;
    }

}
