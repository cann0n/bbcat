//package com.sdkj.bbcat.activity;
//
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.error.AuthFailureError;
//import com.android.volley.error.NoConnectionError;
//import com.android.volley.error.ParseError;
//import com.android.volley.error.VolleyError;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.huaxi100.networkapp.activity.BaseActivity;
//import com.huaxi100.networkapp.network.PostParams;
//import com.huaxi100.networkapp.network.RespJSONObjectListener;
//import com.huaxi100.networkapp.utils.Utils;
//
//import org.apache.http.HttpEntity;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Test extends Request<JSONObject> {
//    private RespJSONObjectListener listener = null;
//    private PostParams params = null;
//    private HttpEntity httpEntity = null;
//
//    public Test(final BaseActivity activity, PostParams params, final String url, final RespJSONObjectListener listener) {
//        super(Method.POST, url, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error instanceof NoConnectionError) {
//                    activity.toast("请检查网络");
//                }else {
//                    System.err.println(Utils.isEmpty(error.getMessage()) ? "访问:" + url + "出错" : error.getMessage());
//                }
//                listener.doFailed();
//            }
//        });
//        this.params = params;
//        this.listener = listener;
//        setShouldCache(false);
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        if (params != null) {
//            httpEntity = params.getEntity();
//            try {
//                httpEntity.writeTo(baos);
//            } catch (IOException e) {
//            }
//        }
//        return baos.toByteArray();
//    }
//
//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String jsonString = new String(response.data,
//                    HttpHeaderParser.parseCharset(response.headers));
//            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(response));
//        } catch (JSONException e) {
//            return Response.error(new ParseError(response));
//        }
//    }
//
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> headers = super.getHeaders();
//        if (null == headers || headers.equals(Collections.emptyMap())) {
//            headers = new HashMap<String, String>();
//        }
//        headers.put("Accept-Language","zh-cn,zh;q=0.5");
//        return headers;
//    }
//
//    @Override
//    public String getBodyContentType() {
//        System.out.println("httpEntity.getContentType().getValue() = " + httpEntity.getContentType().getValue());
////        return httpEntity.getContentType().getValue();
//        return "application/json";
////        return "application/x-www-form-urlencoded";
//    }
//
//
//    @Override
//    protected void deliverResponse(JSONObject response) {
//        listener.onResponse(response);
//    }
//
//    @Override
//    protected VolleyError parseNetworkError(VolleyError volleyError) {
//        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//            volleyError = error;
//        }
//        return volleyError;
//    }
//}