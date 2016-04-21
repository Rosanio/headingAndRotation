package com.epicodus.headingandrotation;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class HeadingAndRotationActivity extends Activity {

    HeadingAndRotationView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        view = new HeadingAndRotationView(this, size.x, size.y);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        view.pause();
    }
}