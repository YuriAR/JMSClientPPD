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
        return jmsservice.getMessageFromQueue(queueName);
    }

    public void putMessageInQueue(String queueName, String msg){
        jmsservice.putMessageInQueue(queueName,msg);
    }

    public List<String> listUsersOnline(){
        return jmsservice.listQueues();
//        for(String queue:jmsservice.listQueues()){
//
//        }
    }
}
