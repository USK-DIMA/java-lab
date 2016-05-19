package ru.uskov.dmitry;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



public class JsonToXmlServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JsonToXmlServlet.class);

    JsonToXmlService service = new JsonToXmlService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("Strated method doGet");
        // Set the response message's MIME type.
        response.setContentType("text/html;charset=UTF-8");
        // Allocate a output writer to write the response message into the network socket.
        try(PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");  // HTML 5
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            String title = "Title";
            out.println("<title>" + title + "</title></head>");
            out.println("<body>");
            out.println("<h1>" + title + "</h1>");  // Prints "Hello, world!"
            // Set a hyperlink image to refresh this page
            out.println("</body></html>");
        }
    }


    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("Strated method doPost. Request: "+req);
        JSONObject json = service.getJsonByRequest(req);
        log.info("Json: "+json.toString());
        String xml = service.getXmlByJson(json);
        res.setContentType("text/html");
        res.getWriter().print(xml);
    }


}
