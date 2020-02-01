package com.jh.automatic_titrator.common.file;

import android.content.Context;

import com.hendrix.pdfmyxml.PdfDocument;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.entity.common.Test;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.ui.pdf.DetailVertical;
import com.jh.automatic_titrator.ui.pdf.DetailVertical2;
import com.jh.automatic_titrator.ui.pdf.SimpleHorizental;
import com.jh.automatic_titrator.ui.pdf.SimpleHorizental2;
import com.jh.automatic_titrator.ui.pdf.SimpleVertical;
import com.jh.automatic_titrator.ui.pdf.SimpleVertical2;

import java.io.File;
import java.util.List;

/**
 * Created by apple on 2016/10/15.
 */
public class PDFTemplete {

    public static void writePDFHorizentalSimple(final Context context, final String fileDir, String fileName, String title1, String title2, String title3, User user, int progessMsg, List<Test> tests, final ExportResult exportResult) {
        File file = new File(fileDir);

        final PdfDocument doc = new PdfDocument(context);
        if (tests.size() > 10) {
            SimpleHorizental page = new SimpleHorizental(context, R.layout.export_simple_horizental_1, title1, title2, title3, user, tests.subList(0, 10));
            page.setReuseBitmap(true);
            doc.addPage(page);
            for (int currentIndex = 10; currentIndex < tests.size(); currentIndex++) {
                if (tests.size() - currentIndex > 14) {
                    SimpleHorizental2 page2 = new SimpleHorizental2(context, R.layout.export_simple_horizental_2, tests.subList(currentIndex, currentIndex + 14));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    currentIndex += 14;
                } else {
                    SimpleHorizental2 page2 = new SimpleHorizental2(context, R.layout.export_simple_horizental_2, tests.subList(currentIndex, tests.size()));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    break;
                }
            }
        } else {
            SimpleHorizental page = new SimpleHorizental(context, R.layout.export_simple_horizental_1, title1, title2, title3, user, tests);
            page.setReuseBitmap(true);
            doc.addPage(page);
        }
        doc.setRenderWidth(2115);
        doc.setRenderHeight(1500);
        doc.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        doc.setProgressTitle(R.string.gen_please_wait);
        doc.setProgressMessage(progessMsg);
        doc.setSaveDirectory(file);
        doc.setFileName(fileName);
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                exportResult.success();
                doc.dispose();
            }

            @Override
            public void onError(Exception e) {
                exportResult.failed(e.getMessage());
                doc.dispose();
            }
        });

        doc.createPdf(context);
    }

    public static void writePDFVerticalDetail(final Context context, final String fileDir, String fileName, String title1, String title2, String title3, User user, int progessMsg, List<Test> tests, final ExportResult exportResult) {
        File file = new File(fileDir);

        final PdfDocument doc = new PdfDocument(context);
        if (tests.size() > 2) {
            DetailVertical page = new DetailVertical(context, R.layout.export_detail_vertical, title1, title2, title3, user, tests.subList(0, 2));
            page.setReuseBitmap(true);
            doc.addPage(page);
            for (int currentIndex = 2; currentIndex < tests.size(); currentIndex++) {
                if (tests.size() - currentIndex > 3) {
                    DetailVertical2 page2 = new DetailVertical2(context, R.layout.export_detail_vertical2, tests.subList(currentIndex, currentIndex + 3));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    currentIndex += 3;
                } else {
                    DetailVertical2 page2 = new DetailVertical2(context, R.layout.export_detail_vertical2, tests.subList(currentIndex, tests.size()));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    break;
                }
            }
        } else {
            DetailVertical page = new DetailVertical(context, R.layout.export_detail_vertical, title1, title2, title3, user, tests);
            page.setReuseBitmap(true);
            doc.addPage(page);
        }
        doc.setRenderHeight(2115);
        doc.setRenderWidth(1500);
        doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
        doc.setProgressTitle(R.string.gen_please_wait);
        doc.setProgressMessage(progessMsg);
        doc.setSaveDirectory(file);
        doc.setFileName(fileName);
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                exportResult.success();
                doc.dispose();
            }

            @Override
            public void onError(Exception e) {
                exportResult.failed(e.getMessage());
                doc.dispose();
            }
        });

        doc.createPdf(context);
    }

    public static void writePDFVerticalSimple(final Context context, final String fileDir, String fileName, String title1, String title2, String title3, User user, int progessMsg, List<Test> tests, final ExportResult exportResult) {
        File file = new File(fileDir);

        final PdfDocument doc = new PdfDocument(context);
        if (tests.size() > 24) {
            SimpleVertical page = new SimpleVertical(context, R.layout.export_simple_vertical, title1, title2, title3, user, tests.subList(0, 24));
            page.setReuseBitmap(true);
            doc.addPage(page);
            for (int currentIndex = 24; currentIndex < tests.size(); currentIndex++) {
                if (tests.size() - currentIndex > 32) {
                    SimpleVertical2 page2 = new SimpleVertical2(context, R.layout.export_simple_vertical2, tests.subList(currentIndex, currentIndex + 32));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    currentIndex += 32;
                } else {
                    SimpleVertical2 page2 = new SimpleVertical2(context, R.layout.export_simple_vertical2, tests.subList(currentIndex, tests.size()));
                    page2.setReuseBitmap(true);
                    doc.addPage(page2);
                    break;
                }
            }
        } else {
            SimpleVertical page = new SimpleVertical(context, R.layout.export_simple_vertical, title1, title2, title3, user, tests);
            page.setReuseBitmap(true);
            doc.addPage(page);
        }
        doc.setRenderHeight(2115);
        doc.setRenderWidth(1500);
        doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
        doc.setProgressTitle(R.string.gen_please_wait);
        doc.setProgressMessage(progessMsg);
        doc.setSaveDirectory(file);
        doc.setFileName(fileName);
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                exportResult.success();
                doc.dispose();
            }

            @Override
            public void onError(Exception e) {
                exportResult.failed(e.getMessage());
                doc.dispose();
            }
        });

        doc.createPdf(context);
    }
}
