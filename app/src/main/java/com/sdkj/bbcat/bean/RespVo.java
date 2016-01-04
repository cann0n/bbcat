package com.sdkj.bbcat.bean;

import com.huaxi100.networkapp.utils.GsonTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 15:16
 * 服务端返回数据
 */
public class RespVo<T> implements Serializable {

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData(JSONObject obj, Class<T> cls) {
        T data = null;
        try {
            data = GsonTools.getVo(obj.getJSONObject("data").toString(), cls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<T> getListData(JSONObject obj, Class<T> cls) {
        List<T> data = null;
        try {
            JSONArray array =obj.getJSONArray("data");
            data = GsonTools.getList(array, cls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean isSuccess(){
        if(code==null){
            return  false;
        }
        return  "0".equals(code);
    }
    public boolean isNeedLogin(){
        if(code==null){
            return  false;
        }
        return  "10401".equals(code);
    }
}
