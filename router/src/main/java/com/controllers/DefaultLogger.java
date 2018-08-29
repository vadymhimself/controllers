package com.controllers;

import android.util.Log;

public class DefaultLogger implements Logger {

  private final String controllerTag;

  DefaultLogger(String controllerTag) {
    this.controllerTag = controllerTag;
  }

  @Override public void logv(String tag, String msg) {
    log(Log.VERBOSE, tag, msg);
  }

  @Override public void logd(String tag, String msg) {
    log(Log.DEBUG, tag, msg);
  }

  @Override public void logi(String tag, String msg) {
    log(Log.INFO, tag, msg);
  }

  @Override public void logw(String tag, String msg) {
    log(Log.WARN, tag, msg);
  }

  @Override public void loge(String tag, String msg) {
    log(Log.ERROR, tag, msg);
  }

  private void log(int level, String tag, String msg) {
    if (BuildConfig.DEBUG) {
      Log.println(level, Const.LOG_PREFIX + controllerTag + "/" + tag, msg);
    }
  }
}
