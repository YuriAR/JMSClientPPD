package sample;

/**
 * Created by yurireis on 03/03/17.
 */
public class MessagingThread extends Thread{

    String userName;
    Manager manager;
    UICallback callback;

    public MessagingThread(String userName, Manager manager, UICallback callback){
        this.userName = userName;
        this.callback = callback;
        this.manager = manager;
    }

    public void run(){

        try {

            for(;;){
                callback.updateChat(manager.getMessageFromQueue(userName));
                Thread.sleep(3000);
            }

        } catch(InterruptedException e) {
            System.out.println("sleep interrupted");
        }
    }
}