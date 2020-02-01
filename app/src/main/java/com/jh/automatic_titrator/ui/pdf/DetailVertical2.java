package com.jh.automatic_titrator.ui.pdf;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.entity.common.Test;

import java.util.List;

/**
 * Created by apple on 2016/11/19.
 */
public class DetailVertical2 extends AbstractViewRenderer {

    private List<Test> tests;

    private Context context;

    public DetailVertical2(Context ctx, int layoutResId, List<Test> tests) {
        super(ctx, layoutResId);
        this.context = ctx;
        this.tests = tests;
    }

    @Override
    protected void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.export_dv2_lv);

        DetailVerticalAdaptor adaptor = new DetailVerticalAdaptor(context, tests);
        listView.setAdapter(adaptor);
    }
}
