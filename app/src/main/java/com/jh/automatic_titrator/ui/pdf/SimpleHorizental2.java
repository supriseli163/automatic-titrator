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
public class SimpleHorizental2 extends AbstractViewRenderer {

    private List<Test> tests;

    private Context context;

    public SimpleHorizental2(Context ctx, int layoutResId, List<Test> tests) {
        super(ctx, layoutResId);
        this.context = ctx;
        this.tests = tests;
    }

    @Override
    protected void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.export_sh2_lv);

        SimpleHorizentalAdaptor adaptor = new SimpleHorizentalAdaptor(context, tests);
        listView.setAdapter(adaptor);
    }
}
