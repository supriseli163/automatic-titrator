package com.jh.automatic_titrator.common.printer;

import android.content.Context;
import android.util.Log;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.trunk.SerialPortUtil1;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by apple on 2017/7/13.
 */

public class Sensitive {

    public static void printTests(Context context, List<Test> tests) {
        printHead(context);
        for (Test test : tests) {
            printTest(context, test);
        }
        printEnd(context);
    }

    public static void printSingleTest(Context context, Test test) {
        printHead(context);
        printTest(context, test);
        printEnd(context);
    }

    public static void printTest(Context context, Test test) {
        SerialPortUtil1 serialPortUtil1 = SerialPortUtil1.getInstance();
        String content = getBufferFromLights(context, test);
        try {
            serialPortUtil1.sendBuffer(content.getBytes("GB18030"));
            Log.e("content", content);
        } catch (UnsupportedEncodingException e) {
            Log.e("content_err", e.getMessage());
        }
    }

    public static void printHead(Context context) {
        SerialPortUtil1 serialPortUtil1 = SerialPortUtil1.getInstance();
        String content = getHeadBuffer(context);
        try {
            serialPortUtil1.sendBuffer(content.getBytes("GB18030"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printEnd(Context context) {
        SerialPortUtil1 serialPortUtil1 = SerialPortUtil1.getInstance();
        String content = "=========================================\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\f";
        try {
            serialPortUtil1.sendBuffer(content.getBytes("GB18030"));
            serialPortUtil1.sendBuffer(new byte[]{0x1D, 0x56,0x00});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String getHeadBuffer(Context context) {
        StringBuilder stringBuilder = new StringBuilder();

        String currentDate = TimeTool.currentDate();

        stringBuilder.append(context.getString(R.string.date));
        stringBuilder.append(":");
        stringBuilder.append(TimeTool.dateToDate(currentDate));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.time));
        stringBuilder.append(":");
        stringBuilder.append(TimeTool.dateToTime(currentDate));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.operator));
        stringBuilder.append(":");
        stringBuilder.append(Cache.getUser().getUserName());
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }

    private static String getBufferFromLights(Context context, Test test) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(context.getString(R.string.sort_number));
        stringBuilder.append(":");
        stringBuilder.append(test.getTestId() + "");
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.sample_name));
        stringBuilder.append(":");
        stringBuilder.append(test.getTestName());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.sample_no));
        stringBuilder.append(":");
        stringBuilder.append(test.getTestId());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.date));
        stringBuilder.append(":");
        stringBuilder.append(TimeTool.dateToDate(test.getDate()));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.time));
        stringBuilder.append(":");
        stringBuilder.append(TimeTool.dateToTime(test.getDate()));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.result_type));
        stringBuilder.append(":");
        stringBuilder.append(test.getFormulaName());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.test_result));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%." + test.getDecimal() + "f%s", test.getRes(), test.getFormulaUnit()));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.temperature_setting));
        stringBuilder.append(":");
        stringBuilder.append(test.getWantedTemperature());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.real_temperature));
        stringBuilder.append(":");
        stringBuilder.append(test.getRealTemperature());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.wavelength));
        stringBuilder.append(":");
        stringBuilder.append(test.getWavelength());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.testtubelength));
        stringBuilder.append(":");
        if (test.getTubelength() != null) {
            stringBuilder.append(test.getTubelength());
        }
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.automatic_titrator));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%." + test.getDecimal() + "f", test.getOptical()));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.specificrotation));
        stringBuilder.append(":");
        if (test.getSpecificrotation() != null) {
            stringBuilder.append(test.getSpecificrotation());
        }
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.concentration));
        stringBuilder.append(":");
        if (test.getConcentration() != null) {
            stringBuilder.append(test.getConcentration());
        }
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.sugger));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%." + test.getDecimal() + "f", test.getOptical() * 2.888));
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.test_method_name));
        stringBuilder.append(":");
        stringBuilder.append(test.getTestMethod());
        stringBuilder.append("\r\n");

        stringBuilder.append(context.getString(R.string.operator));
        stringBuilder.append(":");
        stringBuilder.append(test.getTestCreator());
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }

}
