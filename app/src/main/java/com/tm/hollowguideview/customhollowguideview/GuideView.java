package com.tm.hollowguideview.customhollowguideview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class GuideView extends View {

    private Paint paint;

    private List<TargetValue> listTargetRects;

    private int hollowStyle;

    @IntDef({HollowStyle.rect, HollowStyle.circle, HollowStyle.oval,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HollowStyle {
        int rect = 0;
        int circle = 1;
        int oval = 2;
    }

    public GuideView(Context context) {
        super(context);
        init();
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GuideView);
        hollowStyle = a.getInt(R.styleable.GuideView_hollow_style, HollowStyle.rect);
        a.recycle();

        init();
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GuideView, defStyleAttr, 0);
        hollowStyle = a.getInt(R.styleable.GuideView_hollow_style, HollowStyle.rect);
        a.recycle();

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.shadow));

//        Button btn = new Button(getContext());
//        btn.setText("知道了");
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER_HORIZONTAL| Gravity.BOTTOM;
//        params.bottomMargin = 30;
//        btn.setLayoutParams(params);
//
//        this.addView(btn);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //绘制背景
        int layerId = canvas.saveLayer(0,0,canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawRect(0,0, canvasWidth, canvasHeight, paint);
        if (listTargetRects == null || listTargetRects.size() == 0) {
            return;
        }

        for (int i = 0; i < listTargetRects.size(); i++) {
            TargetValue value = listTargetRects.get(i);
            int hollowStyle = value.getHollowStyle();
            Rect targetRect = value.getRect();

            if (hollowStyle == HollowStyle.rect) {

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawRect(targetRect,paint);
                paint.setXfermode(null);
            } else if (hollowStyle == HollowStyle.circle) {

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                //绘制圆形，算出外接圆的半径
                canvas.drawCircle((targetRect.left + targetRect.right) /2
                        , (targetRect.top + targetRect.bottom) /2
                        , (int) Math.sqrt((targetRect.right - targetRect.left) * (targetRect.right - targetRect.left)
                                + (targetRect.bottom - targetRect.top) * (targetRect.bottom - targetRect.top)) /2
                        , paint);
                paint.setXfermode(null);

            } else if (hollowStyle == HollowStyle.oval) {

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                //算出targetView外接椭圆的短轴和长轴
                int len1 = (int) (((targetRect.right - targetRect.left) * Math.sqrt(2) - (targetRect.right - targetRect.left)) / 2);
                int len2 = (int) (((targetRect.bottom - targetRect.top) * Math.sqrt(2) - (targetRect.bottom - targetRect.top)) / 2);
                canvas.drawOval(new RectF(targetRect.left - len1
                        , targetRect.top - len2
                        , targetRect.right + len1
                        , targetRect.bottom + len2), paint);
                paint.setXfermode(null);
            }
        }

    }

    public void setTargetView(View targetView) {
        int[] locations =new int[2];
        targetView.getLocationOnScreen(locations);
        int width = targetView.getMeasuredWidth();
        int height = targetView.getMeasuredHeight();
        locations[1] = locations[1] - ScreenUtils.getStatusHeight(getContext());
        Rect targetRect = new Rect(locations[0], locations[1], locations[0] + width, locations[1] + height);

        if (listTargetRects == null) {
            listTargetRects = new ArrayList<>();
        }
        TargetValue value = new TargetValue(targetRect, hollowStyle);
        listTargetRects.add(value);

        invalidate();
    }

    public void setTargetViewAndHollowStyle(View targetView, @HollowStyle int hollowStyle) {
        int[] locations =new int[2];
        targetView.getLocationOnScreen(locations);
        int width = targetView.getMeasuredWidth();
        int height = targetView.getMeasuredHeight();
        locations[1] = locations[1] - ScreenUtils.getStatusHeight(getContext());
        Rect targetRect = new Rect(locations[0], locations[1], locations[0] + width, locations[1] + height);

        if (listTargetRects == null) {
            listTargetRects = new ArrayList<>();
        }

        TargetValue value = new TargetValue(targetRect, hollowStyle);
        listTargetRects.add(value);

        invalidate();
    }

    public void clearTargetView() {
        if (listTargetRects == null) {
            listTargetRects.clear();
        }
    }

    private static class TargetValue {

        public TargetValue(Rect rect, int hollowStyle) {
            this.rect = rect;
            this.hollowStyle = hollowStyle;
        }

        private Rect rect;
        private int hollowStyle;

        public Rect getRect() {
            return rect;
        }

        public int getHollowStyle() {
            return hollowStyle;
        }

    }
}
