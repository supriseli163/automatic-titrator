package com.jh.automatic_titrator.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;

/**
 * Created by violet_k on 6/1 2020.
 */
public class CircleMenuLayout extends ViewGroup {
    //圆形半径
    public float mRadius;
    // 内部空间半径
    public float mInnerRadius;
    //滑动时 item偏移量
    private double offsetRotation = -157.5;

    // 默认使用自动算周长
    public boolean useAuto = false;
    //    private double offsetRotation = 0;
    //适配
    private ListAdapter mAdapter;
    // 每个item 的默认尺寸
    private static final float ITEM_DIMENSION = 3f / 10f;
    //distanceFromCenter Item到中心的距离
    private static final float DISTANCE_FROM_CENTER = 4f / 5f;
    //    private static final float DISTANCE_FROM_CENTER = 1f;
    // MenuItem的点击事件接口
    private OnItemClickListener mOnMenuItemClickListener;
    float l, t, r, b;

    /**
     * @param context
     * @param attrs
     */
    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
    }

    /**
     * @param context
     */
    public CircleMenuLayout(Context context) {
        super(context);
        setPadding(0, 0, 0, 0);
    }

    public void setAdapter(ListAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    // 构建菜单项
    private void buildMenuItems() {
        // 根据用户设置的参数，初始化menu item
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final View itemView = mAdapter.getView(i, null, this);
            final int position = i;
            if (itemView != null) {
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.onItemClickListener(itemView, position);
                        }
                    }
                });
            }
            // 添加view到容器中
            addView(itemView);
        }
    }

    //窗口关联
    @Override
    protected void onAttachedToWindow() {
        if (mAdapter != null) {
            buildMenuItems();
        }
        super.onAttachedToWindow();
    }

    //设置布局的宽高，并策略menu item宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 丈量自身尺寸
        measureMyself(widthMeasureSpec, heightMeasureSpec);
        // 丈量菜单项尺寸
        measureChildViews();
    }

    private void measureMyself(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;
        // 根据传入的参数，分别获取测量模式和测量值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 如果宽或者高的测量模式非精确值
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            // 主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            // 如果都设置为精确值，则直接取小值；
            resWidth = Math.min(width, height);
            resHeight = Math.min(width, height);
        }
        // 主要设置为背esHeight == 0 ? getDefaultWidth() : resHeight;
        setMeasuredDimension(resWidth, resHeight);
    }

    private void measureChildViews() {
        // 获得半径
        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        // menu mRadius
        final int count = getChildCount();
        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = 0;
            ViewGroup.LayoutParams params = child.getLayoutParams();
            params.height = (int) getChildSize(child, count);
            params.width = (int) getChildSize(child, count);
            child.setLayoutParams(params);
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }
    }

    // 布局menu item的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        refresh("onLayout");
    }

    //刷新  偏移角度移动item
    public void refresh(String tag) {
        final float childCount = getChildCount();
        // 根据menu item的个数，计算item的布局占用的角度
        float angleDelay = 360 / (childCount);
        // 遍历所有菜单项设置它们的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            float childSize = getChildSize(child, childCount);
            float x = Math.round(Math.cos(Math.toRadians(angleDelay * (i + 1) - offsetRotation)) * (mRadius * DISTANCE_FROM_CENTER));
            float y = Math.round(Math.sin(Math.toRadians(angleDelay * (i + 1) - offsetRotation)) * (mRadius * DISTANCE_FROM_CENTER));
            //计算item 距离左边距  上边距 的距离
            if (x <= 0 && y >= 0) {
                x = mRadius - Math.abs(x);
                y = mRadius - Math.abs(y);
                //计算item中心点 距离左边距  上边距 的距离
            } else if (x <= 0 && y <= 0) {
                y = mRadius + Math.abs(y);
                x = mRadius - Math.abs(x);
            } else if (x >= 0 && y <= 0) {
                y = mRadius + Math.abs(y);
                x = mRadius + x;
            } else if (x >= 0 && y >= 0) {
                x = mRadius + x;
                y = mRadius - Math.abs(y);
            }
            mInnerRadius = mRadius - child.getMeasuredWidth();
            l = x - childSize / 2f;
            t = y - childSize / 2f;
            r = x + childSize / 2f;
            b = y + childSize / 2f;
            child.layout((int) l, (int) t, (int) r, (int) b);
            Log.d("ExecuteFragment", "refreshMenu000: " + mInnerRadius + " mRadius: " + mRadius);
//            if (mOnMenuItemClickListener != null) {
//                Log.d("songkai", "refreshMenu111");
//                mOnMenuItemClickListener.onRefreshFinish();
//                Log.d("songkai", "refreshMenu222");
//            }
        }
    }

    private float getChildSize(View child, float childCount) {
        if (useAuto) {
            // TODO: 2020-06-07 auto is not ready to use, please not use this ,because is error;
            float value = 360 / childCount;
            return (float) (2 * mRadius * 2 * mRadius * (1 - Math.cos(value)));
        } else {
            return child.getMeasuredWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#dddddd"));
        canvas.drawCircle(mRadius, mRadius, mRadius, paint);
        refresh("ExecuteFragment onDraw");
    }

    //定义点击接口
    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);

        void onRefreshFinish();
    }

    // 设置MenuItem的点击事件接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }
}
