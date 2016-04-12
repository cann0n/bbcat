package com.sdkj.bbcat.BluetoothBle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Tools {
	public static RFLampDevice device=null;
	public static boolean isPick;
	protected static final byte[] CRCPASSWORD = { 'C', 'h', 'e', 'c', 'k', 'A',
			'e', 's' };
	private static Bitmap after;
	
	public static int getRectWidth(Context context){
		int width;
		WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		width=manager.getDefaultDisplay().getWidth();
		return width/12;
	}
	
	public static String[] splitTimeStr(String str){
		String[] str1  = new String[2];
		String[] str2 = str.split(":");
		for(int i = 0; i<str2.length;i++){
			str1[i]  = String.valueOf(str2[i]);
		}
		return str1;
	}
	
	/**
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public static String byte2Hex(byte[] data) {
		
		if (data != null && data.length > 0) {
			StringBuilder sb = new StringBuilder(data.length);
			for (byte tmp : data) {
				sb.append(String.format("%02X", tmp));
			}
			return sb.toString();
		}
		return "no data";
	}


	public static void encodeArray(byte arrayLengh, byte[] arrayEncode,
			byte[] arrayDecode) {
		byte crcChecksum = crc_checksum(arrayLengh, arrayDecode);
		arrayEncode[0] = crcChecksum;
		for (int position = 0; position < arrayLengh; position++) {
			arrayEncode[position + 1] = (byte) (crcChecksum ^ arrayDecode[position]);
		}
	}

	public static boolean decodeArray(byte arrayLengh, byte[] encodeArray,
			byte[] decodeArray) {

		boolean checkout = false;
		for (int position = 0; position < arrayLengh; position++) {
			decodeArray[position] = (byte) (encodeArray[0] ^ encodeArray[1]);
		}
		byte crcChecksum = crc_checksum(arrayLengh, decodeArray);
		if (crcChecksum == encodeArray[0]) {
			checkout = true;
		}
		return checkout;
	}

	private static byte crc_checksum(byte arrayLengh, byte[] array) {
		int i, j;
		byte[] crcPassword = CRCPASSWORD;
		byte CRC_Checkout = 0x0;

		for (i = 0; i < arrayLengh; i++) {
			byte CRC_Temp = array[i];
			for (j = 0; j < 8; j++) {
				CRC_Temp = (byte) (CRC_Temp & 0x01);
				CRC_Checkout = (byte) (CRC_Checkout ^ crcPassword[j]);
				CRC_Temp = (byte) (CRC_Temp >> 1);
			}
		}
		return CRC_Checkout;
	}
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  

	public static byte[] getSystemTime() {
		byte[] cal = new byte[7];
		Calendar calendar = Calendar.getInstance();
		cal[0] = (byte) (calendar.get(Calendar.YEAR) & 0xff);
		cal[1] = (byte) (calendar.get(Calendar.YEAR) >> 8 & 0xff);
		cal[2] = (byte) ((calendar.get(Calendar.MONTH) + 1) & 0xff);
		cal[3] = (byte) (calendar.get(Calendar.DAY_OF_MONTH) & 0xff);
		cal[4] = (byte) (calendar.get(Calendar.HOUR_OF_DAY) & 0xff);
		cal[5] = (byte) (calendar.get(Calendar.MINUTE) & 0xff);
		cal[6] = (byte) (calendar.get(Calendar.SECOND) & 0xff);
		return cal;
	}
	public static boolean existSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}
	public static class dir{
		@SuppressLint("SdCardPath")
		public static final String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/funlight";
		public static final String pushImage = baseDir + "/images";
		public static final String speaker = baseDir + "/media/";
		public static final String speakerFile = speaker+"/"+"record.amr";
		
		public static final String log = baseDir + "/Log";
		public static final String btCommandLog = log+"/btCommandLog";
	}
	
	public static int restoreColor(int light, int color) {

		int lightValue = light * 255 / 100;
		int colorArray[] = new int[3];
		colorArray[0] = Color.red(color);
		colorArray[1] = Color.green(color);
		colorArray[2] = Color.blue(color);
		int hsl[] = new int[3];
		Tools.RGB2HSL(colorArray[0], colorArray[1], colorArray[2], hsl);

		int rgb[] = new int[3];
		Tools.HSL2RGB(hsl[0], hsl[1], lightValue, rgb);

		return Color.rgb(rgb[0], rgb[1], rgb[2]);
	}
	public static int countLight(int color) {
		int colorArray[] = new int[3];
		colorArray[0] = Color.red(color);
		colorArray[1] = Color.green(color);
		colorArray[2] = Color.blue(color);
		int hsl[] = new int[3];
		Tools.RGB2HSL(colorArray[0], colorArray[1], colorArray[2], hsl);
		return 100 * hsl[2] / 255;
	}
	public static void RGB2HSL(int red, int green, int blue, int[] hsl) {
		float h = 0, s = 0, l = 0;
		float r = red / 255.0f;
		float g = green / 255.0f;
		float b = blue / 255.0f;
		float maxVal = Math.max(Math.max(r, g), b);
		float minVal = Math.min(Math.min(r, g), b);
		float max_add_min = maxVal + minVal;
		float max_sub_min = maxVal - minVal;

		// hue
		if (max_sub_min == 0) {
			h = 0; // 无符�?		
		} else if (maxVal == r && g >= b) {
			h = 60.0f * (g - b) / max_sub_min;
		} else if (maxVal == r && g < b) {
			h = 60.0f * (g - b) / max_sub_min + 360.0f;
		} else if (maxVal == g) {
			h = 60.0f * (b - r) / max_sub_min + 120.0f;
		} else if (maxVal == b) {
			h = 60.0f * (r - g) / max_sub_min + 240.f;
		}

		// luminance
		l = max_add_min / 2.0f;

		// saturation
		if (l == 0 || max_sub_min == 0) {
			s = 0;
		} else if (0 < l || l <= 0.5f) {
			s = max_sub_min / max_add_min;
		} else if (l > 0.5f) {
			s = max_sub_min / (2 - max_add_min);
		}

		float hueFloat = ((h > 360) ? 360 : ((h < 0) ? 0 : h)) / 360.0f;
		float saturationFloat = ((s > 1) ? 1 : ((s < 0) ? 0 : s));
		float luminanceFloat = ((l > 1) ? 1 : ((l < 0) ? 0 : l));

		 hsl[0] = (int) (239.0f * hueFloat);
		 hsl[1] = (int) (240.0f * saturationFloat);
		 hsl[2] = (int) (240.0f * luminanceFloat);
//		hsl[0] = (int) (255.0f * hueFloat);
//		hsl[1] = (int) (255.0f * saturationFloat);
//		hsl[2] = (int) (255.0f * luminanceFloat);
	}

	/**
	 * HSL转RGB
	 * 
	 * @param hue
	 * @param saturation
	 * @param luminance
	 * @param rgb
	 */

	public static void HSL2RGB(int hue, int saturation, int luminance, int[] rgb) {
		float h = hue / 255.0f;
		float s = saturation / 255.0f;
		float l = luminance / 255.0f;
		float r, g, b;
		if (saturation == 0) {
			r = g = b = l * 255.0f;
		} else {
			float q = (l < 0.5f) ? (l * (1.0f + s)) : (l + s - (l * s));
			float p = (2.0f * l) - q;
			float HK = h;
			float T[] = new float[3];
			T[0] = HK + 0.3333333f;
			T[1] = HK;
			T[2] = HK - 0.3333333f;

			for (int count = 0; count < 3; count++) {
				if (T[count] < 0)
					T[count] += 1.0f;
				if (T[count] > 1)
					T[count] -= 1.0f;
				if ((T[count] * 6 < 1)) {
					T[count] = p + ((q - p) * 6.0f * T[count]);
				} else if ((T[count] * 2.0f) < 1) {
					T[count] = q;
				} else if (T[count] * 3.0f < 2) {
					T[count] = p + (p - q) * ((2.0f / 3.0f) - T[count]) * 6.0f;
				} else {
					T[count] = p;
				}
			}
			r = T[0] * 255.0f;
			g = T[1] * 255.0f;
			b = T[2] * 255.0f;
		}
		rgb[0] = (int) ((r > 255) ? 255 : ((r < 0) ? 0 : r));
		rgb[1] = (int) ((g > 255) ? 255 : ((g < 0) ? 0 : g));
		rgb[2] = (int) ((b > 255) ? 255 : ((b < 0) ? 0 : b));
	}
	public static float[] rgb2hsb(int rgbR, int rgbG, int rgbB) {  
	    assert 0 <= rgbR && rgbR <= 255;  
	    assert 0 <= rgbG && rgbG <= 255;  
	    assert 0 <= rgbB && rgbB <= 255;  
	    int[] rgb = new int[] { rgbR, rgbG, rgbB };  
	    Arrays.sort(rgb);  
	    int max = rgb[2];  
	    int min = rgb[0];  
	  
	    float hsbB = max / 255.0f;  
	    float hsbS = max == 0 ? 0 : (max - min) / (float) max;  
	  
	    float hsbH = 0;  
	    if (max == rgbR && rgbG >= rgbB) {  
	        hsbH = (rgbG - rgbB) * 60f / (max - min) + 0;  
	    } else if (max == rgbR && rgbG < rgbB) {  
	        hsbH = (rgbG - rgbB) * 60f / (max - min) + 360;  
	    } else if (max == rgbG) {  
	        hsbH = (rgbB - rgbR) * 60f / (max - min) + 120;  
	    } else if (max == rgbB) {  
	        hsbH = (rgbR - rgbG) * 60f / (max - min) + 240;  
	    }  
	    return new float[] { hsbH, hsbS, hsbB };  
	  //  return new int[] { (int)(hsbH*255.0f), (int)(hsbS*255.0f),(int)(hsbB*255.0f) };  
	}  
	  
	public static int[] hsb2rgb(float h, float s, float v) {  
	    assert Float.compare(h, 0.0f) >= 0 && Float.compare(h, 360.0f) <= 0;  
	    assert Float.compare(s, 0.0f) >= 0 && Float.compare(s, 1.0f) <= 0;  
	    assert Float.compare(v, 0.0f) >= 0 && Float.compare(v, 1.0f) <= 0;  
	    float r = 0, g = 0, b = 0;  
	    int i = (int) ((h / 60) % 6);  
	    float f = (h / 60) - i;  
	    float p = v * (1 - s);  
	    float q = v * (1 - f * s);  
	    float t = v * (1 - (1 - f) * s);  
	    switch (i) {  
	    case 0:  
	        r = v;  
	        g = t;  
	        b = p;  
	        break;  
	    case 1:  
	        r = q;  
	        g = v;  
	        b = p;  
	        break;  
	    case 2:  
	        r = p;  
	        g = v;  
	        b = t;  
	        break;  
	    case 3:  
	        r = p;  
	        g = q;  
	        b = v;  
	        break;  
	    case 4:  
	        r = t;  
	        g = p;  
	        b = v;  
	        break;  
	    case 5:  
	        r = v;  
	        g = p;  
	        b = q;  
	        break;  
	    default:  
	        break;  
	    }  
	    return new int[] { (int) (r * 255.0), (int) (g * 255.0),  
	            (int) (b * 255.0) };  
	}  
	public static List<UUID> parseUuids(byte[] advertisedData) {
	     List<UUID> uuids = new ArrayList<UUID>();
	     ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
	     while (buffer.remaining() > 2) {
	         byte length = buffer.get();
	         if (length == 0) break;

	         byte type = buffer.get();
	         switch (type) {
	             case 0x02: // Partial list of 16-bit UUIDs
	             case 0x03: // Complete list of 16-bit UUIDs
	                 while (length >= 2) {
	                     uuids.add(UUID.fromString(String.format(
	                             "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
	                     length -= 2;
	                 }
	                 break;

	             case 0x06: // Partial list of 128-bit UUIDs
	             case 0x07: // Complete list of 128-bit UUIDs
	                 while (length >= 16) {
	                     long lsb = buffer.getLong();
	                     long msb = buffer.getLong();
	                     uuids.add(new UUID(msb, lsb));
	                     length -= 16;
	                 }
	                 break;

	             default:
	                 buffer.position(buffer.position() + length - 1);
	                 break;
	         }
	     }

	     return uuids;}
}
