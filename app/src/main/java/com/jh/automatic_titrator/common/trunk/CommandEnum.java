package com.jh.automatic_titrator.common.trunk;


import com.jh.automatic_titrator.common.utils.HexUtil;

import java.nio.ByteBuffer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指令枚举
 */
@AllArgsConstructor
public enum  CommandEnum {

    HAND_SHAKE("握手指令0001", "00 01"),
    HAND_SHAKE_RESPONSE("下位机响应握手指令8001", "80 01"),

    SOFTWARE_CLOSE("软件关闭指令0002", "00 02"),
    SOFTWARE_CLOSE_RESPONSE("软件关闭指令响应结果8002", "80 02"),

    TITRATOR_METHOD("滴定方法指令0010", "00 10"),
    START_TITRATOR("开始滴定指令0012", "00 12"),
    STOP_TITRATOR("停止滴定指令0013", "00 13"),

    FINSHED_TITRATOR("完成滴定命令8090", "8090"),

    TITRATOR_FINSHED("滴定完成指令8014", "8014"),
    TITRATOR_DATA("滴定数据指令8015", "80 15"),


    SET_TITRATOR_VOLUME("手动滴定中的设置添加体积指令0016", "00 16"),
    END_POINT("传输终点信息指令8019", "80 19"),


    START_DEMARCATE("启动标定指令0020", "00 20"),
    DEMARCATE_DATA("标定数据格式指令8021", "80 21"),
    FINSHED_DEMARCATE("标定完成指令8022", "80 22"),
    SEND_DEMARCATE_INDEX("电极标定后的电极斜率结果传输到机器指令0074", "00 74"),
    STOP_DEMARCATE("停止标定指令0080", "00 80"),

    START_CLEAN("清洗指令0030", "0030"),
    CLEAN_STATUS("清洗状态8031", "8031"),
    STOP_CLEAN("停止清洗0034", "0034"),
    SEND_CLEAN_DATA("传输清洗0091", "0091"),

    ADD_SUPPLEMENT("补液", "00 32"),
    STOP_SUPPLEMENT("停止补液", "00 35"),
    SUPPLEMENT_FINSHED("补液完成", "80 33"),

    START_STIR("启动搅拌0055", "00 55"),
    STOP_STIR("停止搅拌0056", "00 56"),

    IS_STOP_TITRATOR("是否停止滴定", "80 70"),
    FINSHED_AND_STOP_TITRATOR("完成并停止滴定", "00 71"),
    MUST_CONTINUE_TITRATOR("要求继续滴定指令8072", "80 72"),

    QUERY_DEVICE_STATUS("查询外设机器状态0082", "00 82"),

    DATA_SEND("数据传输格式指令00C1", "00 C1"),
    ADD_SAMPLE_STYLE("进样方式指令00C2", "00 C2"),

    ;

    //描述
    @Getter
    private String description;
    //code
    @Getter
    private String code;

    public static CommandEnum fromCodes(String code) {
        for(CommandEnum commandEnum : CommandEnum.values()) {
            if(commandEnum.getCode().equals(code)) {
                return commandEnum;
            }
        }
        return null;
    }

    public static byte[] getCommandBytes(CommandEnum commandEnum) {
       String code = commandEnum.getCode();
       return HexUtil.hexStringToByteArray(code);
    }

//    public static CommandEnum parseCommandType(String command) {
//        String commandStr = command.substring();
//    }

    @Override
    public String toString() {
        return "CommandEnum{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
