package com.epiboly.bea2.http.model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LifecycleOwner;

import com.epiboly.bea2.login.LoginActivity;
import com.epiboly.bea2.manager.ActivityManager;
import com.google.gson.JsonSyntaxException;
import com.epiboly.bea2.R;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyLog;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestHandler;
import com.hjq.http.exception.CancelException;
import com.hjq.http.exception.DataException;
import com.hjq.http.exception.HttpException;
import com.hjq.http.exception.NetworkException;
import com.hjq.http.exception.ResponseException;
import com.hjq.http.exception.ServerException;
import com.hjq.http.exception.TimeoutException;
import com.hjq.http.exception.TokenException;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 请求处理类
 */
public final class RequestHandler implements IRequestHandler {

    private final Application mApplication;
    private final MMKV mMmkv;

    public RequestHandler(Application application) {
        mApplication = application;
        mMmkv = MMKV.mmkvWithID("http_cache_id");
    }

    @Override
    public Object requestSucceed(LifecycleOwner lifecycle, IRequestApi api, Response response, Type type) throws Exception {

        if (Response.class.equals(type)) {
            return response;
        }

        if (!response.isSuccessful()) {
            // 返回响应异常
            throw new ResponseException(mApplication.getString(R.string.http_response_error) + "，responseCode：" + response.code() + "，message：" + response.message(), response);
        }

        if (Headers.class.equals(type)) {
            return response.headers();
        }

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        if (InputStream.class.equals(type)) {
            return body.byteStream();
        }

        String text;
        try {
            text = body.string();
        } catch (IOException e) {
            // 返回结果读取异常
            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        // 打印这个 Json 或者文本
        EasyLog.json(text);

        if (String.class.equals(type)) {
            return text;
        }

        if (JSONObject.class.equals(type)) {
            try {
                // 如果这是一个 JSONObject 对象
                return new JSONObject(text);
            } catch (JSONException e) {
                throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
            }
        }

        if (JSONArray.class.equals(type)) {
            try {
                // 如果这是一个 JSONArray 对象
                return new JSONArray(text);
            } catch (JSONException e) {
                throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
            }
        }

        final Object result;

        try {
            result = GsonFactory.getSingletonGson().fromJson(text, type);
        } catch (JsonSyntaxException e) {
            // 返回结果读取异常
            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        if (result instanceof HttpData) {
            HttpData<?> model = (HttpData<?>) result;

            if (model.isRequestSucceed()) {
                // 代表执行成功
                return result;
            }

            if (model.isTokenFailure()) {
                // 代表登录失效，需要重新登录
                throw new TokenException(mApplication.getString(R.string.http_token_error));
            }

            return result;

            // 代表执行失败
//            throw new ResultException(model.getMessage(), model);
        }
        return result;
    }

    @Override
    public Exception requestFail(LifecycleOwner lifecycle, IRequestApi api, Exception e) {
        // 判断这个异常是不是自己抛的
        if (e instanceof HttpException) {
            if (e instanceof TokenException) {
                // 登录信息失效，跳转到登录页
                Application application = ActivityManager.getInstance().getApplication();
                Intent intent = new Intent(application, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                application.startActivity(intent);
                // 销毁除了登录页之外的 Activity
                ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
            }
            return e;
        }

        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(mApplication.getString(R.string.http_server_out_time), e);
        }

        if (e instanceof UnknownHostException) {
            NetworkInfo info = ((ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            // 判断网络是否连接
            if (info == null || !info.isConnected()) {
                // 没有连接就是网络异常
                return new NetworkException(mApplication.getString(R.string.http_network_error), e);
            }

            // 有连接就是服务器的问题
            return new ServerException(mApplication.getString(R.string.http_server_error), e);
        }

        if (e instanceof IOException) {
            //e = new CancelException(context.getString(R.string.http_request_cancel), e);
            return new CancelException("", e);
        }

        return new HttpException(e.getMessage(), e);
    }

    @Override
    public Object readCache(LifecycleOwner lifecycle, IRequestApi api, Type type) {
        String cacheKey = GsonFactory.getSingletonGson().toJson(api);
        String cacheValue = mMmkv.getString(cacheKey, null);
        if (cacheValue == null || "".equals(cacheValue) || "{}".equals(cacheValue)) {
            return null;
        }
        EasyLog.print("---------- cacheKey ----------");
        EasyLog.json(cacheKey);
        EasyLog.print("---------- cacheValue ----------");
        EasyLog.json(cacheValue);
        return GsonFactory.getSingletonGson().fromJson(cacheValue, type);
    }

    @Override
    public boolean writeCache(LifecycleOwner lifecycle, IRequestApi api, Response response, Object result) {
        String cacheKey = GsonFactory.getSingletonGson().toJson(api);
        String cacheValue = GsonFactory.getSingletonGson().toJson(result);
        if (cacheValue == null || "".equals(cacheValue) || "{}".equals(cacheValue)) {
            return false;
        }
        EasyLog.print("---------- cacheKey ----------");
        EasyLog.json(cacheKey);
        EasyLog.print("---------- cacheValue ----------");
        EasyLog.json(cacheValue);
        return mMmkv.putString(cacheKey, cacheValue).commit();
    }
}