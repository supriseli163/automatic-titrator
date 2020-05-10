package com.jh.automatic_titrator.common.trunk;

import com.jh.automatic_titrator.entity.common.titrator.SampleTypeEnum;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import java8.util.Maps;

import static com.jh.automatic_titrator.common.utils.HexUtil.hexStringToByteArray;

public class TitratorCommandAdapter extends DataFrame {
    /**
     * 开始滴定命令参数
     * 主控模块下有四个滴定模块，其对应的地址如下：
     * 滴定模块1，地址(01)
     *
     * 滴定模块2，地址02
     * 滴定模块3，地址03
     * 滴定模块4，地址04
     * @param titratorModelNum
     * @return
     */

    public static  Map<Integer, Byte> titratorCommandModuleMap;
    public static  Map<TitratorTypeEnum, Byte> titratorTypeMap;

    static {
        byte[] moduleBytes = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] titratorTypeBytes = new byte[]{0x01, 0x02, 0x03, 0x04};
//        byte[] sampleTypeBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,0x09, 0x0A
//        0xA0, 0x0B,0x0C,0x0D,0x0E, 0x0F};

        titratorCommandModuleMap = new HashMap<>();
        titratorCommandModuleMap.put(1, moduleBytes[0]);
        titratorCommandModuleMap.put(2, moduleBytes[1]);

        for(int index =0; index < moduleBytes.length; index ++) {
            titratorCommandModuleMap.put(index + 1, moduleBytes[index]);
        }

        titratorTypeMap = new HashMap<>();

        titratorTypeMap.put(TitratorTypeEnum.DynamicTitrator, titratorTypeBytes[01]);
        titratorTypeMap.put(TitratorTypeEnum.ManualTitrator, titratorTypeBytes[02]);
        titratorTypeMap.put(TitratorTypeEnum.EndPointTitrator, titratorTypeBytes[03]);
    }

    /**
     * 滴定方法指令
     *              AA 55 SIZE 00 10 00 00 00(滴定类型(4)) 00 00 00 00 00 00 00 00 00 00 00
     指令编号      00 10
     *0x01：单个电极支架，0x03:进样器   00
     * 8位前4位表示滴定速度，后四位表示电极支架或者进样器
     * 动态滴定，0x11是极慢速度+单个电极支架
     *          0x21慢速度
     *          0x31标准速度
     *          0x51快速度
     *          0xA1极快速度
     * 其他模式速度位0，比如终点滴定单个电极支架发送0x01
     *  00 电极支架的情况下代表地址，进样器的情况下代表样品孔位(最多16个孔)
     *  目前观察电极支架发送位0x00
     * @return
     */
    public DataFrame getTitratorMethodDataFrame(TitratorMethod titratorMethod) {
        StringBuilder command = new StringBuilder();
        //滴定类型8bit， 微量滴定(1)、动态滴定(2)、手动滴定(3)、终点滴定(4)
        //最高位1代表显示的单位是mv，最高0位代表显示单位是ph
        command.append(titratorTypeMap.get(titratorMethod));
        //滴定管体积8bit 5ml(5) 10ml(10) 25ml(25)
        if(titratorMethod.getBuretteVolume() == 5) {
            command.append("05");
        } else if(titratorMethod.getBuretteVolume() == 10) {
            command.append("0A");
        } else if(titratorMethod.getBuretteVolume() == 25) {
            command.append("19");
        }

        //电极搅拌速度，数据在1-10之间
        String stireSpeedStr = "";
        if(titratorMethod.getStiringSpeed().equals("10")) {
            stireSpeedStr = "0A";
        } else {
            stireSpeedStr = String.format("0%s", titratorMethod.getStiringSpeed());
        }
        //00 电极平衡时间,数据在1-5s
        command.append(String.format("0%s", titratorMethod.getElectroedEquilibrationTime()));
        //00 电极平衡电位8bit 数据在0.1-5s把数据放大十倍避免传小数，例如0.1s就传1
        String electroctroedEquilibrium = titratorMethod.getElectroedEquilibriumPotential().replace(".", "");
        command.append(electroctroedEquilibrium);
        //00 预搅拌时间8bit 数据在5-300 秒


        //00 补液速度(滴定完成后，仪器自动补液的速度)从1到10
        if(titratorMethod.getReplenishmentSpeed().equals("10")) {
            command.append("0A");
        } else {
            command.append(String.format("0%s", titratorMethod.getReplenishmentSpeed()));
        }

        //        //结束体积,如果没有则传0
        //手动滴定没有结束体积，传00 00 00
        String endVolume;
        if(titratorMethod.getTitratorType().equals(TitratorTypeEnum.ManualTitrator)) {
            endVolume = "000000";
        } else {
            endVolume = String.format("0%s", titratorMethod.getEndVolume());
        }
        command.append(endVolume);

        //代表初次添加后搅拌时间 秒
        command.append(String.format("0%s", titratorMethod.getPreStiringTime()));

        //00 00 代表初次添加的体积，把体积ml转换成ul避免传输小数，如0.1ml传送的时候100代表100ul
        double preAddVolume = Double.valueOf(titratorMethod.getPerAddVolume());




        //00传送终点个数
        command.append(String.format("0%s", titratorMethod.getTitratorEndPoints().size()));

        //
        return new DataFrame();
    }

    /**
     * 清洗指令 00 30
     * AA 55 SIZE 00 30 xx 00 SUM CC 33
     * @param titratorModuleNum     滴定模块地址
     * @param cleanTimes            清洗次数
     * @param cleanSpeed            清洗速度
     * @return
     */
    public DataFrame getCleanCommand(int titratorModuleNum, int cleanTimes, int cleanSpeed) {
        DataFrame dataFrame = new DataFrame();
        this.commandEnum = CommandEnum.START_CLEAN;
        byte[] dataBytes = new byte[3];
//        dataBytes[0] = titratorCommandModuleMap.get(titratorModuleNum);
//        dataBytes[1] = 0x00;
//        dataBytes[2] =

        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append();

        initDefaultArgument();
        return dataFrame;
    }

    /**
     * 停止清洗命令 00 34
     * AA 55 SIZE 00 34 xx SUM CC 33
     * @param titratorModuleNum
     * @return
     */
    public DataFrame getStopCleanCommand(int titratorModuleNum) {
        DataFrame dataFrame = new DataFrame();
        byte data = titratorCommandModuleMap.get(titratorModuleNum);
        dataFrame.setData(new byte[]{data});
        dataFrame.setCommandEnum(CommandEnum.STOP_CLEAN);
        initDefaultArgument();
        return dataFrame;
    }

    /**
     * 补液 0032
     * @param titratorModuleNum
     * @param speed
     * @return
     */
    public DataFrame getAddSupplementCommand(int titratorModuleNum, int speed) {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.STOP_SUPPLEMENT);
        byte titratorModuleByte = titratorCommandModuleMap.get(titratorModuleNum);

        return dataFrame;
    }

    /**
     * 停止补液 00 35
     * @param titratorModuleNum
     * @return
     */
    public DataFrame getStopSupplementCommand(int titratorModuleNum) {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.STOP_SUPPLEMENT);
        byte moduleNum = titratorCommandModuleMap.get(titratorModuleNum);
        dataFrame.setData(new byte[]{moduleNum});
        initDefaultArgument();
        return dataFrame;
    }


    /**
     * 开始搅拌
     * @param titratorModuleNum
     * @return
     */
    public DataFrame getStartStirCommand(int titratorModuleNum) {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.START_STIR);
        byte moduleNum = titratorCommandModuleMap.get(titratorModuleNum);
        dataFrame.setData(new byte[]{moduleNum});
        initDefaultArgument();
        return dataFrame;
    }

    /**
     * 停止搅拌
     * @param titratorModuleNum
     * @return
     */
    public DataFrame getStopStirCommand(int titratorModuleNum) {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.STOP_STIR);
        byte moduleNum = titratorCommandModuleMap.get(titratorModuleNum);
        dataFrame.setData(new byte[]{moduleNum});
        initDefaultArgument();
        return dataFrame;
    }

    /**
     * 查询设备状态
     * 00 82 电脑发送给机器的命令
     * @return
     */
    public DataFrame getDeviceStatusCommand() {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.QUERY_DEVICE_STATUS);
        initDefaultArgument();
        return dataFrame;
    }

    /**
     * 设置进样方式指令
     * 00 C2
     *
     * 进样方式选择:只保留单孔和多孔
     * 在设置--进样方式中选择进样器，主界面会出现圆形
     * @return
     */
    public DataFrame getSetSampleStyleCommand(SampleTypeEnum sampleTypeEnum) {
        DataFrame dataFrame = new DataFrame();
        dataFrame.setCommandEnum(CommandEnum.ADD_SAMPLE_STYLE);
        byte[] sampleType = new byte[1];
        if(sampleTypeEnum == SampleTypeEnum.ONE) {
            sampleType[0] = 0x01;
        } else {
            sampleType[0] = 0x03;
        }
        dataFrame.setData(sampleType);
        initDefaultArgument();
        return dataFrame;
    }

}
