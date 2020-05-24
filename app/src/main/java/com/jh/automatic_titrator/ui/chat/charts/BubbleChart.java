
package com.jh.automatic_titrator.ui.chat.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.jh.automatic_titrator.ui.chat.data.BubbleData;
import com.jh.automatic_titrator.ui.chat.interfaces.dataprovider.BubbleDataProvider;
import com.jh.automatic_titrator.ui.chat.renderer.BubbleChartRenderer;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {

    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BubbleChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public BubbleData getBubbleData() {
        return mData;
    }
}
