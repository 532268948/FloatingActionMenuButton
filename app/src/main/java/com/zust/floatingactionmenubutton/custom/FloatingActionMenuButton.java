package com.zust.floatingactionmenubutton.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.zust.floatingactionmenubutton.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作 者： ZUST_YTH
 * 日 期： 2018/4/14
 * 时 间： 14:48
 * 项 目： FloatingActionMenuButton
 * 描 述：
 */


public class FloatingActionMenuButton {

    private View mainActionView;
    private int startAngle;
    private int endAngle;
    private int radius;
    private boolean open;
    private List<Item> subActionItems;

    public FloatingActionMenuButton(View mainActionView,
                                    int startAngle,
                                    int endAngle,
                                    int radius,
                                    List<Item> subActionItems) {
        this.mainActionView = mainActionView;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.radius = radius;
        this.subActionItems = subActionItems;

        this.mainActionView.setClickable(true);
        this.mainActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    private void open() {
        Point center = calculateItemPosition();
        for (int i = 0; i < subActionItems.size(); i++) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionItems.get(i).width, subActionItems.get(i).height, Gravity.TOP | Gravity.LEFT);
            params.setMargins(center.x-subActionItems.get(i).width/2,center.y-subActionItems.get(i).height/2,0,0);
            addViewToCurrentContainer(subActionItems.get(i).view,params);
        }
        open=true;
//        Log.e("TAG",subActionItems.size()+"");
        for (int i = 0; i < subActionItems.size(); i++) {

            subActionItems.get(i).view.setScaleX(0);
            subActionItems.get(i).view.setScaleY(0);
            subActionItems.get(i).view.setAlpha(0);

            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, subActionItems.get(i).x - center.x + subActionItems.get(i).width / 2);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, subActionItems.get(i).y - center.y + subActionItems.get(i).height / 2);
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 720);
            PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
            PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1);

            final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(subActionItems.get(i).view, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA);
            animation.setDuration(500);
            animation.setInterpolator(new OvershootInterpolator(0.9f));

            animation.setStartDelay((subActionItems.size() - i) * 20);
            animation.start();
        }
    }

    private void addViewToCurrentContainer(View view, ViewGroup.LayoutParams params) {
        try {
            if (params!=null){
                FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)params;
                ((ViewGroup)getActivityContentView()).addView(view,lp);
            }else {
                ((ViewGroup)getActivityContentView()).addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Point calculateItemPosition() {
        Point center = getActionViewCenter();
        RectF area = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
        Path orbit = new Path();
        orbit.addArc(area, startAngle, endAngle - startAngle);
        PathMeasure pathMeasure = new PathMeasure(orbit, false);
        int divisor;
        if (Math.abs(endAngle - startAngle) >= 360 || subActionItems.size() <= 1) {
            divisor = subActionItems.size();
        } else {
            divisor = subActionItems.size() - 1;
        }
        for (int i = 0; i < subActionItems.size(); i++) {
            float[] coords = new float[2];
            pathMeasure.getPosTan((i) * pathMeasure.getLength() / divisor, coords, null);
            subActionItems.get(i).x = (int) coords[0] - subActionItems.get(i).width / 2;
            subActionItems.get(i).y = (int) coords[1] - subActionItems.get(i).height / 2;
        }
        return center;
    }

    private Point getActionViewCenter() {
        Point center = getActionViewCoordinates();
        center.x += mainActionView.getMeasuredWidth() / 2;
        center.y += mainActionView.getMeasuredHeight() / 2;
        return center;
    }

    private Point getActionViewCoordinates() {
        int[] coords = new int[2];
        mainActionView.getLocationOnScreen(coords);
        Rect activityFrame = new Rect();
        getActivityContentView().getWindowVisibleDisplayFrame(activityFrame);
        coords[0] -= (getScreenSize().x - getActivityContentView().getMeasuredWidth());
        coords[1] -= (activityFrame.height() + activityFrame.top - getActivityContentView().getMeasuredHeight());
        return new Point(coords[0], coords[1]);
    }

    private Point getScreenSize() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size;
    }

    public WindowManager getWindowManager() {
        return (WindowManager) mainActionView.getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private View getActivityContentView() {
        return ((Activity) mainActionView.getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    }


    private void close() {
    }


public static class Item {
    public int x;
    public int y;
    public int width;
    public int height;

    public float alpha;

    public View view;

    public Item(View view, int width, int height) {
        this.view = view;
        this.width = width;
        this.height = height;
        alpha = view.getAlpha();
        x = 0;
        y = 0;
    }
}

public static class Builder {

    private View mainActioniew;
    private int startAngle;
    private int endAngle;
    private int radius;
    private boolean open;
    private List<Item> subActionItems;

    public Builder(Context context) {
        this.startAngle = 180;
        this.endAngle = 270;
        this.radius = context.getResources().getDimensionPixelSize(R.dimen.floating_menu_button_radius);
        this.open = false;
        this.subActionItems = new ArrayList<>();
    }

    public Builder attachView(View mainActionView) {
        this.mainActioniew = mainActionView;
        return this;
    }

    public Builder setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        return this;
    }

    public Builder setEndAngle(int endAngle) {
        this.endAngle = endAngle;
        return this;
    }

    public Builder setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public Builder addSubActionItem(View subActionView, int width, int height) {
        this.subActionItems.add(new Item(subActionView, width, height));
        return this;
    }

    public FloatingActionMenuButton build() {
        return new FloatingActionMenuButton(mainActioniew,
                startAngle,
                endAngle,
                radius,
                subActionItems);
    }
}
}
