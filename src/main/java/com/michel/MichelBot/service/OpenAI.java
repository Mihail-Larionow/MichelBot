package com.michel.MichelBot.service;

import com.michel.MichelBot.api.OpenAIApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import okhttp3.logging.HttpLoggingInterceptor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

import java.io.IOException;

@Service
public class OpenAI {

    @Value("${openai.api-key}")
    private String apiKey;
    private static final String BASE_URL = "https://api.openai.com/";

    private final OpenAIApi api;

    public OpenAI() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(chain -> {
            okhttp3.Request original = chain.request();
            okhttp3.Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + apiKey)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        api = retrofit.create(OpenAIApi.class);
    }

    public String generateCode(String input) {

        String code = "";
        Call<ResponseBody> call = api.generateCode(input);

        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                code = response.body().string();
            }
            else {
                throw new IOException("Failed to generate code: " + response.errorBody().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;

    }
}
