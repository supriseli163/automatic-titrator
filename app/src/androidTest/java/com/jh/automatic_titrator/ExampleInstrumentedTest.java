package com.jh.automatic_titrator;


import com.alibaba.fastjson.JSON;
import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.common.db.TestHelper;
import com.jh.automatic_titrator.common.db.titrator.TitratorParamsBeanHelper;
import com.jh.automatic_titrator.entity.TestEntity;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private DBHelper dbHelper;
    private TestHelper helper1;
    private TitratorParamsBeanHelper titratorParamsBeanHelper;

    @Test
    public void add() {
//        assertEquals(4, 4 + 2);
    }

    @Before
    public void init() {
        dbHelper = new DBHelper(BaseApplication.getApplication());
        helper1 = new TestHelper(dbHelper);
        titratorParamsBeanHelper = new TitratorParamsBeanHelper();
    }


    @Test
    public void useAppContext() {
//        helper1.saveTest(com.jh.automatic_titrator.entity.common.Test.getTestSample());
    }

    /**
     * 测试新建方法
     */
    @Test
    public void testInsertTitratorMethod() {
        TitratorParamsBean titratorParamsBean = TestEntity.getTitratorParamsBean();
        titratorParamsBeanHelper.insert(titratorParamsBean);
    }

    /**
     * 测试查询方法
     * @throws Throwable
     */
    @Test
    public void testSelectByTitratorType() throws Throwable {
        List<TitratorParamsBean> titratorParamsBeans =  titratorParamsBeanHelper.listMethodByType(TestEntity.titratorTypeEnum, 0, 10);
        System.err.println(titratorParamsBeans.size());
        System.err.println(JSON.toJSONString(titratorParamsBeans));
    }

    /**
     * 测试更新方法
     */
    @Test
    public void testUpdateTitratorType() {

    }

    @Test
    public void testDeleteTitratorMethod() {
        int methodId = 15;
        titratorParamsBeanHelper.deleteByTitratorMethodId(methodId);
    }
}
