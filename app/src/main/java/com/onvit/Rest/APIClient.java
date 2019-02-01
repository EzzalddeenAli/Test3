package com.onvit.Rest;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private Retrofit retrofit = null;

    public Retrofit getClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8088/Test3/")
                .client(new OkHttpClient.Builder()
                        .hostnameVerifier(new NullHostNameVerifier())
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .addInterceptor(new AddCookiesInterceptor(context))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public Retrofit getClient(Context context, String baseURL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(new OkHttpClient.Builder()
                        .hostnameVerifier(new NullHostNameVerifier())
                        .addInterceptor(interceptor)
                        .addInterceptor(new AddCookiesInterceptor(context))
                        .addInterceptor(new ReceivedCookiesInterceptor(context))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    class ReceivedCookiesInterceptor implements Interceptor {
        Context con;

        public ReceivedCookiesInterceptor(Context context) {
            con = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());


            Log.d("TAG", "intercept: " + chain.proceed(chain.request()).body().string().trim());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE).edit().putStringSet("Cookie", cookies).commit();
                Log.d("Cookies", "ReceivedCookiesInterceptor : " + cookies);

            }
            return originalResponse;
        }
    }

    class AddCookiesInterceptor implements Interceptor {
        Context con;

        public AddCookiesInterceptor(Context context) {
            con = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            HttpUrl url = chain.request().url().newBuilder().addQueryParameter("AppID", con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE).getString("AppID", "")).build();
            builder.url(url).build();
            Set<String> preferences = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE).getStringSet("Cookie", new HashSet<String>());

            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.d("Cookies", "AddCookiesInterceptor : " + cookie);
            }

            builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");

            Response originalResponse = chain.proceed(builder.build());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE).edit().putStringSet("Cookie", cookies).commit();
                Log.d("Cookies", "ReceivedCookiesInterceptor : " + cookies);

            }
            return originalResponse;
        }
    }
}
