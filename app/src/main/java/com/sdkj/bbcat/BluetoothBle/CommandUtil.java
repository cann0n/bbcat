package com.sdkj.bbcat.BluetoothBle;

import android.text.TextUtils;

import com.huaxi100.networkapp.utils.Utils;

/**
 * Created by ${Rhino} on 2016/4/12 11:44
 */
public class CommandUtil {

    public static byte[] inputPass(String passWord) {
        byte[] value = new byte[16];
        value[0] = getByte("20");
        value[1] = getByte(StringHexUtils.toHexString(passWord.substring(0,1)));
        value[2] = getByte(StringHexUtils.toHexString(passWord.substring(1, 2)));
        value[3] = getByte(StringHexUtils.toHexString(passWord.substring(2, 3)));
        value[4] = getByte(StringHexUtils.toHexString(passWord.substring(3, 4)));
        value[5] = getByte(StringHexUtils.toHexString(passWord.substring(4, 5)));
        value[6] = getByte(StringHexUtils.toHexString(passWord.substring(5, 6)));
        value[7] = getByte("00");
        value[8] = getByte("00");
        value[9] = getByte("00");
        value[10] = getByte("00");
        value[11] = getByte("00");
        value[12] = getByte("00");
        value[13] = getByte("00");
        value[14] = getByte("00");
        int s = 0;
        for (int i = 0; i < 15; i++) {
            s += value[i];
        }
        value[15] = (byte) s;
        return value;
    }

    public static byte[] setCaryRd() {
        byte[] value = new byte[16];
        value[0] = getByte("5C");
        value[1] = getByte("01");
        value[2] = getByte("01");
        value[3] = getByte("00");
        value[4] = getByte("00");
        value[5] = getByte("00");
        value[6] = getByte("00");
        value[7] = getByte("00");
        value[8] = getByte("00");
        value[9] = getByte("00");
        value[10] = getByte("00");
        value[11] = getByte("00");
        value[12] = getByte("00");
        value[13] = getByte("00");
        value[14] = getByte("00");
        int s = 0;
        for (int i = 0; i < 15; i++) {
            s += value[i];
        }
        value[15] = (byte) s;
        return value;
    }

    public static byte[] getCaryRd() {
        byte[] value = new byte[16];
        value[0] = getByte("43");
        value[1] = getByte("00");
        value[2] = getByte("00");
        value[3] = getByte("00");
        value[4] = getByte("00");
        value[5] = getByte("00");
        value[6] = getByte("00");
        value[7] = getByte("00");
        value[8] = getByte("00");
        value[9] = getByte("00");
        value[10] = getByte("00");
        value[11] = getByte("00");
        value[12] = getByte("00");
        value[13] = getByte("00");
        value[14] = getByte("00");
        int s = 0;
        for (int i = 0; i < 15; i++) {
            s += value[i];
        }
        value[15] = (byte) s;
        return value;
    }

//    public static byte[] getTemperature(){
//        byte[] value = new byte[16];
//        value[0] = getByte("09");
//        value[1] = getByte("00");
//        value[2] = getByte("00");
//        value[3] = getByte("00");
//        value[4] = getByte("00");
//        value[5] = getByte("00");
//        value[6] = getByte("00");
//        value[7] = getByte("00");
//        value[8] = getByte("00");
//        value[9] = getByte("00");
//        value[10] = getByte("00");
//        value[11] = getByte("00");
//        value[12] = getByte("00");
//        value[13] = getByte("00");
//        value[14] = getByte("00");
//        int s = 0;
//        for (int i = 0; i < 15; i++) {
//            s += value[i];
//        }
//        value[15] = (byte) s;
//        return value; 
//    }

    public static byte[] startGetTemperature(){
        byte[] value = new byte[16];
        value[0] = getByte("5b");
        value[1] = getByte("01");
        value[2] = getByte("00");
        value[3] = getByte("00");
        value[4] = getByte("00");
        value[5] = getByte("00");
        value[6] = getByte("00");
        value[7] = getByte("00");
        value[8] = getByte("00");
        value[9] = getByte("00");
        value[10] = getByte("00");
        value[11] = getByte("00");
        value[12] = getByte("00");
        value[13] = getByte("00");
        value[14] = getByte("00");
        int s = 0;
        for (int i = 0; i < 15; i++) {
            s += value[i];
        }
        value[15] = (byte) s;
        return value;
    }
    public static byte[] stopGetTemperature(){
        byte[] value = new byte[16];
        value[0] = getByte("5b");
        value[1] = getByte("00");
        value[2] = getByte("00");
        value[3] = getByte("00");
        value[4] = getByte("00");
        value[5] = getByte("00");
        value[6] = getByte("00");
        value[7] = getByte("00");
        value[8] = getByte("00");
        value[9] = getByte("00");
        value[10] = getByte("00");
        value[11] = getByte("00");
        value[12] = getByte("00");
        value[13] = getByte("00");
        value[14] = getByte("00");
        int s = 0;
        for (int i = 0; i < 15; i++) {
            s += value[i];
        }
        value[15] = (byte) s;
        return value;
    }
    
    public static byte getByte(String trim) {
        if (TextUtils.isEmpty(trim)) return 0;
        return (byte) Integer.parseInt(trim, 16);
    }

    public static int getResultMsg(String value) {
        String command = value.substring(0, 2);
        if (command.equals("20")) {
            if (value.substring(3, 5).equals("00")) {
                //密码错误
                return 0;
            } else {
                //密码正确，并成功链接
                return 1;
            }
        } else if (command.equals("43")) {
            //正确获取到卡里路和温度
            return 2;
        }else if(command.equals("09")){
            //正确获取到活动量.电池电量.温度
            return  3;
        }else if(command.equals("5B")){
            return  4;
        }
        return -1;
    }

    public static int getVlalue(byte[] value) {
        int t = value[2] + (256 * value[1]);
        return t;
    }
}
