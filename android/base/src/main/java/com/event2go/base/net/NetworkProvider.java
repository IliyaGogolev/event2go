package com.event2go.base.net;

import android.support.annotation.IntDef;

import com.event2go.base.utils.Logger;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;

/**
 * Created by Iliya Gogolev on 3/7/16.
 */
public class NetworkProvider {

    @IntDef({LOAD_STATUS_NONE, LOAD_STATUS_START, LOAD_STATUS_SUCCESS, LOAD_STATUS_FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadDataStatus {
    }

    public static final int LOAD_STATUS_NONE = 0;
    public static final int LOAD_STATUS_START = 1;
    public static final int LOAD_STATUS_SUCCESS = 2;
    public static final int LOAD_STATUS_FAIL = 3;
    public static final int LOAD_STATUS_CANCELED = 4;

    private static ServerApi api;

    public static ServerApi getApi() {
        if (api == null) {
            initUsingEnvironmentSettings();
        }
        return api;
    }

    public static void initUsingEnvironmentSettings() {

        String baseUrl = "not in use...";
        initRestClient(baseUrl);
    }


    private static void initRestClient(String url) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new ErrorInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ServerApi.class);
    }

    public static class ErrorInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %1$s on %2$s %3$s",
                    request.url(), chain.connection(), request.headers());

            if (request.method().compareToIgnoreCase("post") == 0) {
                requestLog = requestLog + "\n" + bodyToString(request);
            }
            Logger.d("request: \n" + requestLog);

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String responseLog = String.format("Received response for %s in %.1fms",
                    response.request().url(), (t2 - t1) / 1e6d);

            String bodyString = response.body().string();

            Logger.d("response" + "\n" + responseLog + "\n" + bodyString);

            switch (response.code()) {

                case 404: // page not found
                    break;
                case 503: // service unavailable
                    break;
            }

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
        }
    }

    public static String bodyToString(final Request request) {
        try {

            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();

        } catch (final IOException e) {
            return "Exception: " + e.getMessage();
        }
    }
}
