package com.jh.automatic_titrator.common.trunk;

/**
 * 指令数字为16进制表示
 * AA 55 SIZE CMD DATA SUM CC 33
 * AA 55:包头
 * SIZE 长度为2字节，整个数据包长度，高字节在前，低字节在后
 * CMD  2 字节、标志位、步骤不同标志位不同、高字节在前、低字节在后
 * SUM  校验和(2个字节，高位在前，低位在后，由SIZE、CMD、DATA三项逐字节按无符号数累加)
 * CC 33:包尾
 *
 * PC向下位机传输指令，指令码类型编号为00 xx,下位机对应的回答为80 xx
 * 例如:开始滴定 AA 55 SIZE 00 12 00 SUM CC 33
 * 单片机上发给上位机 AA 55 SIZE 80 12 SUM CC 33
 *
 * 上面的指令码为12， PC发送为0012,应答指令为8012
 */
public class TrankCommandConst {

    /**
     * 握手指令 01
     * 返回握手命令01，指令AA 55 SIZE 80 81 00 SUM CC 33
     * 00 表示工作正常，否则有其他错误代码
     */
    public static final int HAND_SHAKE = 0x01;

    /**
     * 软件关闭命令 02
     * 指令AA 55 SIZE 00 02 SUM CC 33
     * 单片机收到后返回关闭响应指令:AA 55 SIZE 80 02 00 SUM 33
     * 00 表示单片机状态数据
     */
    public static final int SOFTWARE_CLOSE = 0x02;

    /**
     *
     */
    public static final int TITRATOR_METHOD = 0x10;

    /**
     * 开始滴定命令 12
     */
    public static final int START_TITRATOR = 0x12;

    /**
     * 停止滴定命令 13
     * 停止后返回状态命令: AA 55 SIZE 80 13 XX xx SUM CC 33
     * XX:  0x01 表示设备正在停止过程中
     *      0x02 故障
     *      0x03 停止操作返程
     */
    public static final int STOP_TITRATOR = 0x13;

    /**
     * 滴定完成命令80 14(此命令为下位机向PC传输
     * AA 55 SIZE 80 14 XX SUM CC 33
     * XX 电极支架的情况下代表地址，进样器的情况下代表样品孔位
     */
    public static final int TITRATOR_FINSHED = 0x8014;

    /**
     * 滴定数据命令: 80 15(此命令为下位机向pc传输)
     */
    public static final int TITRATOR_DATA = 0x8015;

    /**
     * 滴定数据格式   AA 55 SIZE 80 15 xx 00 0000 00 00 00 00 0000 00 00 000000 SUM CC 33
     * 80 15        向主机传输测试数据
     * xx:          数据地址
     * 00:          当是1的时候画曲线 当是0的时候只显示体积和电压值
     * 0000:        数据顺序编号(从0开始)
     * 00 00 00 00  体积(单位ul)
     * 00 00        电压值(放大10倍精确到小数点后1位 即精确到0.1mv)ph值(放大了1000倍)
     * 00 00        温度值(精确到小数点后1位 即0.1度)，当温度发送的值大于或者等于10000时，该值为无效值，表示没有接温度电极
     * 0000         导数值
     */


    /**
     * AA 55 SIZE 80 22 xx SUM CC 33
     * 80 22    电极标定命令
     * xx       主控制模块地址
     * AA
     */
    public static final int DEMARCATE_FINSHED = 0x022;

    /**
     * 电极标定后的电极斜率结果传输到机器命令 74(可以只做两点标定)
     * (此方式是3点标定    2点标定的时候 2部分电极斜率相同即可)
     *
     * AA 55 SIZE 00 74 00 0000 0000 0000 SUM CC 33
     * 00 74    电极标定结果命令
     * 00       主控制模块地址
     * 0000     PH0的值(放大1000倍)
     * 0000     PH电极斜率(放大1000倍)
     * 0000     标定电极时候的温度
     * 在软件上显示百分比的时候就用,得出斜率/-59.16
     */
    public static final int DEMARCATE_RATE = 0x74;

    /**
     * 停止标定命令 80
     * 停止标定 AA 55 IZE 00 80 XX SUM CC 33
     * 00 80  停止标定命令
     * XX  代表主控模块地址
     *
     */
    public static final String STOP_DEMARCATE = "80";


    /**
     * 自检命令24(此功能下位机可能暂时不会响应)
     * 00 24        自检命令
     * xx           测试模块地址
     * 00 0x02      搅拌系统
     * 0x03         进样器
     */
    public static final String  START_SELF_CHECK = "AA 55 SIZE 00 24 xx  00  SUM CC 33";


    /**
     * 反馈状态 AA 55 SIZE 80 25 xx 00 00 SUM CC 33
     */
    public static final String SELF_CHECK_COMMAND = "8025";



    //******************************************************************
    /**
     * 清洗和补液命令参考
     */
    public static final String CLEAN_COMMAND =

}
