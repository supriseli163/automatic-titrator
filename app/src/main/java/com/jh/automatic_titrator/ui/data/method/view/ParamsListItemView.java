package com.jh.automatic_titrator.ui.data.method.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.databinding.TitratorEndFormItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.jh.automatic_titrator.common.utils.ViewUtils.dpToPx;

public class ParamsListItemView extends LinearLayout {
    private List<List<String>> arraysList;
    private TitratorEndFormItemBinding binding;
    private OperateListener listener;
    private int currentP = -1;

    public ParamsListItemView(Context context) {
        this(context, null);
    }

    public ParamsListItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParamsListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        arraysList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = TitratorEndFormItemBinding.inflate(inflater, this, true);
    }

    public void setArraysList(List<List<String>> arraysList, OperateListener listener) {
        this.listener = listener;
        this.arraysList = arraysList;
        addBannerTop();
        addItem();
        initListener();
    }

    private void initListener() {
        binding.addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddEvent(binding.paramsList.getChildCount());
                }
            }
        });
        binding.modifyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (currentP < 0) {
                        return;
                    }
                    listener.onModifyEvent(currentP);
                }
            }
        });
        binding.deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteItem(currentP);
                if (listener != null) {
                    listener.onDeleteEvent(currentP);
                }
            }
        });
    }

    private void addBannerTop() {
        if (arraysList.size() > 0) {
            int count = 0;
            List<String> top = arraysList.get(0);
            binding.bannerTop.removeAllViews();
            for (int i = 0; i < top.size(); i++) {
                String content = top.get(i);
                count = content.length() + count;
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams topContentTextView_lp = new LinearLayout.LayoutParams(
                        dpToPx(130), LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(topContentTextView_lp);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(dpToPx(10), 0, dpToPx(10), 0);
                textView.setTextColor(getResources().getColor(R.color.fontWhite));
                textView.setText(content);
                Log.d("songkai", "content:" + content);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setBackgroundColor(getResources().getColor(R.color.transparent));
                binding.bannerTop.addView(textView);
                LinearLayout.LayoutParams viewLayout = new LinearLayout.LayoutParams(
                        dpToPx(1), LinearLayout.LayoutParams.MATCH_PARENT);
                View view = new View(getContext());
                view.setLayoutParams(viewLayout);
                view.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                binding.bannerTop.addView(view);
                Log.d("songkai", "count:" + binding.bannerTop.getChildCount());
            }
        }
    }

    private void addItem() {
        binding.paramsList.removeAllViews();
        if (arraysList.size() > 1) {
            for (int k = 1; k < arraysList.size(); k++) {
                List<String> itemList = arraysList.get(k);
                addItemData(itemList, k - 1);
            }
        }
    }

    // 外部处理数据传入
    public void addItemData(List<String> itemList, int position) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        // 设置checkBox
        linearLayout.setBackgroundColor(getResources().getColor(R.color.fontLightGray));
        CheckBox checkBox = new CheckBox(getContext());
        LinearLayout.LayoutParams checkBoxLayout = new LayoutParams(dpToPx(70), dpToPx(40));
        checkBoxLayout.gravity = Gravity.CENTER;
        checkBox.setLayoutParams(checkBoxLayout);
        checkBox.setPadding(dpToPx(15), 0, 0, 0);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setChecked(position == currentP && currentP > 0);
        checkBox.setId(R.id.text_view_1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLUE));
        }
        linearLayout.addView(checkBox);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
//            }
        addViewLine(linearLayout);
        for (int i = 0; i < itemList.size(); i++) {
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            linearLayout.setGravity(Gravity.CENTER);
//            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorGray1));

            // 设置名称等
            String content = itemList.get(i);
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams topContentTextView_lp = new LayoutParams(dpToPx(130), dpToPx(40));
            textView.setLayoutParams(topContentTextView_lp);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.fontBlack));
            textView.setText(content);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            linearLayout.addView(textView);
            addViewLine(linearLayout);
//            if (i != itemList.size() - 1) {
//            addViewLine(linearLayout);
//            }

            checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    processSelectItemStatus((Integer) linearLayout.getTag());
                }
            });
        }
        binding.paramsList.addView(linearLayout, position);
        initTag();
    }

    private void initTag() {
        for (int i = 0; i < binding.paramsList.getChildCount(); i++) {
            View view = binding.paramsList.getChildAt(i);
            view.setTag(i);
        }
    }

    // 修改某个Item
    public void modifyItem(List<String> arrayList, int position) {
        onDeleteItem(position);
        addItemData(arrayList, position);
    }

    // 删除某个item
    public void onDeleteItem(int position) {
        if (binding.paramsList.getChildCount() > position) {
            binding.paramsList.removeViewAt(position);
        }
        initTag();
    }

    private void addViewLine(LinearLayout viewGroup) {
        LayoutParams layoutParams = new LayoutParams(dpToPx(1), dpToPx(40));
        View view = new View(getContext());
        view.setLayoutParams(layoutParams);
        view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        view.setBackgroundColor(getResources().getColor(R.color.colorWrite));
        viewGroup.addView(view);
    }

    private void processSelectItemStatus(int position) {
        currentP = position;
        Log.d("processSelectItemStatus", "processSelectItemStatus 设置当前position: " + currentP);
        for (int i = 0; i < binding.paramsList.getChildCount(); i++) {
            LinearLayout subView = (LinearLayout) binding.paramsList.getChildAt(i);
            ((CheckBox) subView.findViewById(R.id.text_view_1)).setChecked(i == position);
        }
    }

    public interface OperateListener {
        void onAddEvent(int position);

        void onModifyEvent(int position);

        void onDeleteEvent(int position);
    }
}