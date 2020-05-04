package com.jh.automatic_titrator.ui.data.method.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
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
                    listener.onAddEvent();
                }
            }
        });
        binding.modifyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
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
            List<String> top = arraysList.get(0);
            for (int i = 0; i < arraysList.size(); i++) {
                String content = top.get(i);
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams topContentTextView_lp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, content.length());
                textView.setLayoutParams(topContentTextView_lp);
                textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                textView.setTextColor(getResources().getColor(R.color.colorWrite));
                textView.setText(content);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.bannerTop.addView(textView);
                if (i != arraysList.size() - 1) {
                    LinearLayout.LayoutParams viewLayout = new LinearLayout.LayoutParams(
                            dpToPx(R.dimen._1px), LinearLayout.LayoutParams.MATCH_PARENT);
                    View view = new View(getContext());
                    view.setLayoutParams(viewLayout);
                    view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                    view.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                    binding.bannerTop.addView(view);
                }
            }
        }
    }

    private void addItem() {
        if (arraysList.size() > 1) {
            for (int k = 0; k < arraysList.size(); k++) {
                List<String> itemList = arraysList.get(k);
                addItemData(itemList, 0);
            }
        }
    }


    // 外部处理数据传入
    public void addItemData(List<String> itemList, int position) {
        for (int i = 0; i < itemList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            // 设置checkBox
            CheckBox checkBox = new CheckBox(getContext());
            LayoutParams checkBoxLayout = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            checkBox.setLayoutParams(checkBoxLayout);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setChecked(i == currentP);
            checkBox.setId(R.id.text_view_1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
            }
            addViewLine(linearLayout);
            // 设置名称等
            LayoutParams viewLayout = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(viewLayout);
            String content = itemList.get(i);
            TextView textView = new TextView(getContext());
            LayoutParams topContentTextView_lp = new LayoutParams(
                    0, LayoutParams.MATCH_PARENT, content.length());
            textView.setLayoutParams(topContentTextView_lp);
            textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(getResources().getColor(R.color.colorWrite));
            textView.setText(content);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            linearLayout.addView(textView);
            if (i != arraysList.size() - 1) {
                addViewLine(linearLayout);
            }
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    processSelectItemStatus((Integer) linearLayout.getTag());
                }
            });
            binding.paramsList.addView(linearLayout, position);
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
    }

    private void addViewLine(LinearLayout viewGroup) {
        LayoutParams layoutParams = new LayoutParams(
                dpToPx(R.dimen._1px), LayoutParams.MATCH_PARENT);
        View view = new View(getContext());
        view.setLayoutParams(layoutParams);
        view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        view.setBackgroundColor(getResources().getColor(R.color.colorWrite));
        viewGroup.addView(view);
    }

    private void processSelectItemStatus(int position) {
        currentP = position;
        for (int i = 0; i < binding.paramsList.getChildCount(); i++) {
            LinearLayout subView = binding.paramsList;
            ((CheckBox) subView.findViewById(R.id.text_view_1)).setChecked(i == position);
        }
    }

    public interface OperateListener {
        void onAddEvent();

        void onModifyEvent(int position);

        void onDeleteEvent(int position);
    }
}