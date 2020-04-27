package com.moonactive.automation.api;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

import static com.moonactive.automation.logger.LoggerFactory.LOG;

public class SimpleHttpClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    Headers.Builder builder = new Headers.Builder();

    public String sendGetRequest(String url, HashMap<String, String> headersMap) throws IOException {
        Headers headers = addHeaders(headersMap);
        Request request = new Request.Builder()
                .url(url).headers(headers).build();
        try (Response response = httpClient.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if(!response.isSuccessful()) {
                LOG.error("Request failed. Returning response for further analysis");
            }
            LOG.info(String.format("Response data is: =[%s]", responseBody));
            return responseBody;
        }
    }

    private Headers addHeaders(HashMap<String, String> headersMap){
        headersMap.forEach((key, value) -> builder.add(key, value));
        return builder.build();

    }

}
