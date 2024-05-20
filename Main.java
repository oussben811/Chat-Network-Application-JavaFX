package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class Main extends Application {
    private ChatService chatService;
    private String clientName;
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        chatService = (ChatService) Naming.lookup("rmi://localhost/chat");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setChatService(chatService);
        clientName = getClientName(primaryStage);
        controller.setClientName(clientName);
        chatService.joinChat(clientName);
        controller.getChatArea().appendText("Welcome, " + clientName + "!\n");
        controller.getMessageField().setOnAction(event -> sendMessage());
        primaryStage.setTitle("RMIChatXpress");
        primaryStage.setScene(new Scene(root, 600, 310)); // Adjust dimensions here
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> handleCloseRequest());
        Button disconnectButton = (Button) loader.getNamespace().get("disconnectButton");
        disconnectButton.setOnAction(event -> disconnect(primaryStage));
        Thread receiverThread = new Thread(this::receiveMessages);
        receiverThread.setDaemon(true); // Daemon thread to stop when the application exits
        receiverThread.start();
        updateConnectedUsers();
    }
    private void disconnect(Stage primaryStage) {
    	handleCloseRequest();
        primaryStage.close();
        Platform.exit();
    }
    private String getClientName(Stage primaryStage) throws Exception {
        String name = null;
        do {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Your Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter your name:");
            Optional<String> result = dialog.showAndWait();
            name = result.orElse(null);
            if (name != null && name.isEmpty()) {
                // Show a background message if the user entered nothing
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Name");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a name or choose Anonymous.");
                alert.showAndWait();
            } else if (name != null && chatService.getConnectedUsers().contains(name)) {
                // Check if the name is already taken
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Name Already Taken");
                alert.setHeaderText(null);
                alert.setContentText("This name is already taken. Please choose a different name.");
                alert.showAndWait();
                name = "";
            }
        } while (name != null && name.isEmpty());  // Keep prompting until a valid name is entered
        if (name == null) {
            int num = 1;
            do {
                name = "Anonymous" + num;
                num++;
            } while (chatService.getConnectedUsers().contains(name));
        }
        return name;
    }
    private void sendMessage() {
        String message = controller.getMessageField().getText().trim();
        if (!message.isEmpty()) {
            try {
                chatService.sendMessage(message, clientName);
                controller.getMessageField().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void receiveMessages() {
        try {
            int lastReceivedIndex = -1;
            while (true) {
                List<String> newMessages = chatService.receiveNewMessages(lastReceivedIndex);
                if (!newMessages.isEmpty()) {
                    lastReceivedIndex += newMessages.size();
                    for (String message : newMessages) {
                        controller.getChatArea().appendText(message + "\n");
                    }
                }
                Thread.sleep(1000); // Update interval for receiving messages
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateConnectedUsers() {
        try {
            List<String> connectedUsers = chatService.getConnectedUsers();
            controller.updateConnectedUsers(connectedUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleCloseRequest() {
        try {
            chatService.disconnect(clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
