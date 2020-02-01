package com.jh.automatic_titrator.common.net.printer;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * Created by apple on 2016/11/12.
 */
@SuppressLint("NewApi")
public class Printer {

    public Socket socket;

    public static int POS_OPEN_NETPORT = 9100;

    public boolean IFOpen = false;

    public int Net_SendTimeout = 1000;

    public int Net_ReceiveTimeout = 1500;

    public String command = ""; //打印命令字符串

    public byte[] outbytes; //传输的命令集

    PrinterCMD pcmd = new PrinterCMD();

    /// <summary>
    /// 网络打印机 打开网络打印机
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public boolean open(String ipaddress, int netport) {
        if (socket == null) {
            try {
                SocketAddress ipe = new InetSocketAddress(ipaddress, netport);
                socket = new Socket();  //Socket(ipaddress, netport,true);
                socket.connect(ipe, 5000);
                socket.setSoTimeout(Net_ReceiveTimeout);
                //socket.SendTimeout = Net_SendTimeout;
                IFOpen = true;
                //System.out.print("连接成功");
            } catch (Exception e) {
                //MessageBox.Show("连接不成功");
                e.printStackTrace();
                IFOpen = false;
            }
        } else {
            try {
                socket.close();
                SocketAddress ipe = new InetSocketAddress(ipaddress, netport);
                socket = new Socket();  //Socket(ipaddress, netport,true);
                socket.connect(ipe);
                socket.setSoTimeout(Net_ReceiveTimeout);
                //socket.SendTimeout = Net_SendTimeout;
                IFOpen = true;
            } catch (Exception e) {
                e.printStackTrace();
                IFOpen = false;
            }
        }
        return IFOpen;
    }

    /// <summary>
    /// 网络打印机 关闭
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public void Close() {
        try {
            socket.close();
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机 初始化打印机
    /// </summary>
    public void Set() {
        try {
            command = pcmd.CMD_SetPos();
            OutputStream stream = socket.getOutputStream();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机 打印的文本
    /// </summary>
    /// <param name="pszString"></param>
    /// <param name="nFontAlign">0:居左 1:居中 2:居右</param>
    /// <param name="nfontsize">字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小</param>
    /// <param name="ifzhenda">0:非针打  1:针打</param>
    public void PrintText(String pszString, int nFontAlign, int nFontSize, int ifzhenda) {
        try {
            OutputStream stream = socket.getOutputStream();
            command = pcmd.CMD_TextAlign(nFontAlign);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            if (ifzhenda == 1) {
                command = pcmd.CMD_FontSize_BTP_M280(nFontSize);
                outbytes = command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);

                command = pcmd.CMD_FontSize_BTP_M2801(nFontSize);
                outbytes = command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            } else {
                command = pcmd.CMD_FontSize(nFontSize);
                outbytes = command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }

            command = pszString;// +CMD_Enter();
            outbytes = command.getBytes(Charset.forName("GB2312")); //Charset.defaultCharset()); //forName("UTF-8")
            stream.write(outbytes);
            stream.flush();
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            inputStream.read(buffer);
            String res = new String(buffer);
            System.out.println(res);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

    }

    /// <summary>
    /// 网络打印机 回车
    /// </summary>
    public void PrintEnter() {
        try {
            command = pcmd.CMD_Enter();
            OutputStream stream = socket.getOutputStream();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /// <summary>
    /// 网络打印机 切割
    /// </summary>
    /// <param name="pagenum">切割时，走纸行数</param>
    public void CutPage(int pagenum) {
        try {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.CMD_PageGO(pagenum) + pcmd.CMD_Enter();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_CutPage() + pcmd.CMD_Enter();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机换行
    /// </summary>
    /// <param name="lines"></param>
    public void PrintNewLines(int lines) {
        try {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.CMD_FontSize(0);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
            for (int i = 0; i < lines; i++) {
                command = "" + pcmd.CMD_Enter();
                outbytes = command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 打开钱箱
    /// </summary>
    public void OpenQianXiang() {
        try {
            OutputStream stream = socket.getOutputStream();
            command = "" + pcmd.CMC_QianXiang();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /// <summary>
    /// 打印条码
    /// </summary>
    /// <param name="numstr"></param>
    /// <param name="height"></param>
    /// <param name="width"></param>
    /// <param name="numweizi">1:上方  2:下方  0:不打印数字</param>
    /// <param name="fontalign">0:居左 1:居中 2:居右</param>
    /// <param name="fontsize">字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小</param>
    public void PrintTiaoMa(String numstr, int height, int width, int numweizi, int fontalign, int fontsize) {
        try {
            OutputStream stream = socket.getOutputStream();
            command = pcmd.CMD_TiaoMaHeight(height);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaWidth(width);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaWeiZi(numweizi);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TextAlign(fontalign);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_FontSize(fontsize);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaPrint(numstr);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
