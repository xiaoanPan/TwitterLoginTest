package com.example.myapplication.ui.theme;

import android.util.Log;
import android.view.ViewStub;

class TestChild extends TestParent {
   @Override
   public void onCreate(ViewStub viewStub) {
      super.onCreate(viewStub);
      Log.e("xxx", "view stub");
   }
}
