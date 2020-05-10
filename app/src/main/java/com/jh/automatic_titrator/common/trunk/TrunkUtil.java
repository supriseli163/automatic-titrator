package com.jh.automatic_titrator.common.trunk;

import com.jh.automatic_titrator.entity.common.StandardData;
import com.jh.automatic_titrator.entity.common.StandardDataRes;
import com.jh.automatic_titrator.entity.common.TestData;
import com.jh.automatic_titrator.service.ExecutorService;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by apple on 2016/12/8.
 */

public class TrunkUtil {

    private static TrunkUtil trunkUtil;
    private SerialPortUtil serialPortUtil;
    private Map<Integer, TrunkListener> trunkListeners;
    private Map<Integer, TitratorTrunkListener> titratorTrunkListenerMap;

    private ByteBuffer byteBuffer;

    private byte lastByte = 0;

    private boolean readStarted = false;

    private boolean mockStart = false;

    private int readLength = 0;

    private ReentrantLock reentrantLock = new ReentrantLock();

    private TrunkUtil() {
        this(false);
    }

    private TrunkUtil(boolean mockStart) {
        this.mockStart = mockStart;
        mock();
        serialPortUtil = SerialPortUtil.getInstance();
        trunkListeners = new HashMap<>();
        byteBuffer = ByteBuffer.allocate(512);
        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                readData(buffer, size, 0);
            }

            @Override
            public void cleanBuffer() {
                byteBuffer = ByteBuffer.allocate(512);
                readLength = 0;
                lastByte = 0;
            }
        });
    }


    public static TrunkUtil getInstance() {
        if (trunkUtil == null) {
            trunkUtil = new TrunkUtil();
        }
        return trunkUtil;
    }

    public static TrunkUtil getMockInstance() {
        if (trunkUtil == null) {
            trunkUtil = new TrunkUtil(true);
        }
        return trunkUtil;
    }

    private static TestData readTestData(ByteBuffer byteBuffer) {
        TestData testData = new TestData();
        byte[] array = byteBuffer.array();
        testData.setType(array[6]);
        long data = 0;

        data = data + ((array[8] & 0xFF) << 16);
        data = data + ((array[9] & 0xFF) << 8);
        data = data + (array[10] & 0xFF);
        if (array[7] == (byte) 1) {
            data = 0 - data;
        }
        testData.setData(data);
        return testData;
    }


    private void readData(byte[] buffer, int size, int startIndex) {
        int finishIndex = checkFinish(lastByte, buffer, size, startIndex);

        if (!readStarted) {
            int getStartIndex = readFromStart(buffer, size, startIndex, lastByte);
            readLength += readStartData(byteBuffer, buffer, size, lastByte, getStartIndex, finishIndex);
        } else {
            readLength += readFinishData(byteBuffer, buffer, size, finishIndex);
        }
        if (finishIndex >= 0) {
            int type = getReadedType(byteBuffer);
            Object data = getData(byteBuffer, type);
            final TrunkData trunkData = new TrunkData(type, data, readLength, getOriginData(byteBuffer));
            reentrantLock.lock();
            try {
                for (final TrunkListener trunkListener : trunkListeners.values()) {
                    if (trunkListener.getListenType() == type) {
                        ExecutorService.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                trunkListener.notifyData(trunkData);
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
            readData(buffer, size, finishIndex + 1);
        }
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

    private int checkFinish(byte lastByte, byte[] buffer, int size, int startIndex) {
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

    private int getReadedType(ByteBuffer byteBuffer) {
        byte[] buffer = byteBuffer.array();
        int buffer4 = buffer[4];
        int buffer5 = buffer[5];
        if (buffer4 == 0xFF && buffer5 == 0x00) {
            return TrunkConst.TYPE_HANDSHAKE;
        }
        if (buffer4 == 0x02 && buffer5 == 0x01) {
            return TrunkConst.TYPE_TESTSETTING;
        }
        if (buffer4 == 0x03) {
            return TrunkConst.TYPE_WAVELENGTH_CHOOSE;
        }
        if (buffer4 == 0x00 && buffer5 == 0x03) {
            return TrunkConst.TYPE_TESTDATA;
        }
        if (buffer4 == 0x01 && buffer5 == 0x01) {
            return TrunkConst.TYPE_CLEAN;
        }
        if (buffer4 == 0x04) {
            return TrunkConst.TYPE_SET_STANDARD_VALUE;
        }
        if (buffer4 == 0x05) {
            return TrunkConst.TYPE_RECOVER_STANDARD;
        }
        if (buffer4 == 0x06 && buffer5 == 0x01) {
            return TrunkConst.TYPE_SAVE_FACTORY;
        }
        if (buffer4 == 0x07 && buffer5 == 0x01) {
            return TrunkConst.TYPE_RECOVER;
        }
        if (buffer4 == 0x02 && buffer5 == 0x02) {
            return TrunkConst.TYPE_TEMPRETURE_CHANGE;
        }
        if (buffer4 == 0x02 && buffer5 == 0x03) {
            return TrunkConst.TYPE_TEMPRETURE_STATE;
        }
        if (buffer4 == 0x02 && buffer5 == 0x01) {
            return TrunkConst.TYPE_TEMPRETURE_COMPLETE;
        }
        if (buffer4 == 0x09 && buffer5 == 0x01) {
            return TrunkConst.TYPE_TEMPRETURE_ADD;
        }
        if (buffer4 == 0x09 && buffer5 == 0x02) {
            return TrunkConst.TYPE_TEMPRETURE_ADD_INIT;
        }
        if (buffer4 == 0x0A && buffer5 == 0x01) {
            return TrunkConst.TYPE_AUTOCLEAN;
        }
        if (buffer4 == 0x00 && buffer5 == 0x13) {
            return TrunkConst.TYPE_CORRECT_TEST;
        }
        return -1;
    }

    private Object getData(ByteBuffer byteBuffer, int type) {
        switch (type) {
            case TrunkConst.TYPE_CLEAN:
                return readCleanData(byteBuffer);
            case TrunkConst.TYPE_HANDSHAKE:
                return readHandShakeData(byteBuffer);
            case TrunkConst.TYPE_TESTSETTING:
                return readTestSettingData();
            case TrunkConst.TYPE_TESTDATA:
                return readTestData(byteBuffer);
            case TrunkConst.TYPE_WAVELENGTH_CHOOSE:
                return readWaveLengthData(byteBuffer);
            case TrunkConst.TYPE_SET_STANDARD_VALUE:
                return readSetStandardDataRes(byteBuffer);
            case TrunkConst.TYPE_RECOVER_STANDARD:
                return readRecoverStandard(byteBuffer);
            case TrunkConst.TYPE_SAVE_FACTORY:
                return readFactory();
            case TrunkConst.TYPE_RECOVER:
                return readRecover();
            case TrunkConst.TYPE_TEMPRETURE_CHANGE:
                return temperatureChange(byteBuffer);
            case TrunkConst.TYPE_TEMPRETURE_COMPLETE:
                return temperatureComplete(byteBuffer);
            case TrunkConst.TYPE_TEMPRETURE_STATE:
                return temperatureState(byteBuffer);
            case TrunkConst.TYPE_TEMPRETURE_ADD:
                return temperatureAdd();
            case TrunkConst.TYPE_TEMPRETURE_ADD_INIT:
                return temperatureAddInit(byteBuffer);
            case TrunkConst.TYPE_AUTOCLEAN:
                return autoClean();
            case TrunkConst.TYPE_CORRECT_TEST:
                return readTestData(byteBuffer);
        }
        return byteBuffer.array();
    }

    private int autoClean() {
        return 1;
    }

    private int temperatureAdd() {
        return 1;
    }

    private float temperatureAddInit(ByteBuffer buffer) {
        byte[] array = buffer.array();
        int a = (array[7] & 0xFF);
        if (array[6] == 1) {
            a = 0 - a;
        }
        return a / 10.0f;
    }

    private int temperatureState(ByteBuffer byteBuffer) {
        return byteBuffer.array()[6];
    }

    private int temperatureComplete(ByteBuffer byteBuffer) {
        return 0;
    }

    private int temperatureChange(ByteBuffer byteBuffer) {
        int temperatureValue = 0;
        byte[] buffer = byteBuffer.array();
        temperatureValue = temperatureValue + ((buffer[6] & 0xFF) << 8);
        temperatureValue = temperatureValue + (buffer[7] & 0xFF);
        return temperatureValue;
    }

    private StandardData readRecoverStandard(ByteBuffer byteBuffer) {
        StandardData standardData = new StandardData();
        byte[] buffer = byteBuffer.array();
        int testValue = 0;
        testValue = testValue + ((buffer[7] & 0xFF) << 16);
        testValue = testValue + ((buffer[8] & 0xFF) << 8);
        testValue = testValue + (buffer[9] & 0xFF);
        if (buffer[6] == (byte) 1) {
            testValue = 0 - testValue;
        }

        int standardValue = 0;
        standardValue = standardValue + ((buffer[11] & 0xFF) << 16);
        standardValue = standardValue + ((buffer[12] & 0xFF) << 8);
        standardValue = standardValue + (buffer[13] & 0xFF);
        if (buffer[10] == (byte) 1) {
            standardValue = 0 - standardValue;
        }

        int temperatureValue = 0;
        temperatureValue = temperatureValue + ((buffer[14] & 0xFF) << 8);
        temperatureValue = temperatureValue + (buffer[15] & 0xFF);

        standardData.setTestValue(testValue / 10000.0);
        standardData.setStandardValue(standardValue / 10000.0);
        standardData.setTemperature(temperatureValue / 10.0);
        return standardData;
    }

    private int readRecover() {
        return 1;
    }

    private int readFactory() {
        return 1;
    }

    private StandardDataRes readSetStandardDataRes(ByteBuffer byteBuffer) {
        StandardDataRes standardDataRes = new StandardDataRes();
        byte[] buffer = byteBuffer.array();
        standardDataRes.setSort(buffer[6]);
        standardDataRes.setWavelength(buffer[5]);
        return standardDataRes;
    }

    private int readHandShakeData(ByteBuffer byteBuffer) {
        byte[] array = byteBuffer.array();
        if (array[6] == 0) {
            return 1;
        }
        if (array[6] == 1) {
            return 2;
        }
        if (array[6] == 2) {
            return 3;
        }
        return 0;
    }

    private int readWaveLengthData(ByteBuffer byteBuffer) {
        byte[] array = byteBuffer.array();
        if (array[4] == 0x03 && array[5] == 0x01) {
            return 1;
        }
        return 0;
    }

    private int readCleanData(ByteBuffer byteBuffer) {
        byte[] array = byteBuffer.array();
        if (array[4] == 0x01 && array[5] == 0x01) {
            return 1;
        }
        return 0;
    }

    private int readTestSettingData() {
        return 1;
    }

    private byte[] getOriginData(ByteBuffer byteBuffer) {
        byte[] buffer = new byte[readLength];
        byteBuffer.flip();
        byteBuffer.get(buffer);
        return buffer;
    }

    public int handShake() {
        sendCmd(TrunkConst.HANDSHAKE);
        return 0;
    }

    public int singleTest() {
        sendCmd(TrunkConst.TEST);
        return 0;
    }

    public int chooseWaveLength(int waveLengthIndex) {
        byte[] buffer = new byte[3];
        buffer[0] = 0x03;
        buffer[1] = 0x01;
        buffer[2] = (byte) (waveLengthIndex);
        sendCmd(buffer);
        return 0;
    }

    public int setting(int temperature, int waveLengthIndex, boolean isFastTest) {
        byte[] setBuffer = new byte[6];

        setBuffer[0] = (byte) 0x02;
        setBuffer[1] = (byte) 0x01;
        setBuffer[2] = (byte) (temperature % (256 * 256) / 256);
        setBuffer[3] = (byte) (temperature % 256);

        if (isFastTest) {
            setBuffer[4] = (byte) 0x00;
        } else {
            setBuffer[4] = (byte) 0x01;
        }
        setBuffer[5] = (byte) waveLengthIndex;

        sendCmd(setBuffer);

        return 0;
    }

    public int setting(int waveLengthIndex, boolean isFastTest) {
        byte[] setBuffer = new byte[6];

        setBuffer[0] = (byte) 0x02;
        setBuffer[1] = (byte) 0x01;
        setBuffer[2] = (byte) 0xFF;
        setBuffer[3] = (byte) 0xFF;

        if (isFastTest) {
            setBuffer[4] = (byte) 0x00;
        } else {
            setBuffer[4] = (byte) 0x01;
        }
        setBuffer[5] = (byte) waveLengthIndex;

        sendCmd(setBuffer);

        return 0;
    }

    public int clean() {
        sendCmd(TrunkConst.CLEAN);
        return 0;
    }

    public void sendCmd(byte[] cmd) {
        int length = cmd.length + 8;
        int sum = length % (256 * 256) / 256 + length % 256;
        for (byte b : cmd) {
            sum += b & 0xff;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.put((byte) 0xAA).put((byte) 0x55);
        byteBuffer.put((byte) (length % (256 * 256) / 256)).put((byte) (length % 256));
        byteBuffer.put(cmd);
        byteBuffer.put((byte) ((sum & 0xff00) >> 8)).put((byte) (sum & 0xff));
        byteBuffer.put((byte) 0xCC).put((byte) 0x33);

        serialPortUtil.sendBuffer(byteBuffer.array());
    }

    public void sendStandardValue(double testValue, double standardValue,
                                  double temperature, int waveLength, int sort) {
        int testValue1 = (int) Math.abs(testValue * 10000);
        int standardValue1 = (int) Math.abs(standardValue * 10000);
        int temperature1 = (int) (temperature * 10);
        byte[] buffer = new byte[13];
        buffer[0] = (byte) 0x04;
        buffer[1] = (byte) waveLength;
        if (testValue > 0) {
            buffer[2] = 0;
        } else {
            buffer[2] = 1;
        }
        buffer[3] = (byte) ((testValue1 & 0xff0000) >> 16);
        buffer[4] = (byte) ((testValue1 & 0xff00) >> 8);
        buffer[5] = (byte) (testValue1 & 0xff);
        if (standardValue > 0) {
            buffer[6] = 0;
        } else {
            buffer[6] = 1;
        }
        buffer[7] = (byte) ((standardValue1 & 0xff0000) >> 16);
        buffer[8] = (byte) ((standardValue1 & 0xff00) >> 8);
        buffer[9] = (byte) (standardValue1 & 0xff);
        buffer[10] = (byte) ((temperature1 & 0xff00) >> 8);
        buffer[11] = (byte) (temperature1 & 0xff);
        buffer[12] = (byte) sort;

        sendCmd(buffer);
    }

    public void sendStandardRecover(int wavelength, int sort) {
        byte[] buffer = new byte[3];
        buffer[0] = 0x05;
        buffer[1] = (byte) wavelength;
        buffer[2] = (byte) sort;
        sendCmd(buffer);
    }

    public void sendTemperatureAdd(float temperature) {
        byte[] buffer = new byte[4];
        buffer[0] = 0x09;
        buffer[1] = 0x01;
        if (temperature > 0) {
            buffer[2] = 0x01;
        } else {
            buffer[2] = 0x00;
        }
        int temperatureAdd = (int) Math.abs(temperature * 10);
        buffer[3] = (byte) (temperatureAdd & 0xff);
        sendCmd(buffer);
    }

    public void sendTemperatureInit() {
        sendCmd(TrunkConst.TEMPRETURE_ADD_INIT);
    }

    public void sendAutoClean(double value) {
        int sendValue = (int) (value * 10000);
        byte[] buffer = new byte[3];
        buffer[0] = 0x0A;
        buffer[1] = 0x01;
        buffer[2] = (byte) (sendValue & 0xff);
        sendCmd(buffer);
    }

    public int addListener(TrunkListener trunkListener, int id) {
        reentrantLock.lock();
        try {
            trunkListeners.put(id, trunkListener);
        } finally {
            reentrantLock.unlock();
        }
        return id;
    }

    public int addEventListener(TitratorTrunkListener trunkListener, int id) {
        reentrantLock.lock();
        try {
            titratorTrunkListenerMap.put(id, trunkListener);
        } finally {
            reentrantLock.unlock();
        }
        return id;
    }



    public void cleanListener() {
        reentrantLock.lock();
        try {
            trunkListeners.clear();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void removeListener(int id) {
        reentrantLock.lock();
        try {
            trunkListeners.remove(id);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void removeEventListener(int id) {
        reentrantLock.lock();
        try {
            titratorTrunkListenerMap.remove(id);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void close() {
        serialPortUtil.closeSerialPort();
        mockStart = false;
    }

    public void mock() {
        if (mockStart) {
            ExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    while (mockStart) {
                        try {
                            Thread.sleep(1000);
                            reentrantLock.lock();
                            try {
                                final TrunkData trunkData = mockData();
                                for (final TrunkListener trunkListener : trunkListeners.values()) {
                                    if (trunkListener.getListenType() == TrunkConst.TYPE_TESTDATA) {
                                        ExecutorService.getInstance().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                trunkListener.notifyData(trunkData);
                                            }
                                        });
                                    }
                                }
                            } finally {
                                reentrantLock.unlock();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private TrunkData mockData() {
        TestData testData = new TestData();
        Random random = new Random();
        testData.setType(Math.abs(random.nextInt(2)));
        int data = random.nextInt(1000);
        testData.setData(data);
        TrunkData trunkData = new TrunkData(TrunkConst.TYPE_TESTDATA, testData, readLength, new byte[64]);
        return trunkData;
    }

    public static byte sumCheck(byte[] b, int len) {
        int sum = 0;
        for(int i = 0; i < len; i++) {
            sum = sum + b[i];
        }
        if(sum > 0xff) {
            sum = ~sum;
            sum = sum + 1;
        }
        return (byte)(sum & 0xff);
    }
}
