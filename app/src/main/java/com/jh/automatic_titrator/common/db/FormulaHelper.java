package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.Conversion;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.exception.IndexAlreadyExistException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 2017/1/7.
 */

public class FormulaHelper {
    private DBHelper dbHelper;

    private SQLiteDatabase db;

    public FormulaHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public void addFormula(Formula formula) throws Exception {
        Formula formula1 = findFormula(formula.getFormulaName());
        if (formula1 != null) {
            throw new IndexAlreadyExistException("formula name already exist");
        }
        String sql = "insert into formula (formulaName, simpleName, unit, " +
                "decimal, showPercent, date, desc, creator) " +
                "values(" +
                "'" + formula.getFormulaName() + "'," +
                "'" + formula.getSimpleName() + "'," +
                "'" + formula.getUnit() + "'," +
                "" + formula.getDecimal() + "," +
                "" + (formula.isShowPercent() ? 1 : 0) + "," +
                "'" + formula.getCreateDate() + "'," +
                "'" + formula.getDesc() + "'," +
                "'" + formula.getCreator() + "'" +
                ")";
        db.execSQL(sql);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select MAX(id) from formula", null);

            cursor.moveToFirst();
            int id = 0;
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }

            for (Conversion conversion : formula.getConversions()) {
                String conversionSql = "insert into conversion (formulaId," +
                        "start,end,p0,p1,p2,p3,p4,p5) values(" +
                        "" + id + "," +
                        "" + conversion.getStart() + "," +
                        "" + conversion.getEnd() + "," +
                        "" + conversion.getP0() + "," +
                        "" + conversion.getP1() + "," +
                        "" + conversion.getP2() + "," +
                        "" + conversion.getP3() + "," +
                        "" + conversion.getP4() + "," +
                        "" + conversion.getP5() + "" +
                        ")";
                db.execSQL(conversionSql);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void updateFormula(String formulaName, Formula formula) {
        String sql = "update formula set " +
                "formulaName = '" + formula.getFormulaName() + "'," +
                "simpleName = '" + formula.getSimpleName() + "'," +
                "unit = '" + formula.getUnit() + "'," +
                "decimal = '" + formula.getDecimal() + "'," +
                "showPercent = " + (formula.isShowPercent() ? 1 : 0) + "," +
                "desc = '" + formula.getDesc() + "'" +
                " where " +
                "formulaName = '" + formulaName + "'";
        db.execSQL(sql);
        Formula formula1 = findFormula(formulaName);
        if (formula1 != null) {
            deleteConversions(formula1.getId());
            insertConversionList(formula1.getId(), formula.getConversions());
        } else {
            deleteConversions(formula.getId());
            insertConversionList(formula.getId(), formula.getConversions());
        }
    }

    private void deleteConversions(int formulaId) {
        db.execSQL("delete from conversion where formulaId = " + formulaId);
    }

    private void insertConversionList(int formulaId, List<Conversion> conversions) {
        for (Conversion conversion : conversions) {
            String conversionSql = "insert into conversion (formulaId," +
                    "start,end,p0,p1,p2,p3,p4,p5) values(" +
                    "" + formulaId + "," +
                    "" + conversion.getStart() + "," +
                    "" + conversion.getEnd() + "," +
                    "" + conversion.getP0() + "," +
                    "" + conversion.getP1() + "," +
                    "" + conversion.getP2() + "," +
                    "" + conversion.getP3() + "," +
                    "" + conversion.getP4() + "," +
                    "" + conversion.getP5() + "" +
                    ")";
            db.execSQL(conversionSql);
        }
    }

    public void deleteFormula(Collection<Integer> ids) {
        for (int id : ids) {
            db.execSQL("delete from formula where id = " + id);
            db.execSQL("delete from conversion where formulaId = " + id);
        }
    }

    public void deleteFormula(String startDate, String endDate, String creator) {
        Set<Integer> ids = getFormulaIds(startDate, endDate, creator);
        ids.remove(1);
        deleteFormula(ids);
    }

    private Set<Integer> getFormulaIds(String startDate, String endDate, String creator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select id from formula ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`createDate`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`createDate`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("creator = '").append(creator).append("' ");
            }
        }

        Set<Integer> ids = new HashSet<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ids.add(cursor.getInt(0));
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from formula", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public int count(String startDate, String endDate, String creator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from formula ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("creator = '").append(creator).append("' ");
            }
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public List<Formula> listFormula() {
        List<Formula> formulas = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from formula", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Formula formula = new Formula();
                formula.setId(cursor.getInt(0));
                formula.setFormulaName(cursor.getString(1));
                formula.setSimpleName(cursor.getString(2));
                formula.setUnit(cursor.getString(3));
                formula.setDecimal(cursor.getInt(4));
                formula.setShowPercent(cursor.getInt(5) != 0);
                formula.setCreateDate(cursor.getString(6));
                formula.setDesc(cursor.getString(7));
                formula.setCreator(cursor.getString(8));
                formulas.add(formula);
                cursor.moveToNext();
            }
            return formulas;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Formula> list(int page, int pageSize) {
        List<Formula> formulas = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from formula order by id desc LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Formula formula = new Formula();
                formula.setId(cursor.getInt(0));
                formula.setFormulaName(cursor.getString(1));
                formula.setSimpleName(cursor.getString(2));
                formula.setUnit(cursor.getString(3));
                formula.setDecimal(cursor.getInt(4));
                formula.setShowPercent(cursor.getInt(5) != 0);
                formula.setCreateDate(cursor.getString(6));
                formula.setDesc(cursor.getString(7));
                formula.setCreator(cursor.getString(8));
                formulas.add(formula);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return formulas;
    }

    public List<Formula> list(String startDate, String endDate, String creator, int page, int pageSize) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from formula ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("creator = '").append(creator).append("' ");
            }
        }
        sqlSb.append("order by id desc LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        List<Formula> formulas = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Formula formula = new Formula();
                formula.setId(cursor.getInt(0));
                formula.setFormulaName(cursor.getString(1));
                formula.setSimpleName(cursor.getString(2));
                formula.setUnit(cursor.getString(3));
                formula.setDecimal(cursor.getInt(4));
                formula.setShowPercent(cursor.getInt(5) != 0);
                formula.setCreateDate(cursor.getString(6));
                formula.setDesc(cursor.getString(7));
                formula.setCreator(cursor.getString(8));
                formulas.add(formula);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return formulas;
    }

    public Formula findFormula(int id) {
        Cursor cursor = null;
        Formula formula = new Formula();
        try {
            cursor = db.rawQuery("select * from formula where id = " + id, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                formula.setId(cursor.getInt(0));
                formula.setFormulaName(cursor.getString(1));
                formula.setSimpleName(cursor.getString(2));
                formula.setUnit(cursor.getString(3));
                formula.setDecimal(cursor.getInt(4));
                formula.setShowPercent(cursor.getInt(5) != 0);
                formula.setCreateDate(cursor.getString(6));
                formula.setDesc(cursor.getString(7));
                formula.setCreator(cursor.getString(8));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        List<Conversion> conversions = new ArrayList<>();
        try {
            cursor = db.rawQuery("select * from conversion where formulaId = " + id, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Conversion conversion = new Conversion();
                conversion.setId(cursor.getInt(0));
                conversion.setFormulaId(cursor.getInt(1));
                conversion.setStart(cursor.getDouble(2));
                conversion.setEnd(cursor.getDouble(3));
                conversion.setP0(cursor.getDouble(4));
                conversion.setP1(cursor.getDouble(5));
                conversion.setP2(cursor.getDouble(6));
                conversion.setP3(cursor.getDouble(7));
                conversion.setP4(cursor.getDouble(8));
                conversion.setP5(cursor.getDouble(9));
                conversions.add(conversion);
                cursor.moveToNext();
            }
            formula.setConversions(conversions);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return formula;
    }

    public Formula findFormula(String name) {
        Cursor cursor = null;
        Formula formula = new Formula();
        try {
            cursor = db.rawQuery("select * from formula where formulaName = '" + name + "'", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                formula.setId(cursor.getInt(0));
                formula.setFormulaName(cursor.getString(1));
                formula.setSimpleName(cursor.getString(2));
                formula.setUnit(cursor.getString(3));
                formula.setDecimal(cursor.getInt(4));
                formula.setShowPercent(cursor.getInt(5) != 0);
                formula.setCreateDate(cursor.getString(6));
                formula.setDesc(cursor.getString(7));
                formula.setCreator(cursor.getString(8));
                List<Conversion> conversions = new ArrayList<>();
                cursor = db.rawQuery("select * from conversion where formulaId = " + formula.getId(), null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Conversion conversion = new Conversion();
                    conversion.setId(cursor.getInt(0));
                    conversion.setFormulaId(cursor.getInt(1));
                    conversion.setStart(cursor.getDouble(2));
                    conversion.setEnd(cursor.getDouble(3));
                    conversion.setP0(cursor.getDouble(4));
                    conversion.setP1(cursor.getDouble(5));
                    conversion.setP2(cursor.getDouble(6));
                    conversion.setP3(cursor.getDouble(7));
                    conversion.setP4(cursor.getDouble(8));
                    conversion.setP5(cursor.getDouble(9));
                    conversions.add(conversion);
                    cursor.moveToNext();
                }
                formula.setConversions(conversions);
                return formula;
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<String> findFormulaNames() {
        List<String> formulas = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from formula order by id desc", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String formulaName = cursor.getString(1);
                String formulaSimpleName = cursor.getString(2);
                if (formulaSimpleName != null && formulaSimpleName.length() > 0) {
                    formulas.add(formulaName + "(" + formulaSimpleName + ")");
                } else {
                    formulas.add(formulaName);
                }
                cursor.moveToNext();
            }
            return formulas;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
