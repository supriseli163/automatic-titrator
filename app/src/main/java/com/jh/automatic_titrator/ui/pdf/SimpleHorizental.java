package com.jh.automatic_titrator.ui.pdf;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Test;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.ui.HomePageActivity;

import java.util.List;

/**
 * Created by apple on 2016/11/19.
 */
public class SimpleHorizental extends AbstractViewRenderer {

    private String title1;

    private String title2;

    private String title3;

    private User user;

    private List<Test> tests;

    private Context context;

    public SimpleHorizental(Context ctx, int layoutResId, String title1, String title2, String title3, User user, List<Test> tests) {
        super(ctx, layoutResId);
        this.context = ctx;
        this.title1 = title1;
        this.title2 = title2;
        this.title3 = title3;
        this.user = user;
        this.tests = tests;
    }

    @Override
    protected void initView(View view) {
        ((TextView) view.findViewById(R.id.export_sh_title1)).setText(title1);
        if (StringUtils.isEmpty(title2)) {
            view.findViewById(R.id.export_sh_title2).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.export_sh_title2)).setText(title2);
        }
        if (StringUtils.isEmpty(title3)) {
            view.findViewById(R.id.export_sh_title3).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.export_sh_title3)).setText(title3);
        }
        String currentDate = TimeTool.currentDate();
        String date = context.getString(R.string.date) + ": " + TimeTool.dateToDate(currentDate);
        String time = context.getString(R.string.time) + ": " + TimeTool.dateToTime(currentDate);
        ((TextView) view.findViewById(R.id.export_sh_date)).setText(date);
        ((TextView) view.findViewById(R.id.export_sh_time)).setText(time);
        String creator = context.getString(R.string.operator) + ": " + user.getUserName();
        ((TextView) view.findViewById(R.id.export_sh_creator)).setText(creator);

        //电子签名
        HomePageActivity homePageActivity = (HomePageActivity) context;
        String autograph = context.getString(R.string.autograph) + ": " + homePageActivity.readconf("autograph");
        ((TextView)view.findViewById(R.id.export_sh_autograph)).setText(autograph);

        ListView listView = (ListView) view.findViewById(R.id.export_sh_lv);

        SimpleHorizentalAdaptor adaptor = new SimpleHorizentalAdaptor(context, tests);
        listView.setAdapter(adaptor);
        Log.d("initView", "initView: true " + tests.size());
    }
}
