package com.jh.automatic_titrator;

import android.content.Context;
import android.util.Log;

import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.common.db.TestHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
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
        TestHelper helper1=new TestHelper(helper);
        helper1.saveTest(com.jh.automatic_titrator.entity.common.Test.getTestSample());
    }
}
