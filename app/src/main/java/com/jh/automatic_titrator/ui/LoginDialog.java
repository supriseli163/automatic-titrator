package com.jh.automatic_titrator.ui;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by apple on 16/9/15.
 */
public class LoginDialog extends Dialog {
    public LoginDialog(Context context) {
        super(context);
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
