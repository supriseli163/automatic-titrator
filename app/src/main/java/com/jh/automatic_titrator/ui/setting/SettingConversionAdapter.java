package com.jh.automatic_titrator.ui.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.entity.common.Conversion;
import com.jh.automatic_titrator.service.ExecutorService;

import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class SettingConversionAdapter extends BaseAdapter {

    private List<Conversion> conversions;

    private Context context;

    private boolean editable;

    private DeleteListener deleteListener;

    public SettingConversionAdapter(List<Conversion> conversions, Context context, boolean editable, DeleteListener deleteListener) {
        this.conversions = conversions;
        this.context = context;
        this.editable = editable;
        this.deleteListener = deleteListener;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public int getCount() {
        return conversions.size();
    }

    @Override
    public Object getItem(int position) {
        return conversions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return conversions.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_formula_conversion_item, null);

        final Conversion conversion = conversions.get(position);

        if (editable) {
            view.setEnabled(true);
        } else {
            view.setEnabled(false);
        }

        final ImageView delete = (ImageView) view.findViewById(R.id.conversion_item_delete);
        if (conversions.size() > 1 && editable) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.INVISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    ExecutorService.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (deleteListener != null && position < conversions.size()) {
                                deleteListener.delete(position);
                            }
                        }
                    });
                }
            }
        });

        TextView p5 = (TextView) view.findViewById(R.id.conversion_item_p5);
        if (conversion.getP5() != 1) {
            p5.setText(conversion.getP5() + "");
        }

        TextView p4 = (TextView) view.findViewById(R.id.conversion_item_p4);
        if (conversion.getP4() != 1) {
            p4.setText(conversion.getP4() + "");
        }

        TextView p3 = (TextView) view.findViewById(R.id.conversion_item_p3);
        if (conversion.getP3() != 1) {
            p3.setText(conversion.getP3() + "");
        }

        TextView p2 = (TextView) view.findViewById(R.id.conversion_item_p2);
        if (conversion.getP2() != 1) {
            p2.setText(conversion.getP2() + "");
        }

        TextView p1 = (TextView) view.findViewById(R.id.conversion_item_p1);
        if (conversion.getP1() != 1) {
            p1.setText(conversion.getP1() + "");
        }

        TextView p0 = (TextView) view.findViewById(R.id.conversion_item_p0);
        if (conversion.getP0() != 1) {
            p0.setText(conversion.getP0() + "");
        }

        TextView start = (TextView) view.findViewById(R.id.conversion_item_start);
        if (conversion.getStart() != null) {
            start.setText(conversion.getStart() + "");
        }

        TextView end = (TextView) view.findViewById(R.id.conversion_item_end);
        if (conversion.getEnd() != null) {
            end.setText(conversion.getEnd() + "");
        }

        p0.addTextChangedListener(new ConversionTextWatcher(conversion, 0));
        p1.addTextChangedListener(new ConversionTextWatcher(conversion, 1));
        p2.addTextChangedListener(new ConversionTextWatcher(conversion, 2));
        p3.addTextChangedListener(new ConversionTextWatcher(conversion, 3));
        p4.addTextChangedListener(new ConversionTextWatcher(conversion, 4));
        p5.addTextChangedListener(new ConversionTextWatcher(conversion, 5));
        start.addTextChangedListener(new ConversionTextWatcher(conversion, 6));
        end.addTextChangedListener(new ConversionTextWatcher(conversion, 7));
        p0.setEnabled(editable);
        p1.setEnabled(editable);
        p2.setEnabled(editable);
        p3.setEnabled(editable);
        p4.setEnabled(editable);
        p5.setEnabled(editable);
        start.setEnabled(editable);
        end.setEnabled(editable);

        return view;
    }

    private class ConversionTextWatcher implements TextWatcher {

        private Conversion conversion;

        private int position;

        public ConversionTextWatcher(Conversion conversion, int position) {
            this.conversion = conversion;
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            double value = 1;
            if (s != null && s.length() > 0) {
                if (s.toString().equals("-")) {
                    value = -1;
                } else {
                    value = Double.parseDouble(s.toString());
                }
            }
            switch (position) {
                case 0:
                    conversion.setP0(value);
                    break;
                case 1:
                    conversion.setP1(value);
                    break;
                case 2:
                    conversion.setP2(value);
                    break;
                case 3:
                    conversion.setP3(value);
                    break;
                case 4:
                    conversion.setP4(value);
                    break;
                case 5:
                    conversion.setP5(value);
                    break;
                case 6:
                    conversion.setStart(value);
                    break;
                case 7:
                    conversion.setEnd(value);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public interface DeleteListener {
        void delete(int position);
    }
}
