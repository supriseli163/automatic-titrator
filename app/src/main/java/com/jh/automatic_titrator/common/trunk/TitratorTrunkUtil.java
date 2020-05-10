package com.jh.automatic_titrator.common.trunk;

import android.util.Log;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.trunk.data.CleanStatusResponseData;
import com.jh.automatic_titrator.common.trunk.data.ResponseStatusEnum;
import com.jh.automatic_titrator.common.trunk.data.TitratorEndPointResponseData;
import com.jh.automatic_titrator.common.utils.HexUtil;
import com.jh.automatic_titrator.service.ExecutorService;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 滴定仪器通讯util
 */
public class TitratorTrunkUtil {

    private static TitratorTrunkUtil titratorTrunkUtil;
    private SerialPortUtil serialPortUtil;
    private Map<Integer, TitratorTrunkListener> titratorTrunkListenerMap;

    private ByteBuffer byteBuffer;
    private byte lastByte = 0;
    private boolean readStarted = false;
    private boolean mockStart = false;
    private int readLength = 0;

    private ReentrantLock reentrantLock = new ReentrantLock();
    private TitratorTrunkUtil() {

    }

    private TitratorTrunkUtil(boolean mockStart) {
        this.mockStart = mockStart;
        serialPortUtil = SerialPortUtil.getInstance();
        titratorTrunkListenerMap = new HashMap<>();
        byteBuffer = ByteBuffer.allocate(512);
        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                readTitratorData(buffer, size, 0);
            }

            @Override
            public void cleanBuffer() {
                byteBuffer = ByteBuffer.allocate(512);
                readLength = 0;
                lastByte = 0;
            }
        });
    }

    public static TitratorTrunkUtil getInstance() {
        if(titratorTrunkUtil == null) {
            titratorTrunkUtil = new TitratorTrunkUtil();
        }
        return titratorTrunkUtil;
    }

    public static TitratorTrunkUtil getMockInstance() {
        if(titratorTrunkUtil == null) {
            titratorTrunkUtil = new TitratorTrunkUtil(true);
        }
        return titratorTrunkUtil;
    }

    private void readTitratorData(byte[] buffer, int size, int startIndex) {
        int finishIndex = checkFinish(lastByte, buffer, size, startIndex);
        if(!readStarted) {
            int getStartIndex = readFromStart(buffer, size, startIndex, lastByte);
            readLength += readStartData(byteBuffer, buffer, size, lastByte, getStartIndex, finishIndex);
        } else {
            readLength += readFinishData(byteBuffer, buffer, size, finishIndex);
        }

        if (finishIndex >= 0) {
            CommandEnum type = getReadDataType(byteBuffer);
            Object data = getData(byteBuffer.array(), type);
            final DataFrame dataFrame = new DataFrame(byteBuffer.array());
            reentrantLock.lock();
            try {
                for (final TitratorTrunkListener trunkListener : titratorTrunkListenerMap.values()) {
                    if (trunkListener.getEventType() == type) {
                        ExecutorService.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                trunkListener.notifyData(dataFrame);
                            }
                        });
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
            byteBuffer = ByteBuffer.allocate(512);
            readLength = 0;
            lastByte = 0;
        }
        if (finishIndex != -1 && finishIndex + 1 < size) {
            readTitratorData(buffer, size, finishIndex + 1);
        }

    }

    private byte[] getOriginData(ByteBuffer byteBuffer) {
        byte[] buffer = new byte[readLength];
        byteBuffer.flip();
        byteBuffer.get(buffer);
        return buffer;
    }

    private int readFinishData(ByteBuffer byteBuffer, byte[] buffer, int size, int finishIndex) {
        int length = 0;
        if (finishIndex == -1) {
            length = size;
            byteBuffer.put(buffer, 0, size);
        } else {
            length = finishIndex + 1;
            byteBuffer.put(buffer, 0, length);
        }
        return length;
    }

    private int readStartData(ByteBuffer byteBuffer, byte[] buffer, int size, byte lastByte, int getStartIndex, int finishIndex) {
        int length = 0;

        try {
            if (finishIndex == -1) {
                length = size - getStartIndex;
            } else {
                length = finishIndex - getStartIndex + 1;
            }
            if (getStartIndex == -1) {
                byteBuffer.put(buffer, 0, length);
            } else {
                byteBuffer.put(buffer, getStartIndex, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    private int checkFinish(int lastByte, byte[] buffer, int size, int startIndex) {
        if (size <= startIndex) {
            return -1;
        }
        if (lastByte == (byte) 0xCC && buffer[startIndex] == (byte) 0x33) {
            return 0;
        }
        if (size == startIndex + 1) {
            return -1;
        }
        byte tempByte = buffer[startIndex];
        this.lastByte = tempByte;
        for (int i = startIndex + 1; i < size; i++) {
            if (tempByte == (byte) 0xCC && buffer[i] == (byte) 0x33) {
                return i;
            }
            tempByte = buffer[i];
            this.lastByte = tempByte;
        }
        return -1;
    }

    private int readFromStart(byte[] buffer, int size, int startIndex, byte lastByte) {
        if (size - startIndex < 1) {
            return -1;
        }
        if (size - startIndex == 1) {
            if (lastByte == (byte) 0xAA && buffer[startIndex] == (byte) 0x55) {
                return 0;
            }
        }
        int tempByte = buffer[startIndex];
        startIndex++;
        for (; startIndex < size; startIndex++) {
            if (tempByte == (byte) 0xAA && buffer[startIndex] == (byte) 0x55) {
                return startIndex - 1;
            }
        }
        return 0;
    }

    /**
     * AA 55 00 19 80 15 00 00 19 CB 00 00 00 00 27 12 00 B0 00 00 00 02 7B CC 33
     * @param byteBuffer
     * @return
     */
    public CommandEnum getReadDataType(ByteBuffer byteBuffer) {
        String string = HexUtil.toHexString(byteBuffer.array(),4,2);
        return CommandEnum.fromCodes(string);
    }

    private Object getData(byte[] dataBytes, CommandEnum commandEnum) {
        switch (commandEnum) {
            case HAND_SHAKE_RESPONSE:
                return readHandShakeData(dataBytes);
            case SOFTWARE_CLOSE_RESPONSE:
                return readHandShakeData(dataBytes);
            case FINSHED_TITRATOR:
                return readTItratorFinshedData(dataBytes);
            case END_POINT:
                return readTitratorEndPointData(dataBytes);
            case SUPPLEMENT_FINSHED:
                return readSupplementFinshedData(dataBytes);
            case CLEAN_STATUS:
                return readCleanStatusData(dataBytes);
                default:
                    return null;

        }
    }

    private Object readHandShakeData(byte[] data) {
        if(data[0] == 0x00) {
            return ResponseStatusEnum.SUCCESS;
        } else {
            return ResponseStatusEnum.FAILED;
        }
    }

    /**
     * 滴定完成数据指令：
     * 在电极支架的情况下代表地址，在进样器的情况下代表样品孔位
     * 指令表示某一个电机电极支架对应的滴定实验已完成
     * @param data
     * @return
     */
    private Object readTItratorFinshedData(byte[] data) {
        int moduleName = data[0];
        return moduleName;
    }

    /**
     * 传输终点信息指令80 19
     * @return
     */
    private Object readTitratorEndPointData(byte[] data) {
        int moduleAddrress= data[0];
        int endPointNum = data[1];
//        int endPoint = data[3][4];
        return new TitratorEndPointResponseData();
    }

    /**
     * 补液完成
     * 返回主控模块地址
     * @param data
     * @return
     */
    private Object readSupplementFinshedData(byte[] data) {
        int moduleAddress = data[0];
        return moduleAddress;
    }

    /**
     * 清洗状态
     * @param data
     * @return
     */
    private Object readCleanStatusData(byte[] data) {
        CleanStatusResponseData cleanStatusResponseData = new CleanStatusResponseData();
        int moduleAddress = data[0];
        int cleanCount = data[1];
        cleanStatusResponseData.setCleanCount(cleanCount);
        cleanStatusResponseData.setModuleAddress(moduleAddress);
        return cleanStatusResponseData;
    }









////    /**
////     * 开始滴定，此处传入参数，绑定事件监听器，向下位机发送消息
////     */
////    public void startTitrator(int titratorModelNum) {
////        DataFrame dataFrame = new DataFrame();
////        dataFrame
////    }
//
//
//    private void doStartTitrator() {
//        final Future<Void> future = ExecutorService.getInstance().submit(
//                new Callable<Void>() {
//                    @Override
//                    public Void call() throws Exception {
//                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<>();
//                        trunkUtil.addEventListener(new TitratorTrunkListener() {
//
//                            @Override
//                            public CommandEnum getEventType() {
//                                return CommandEnum.START_TITRATOR;
//                            }
//
//                            @Override
//                            public void notifyData(TrunkData trunkData) {
//                                trunkUtil.removeEventListener(R.id.setting_initail_factory_recover_btn);
//                                trunkDatas.offer(trunkData);
//                            }
//                        }, R.id.setting_initail_factory_recover_btn);
//
//                        for (int m = 0; m < 3; m++) {
//                            trunkUtil.sendCmd(TrunkConst.RECOVER);
//                            for (int k = 0; k < 3; k++) {
//                                try {
//                                    TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
//                                    Log.d("recover", "recovering");
//                                    if (trunkData != null) {
//                                        Log.d("recover", "recover success");
//                                        return null;
//                                    }
//                                } catch (Exception e) {
//                                    //IGNORE
//                                }
//                            }
//                        }
//                        return null;
//                    }
//                }
//        );
//        try {
//            future.get();
//        } catch (Exception ex) {
//            ex.printStackTrace();
////            Message.obtain(settingInitialHandler, BaseActivity.SHOW_MSG, ex.getMessage()).sendToTarget();
//        }
//    }
//
//    public void stopTitrator() {
//        final Future<Void> future = ExecutorService.getInstance().submit();
//    }
}
