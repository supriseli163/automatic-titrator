package com.jh.automatic_titrator.trunk;

import android.os.Message;
import android.util.Log;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.trunk.CommandEnum;
import com.jh.automatic_titrator.common.trunk.DataFrame;
import com.jh.automatic_titrator.common.trunk.TitratorTrunkListener;
import com.jh.automatic_titrator.common.trunk.TitratorTrunkUtil;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.HexUtil;
import com.jh.automatic_titrator.ui.BaseActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TrunkUtilTest {

    @Test
    public void testHandShake() {
        TitratorTrunkUtil titratorTrunkUtil = TitratorTrunkUtil.getInstance();
        DataFrame dataFrame = new DataFrame();
        dataFrame = dataFrame.getHandShakeDataFrame();
        titratorTrunkUtil.sendCmd(HexUtil.hexStringToByteArrayWithSpiliter(dataFrame.getCmd()));



//        Future thread;
//        thread = new Callable() {
//            @Override
//            public Object call() throws Exception {
//                return null;
//            }
//        };
//
//        new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }
        TitratorTrunkUtil.getInstance().addListener(new TitratorTrunkListener() {
            @Override
            public CommandEnum getEventType() {
                return CommandEnum.HAND_SHAKE;
            }

            @Override
            public void notifyData(DataFrame dataFrame) {
                System.err.println("Receive handShake:::::");
            }
        }, 11);
    }

    @Test
    public void checkSum() {
        String command = "AA 55 00 19 80 15 00 00 01 26 00 00 00 00 FF 0E 00 CF 00 00 00 02 B1 CC 33";
        DataFrame dataFrame2 = new DataFrame(command);

        byte[] sum = ArrayUtils.addAll(dataFrame2.getSize(), dataFrame2.getCommand());
        sum = ArrayUtils.addAll(dataFrame2.getData(), sum);

        System.err.println(HexUtil.toHexString(sum));

        System.err.println((HexUtil.toHexString(HexUtil.SumCheck(sum, 2))));

        String checkSumPrefix = "00 19 80 15 00 00 01 26 00 00 00 00 FF 0E 00 CF 00 00 00";
        byte[] checkSumPrefixByte = HexUtil.hexStringToByteArrayWithSpiliter(checkSumPrefix);
        System.err.println((HexUtil.toHexString(HexUtil.SumCheck(checkSumPrefixByte,2))));

        String cmdStr = "";
        byte[] bytes = ArrayUtils.addAll(dataFrame2.getHeader(), dataFrame2.getSize());
        bytes = ArrayUtils.addAll(bytes, dataFrame2.getCommand());
        bytes = ArrayUtils.addAll(bytes, dataFrame2.getData());
        bytes = ArrayUtils.addAll(bytes, dataFrame2.getSum());
        bytes = ArrayUtils.addAll(bytes, dataFrame2.getEnd());
        System.err.println(HexUtil.bytesToString(bytes));

    }
}
