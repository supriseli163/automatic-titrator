package com.jh.automatic_titrator.ui.help;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.UDisk;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.SystemService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class HelpUpdateFragment extends Fragment implements View.OnClickListener {

    View searchVersion_btn;

    private Toast mToast;

    private HelpUpdateHandler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_update_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        mHandler = new HelpUpdateHandler();
        searchVersion_btn = view.findViewById(R.id.help_update_search_btn);
        searchVersion_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_update_search_btn:
                doUpdate();
                break;
        }
    }

    private void doUpdate() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<UDisk> uDisks = SystemService.getUDisksNew(getActivity());
                        if (uDisks == null || uDisks.size() == 0) {
                            Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.apk_not_found)).sendToTarget();
                            return;
                        }
                        File file = new File(uDisks.get(0).getFilePath() + "/zhuoguang.apk");
//                        File file = new File(getTempFileDir() + "/hanon.apk");
                        if (!file.exists()) {
                            Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.apk_not_found)).sendToTarget();
                            return;
                        } else {
                            Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.updating)).sendToTarget();
//                            install(file);
                            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.help), getString(R.string.update), getString(R.string.update), Cache.currentTime(), DBService.getAuditHelper(getActivity()));
                            boolean result = install(file.getAbsolutePath());
                            if (!result) {
                                Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.install_app_not_found)).sendToTarget();
                            } else {
                                Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.update_success)).sendToTarget();
                            }
                        }
                    }
                }
        );
    }

    private void install(File file) {
        Intent install = new Intent();
        install.setAction(Intent.ACTION_VIEW);
        install.setData(Uri.parse("file://" + file.getAbsolutePath()));
        install.setType("application/vnd.android.package-archive");
//        install.setType("application/*");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getActivity().startActivity(install);
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(mHandler, BaseActivity.SHOW_MSG, getString(R.string.install_app_not_found)).sendToTarget();
        }
    }

    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    private String getTempFileDir() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    private class HelpUpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.SHOW_MSG:
                    mToast.setText(String.valueOf(msg.obj));
                    mToast.show();
            }
        }
    }
}
