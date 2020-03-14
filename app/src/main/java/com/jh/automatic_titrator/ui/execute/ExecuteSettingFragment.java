package com.jh.automatic_titrator.ui.execute;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.common.ObserverAble;
import com.jh.automatic_titrator.service.ExecutorService;

import java.util.List;

public class ExecuteSettingFragment extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, ObserverAble, AdapterView.OnItemSelectedListener {

    private static final int REFRESH_TEMSTMETHOD = 0xFFFFFFFE;
    private static final int REFRESH_WAVELENGTH =0xFFFFFFFC;
    private static final int REFRESH_METHODS = 0xFFFFFFFA;
    private static final int TEMPERATURE_SAVING = 0xFFFFFFEE;

    private List<String> testMethods;


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void addObserver(Observer observer) {

    }

    private void loadMethods() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        testMethods.clear();
                        testMethods.addAll()
                    }
                }
        );
    }


}
