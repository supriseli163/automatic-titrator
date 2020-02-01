package com.jh.automatic_titrator.common.file;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Audit;
import com.jh.automatic_titrator.entity.common.MD5;
import com.jh.automatic_titrator.entity.common.Test;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.ui.HomePageActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by apple on 2016/10/17.
 */
public class FileHelper {
    public static String readFile(String filePath) {
        return readFile(new File(filePath));
    }

    public static String readFile(File file) {
        if (file.exists()) {
            try {
                return readFile(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                Log.e("FileHelper.readFile", e.getMessage());
            }
        }
        return null;
    }

    public static String readFile(FileInputStream fileInputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = 0;
        byte[] buffer = new byte[8192];
        try {
            while ((length = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return new String(outputStream.toByteArray());
        } catch (IOException e) {
            Log.e("FileHelper.readFile", e.getMessage());
            try {
                outputStream.close();
            } catch (IOException e1) {
                //ignore
            }
            try {
                fileInputStream.close();
            } catch (IOException e1) {
                //ignore
            }
        }
        return null;
    }

    public static void writeFile(File file, String content) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            writeFile(fileOutputStream, content);
        } catch (IOException e) {
            Log.e("FileHelper.readFile", e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(FileOutputStream fileOutputStream, String content) throws IOException {
        fileOutputStream.write(content.getBytes());
    }

    public static void writeAsExcel(Context context, String dir, String fileName, String creator, String title1, String title2, String title3, List<Test> tests, boolean isDetail) throws Exception {
        if (!isDetail) {
            writeAsExcelSimple(context, dir, fileName, title1, title2, title3, creator, tests);
        } else {
            writeAsExcelDetail(context, dir, fileName, title1, title2, title3, creator, tests);
        }
    }

    public static void writeAsExcelSimple(Context context, String dir, String fileName, String title1, String title2, String title3, String creator, List<Test> tests) throws Exception {
        ProgressDialog progressDialog = showProgressDialog(context);
        if (tests == null || tests.size() == 0) {
            return;
        }

        if (dir.endsWith("/")) {
            fileName = dir + fileName;
        } else {
            fileName = dir + "/" + fileName;
        }

        if (!fileName.endsWith(".xls")) {
            fileName = fileName + ".xls";
        }

        WritableWorkbook wwb = null;
        try {
            wwb = Workbook.createWorkbook(new File(fileName));
            WritableSheet ws = wwb.createSheet("sheet1", 0);
            int i = 0;
            if (title1 != null) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 18, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title1, writableCellFormat));
                i++;
            }
            if (title2 != null) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title2, writableCellFormat));
                i++;
            }
            if (title3 != null) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title3, writableCellFormat));
                i++;
            }

            WritableFont dataWritableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            WritableCellFormat dataWritableCellFormat = new WritableCellFormat(dataWritableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.data), dataWritableCellFormat));
            String date = TimeTool.currentDate();
            ws.addCell(new Label(1, i, TimeTool.dateToDate(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.time), dataWritableCellFormat));
            ws.addCell(new Label(1, i, TimeTool.dateToTime(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.operator), dataWritableCellFormat));
            ws.addCell(new Label(1, i, Cache.getUser().getUserName(), dataWritableCellFormat));
            i++;

            //电子签名
            HomePageActivity homePageActivity = (HomePageActivity) context;
            ws.addCell(new Label(0, i, context.getString(R.string.autograph), dataWritableCellFormat));
            ws.addCell(new Label(1, i, ((HomePageActivity) context).readconf("autograph"), dataWritableCellFormat));
            i++;

            WritableFont writableFont = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, true);
            WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.sort_number), writableCellFormat));
            ws.addCell(new Label(1, i, context.getString(R.string.sample_name), writableCellFormat));
            ws.addCell(new Label(2, i, context.getString(R.string.sample_no), writableCellFormat));
            ws.addCell(new Label(3, i, context.getString(R.string.date), writableCellFormat));
            ws.addCell(new Label(4, i, context.getString(R.string.time), writableCellFormat));
            ws.addCell(new Label(5, i, context.getString(R.string.temperature), writableCellFormat));
            ws.addCell(new Label(6, i, context.getString(R.string.formula_name), writableCellFormat));
            ws.addCell(new Label(7, i, context.getString(R.string.test_result), writableCellFormat));
            ws.addCell(new Label(8, i, context.getString(R.string.operator), writableCellFormat));

            for (int j = 0; j < tests.size(); i++, j++)  {
                Test test = tests.get(j);
                ws.addCell(new Label(0, i, test.getId() + ""));
                ws.addCell(new Label(1, i, test.getTestName()));
                ws.addCell(new Label(2, i, test.getTestId()));
                ws.addCell(new Label(3, i, TimeTool.dateToDate(test.getDate())));
                ws.addCell(new Label(4, i, TimeTool.dateToTime(test.getDate())));
                ws.addCell(new Label(5, i, test.getRealTemperature()));
                ws.addCell(new Label(6, i, test.getFormulaName()));
                ws.addCell(new Label(7, i, test.getRes() + test.getFormulaUnit()));
                ws.addCell(new Label(8, i, test.getTestCreator()));
            }
            wwb.write();
        } finally {
            if (wwb != null) {
                wwb.close();
            }
        }
        closeProgressDialog(context, progressDialog, context.getString(R.string.export_success));
        DBService.getMD5Helper(context).addMD5(new File(fileName));
    }

    public static void writeAsExcelDetail(Context context, String dir, String fileName, String title1, String title2, String title3, String creator, List<Test> tests) throws Exception {
        ProgressDialog progressDialog = showProgressDialog(context);
        if (tests == null || tests.size() == 0) {
            return;
        }

        if (dir.endsWith("/")) {
            fileName = dir + fileName;
        } else {
            fileName = dir + "/" + fileName;
        }

        if (!fileName.endsWith(".xls")) {
            fileName = fileName + ".xls";
        }

        WritableWorkbook wwb = null;
        try {
            wwb = Workbook.createWorkbook(new File(fileName));
            WritableSheet ws = wwb.createSheet("sheet1", 0);
            int i = 0;
            if (StringUtils.isNotEmpty(title1)) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 18, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title1, writableCellFormat));
                i++;
            }
            if (StringUtils.isNotEmpty(title2)) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title2, writableCellFormat));
                i++;
            }
            if (StringUtils.isNotEmpty(title3)) {
                WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, true);
                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                ws.addCell(new Label(0, i, title3, writableCellFormat));
                i++;
            }

            //电子签名

            WritableFont dataWritableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            WritableCellFormat dataWritableCellFormat = new WritableCellFormat(dataWritableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.data), dataWritableCellFormat));
            String date = TimeTool.currentDate();
            ws.addCell(new Label(1, i, TimeTool.dateToDate(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.time), dataWritableCellFormat));
            ws.addCell(new Label(1, i, TimeTool.dateToTime(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.operator), dataWritableCellFormat));
            ws.addCell(new Label(1, i, Cache.getUser().getUserName(), dataWritableCellFormat));
            i++;

            //电子签名
            HomePageActivity homePageActivity = (HomePageActivity) context;
            ws.addCell(new Label(0, i, context.getString(R.string.autograph), dataWritableCellFormat));
            ws.addCell(new Label(1, i, ((HomePageActivity) context).readconf("autograph"), dataWritableCellFormat));
            i++;

            WritableFont writableFont = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, true);
            WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.sort_number), writableCellFormat));
            ws.addCell(new Label(1, i, context.getString(R.string.sample_name), writableCellFormat));
            ws.addCell(new Label(2, i, context.getString(R.string.sample_no), writableCellFormat));
            ws.addCell(new Label(3, i, context.getString(R.string.date), writableCellFormat));
            ws.addCell(new Label(4, i, context.getString(R.string.time), writableCellFormat));
            ws.addCell(new Label(5, i, context.getString(R.string.setting_temperature), writableCellFormat));
            ws.addCell(new Label(6, i, context.getString(R.string.temperature), writableCellFormat));
            ws.addCell(new Label(7, i, context.getString(R.string.formula_name), writableCellFormat));
            ws.addCell(new Label(8, i, context.getString(R.string.test_result), writableCellFormat));
            ws.addCell(new Label(9, i, context.getString(R.string.wavelength), writableCellFormat));
            ws.addCell(new Label(10, i, context.getString(R.string.testtubelength), writableCellFormat));
            ws.addCell(new Label(11, i, context.getString(R.string.specificrotation), writableCellFormat));
            ws.addCell(new Label(12, i, context.getString(R.string.concentration), writableCellFormat));
            ws.addCell(new Label(13, i, context.getString(R.string.operator), writableCellFormat));
            i++;
            for (int j = 0; j < tests.size(); i++, j++) {
                Test test = tests.get(j);
                ws.addCell(new Label(0, i, test.getId() + ""));
                ws.addCell(new Label(1, i, test.getTestName()));
                ws.addCell(new Label(2, i, test.getTestId()));
                ws.addCell(new Label(3, i, TimeTool.dateToDate(test.getDate())));
                ws.addCell(new Label(4, i, TimeTool.dateToTime(test.getDate())));
                ws.addCell(new Label(5, i, test.getWantedTemperature()));
                ws.addCell(new Label(6, i, test.getRealTemperature()));
                ws.addCell(new Label(7, i, test.getFormulaName()));
                ws.addCell(new Label(8, i, test.getRes() + test.getFormulaUnit()));
                ws.addCell(new Label(9, i, test.getWavelength()));
                if (StringUtils.isNotEmpty(test.getTubelength())) {
                    ws.addCell(new Label(10, i, test.getTubelength()));
                }
                if (StringUtils.isNotEmpty(test.getSpecificrotation())) {
                    ws.addCell(new Label(11, i, test.getSpecificrotation()));
                }
                if (StringUtils.isNotEmpty(test.getConcentration())) {
                    ws.addCell(new Label(12, i, test.getConcentration()));
                }
                ws.addCell(new Label(13, i, test.getTestCreator()));
            }
            wwb.write();
        } finally {
            if (wwb != null) {
                wwb.close();
            }
        }
        closeProgressDialog(context, progressDialog, context.getString(R.string.export_success));
        DBService.getMD5Helper(context).addMD5(new File(fileName));
    }

    public static void writeAsExcelAudit(Context context, String dir, String fileName, List<Audit> audits) throws Exception {
//        ProgressDialog progressDialog = showProgressDialog(context);
        if (audits == null || audits.size() == 0) {
            return;
        }

        if (dir.endsWith("/")) {
            fileName = dir + fileName;
        } else {
            fileName = dir + "/" + fileName;
        }

        if (!fileName.endsWith(".xls")) {
            fileName = fileName + ".xls";
        }

        WritableWorkbook wwb = null;
        try {
            wwb = Workbook.createWorkbook(new File(fileName));
            WritableSheet ws = wwb.createSheet("sheet1", 0);

            int i = 0;

            WritableFont dataWritableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            WritableCellFormat dataWritableCellFormat = new WritableCellFormat(dataWritableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.data), dataWritableCellFormat));
            String date = TimeTool.currentDate();
            ws.addCell(new Label(1, i, TimeTool.dateToDate(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.time), dataWritableCellFormat));
            ws.addCell(new Label(1, i, TimeTool.dateToTime(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.operator), dataWritableCellFormat));
            ws.addCell(new Label(1, i, Cache.getUser().getUserName(), dataWritableCellFormat));
            i++;

            //电子签名
            HomePageActivity homePageActivity = (HomePageActivity) context;
            ws.addCell(new Label(0, i, context.getString(R.string.autograph), dataWritableCellFormat));
            ws.addCell(new Label(1, i, ((HomePageActivity) context).readconf("autograph"), dataWritableCellFormat));
            i++;

            WritableFont writableFont = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, true);
            WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.sort_number), writableCellFormat));
            ws.addCell(new Label(1, i, context.getString(R.string.operator), writableCellFormat));
            ws.addCell(new Label(2, i, context.getString(R.string.date), writableCellFormat));
            ws.addCell(new Label(3, i, context.getString(R.string.time), writableCellFormat));
            ws.addCell(new Label(4, i, context.getString(R.string.fragment), writableCellFormat));
            ws.addCell(new Label(5, i, context.getString(R.string.subfragment), writableCellFormat));
            ws.addCell(new Label(6, i, context.getString(R.string.event), writableCellFormat));
            i++;

            for (int j = 0; j < audits.size(); i++, j++) {
                Audit audit = audits.get(j);
                ws.addCell(new Label(0, i, String.valueOf(audit.getId())));
                ws.addCell(new Label(1, i, audit.getOperator()));
                ws.addCell(new Label(2, i, TimeTool.dateToDate(audit.getDate())));
                ws.addCell(new Label(3, i, TimeTool.dateToTime(audit.getDate())));
                ws.addCell(new Label(4, i, audit.getFragment()));
                if (audit.getSubFragment() != null && !audit.getSubFragment().equals("null")) {
                    ws.addCell(new Label(5, i, audit.getSubFragment()));
                }
                ws.addCell(new Label(6, i, audit.getEvent()));
            }
            wwb.write();
        } finally {
            if (wwb != null) {
                wwb.close();
            }
        }
//        closeProgressDialog(context, progressDialog, context.getString(R.string.export_success));
        DBService.getMD5Helper(context).addMD5(new File(fileName));
    }

    public static void writeAsExcelMd5(Context context, String dir, String fileName, List<MD5> md5List) throws Exception {
//        ProgressDialog progressDialog = showProgressDialog(context);
        if (md5List == null || md5List.size() == 0) {
            return;
        }

        if (dir.endsWith("/")) {
            fileName = dir + fileName;
        } else {
            fileName = dir + "/" + fileName;
        }

        if (!fileName.endsWith(".xls")) {
            fileName = fileName + ".xls";
        }

        WritableWorkbook wwb = null;
        try {
            wwb = Workbook.createWorkbook(new File(fileName));
            WritableSheet ws = wwb.createSheet("sheet1", 0);

            int i = 0;

            WritableFont dataWritableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            WritableCellFormat dataWritableCellFormat = new WritableCellFormat(dataWritableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.data), dataWritableCellFormat));
            String date = TimeTool.currentDate();
            ws.addCell(new Label(1, i, TimeTool.dateToDate(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.time), dataWritableCellFormat));
            ws.addCell(new Label(1, i, TimeTool.dateToTime(date), dataWritableCellFormat));
            i++;
            ws.addCell(new Label(0, i, context.getString(R.string.operator), dataWritableCellFormat));
            ws.addCell(new Label(1, i, Cache.getUser().getUserName(), dataWritableCellFormat));
            i++;

            //电子签名
            HomePageActivity homePageActivity = (HomePageActivity) context;
            ws.addCell(new Label(0, i, context.getString(R.string.autograph), dataWritableCellFormat));
            ws.addCell(new Label(1, i, ((HomePageActivity) context).readconf("autograph"), dataWritableCellFormat));
            i++;

            WritableFont writableFont = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, true);
            WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
            ws.addCell(new Label(0, i, context.getString(R.string.sort_number), writableCellFormat));
            ws.addCell(new Label(1, i, context.getString(R.string.date), writableCellFormat));
            ws.addCell(new Label(2, i, context.getString(R.string.time), writableCellFormat));
            ws.addCell(new Label(3, i, context.getString(R.string.file_name), writableCellFormat));
            ws.addCell(new Label(4, i, context.getString(R.string.file_length), writableCellFormat));
            ws.addCell(new Label(5, i, "MD5", writableCellFormat));

            for (int j = 0; j < md5List.size(); i++, j++) {
                MD5 md5 = md5List.get(j);
                ws.addCell(new Label(0, i, String.valueOf(md5.getId())));
                ws.addCell(new Label(1, i, TimeTool.dateToDate(md5.getCreateDate())));
                ws.addCell(new Label(2, i, TimeTool.dateToTime(md5.getCreateDate())));
                ws.addCell(new Label(3, i, md5.getFileName()));
                ws.addCell(new Label(4, i, md5.getFileSize() + ""));
                ws.addCell(new Label(5, i, md5.getMd5()));
            }
            wwb.write();
        } finally {
            if (wwb != null) {
                wwb.close();
            }
        }
//        closeProgressDialog(context, progressDialog, context.getString(R.string.export_success));
        DBService.getMD5Helper(context).addMD5(new File(fileName));
    }

    private static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.exporting_excel), "");
        return progressDialog;
    }

    private static void closeProgressDialog(Context context, ProgressDialog progressDialog, String content) {
        progressDialog.dismiss();
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
