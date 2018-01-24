package com.fwk.moequestdemo.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public final class SnackbarUtil {

  public static void showMessage(View view, String text) {

    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
  }
}
