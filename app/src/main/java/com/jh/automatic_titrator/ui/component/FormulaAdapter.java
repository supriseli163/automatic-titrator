package com.jh.automatic_titrator.ui.component;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.Formula;

import java.util.List;

/**
 * Created by apple on 2017/2/19.
 */

public class FormulaAdapter extends ArrayAdapter<Formula> {

    private int resource;

    private int dropdownResource;

    public FormulaAdapter(Context context, int resource, Formula[] objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public FormulaAdapter(Context context, int resource, List<Formula> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropdownResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Formula formula = getItem(position);
        final View view;
        final TextView text;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        } else {
            view = convertView;
        }
        try {
            //  If no custom field is assigned, assume the whole resource is a TextView
            text = (TextView) view;
            if (StringUtils.isEmpty(formula.getSimpleName())) {
                text.setText(formula.getFormulaName());
            } else {
                text.setText(formula.getFormulaName() + "(" + formula.getSimpleName() + ")");
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Formula formula = getItem(position);
        final View view;
        final TextView text;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(dropdownResource, parent, false);
        } else {
            view = convertView;
        }
        try {
            //  If no custom field is assigned, assume the whole resource is a TextView
            text = (TextView) view;
            if (StringUtils.isEmpty(formula.getSimpleName())) {
                text.setText(formula.getFormulaName());
            } else {
                text.setText(formula.getFormulaName() + "(" + formula.getSimpleName() + ")");
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        return view;
    }
}
