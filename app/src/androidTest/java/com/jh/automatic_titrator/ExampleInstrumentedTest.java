package com.jh.automatic_titrator;


import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.common.db.TestHelper;
import com.jh.automatic_titrator.common.db.titrator.TitratorMethodSettingHelper;
import com.jh.automatic_titrator.entity.TestEntity;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void add() {
        assertEquals(4, 4 + 2);
    }

    @Test
    public void useAppContext() {
        DBHelper helper = new DBHelper(BaseApplication.getApplication());
        TestHelper helper1 = new TestHelper(helper);
        helper1.saveTest(com.jh.automatic_titrator.entity.common.Test.getTestSample());
    }

    @Test
    public void testInsertTitratorMethod() {
        TitratorMethod titratorMethod = TestEntity.getTitratorMethod();
        DBHelper dbHelper = new DBHelper(BaseApplication.getApplication());
        TitratorMethodSettingHelper helper = new TitratorMethodSettingHelper(dbHelper);
        helper.insert(titratorMethod);
//        TitratorMethodHelper titratorMethodHelper = new TitratorMethodHelper(dbHelper);
//        dbHelper.onCreate(dbHelper.getWritableDatabase());
//        titratorMethodHelper.insertTitratorMethod(titratorMethod);
//        List<TitratorMethod>
    }
}
