package com.controllers;

import java.io.Serializable;

public interface Logger extends Serializable {
  void logv(String tag, String msg);
  void logd(String tag, String msg);
  void logi(String tag, String msg);
  void logw(String tag, String msg);
  void loge(String tag, String msg);
}
