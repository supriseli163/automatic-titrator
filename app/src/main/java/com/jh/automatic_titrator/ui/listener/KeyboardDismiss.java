package com.jh.automatic_titrator.ui.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

/**
 * Created by apple on 2017/3/18.
 */

public class KeyboardDismiss implements DialogInterface.OnDismissListener, PopupWindow.OnDismissListener {

    private Activity mActivity;

    public KeyboardDismiss(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dismiss();
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    private void dismiss() {
        int mode = mActivity.getWindow().getAttributes().softInputMode;
        System.out.println(mode + "------------");
        InputMethodManager inputMgr = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        System.out.println(inputMgr.isActive() + "-----------");
        View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            inputMgr.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        } else if (inputMgr.isActive()) {
            inputMgr.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
