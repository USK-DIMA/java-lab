import org.apache.log4j.Logger;
import ru.uskov.dmitry.service.ClientService;
import ru.uskov.dmitry.view.MainForm;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("==================================================");
        log.info("==============START APPLICATION===================");
        log.info("==================================================");
        ClientService clientService = new ClientService();
        new MainForm(clientService).show();
    }

}
