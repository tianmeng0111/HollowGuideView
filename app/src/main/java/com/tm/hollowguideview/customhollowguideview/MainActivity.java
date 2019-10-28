package com.tm.hollowguideview.customhollowguideview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private View targetView;
    private View targetView1;
    private View targetView2;

    private FrameLayout guideLayout;
    private GuideView guideView;
    private View known;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        targetView = findViewById(R.id.btn);
        targetView1 = findViewById(R.id.iv);
        targetView2 = findViewById(R.id.tv);

        guideLayout = findViewById(R.id.guide_layout);
        guideView = findViewById(R.id.guide);
        known = findViewById(R.id.tv_known);
        known.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            guideLayout.setVisibility(View.VISIBLE);

            guideView.setTargetView(targetView);
            guideView.setTargetViewAndHollowStyle(targetView1, GuideView.HollowStyle.circle);
            guideView.setTargetViewAndHollowStyle(targetView2, GuideView.HollowStyle.rect);
        }
    }

}
