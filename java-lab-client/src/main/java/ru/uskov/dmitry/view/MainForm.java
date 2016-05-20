package ru.uskov.dmitry.view;

import org.apache.log4j.Logger;
import ru.uskov.dmitry.service.ClientService;
import ru.uskov.dmitry.model.RequestInfo;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainForm {

    private static final int BORDER_SPACE = 5;
    private JFrame frame;

    private JPanel mainPanel;
    private JTextField countThreadtextField;
    private JButton sendRequestButton;
    private JList responseList;
    private JLabel statusLabel;

    private static final Logger log = Logger.getLogger(MainForm.class);

    /**
     * Служит для динамического заполнения responseList
     */
    private DefaultListModel listModel;

    /**
     * Логика запросов к серверу
     */
    private ClientService clientService;

    /**
     * Информаци об ответах от сервера
     */
    private List<RequestInfo> responses;

    /**
     * Информаци об ответах от сервера преобразованная для вывода на экран
     */
    private String[] responseString;

    /**
     * Объект, отвечающий за ассинхронное обновление списка
     */
    ScheduledExecutorService listUpdateExecutorService;

    /**
     * Блокировка для обработчика кнопки "Отправить". Блокировка стоит, пока все запросы не получат ответ
     */
    Lock sendButtonLock;


    /**
     * true, если требуется обновлять список на экране
     */
    boolean listMustUpdate;

    public MainForm(ClientService clientService) {
        this.clientService = clientService;

        frame = new JFrame("Java-lab-Client");
        frame.setContentPane(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        listModel = new DefaultListModel();
        responseList.setModel(listModel);
        sendButtonLock = new ReentrantLock();
        sendRequestButton.addMouseListener(new SendRequestsButtonLestener());
        listMustUpdate = false;
    }

    public void show() {
        frame.setVisible(true);
    }

    /**
     * Запускает опрос ответов от сервера по таймеру.
     * @param responses
     */
    private void printResponseInfo(final List<RequestInfo> responses) {
        setStatus("Исполняется");
        listUpdateExecutorService = Executors.newScheduledThreadPool(1);
        Runnable runnable = new Runnable() {
            List<RequestInfo> resp = responses;
            public void run() {
                log.info("read Resposes");
                responseString = new String[responses.size()];
                listMustUpdate = !clientService.getResponseString(resp, responseString);
                updateList(responseString);
                interruptListUpdateExecutorService(!listMustUpdate);
            }
        };
        listMustUpdate = true;
        listUpdateExecutorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);

    }

    /**
     * Отчищает responseList и записывает в него новые результаты
     * @param responseString
     */
    private void updateList(String[] responseString) {
        log.info("Update list on the Display");
        listModel = new DefaultListModel();
        log.info("clear List");
        for(int i=0; i<responseString.length; i++) {
            listModel.addElement(responseString[i]);
            log.info("Added element on list: "+responseString[i]);
        }
        responseList.setModel(listModel);
    }




    /**
     * Прерывает обновление списка ответов, если boolean interrupt = true;
     * @param interrupt
     */
    private void interruptListUpdateExecutorService(boolean interrupt) {
        if(interrupt && listUpdateExecutorService!=null){
            log.info("Interrupt listUpdaterExecutorService");
            setStatus("Готово");
            setSendButtonEnable();
            listUpdateExecutorService.shutdown();
        }
    }

    /**
     * Возвращает количетсво запросов, которое будет отправлять запросы на сервер
     * @return
     */
    private int readCountRequests() {
        String responseCount = countThreadtextField.getText();
        int count = Integer.parseInt(responseCount);
        return count;
    }


    /**
     * делает кнопку на экране не активной и ставит блокировку на обработчик этой кнопки
     */
    void setSendButtonDisable(){
        log.info("Disable button");
        sendRequestButton.setEnabled(false);
        sendButtonLock.lock();
    }

    /**
     * делает кнопку на экране активной и снимает блокировку с обработчика этой кнопки
     */
    void setSendButtonEnable(){
        log.info("Enable button");
        sendRequestButton.setEnabled(true);
        sendButtonLock.unlock();
    }

    private class SendRequestsButtonLestener implements MouseListener{

        public void mouseClicked(MouseEvent e) {
            int count;
            try {
                count = readCountRequests();
            }
            catch (Exception exep){
                log.warn("Incorrect value of thread count");
                setStatus("Некорректное число запросов");
                return;
            }
            responses = clientService.sendRequest(count);
            setSendButtonDisable();
            printResponseInfo(responses);

        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

    private void setStatus(String s) {
        statusLabel.setText(s);
    }

}
