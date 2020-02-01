package com.jh.automatic_titrator.common.printer;

import android.app.Activity;
import android.net.Uri;
import android.print.PrintAttributes;

import androidx.print.PrintHelper;

import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;


/**
 * Created by apple on 2017/3/26.
 */

public class PrinterTask {

    public static int printPDF(Activity activity, String file) {
        PDFAsset pdfAsset = new PDFAsset(file);
        PrintAttributes printAttributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4).build();
        PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfAsset);
        PrintJobData printJobData = new PrintJobData(activity, printItemDefault);
        printJobData.setJobName(UUID.randomUUID().toString());
        printJobData.setPrintDialogOptions(printAttributes);
        printJobData.getPreviewPaperSize();
        PrintUtil.setPrintJobData(printJobData);
        PrintUtil.createPrintJob(activity);
//        PrintUtil.print(activity);
        return 0;
    }

    public static int printPdf(Activity activity, File file) {
        PrintHelper printHelper = new PrintHelper(activity);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        try {
            printHelper.printBitmap("test", Uri.fromFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
