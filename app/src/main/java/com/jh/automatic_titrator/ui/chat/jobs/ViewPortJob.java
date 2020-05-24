
package com.jh.automatic_titrator.ui.chat.jobs;

import android.view.View;

import com.jh.automatic_titrator.ui.chat.utils.ObjectPool;
import com.jh.automatic_titrator.ui.chat.utils.Transformer;
import com.jh.automatic_titrator.ui.chat.utils.ViewPortHandler;


public abstract class ViewPortJob extends ObjectPool.Poolable implements Runnable {

    protected float[] pts = new float[2];

    protected ViewPortHandler mViewPortHandler;
    protected float xValue = 0f;
    protected float yValue = 0f;
    protected Transformer mTrans;
    protected View view;

    public ViewPortJob(ViewPortHandler viewPortHandler, float xValue, float yValue,
                       Transformer trans, View v) {

        this.mViewPortHandler = viewPortHandler;
        this.xValue = xValue;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;

    }

    public float getXValue() {
        return xValue;
    }

    public float getYValue() {
        return yValue;
    }
}
