package com.jh.automatic_titrator.common.trunk;

import com.jh.automatic_titrator.common.utils.HexUtil;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import static com.jh.automatic_titrator.common.trunk.TitratorCommandAdapter.titratorCommandModuleMap;
import static com.jh.automatic_titrator.common.utils.HexUtil.hexStringToByteArray;
import static com.jh.automatic_titrator.common.utils.HexUtil.hexStringToByteArrayWithSpiliter;
import static com.jh.automatic_titrator.common.utils.HexUtil.toByteArray;
import static com.jh.automatic_titrator.common.utils.HexUtil.toHexString;

@Data
@Builder
public class DataFrame {

    private static final String HEADER = "AA55";
    private static final String END = "CC33";

    public byte[] header = new byte[2];;
    public byte[] size = new byte[2];
    public CommandEnum commandEnum;
    public byte[] command = new byte[2];
    public byte[] data;
    public byte[] sum = new byte[2];
    public byte[] end = new byte[2];
    public boolean up;

    public DataFrame(){

    }

    /**
     * String cmd = "AA 55 00 19 80 15 00 00 19 CB 00 00 00 00 27 12 00 B0 00 00 00 02 7B CC 33"
     *
     * @param frame
     */
    public DataFrame(String frame) {
        byte[] bytes = hexStringToByteArrayWithSpiliter(frame);

//        this.header = new byte[2];
//        this.size = new byte[2];
//        this.command = new byte[2];
//        this.sum = new byte[2];
//        this.end = new byte[2];

        header[0] = bytes[0];
        header[1] = bytes[1];

        size[0] = bytes[2];
        size[1] = bytes[3];

        command[0] = bytes[4];
        command[1] = bytes[5];

        sum[0] = bytes[bytes.length - 4];
        sum[1] = bytes[bytes.length - 3];

        end[0] = bytes[bytes.length - 2];
        end[1] = bytes[bytes.length - 1];

        commandEnum = CommandEnum.fromCodes(String.format("%s %s", toHexString(command[0]), toHexString(command[1])));

        String dataString = frame.substring(17, frame.length() - 11);
        this.data = hexStringToByteArrayWithSpiliter(dataString);
    }

    public DataFrame(byte[] bytes) {
        this(HexUtil.toHexString(bytes));
    }

    /**
     * 握手指令01
     * 指令: AA 55 SIZE 00 01 SUM CC 33
     * 下位机返回握手相应指令01,指令AA 55 SIZE 80 01  00 SUM CC 33
     * 其中00表示工作正常，否则有其他错误代码
     * 主机程序启动后，如果单片机发送握手指令停止单片机程序已启动，单片机收到后必须以握手
     * 相应指令做出响应，如果单片机工作状态正常，数据填写00，工作异常，填写故障编号(01-ff)
     * 如果主机发送握手指令后，在适当延时之后没有收到响应指令，应重复发送，在重复适当次数之后，提示操作者仪器异常
     * @return
     */
    public DataFrame getHandShakeDataFrame(){
        this.data = new byte[]{0x00, 0x01};
        this.commandEnum = CommandEnum.HAND_SHAKE;
        this.command = CommandEnum.getCommandBytes(CommandEnum.HAND_SHAKE);
        initDefaultArgument();
        return this;
    }

    public void initDefaultArgument(){
        this.header = hexStringToByteArray(HEADER);
        this.end = hexStringToByteArray(END);
        this.data = new byte[]{0x00, 0x01};
        this.command = CommandEnum.getCommandBytes(this.commandEnum);
        this.size = toByteArray(computeSize());
        this.sum = computeSum();
    }

    /**
     * 软件关闭指令02
     * 指令:AA 55 SIZE 00 02 SUM CC 33
     * 下位机返回关闭响应指令: AA 55 SIZE 80 02 00 SUM CC 33
     * @return
     */
    public DataFrame getSoftwareCloseDataFare() {
        this.commandEnum = CommandEnum.SOFTWARE_CLOSE;
        initDefaultArgument();
        return this;
    }







    /**
     *开始滴定命令参数
     ** 主控模块下有四个滴定模块，其对应的地址如下：
     ** 滴定模块1，地址(01)
     ** 滴定模块2，地址02
     ** 滴定模块3，地址03
     ** 滴定模块4，地址04
     * @param titratorModelNum
     * @param commandEnum
     * @return
     */
    public DataFrame getTitratorDataFrame(int titratorModelNum, CommandEnum commandEnum) {
        if(commandEnum != CommandEnum.START_TITRATOR && commandEnum != commandEnum.STOP_TITRATOR) {
            throw new IllegalArgumentException("滴定方法不对");
        }
        this.header = hexStringToByteArray(HEADER);
        this.commandEnum = commandEnum;
        this.command = CommandEnum.getCommandBytes(commandEnum);
        byte moduleByte = titratorCommandModuleMap.get(titratorModelNum);
        this.data = new byte[]{moduleByte};
        this.end = hexStringToByteArray(END);

        int size = computeSize();
        this.size = toByteArray(size);
        this.sum = computeSum();
        return this;
    }






    public int computeSize() {
        return this.header.length + this.command.length + 2 + this.end.length + this.data.length;
    }

    public static byte[] computeSum() {
        return new byte[0];
    }


    @Override
    public String toString() {
        return "DataFrame{" +
                "header=" + toHexString(header) +
                ", size=" + toHexString(size) +
                ", commandEnum=" + commandEnum.toString() +
                ", command=" + toHexString(command) +
                ", data=" + toHexString(data) +
                ", sum=" + toHexString(sum) +
                ", end=" + toHexString(end) +
                ", up=" + up +
                '}';
    }
}
