package ru.uskov.dmitry;


import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class JsonToXmlService {

    private static final Logger log = Logger.getLogger(JsonToXmlService.class);

    public JSONObject getJsonByRequest(HttpServletRequest request) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        Map<String,String[]> params = request.getParameterMap();
        for (Map.Entry<String,String[]> entry : params.entrySet()) {
            String v[] = entry.getValue();
            Object o = (v.length == 1) ? v[0] : v;
            jsonObj.put(entry.getKey(), o);
        }
        return jsonObj;
    }

    public String getXmlByJson(JSONObject json) {
        final Semaphore semaphore = new Semaphore(Property.SEMAPHORE_COUNT);//// TODO: 19.05.2016 по идее, при такой реализации должны сначала три потока войти, потом остальные, но в клиенете все 5 поток возвращают результат одновременно 
        try {
            semaphore.acquire();
            pause(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        //TODO реализовать тело метода
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "    <test>\n" +
                "        <user>admin</user>\n" +
                "        <password>12345</password>\n" +
                "    </test>";
    }


    private void pause(int ms){
        try {
            log.info("Pause: "+ms+" ms");
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.warn("Error of Thread.sleep(ms)");
            e.printStackTrace();
        }
    }

}

