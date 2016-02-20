package com.sdkj.bbcat.BluetoothBle;

public class BleIn_EConstructData
{
    private static byte hexCharToByte(char index)
    {
        return (byte) "0123456789ABCDEF".indexOf(index);
    }

    public static byte[] hexStrToByteArray(String hexStr)
    {
        byte[] bytes = null;
        if (!isEmpty(hexStr))
        {
            if(hexStr.length()%2 == 1)
                hexStr += "0";

            hexStr = hexStr.toUpperCase();
            int length = hexStr.length() / 2;
            char[] dataChars = hexStr.toCharArray();

            bytes = new byte[length];
            for (int i = 0; i < length; i++)
            {
                int pos = i * 2;
                bytes[i] = (byte) (hexCharToByte(dataChars[pos]) << 4 | hexCharToByte
                        (dataChars[pos + 1]));
            }
        }
        return bytes;
    }

    public static String byteArrayToHexStr(byte[] bytes)
    {
        if (bytes != null && bytes.length > 0)
        {
            final StringBuilder stringBuilder = new StringBuilder(bytes.length);
            for (byte byteChar : bytes)
            {
                String hexChar = Integer.toHexString(byteChar & 0xFF);
                if (hexChar.length() == 1)
                    hexChar = '0' + hexChar;
                stringBuilder.append(hexChar.toUpperCase());
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public static String binaryStrToHexStr(String binaryStr)
    {
        if (isEmpty(binaryStr))
            return null;
        while(binaryStr.length() % 8 != 0)
        {
            binaryStr += "0";
        }

        StringBuffer stringBuffer = new  StringBuffer();
        for (int index = 0; index < binaryStr.length() / 4; index++)
            stringBuffer.append(Integer.toHexString(Integer.valueOf(binaryStr.substring
                    (index * 4, index * 4 + 4), 2)));
        return stringBuffer.toString();
    }

    public static String hexStrToBinaryStr(String hexStr)
    {
        if (isEmpty(hexStr))
            return null;
        while(hexStr.length() % 2 != 0)
        {
            hexStr += "0";
        }

        StringBuffer stringBuffer = new  StringBuffer();
        for (int index = 0; index < hexStr.length(); index++)
            stringBuffer.append(Integer.toBinaryString(Integer.valueOf(hexStr.substring
                    (index, index + 1), 16)));
        return stringBuffer.toString();
    }

    public static boolean isEmpty(String dataStr)
    {
        if(dataStr == null || "".equals(dataStr))
            return true;
        else
            return false;
    }
}