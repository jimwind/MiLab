package me.mi.milab.view.piechart;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class DesUtils {

    /** 加密工具 */
    private Cipher encryptCipher = null;

    /** 解密工具 */
    private Cipher decryptCipher = null;


    // width below which there are no extra margins
    public static final int NO_PADDING_SCREEN_WIDTH = 480;
    // width beyond which we're always using the MIN_SCALE_FACTOR
    public static final int MAX_PADDING_SCREEN_WIDTH = 800;

    // the minimum scaling factor for the web dialog (50% of screen size)
    private static final double MIN_SCALE_FACTOR = 0.5;

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    public String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn
     *            需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    public byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 指定密钥构造方法
     *
     * @param strKey
     *            指定的密钥
     * @throws Exception
     */
    public DesUtils(String strKey) throws Exception {
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    private int count(int src) {
        int count = 0;
        while (src >= 1) {
            count += src % 10;
            src = src / 10;
            if (src == 0) {
                if (count > 10) {
                    src = count;
                    count = 0;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    public String dORencrypt(String s) {
        int length = s.length();
        int c = s.charAt(length - 1);
        int count = count(c);
        char[] cs = s.toCharArray();
        for (int i = count; i < length; i += count + 1) {
            if (count + i > length || i + count == length) {
                break;
            }
            char a = cs[i - 1], b = cs[i + count - 1];
            cs[i - 1] = b;
            cs[i + count - 1] = a;
        }
        return new String(cs);
    }

    /**
     * 加密字节数组
     *
     * @param arrB
     *            需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn
     *            需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     *
     * @param arrB
     *            需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn
     *            需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

    public static void main(String[] args) {
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("uid", 7196330);
        String key="up360user#activity#key";
        DesUtils des;
        try {
            des = new DesUtils(key);
            String code= des.encrypt(JSON.toJSONString(maps));
            System.out.println("======JSONcode:"+JSON.toJSONString(maps));
            System.out.println("======code:"+code);

            String jsonResult = des.decrypt(code);
            JSONObject jsonObject = JSONObject.parseObject(jsonResult);
            Long userId = jsonObject.getLong("uid");
            System.out.println("======userId:"+userId);
        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int dip2px(Context context , float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕缩放比例（非精确）
     * @param context
     * @return
     */
    public static double getScreenScale(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // always use the portrait dimensions to do the scaling calculations so we always get a portrait shaped
        // web dialog
        int width = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        double scaleSize = getScaledSize(width, metrics.density,
                NO_PADDING_SCREEN_WIDTH, MAX_PADDING_SCREEN_WIDTH);

        return scaleSize;
    }


    /**
     * Returns a scaled size (either width or height) based on the parameters passed.
     *
     * @param screenSize     a pixel dimension of the screen (either width or height)
     * @param density        density of the screen
     * @param noPaddingSize  the size at which there's no padding for the dialog
     * @param maxPaddingSize the size at which to apply maximum padding for the dialog
     * @return a scaled size.
     */
    public static double getScaledSize(int screenSize, float density, int noPaddingSize, int maxPaddingSize) {
        int scaledSize = (int) ((float) screenSize / density);
        double scaleFactor;
        if (screenSize <= noPaddingSize) {
            scaleFactor = 1.0;
        } else if (screenSize >= maxPaddingSize) {
            scaleFactor = MIN_SCALE_FACTOR;
        } else {
            // between the noPadding and maxPadding widths, we take a linear reduction to go from 100%
            // of screen size down to MIN_SCALE_FACTOR
            scaleFactor = MIN_SCALE_FACTOR +
                    ((double) (maxPaddingSize - scaledSize))
                            / ((double) (maxPaddingSize - noPaddingSize))
                            * (1.0 - MIN_SCALE_FACTOR);
        }
        return scaleFactor;
    }



    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}