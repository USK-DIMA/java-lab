package ru.uskov.dmitry.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.uskov.dmitry.Property;
import ru.uskov.dmitry.model.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ClientService {

    private static final Logger log = Logger.getLogger(ClientService.class);

    private static final String REQUEST = "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";

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
                    return sendRequest(REQUEST);
                }
            });
            RequestInfo requestInfo = new RequestInfo("Thread "+ (i+1), s);
            responses.add(requestInfo);
        }
        return responses;
    }

    /**
     * Отправляет POST запрос
     * @param reques запрос
     * @return ответ. В случае возникновения ошибок возвращает null
     */
    private  String sendRequest(String reques) {
        log.info("Sending request: "+reques);
        //TODO реализовать time-out соединения. По истечению времени сделать запись в log и вернуть null
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost httppost = new HttpPost(Property.JSON_TO_URL);

            StringEntity params =new StringEntity("details="+reques);
            httppost.setEntity(params);

            HttpResponse response = httpClient.execute(httppost);

            String stringResponse =EntityUtils.toString(response.getEntity());

            log.info("Received response \"" +stringResponse+"\" by request \""+reques+"\"");
            return stringResponse;
        } catch (Exception e) {
            log.info("Exception in httpClient.execute: " +e);
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Анализирует состояние потоков и записывает в  массив строк результаты
     * @param responses информация о запросах
     * @param responseString массив для записи результатов
     * @return true, если все потоки окончили свою работу
     */
    public boolean getResponseString(List<RequestInfo> responses, String[] responseString) {
        log.info("START RESPONSES READING");
        boolean allResposeHaveReceived = true;

        for(int i=0; i<responses.size(); i++){
            RequestInfo requestInfo = responses.get(i);
            Future<String> future = requestInfo.getFuture();
            try {
                log.info("Start reading FUTURE in "+ requestInfo.getThreadName());
                String result;
                if (future.isDone()) {
                    result = future.get();
                    if(result==null){
                        result="";
                        requestInfo.setStatus(RequestInfo.Status.RES_ERR);
                    }
                    else {
                        requestInfo.setStatus(RequestInfo.Status.COMPLIT);
                    }
                } else {
                    result="-";
                    allResposeHaveReceived = false;
                }
                responseString[i] = requestInfo.getThreadName() + " : "+requestInfo.getStatus()+" : "+result;
                log.info("Readed in future: "+responseString[i]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        log.info("allResposeHaveReceived :" +allResposeHaveReceived);
        return allResposeHaveReceived;
    }
}
