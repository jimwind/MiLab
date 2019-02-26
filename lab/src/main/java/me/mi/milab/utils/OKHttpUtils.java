package me.mi.milab.utils;

import android.os.Environment;
import android.util.JsonToken;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mi.gao on 2017/7/21.
 */

public class OKHttpUtils {
    private OkHttpClient okHttpClient;

    private String getMoJson(Object paramsObject){
        int random  = (int)(Math.random()*10000+10000);
        String sessionkey = stringToMD5("/um/0/3"+"UP360_sysion" + random);
        sessionkey +=";00000000-3720-5010-de74-c0ee0033c587;";//imei
        HashMap<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("random", random);
        paramsMap.put("appId","6702533");
        paramsMap.put("sessionKey", sessionkey);
        paramsMap.put("version","2.3.0");
        paramsMap.put("params",paramsObject);
        paramsMap.put("sysType", "3");
        paramsMap.put("sysVersion", android.os.Build.VERSION.RELEASE);
        paramsMap.put("manufacturer",android.os.Build.MANUFACTURER);
        paramsMap.put("model",android.os.Build.MODEL);

//        HashMap<String,Object> mojson = new HashMap<>();
//        mojson.put("moJson", paramsMap);

        String params = JSON.toJSONString(paramsMap);
        Log.e("jimwind",params);
        return params;
    }
    public void simplePost() {
        okHttpClient = new OkHttpClient();
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("account", "13611985800");
        paramsMap.put("password", stringToMD5("123456"));
        String moJson = getMoJson(paramsMap);

        RequestBody requestBody = new FormBody.Builder()
                .add("moJson", moJson)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.18.48:8080/um/0/3")
                .post(requestBody)
                .addHeader("token", "helloworld")
                .build();

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), moJson);
//        Request request = new Request.Builder()
//                .url("http://192.168.18.48:8080/um/0/3")
//                .post(body)
//                .build();

        okHttpClient.newCall(request).enqueue(callback);
        Log.e("jimwind", "simplePost");
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("jimwind", "onFailure "+e.getMessage());
            setResult(e.getMessage(), false);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.e("jimwind", "onResponse "+response.body().string());
        }
    };
    //显示请求返回的结果
    private void setResult(final String msg, final boolean success) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (success) {
//                    Toast.makeText(DownloadActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DownloadActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//                }
//                resultTextView.setText(msg);
//            }
//        });
    }
    public static String stringToMD5(String str){
        try {
            byte[] strTemp=str.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strTemp);
            return toHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String toHexString(byte[] md) {
        char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f',};
        int j = hexDigits.length;
        char[] str = new char[j*2];
        for (int i = 0; i < j; i++) {
            byte byteo = md[i];
            str[2*i]=hexDigits[byteo>>>4 & 0xf];
            str[2*i+1]=hexDigits[byteo & 0xf];
        }
        return new String(str);
    }
}
