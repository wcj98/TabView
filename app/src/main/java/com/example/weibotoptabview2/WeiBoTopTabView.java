package com.example.weibotoptabview2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class WeiBoTopTabView extends View implements ViewPager.OnPageChangeListener {
    private String[] tab = new String[]{"首页", "动态社区", "我的"};
    private List<Point> tabStartPoint = new ArrayList<>();
    private List<Point> tabEndPoint = new ArrayList<>();
    private List<RectF> tabRectF = new ArrayList<>();
    private int mCurrentSelected = 0;//当前被选中的TAB;
    private float mPositionOffset;
    private Context mContext;
    private Paint paint_tabText;
    private Paint paint_tabLine;
    private int defaultDistance;
    private int width;
    private int height;
    private int defaultColor = 0x5e5e5e5e;
    private int selectedColor = 0xff000000;
    private float offsetX;
    private int distanceStart;
    private int distanceEnd;
    private float offsetEnd;
    private float offsetStart;
    private OnTabClickListener mOnTabClickListener;

    private void setOffsetX(float offset) {
        this.offsetX = offset;
        if (mCurrentSelected < tab.length - 1) {
            distanceStart = tabStartPoint.get(mCurrentSelected).x - tabStartPoint.get(mCurrentSelected + 1).x;
            distanceEnd = tabEndPoint.get(mCurrentSelected + 1).x - tabEndPoint.get(mCurrentSelected).x;
            offsetEnd = distanceEnd * offsetX / 0.5f;
            offsetStart = distanceStart * offsetX / 0.5f;
        } else {
            distanceStart = tabStartPoint.get(mCurrentSelected).x - tabStartPoint.get(tab.length - 1).x;
            distanceEnd = tabEndPoint.get(tab.length - 1).x - tabEndPoint.get(mCurrentSelected).x;
            offsetEnd = distanceEnd * offsetX / 0.5f;
            offsetStart = distanceStart * offsetX / 0.5f;
        }
    }

    public void setOnTabClickListener(OnTabClickListener mOnTabClickListener) {
        this.mOnTabClickListener = mOnTabClickListener;
    }

    public WeiBoTopTabView(Context context) {
        this(context, null);
    }

    public WeiBoTopTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeiBoTopTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        defaultDistance = DisplayUtils.dp2px(mContext, 45);
        init();
    }

    private void init() {
        //初始化画笔
        paint_tabText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_tabText.setTextSize(DisplayUtils.dp2px(mContext, 17));
        paint_tabText.setColor(Color.parseColor("#5e5e5e5e"));
        paint_tabLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_tabLine.setStrokeWidth(6);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        float allTabLength = allTabLength();
        calculatePosition(width, allTabLength);
        Shader shader = new LinearGradient(tabStartPoint.get(0).x,
                tabStartPoint.get(0).y + 25,
                tabEndPoint.get(tab.length - 1).x,
                tabEndPoint.get(tab.length - 1).y + 25,
                Color.parseColor("#C22222"), Color.parseColor("#FF366196"), Shader.TileMode.CLAMP);
        paint_tabLine.setShader(shader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                for (int i = 0; i < tab.length; i++) {
                    boolean contains = tabRectF.get(i).contains(event.getX(), event.getY());
                    if (contains) {
                        if (mOnTabClickListener != null) {
                            mOnTabClickListener.onClick(i);
                        }
                        //  mCurrentSelected = i;
                        //   invalidate();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private float allTabLength() {
        float tabsWidth = 0;
        for (int i = 0; i < tab.length; i++) {
            tabsWidth += paint_tabText.measureText(tab[i]) + defaultDistance;
        }
        tabsWidth -= defaultDistance;
        return tabsWidth;
    }

    private void calculatePosition(float parentWith, float allChildLength) {

        float startPointX = (parentWith - allChildLength) / 2.0f;
        float measureText = 0;
        for (int i = 0; i < tab.length; i++) {
            if (i - 1 >= 0) {
                for (int m = 0; m < i; m++) {
                    measureText += paint_tabText.measureText(tab[m]);
                }
                tabStartPoint.add(new Point((int) (startPointX + i * defaultDistance + measureText), height / 2));
                tabEndPoint.add(new Point(tabStartPoint.get(i).x + (int) paint_tabText.measureText(tab[i]), height / 2));
                measureText = 0;
            } else {
                tabStartPoint.add(new Point((int) (startPointX + i * defaultDistance), height / 2));
                tabEndPoint.add(new Point(tabStartPoint.get(i).x + (int) paint_tabText.measureText(tab[i]), height / 2));
            }
            tabRectF.add(new RectF(tabStartPoint.get(i).x, 0, tabEndPoint.get(i).x, height));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        tabTextTransformation(canvas);
        tabLineTransformation(canvas);

    }

    private void tabLineTransformation(Canvas canvas) {
        for (int i = 0; i < tab.length; i++) {
            if (i == mCurrentSelected) {
                float lineY = tabStartPoint.get(i).y + 20;
                if (offsetEnd < distanceEnd) {
                    canvas.drawLine(tabStartPoint.get(i).x, lineY, tabStartPoint.get(i).x + paint_tabText.measureText(tab[i]) + offsetEnd, lineY, paint_tabLine);
                } else {
                    canvas.drawLine(tabStartPoint.get(i).x - (offsetStart - distanceStart), lineY, tabStartPoint.get(i).x + paint_tabText.measureText(tab[i]) + distanceEnd, lineY, paint_tabLine);
                }
            }
        }
    }

    private void tabTextTransformation(Canvas canvas) {
        for (int i = 0; i < tab.length; i++) {
            canvas.drawPoint(tabStartPoint.get(i).x, tabStartPoint.get(i).y, paint_tabText);
            canvas.drawPoint(tabEndPoint.get(i).x, tabEndPoint.get(i).y, paint_tabText);
            if (i == mCurrentSelected) {
                canvas.save();
                int evaluate = evaluate(1 - mPositionOffset, defaultColor, selectedColor);
                paint_tabText.setColor(Color.parseColor("#" + Integer.toHexString(evaluate)));
                float centerX = (tabEndPoint.get(i).x - tabStartPoint.get(i).x) / 2.0f + tabStartPoint.get(i).x;
                float centerY = height / 2.0f;
                canvas.scale(1 + 0.3f * (1 - mPositionOffset), 1 + 0.3f * (1 - mPositionOffset), centerX, centerY);
                canvas.drawText(tab[i], tabStartPoint.get(i).x, tabStartPoint.get(i).y, paint_tabText);
                canvas.restore();
            } else if (i == mCurrentSelected + 1) {
                canvas.save();
                int evaluate = evaluate(mPositionOffset, defaultColor, selectedColor);
                paint_tabText.setColor(Color.parseColor("#" + Integer.toHexString(evaluate)));
                float centerX = (tabEndPoint.get(i).x - tabStartPoint.get(i).x) / 2.0f + tabStartPoint.get(i).x;
                float centerY = height / 2.0f;
                canvas.scale(1 + 0.3f * (mPositionOffset), 1 + 0.3f * (mPositionOffset), centerX, centerY);
                canvas.drawText(tab[i], tabStartPoint.get(i).x, tabStartPoint.get(i).y, paint_tabText);
                canvas.restore();
            } else {
                canvas.save();
                paint_tabText.setColor(Color.parseColor("#5e5e5e5e"));
                canvas.drawText(tab[i], tabStartPoint.get(i).x, tabStartPoint.get(i).y, paint_tabText);
                canvas.restore();
            }
        }
    }

    /**
     * 计算不同进度值对应的颜色值，这个方法取自 ArgbEvaluator.java 类。
     *
     * @param percentage 进度值，范围[0, 1]。
     * @param startValue 起始颜色值。
     * @param endValue   最终颜色值。
     * @return 返回与进度值相应的颜色值。
     */
    private int evaluate(float percentage, int startValue, int endValue) {
        int startInt = startValue;
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >> 8) & 0xff) / 255.0f;
        float startB = (startInt & 0xff) / 255.0f;

        int endInt = endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >> 8) & 0xff) / 255.0f;
        float endB = (endInt & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + percentage * (endA - startA);
        float r = startR + percentage * (endR - startR);
        float g = startG + percentage * (endG - startG);
        float b = startB + percentage * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPositionOffset = positionOffset;
        mCurrentSelected = position;
        setOffsetX(positionOffset);
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //暴露给外部的接口
    interface OnTabClickListener {
        void onClick(int position);
    }
}
