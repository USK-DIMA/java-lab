package ru.uskov.dmitry;


import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class JsonToXmlService {

    private static final Logger log = Logger.getLogger(JsonToXmlService.class);

    private static final Semaphore semaphore = new Semaphore(Property.SEMAPHORE_COUNT);

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
        try {
            semaphore.acquire();
            log.info("Waiting XmlToJson "+semaphore.getQueueLength()+ "threads");
            pause(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        //TODO реализовать тело метода
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test><user>admin</user><password>12345</password></test>";
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

