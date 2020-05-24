package com.jh.automatic_titrator.ui.chat.view;

import android.content.Context;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.ui.chat.components.MarkerView;
import com.jh.automatic_titrator.ui.chat.data.CandleEntry;
import com.jh.automatic_titrator.ui.chat.data.Entry;
import com.jh.automatic_titrator.ui.chat.highlight.Highlight;
import com.jh.automatic_titrator.ui.chat.utils.MPPointF;
import com.jh.automatic_titrator.ui.chat.utils.Utils;

public class MyMarkerView extends MarkerView {
    private final TextView tvContent;

    public MyMarkerView(Context context) {
        super(context, R.layout.custom_marker_view);

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
