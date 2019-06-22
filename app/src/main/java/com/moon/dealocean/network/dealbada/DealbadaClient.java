package com.moon.dealocean.network.dealbada;

import android.content.Context;
import android.util.Log;

import com.moon.dealocean.network.ResponseCallback;

import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by zambo on 2016-06-27.
 */
public class DealbadaClient {
    public static final String BASE_URL = "http://www.dealbada.com/";
    private static DealbadaClient mDealbadaClient;
    private DealbadaAPIService mDealbadaAPIService;

    private DealbadaClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new WebviewCookieHandler())
//                .cookieJar(new CookieJar() {
//
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        String cookie = Util.getString(context, "cookie", "");
//                        Log.d("moon", "moon moon cookie : " + cookie);
//                        return !cookie.isEmpty() ? Arrays.asList(Cookie.parse(url, cookie)) : Collections.EMPTY_LIST;
//                    }
//                })

                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();

        mDealbadaAPIService = retrofit.create(DealbadaAPIService.class);
    }

    public static DealbadaClient getInstance() {
        if (mDealbadaClient == null) {
            mDealbadaClient = new DealbadaClient();
        }
        return mDealbadaClient;
    }

    public static DealbadaClient newInstance(Context context) {
        if (mDealbadaClient == null) {
            mDealbadaClient = new DealbadaClient();
        }
        return mDealbadaClient;
    }

    private void callAPI(final ResponseCallback responseCallback, Call call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("moon", "moon Success Call : " + call.request().toString());
                try {
                    Source source = new Source(new InputStreamReader(response.body().byteStream(), "utf-8"));
                    responseCallback.opSuccess(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("moon", "moon Fail Call : " + call.request().toString());
                responseCallback.onFail();
            }
        });
    }

    public void getDetail(String url, int commentPageNum, ResponseCallback responseCallback) {
        Call<ResponseBody> call = mDealbadaAPIService.getDetail(url, commentPageNum);
        callAPI(responseCallback, call);
    }

    public void getDealList(String url, int page, ResponseCallback responseCallback) {
        Call<ResponseBody> call = mDealbadaAPIService.getDealList(url, page);
        callAPI(responseCallback, call);
    }

    public void getUserInfo(ResponseCallback responseCallback) {
        Call<ResponseBody> call = mDealbadaAPIService.getUserInfo();
        callAPI(responseCallback, call);
    }
}
