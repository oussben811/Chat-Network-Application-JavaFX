package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainController {
    @FXML
    private Label chatNameLabel;
    @FXML
    private Label clientNameLabel;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;
    @FXML
    private Label dateTimeLabel; 
    private Timeline timeline; 
    @FXML
    private ListView<String> connectedUsersListView;
    @FXML
    private Button refreshButton;
    @FXML
    private Button disconnectButton;
    private ChatService chatService; 
    public void initialize() {
        setCurrentDateTime();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> setCurrentDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void setChatName(String chatName) {
        chatNameLabel.setText(chatName);
    }
    public void setClientName(String clientName) {
        clientNameLabel.setText(clientName);
    }
    public TextArea getChatArea() {
        return chatArea;
    }
    public TextField getMessageField() {
        return messageField;
    }
    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        dateTimeLabel.setText(dateTime);
    }
    public void updateConnectedUsers(List<String> connectedUsers) {
        connectedUsersListView.getItems().setAll(connectedUsers);
    }
    @FXML
    private void refreshClients() {
        try {
            List<String> connectedUsers = chatService.getConnectedUsers();
            updateConnectedUsers(connectedUsers);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }
}
