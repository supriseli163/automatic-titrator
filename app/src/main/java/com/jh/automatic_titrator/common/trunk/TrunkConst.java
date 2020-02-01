package com.jh.automatic_titrator.common.trunk;

/**
 * Created by apple on 2016/12/14.
 */

public interface TrunkConst {

    int TYPE_HANDSHAKE = 0x00;

    int TYPE_CLEAN = 0x01;

    int TYPE_TESTSETTING = 0x02;

    int TYPE_TESTDATA = 0x03;

    int TYPE_WAVELENGTH_CHOOSE = 0x04;

    int TYPE_SET_STANDARD_VALUE = 0x05;

    int TYPE_RECOVER_STANDARD = 0x06;

    int TYPE_SAVE_FACTORY = 0x07;

    int TYPE_RECOVER = 0x08;

    int TYPE_TEMPRETURE_CHANGE = 0x09;

    int TYPE_TEMPRETURE_STATE = 0x10;

    int TYPE_TEMPRETURE_COMPLETE = 0x11;

    int TYPE_TEMPRETURE_ADD = 0x12;

    int TYPE_TEMPRETURE_ADD_INIT = 0x13;

    int TYPE_AUTOCLEAN = 0x14;

    int TYPE_CORRECT_TEST = 0x15;

    byte[] HANDSHAKE = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x0B};

    byte[] CLEAN = new byte[]{(byte) 0x01, (byte) 0x01};

    byte[] TEST = new byte[]{(byte) 0x01, (byte) 0x02};

    byte[] CORRECT_I = new byte[]{(byte) 0x08, (byte) 0x01};

    byte[] CORRECT_O = new byte[]{(byte) 0x08, (byte) 0x02};

    byte[] SETFACTORY = new byte[]{(byte) 0x06, (byte) 0x01};

    byte[] RECOVER = new byte[]{(byte) 0x07, (byte) 0x01};

    byte[] TEMPRETURE_CHANGE = new byte[]{(byte) 0x02, (byte) 0x02};

    byte[] TEMPRETURE_STATE = new byte[]{(byte) 0x02, (byte) 0x03};

    byte[] TEMPRETURE_COMPLETE = new byte[]{(byte) 0x02, (byte) 0x01};

    byte[] TEMPRETURE_ADD = new byte[]{0x09, 0x01};

    byte[] TEMPRETURE_ADD_INIT = new byte[]{0x09, 0x02};

    byte[] AUTO_CLEAN = new byte[]{0x0A, 0x01};

    byte[] CORRECT_TEST = new byte[]{0x00, 0x13};
}
