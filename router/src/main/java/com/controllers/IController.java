package com.controllers;

import android.support.annotation.NonNull;

/**
 * Controller interface for testing purposes.
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

interface IController {
    void onAttachedToStack(@NonNull Router router);
    void onDetachedFromStack(@NonNull Router router);

    void onAttachedToScreen();
    void onDetachedFromScreen();
}
