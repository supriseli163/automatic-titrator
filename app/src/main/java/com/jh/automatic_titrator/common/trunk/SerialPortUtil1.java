package com.jh.automatic_titrator.common.trunk;/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log;

import com.jh.automatic_titrator.common.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtil1 {

    public static final int defaultBaudrate = 9600;
    //    public static final int registerBaudrate = 115200;
    private static SerialPortUtil1 portUtil;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String path = "/dev/ttyS3";
    private int baudrate = defaultBaudrate;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;

    public static SerialPortUtil1 getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPortUtil1();
            portUtil.onCreate();
        }
        return portUtil;
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

//    public void changeToRegister() {
//        this.baudrate = registerBaudrate;
//        closeSerialPort();
//        onCreate();
//    }

//    public void changeToDefault() {
//        this.baudrate = defaultBaudrate;
//        closeSerialPort();
//        onCreate();
//    }

    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd + "\r\n").getBytes();
        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
//        String tail = "\r\n";
//        byte[] tailBuffer = tail.getBytes();
//        byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
//        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
//        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
                mOutputStream.flush();
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);

        void cleanBuffer();
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[512];
            int size;
            while (!isStop && !isInterrupted()) {
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                            byte[] subBuffer = new byte[size];
                            System.arraycopy(buffer, 0, subBuffer, 0, size);
                            Log.d("received", StringUtils.bytesToHexString(subBuffer));
                        }
                    }
                    Thread.sleep(10);
                    buffer = new byte[512];
                } catch (Exception e) {
                    e.printStackTrace();
                    onDataReceiveListener.cleanBuffer();
                }
            }
        }
    }
}
