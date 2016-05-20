package ru.uskov.dmitry.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ru.uskov.dmitry.Property;
import ru.uskov.dmitry.model.RequestInfo;
import ru.uskov.dmitry.view.MainForm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
            RequestInfo requestInfo = new RequestInfo("Thread "+ (i+1), s);
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

            HttpResponse response = httpClient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Анализирует состояние потоков и возвращает массив строк, которые и выведуться на экран.
     * Анализируюя ответы, определяет, все ли запросы выполнены, и в случае, если они выполнены все, прекращает обновление
     * @param responses
     * @return
     */


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
