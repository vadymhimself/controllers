package com.controllers.misc.controllers;

import com.controllers.Controller;
import com.controllers.misc.R;

public class StubController extends Controller {
  @Override
  protected int getLayoutId() {
    return R.layout.layout_stub;
  }

  @Override
  public String getTitle() {
    return "Coming soon";
  }
}
