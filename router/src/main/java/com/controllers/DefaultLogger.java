package com.controllers;

import android.support.annotation.Nullable;
import android.util.Log;

public class DefaultLogger implements Logger {

  private final String controllerTag;

  DefaultLogger(String controllerTag) {
    this.controllerTag = controllerTag;
  }

  @Override public void logv(String tag, String msg) {
    log(Log.VERBOSE, tag, msg, null);
  }

  @Override public void logd(String tag, String msg) {
    log(Log.DEBUG, tag, msg, null);
  }

  @Override public void logi(String tag, String msg) {
    log(Log.INFO, tag, msg, null);
  }

  @Override public void logw(String tag, String msg) {
    log(Log.WARN, tag, msg, null);
  }

  @Override public void loge(String tag, String msg) {
    log(Log.ERROR, tag, msg, null);
  }

  @Override public void loge(String tag, String msg, Throwable t) {
    log(Log.ERROR, tag, msg, t);
  }

  private void log(int level, String tag, String msg, @Nullable Throwable t) {
    if (BuildConfig.DEBUG) {
      String print;
      if (t != null) {
        print = msg + "\n" + Log.getStackTraceString(t);
      } else {
        print = msg;
      }
      Log.println(level, Const.LOG_PREFIX + controllerTag + "/" + tag, print);
    }
  }
}
