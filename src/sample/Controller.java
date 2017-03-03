package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable, UICallback{

    @FXML
    ListView listOnlineUsers;
    @FXML
    TextField msgField;
    @FXML
    TextArea chat;
    @FXML
    Button buttonSend;
    @FXML
    Button buttonRefreshOnline;

    Manager manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        manager = new Manager(this);

        chat.setEditable(false);
        chat.setMouseTransparent(true);
        chat.setFocusTraversable(false);

        createUsserNameDialog();

        refreshOnlineUsers();

        buttonSend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!msgField.getText().isEmpty()){
                    manager.putMessageInQueue(listOnlineUsers.getSelectionModel().getSelectedItem().toString(),msgField.getText());
                }
                msgField.clear();
            }
        });

        msgField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!msgField.getText().equals("")){
                    manager.putMessageInQueue(listOnlineUsers.getSelectionModel().getSelectedItem().toString(),msgField.getText());
                }
                msgField.clear();
            }
        });

        buttonRefreshOnline.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                refreshOnlineUsers();
            }
        });

    }


    public void createUsserNameDialog(){
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nome de usuário");
        dialog.setHeaderText("Digite seu nome de usuário");

        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(manager.queueExists(newValue));
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return username.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            if(manager.createUser(username.getText())){
                MessagingThread messagingThread = new MessagingThread(username.getText(),manager,this);
                messagingThread.start();
            }
        });
    }

    @Override
    public void refreshOnlineUsers() {
        ObservableList<String> itemsQue = FXCollections.observableArrayList (
                manager.listUsersOnline());
        listOnlineUsers.setItems(itemsQue);
    }

    @Override
    public void updateChat(String newMsg) {
        if (newMsg != null){
            chat.appendText(">>" + newMsg + "\n");
        }
    }
}
