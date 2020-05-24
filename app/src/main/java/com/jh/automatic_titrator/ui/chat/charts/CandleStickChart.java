
package com.jh.automatic_titrator.ui.chat.charts;


import android.content.Context;
import android.util.AttributeSet;

import com.jh.automatic_titrator.ui.chat.data.CandleData;
import com.jh.automatic_titrator.ui.chat.interfaces.dataprovider.CandleDataProvider;
import com.jh.automatic_titrator.ui.chat.renderer.CandleStickChartRenderer;

public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider {

    public CandleStickChart(Context context) {
        super(context);
    }

    public CandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new CandleStickChartRenderer(this, mAnimator, mViewPortHandler);

        getXAxis().setSpaceMin(0.5f);
        getXAxis().setSpaceMax(0.5f);
    }

    @Override
    public CandleData getCandleData() {
        return mData;
    }
}
