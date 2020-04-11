package com.jh.automatic_titrator.ui.execute;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.service.MethodService;

public class ExecuteWorkingDetailFragment extends Fragment implements View.OnClickListener {

    private TableLayout tableLayout;
    private TextView titrator_type_tv;
    private TextView titrator_method_name_tv;
    private TextView burette_volume_tv;
    private TextView working_electrode_tv;
    private TextView reference_electrode_tv;

    private TrunkUtil trunkUtil;
    private ExecuteListHandler executeListHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.execute_woking_detail_fragment, container, false);
        titrator_type_tv = (TextView)view.findViewById(R.id.titrator_type_input);
        titrator_method_name_tv = (TextView)view.findViewById(R.id.titrator_method_name_input);
        burette_volume_tv = (TextView)view.findViewById(R.id.titrator_method_burette_volume);
        working_electrode_tv = (TextView)view.findViewById(R.id.working_electrode_input);
        reference_electrode_tv = (TextView)view.findViewById(R.id.reference_electrode_input);

        trunkUtil = TrunkUtil.getInstance();
//        trunkUtil.addListener(new TrunkListener() {
//            @Override
//            public int getListenType() {
//                return 0;
//            }
//
//            @Override
//            public void notifyData(TrunkData trunkData) {
//
//            }
//        })
        executeListHandler = new ExecuteListHandler();
//        MethodService.getInstance().addListener(executeListHandler);
        refresh();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    private void refresh() {
        Log.d("", "refresh:baseInfo");
        TitratorMethod titratorMethod = Cache.getTitratorMethod();
        titrator_type_tv.setText(titratorMethod.getTitratorType());
        titrator_method_name_tv.setText(titratorMethod.getMethodName());
        burette_volume_tv.setText(String.valueOf(titratorMethod.getBuretteVolume()));
        reference_electrode_tv.setText(String.valueOf(titratorMethod.getReferenceElectrode()));
    }

    public class ExecuteListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Ob
//            }
        }
    }


}
