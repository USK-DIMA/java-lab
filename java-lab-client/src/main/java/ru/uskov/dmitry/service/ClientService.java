package ru.uskov.dmitry.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ru.uskov.dmitry.Property;
import ru.uskov.dmitry.model.RequestInfo;
import ru.uskov.dmitry.view.MainForm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientService {

    private static final Logger log = Logger.getLogger(ClientService.class);

    /**
     * Ассинхронно отправляет запросы на сервер
     * @param requestCount колличество ассинхронных запросов (по одному в кадом потоке)
     * @return массив, содержащий информацию о каждом запросе
     */
    public List<RequestInfo> sendRequest(int requestCount){
        ArrayList<RequestInfo> responses = new ArrayList<RequestInfo>(requestCount);

        ExecutorService ex= Executors.newCachedThreadPool();

        for(int i=0; i<requestCount; i++){

            Future<String> s= ex.submit(new Callable<String>() {
                public String call() throws Exception {
                    return sendRequest("");
                }
            });
            RequestInfo requestInfo = new RequestInfo("Thread "+ (i+1), s, false);
            responses.add(requestInfo);
        }
        return responses;
    }

    private  String sendRequest(String reques) {
        //TODO пока что запрос захордкожен и возвращающее значение захордкожено. Возвращ. знаение должно возвращаться из HttpResponse response
        try {

            JSONObject object = new JSONObject();
            object.put("user", "admin");
            object.put("password", "1234");

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(Property.JSON_TO_URL);

            StringEntity params =new StringEntity("details="+object.toString());

            httppost.setEntity(params);
            //Execute and get the response.
            HttpResponse response = httpClient.execute(httppost);

            //// TODO: 19.05.2016 Почемуто метод срабатывает очень быстро, хотя на сервере пауза аж в 10 секунд поставим здесь ещё паузу

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Good!";
    }
}
