package ru.uskov.dmitry;


import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class JsonToXmlService {

    private static final Logger log = Logger.getLogger(JsonToXmlService.class);

    private static final Semaphore semaphore = new Semaphore(Property.SEMAPHORE_COUNT);

    public JSONObject getJsonByRequest(HttpServletRequest request) throws JSONException {
        Map<String,String[]> params = request.getParameterMap();
        String det = ((String[])params.get("details"))[0];
        JSONObject jsonObj = new JSONObject(det);
        return jsonObj;
    }

    public String getXmlByJson(JSONObject json) {
        try {
            semaphore.acquire();
            log.info("Waiting XmlToJson "+semaphore.getQueueLength()+ " threads");
            pause(3000);
        } catch (InterruptedException e) {
            log.error("InterruptedException: "+e);
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        String xml="<root><json>"+XML.toString(json)+"</json></root>";
        log.info("convert json to xml: "+json +" => "+xml);
        return xml;
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

    public String sign(String xml) {
        Document doc = convertStringToDocument(xml);
        Element docElemnt=doc.getDocumentElement();
        docElemnt.appendChild(createInfoElement(doc));
        return convertDocumentToString(doc);
    }

    private Node createInfoElement(Document doc) {
        Element info = doc.createElement("info");
        Element date = doc.createElement("date");
        Text textDate = doc.createTextNode(new Date().toString());
        date.appendChild(textDate);
        info.appendChild(date);
        return info;
    }

    private String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void follow(JSONObject json) {

    }
}

