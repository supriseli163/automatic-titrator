package com.jh.automatic_titrator.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by apple on 2017/2/18.
 */

public class ToastUtil {

    public static void toastShow(Toast toast, String str) {
        toast.setText(str);
        toast.show();
    }

    public static Toast createToast(Context context) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        return toast;
    }
}
