package sample;

import br.ifce.jmsservice.JMSManagerService;
import br.ifce.jmsservice.JMSServices;

import java.util.List;

/**
 * Created by yurireis on 03/03/17.
 */
public class Manager {
    public JMSServices jmsservice;
    public UICallback callback;
    public String username;

    public Manager(UICallback callback){
        this.callback = callback;
        br.ifce.jmsservice.JMSManagerService service = new
                JMSManagerService();
        jmsservice = service.getJMSManagerPort();
    }

    public boolean createUser(String userName){
        return jmsservice.createUser(userName);
    }

    public boolean queueExists(String queueName){
        return jmsservice.queueExists(queueName);
    }

    public String getMessageFromQueue(String queueName){
        if (queueName != null){
            if (jmsservice.messagesInQueue(queueName) > 0){
                return jmsservice.getMessageFromQueue(queueName);
            }
        }
        return null;
    }

    public void putMessageInQueue(String queueName, String msg){
        jmsservice.putMessageInQueue(queueName,username + "-" + msg);
    }

    public List<String> listUsersOnline(){
        return jmsservice.listQueues();
    }
}
