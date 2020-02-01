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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    static {
        System.loadLibrary("native-lib");
    }

    public SerialPort(File device, int baudrate) throws SecurityException, IOException {
        if (!device.canRead() || !device.canWrite()) {
            try {
                String command = "chmod 666 " + device.getAbsolutePath();
                Log.i(TAG, "command ");
                Runtime runtime = Runtime.getRuntime();

                Process proc = runtime.exec(command);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        mFd = open(device.getAbsolutePath(), baudrate);
        if (mFd == null) {
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    private native FileDescriptor open(String path, int baudrate);

    public native int close();
}
