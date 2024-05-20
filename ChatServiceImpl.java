package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private List<String> messages;
    private List<String> connectedUsers;

    protected ChatServiceImpl() throws RemoteException {
        super();
        messages = new ArrayList<>();
        connectedUsers = new ArrayList<>();
    }
    @Override
    public synchronized void sendMessage(String message, String sender) throws RemoteException {
        messages.add(sender + ": " + message);
        System.out.println(sender + " sent: " + message);
    }
    @Override
    public synchronized List<String> receiveNewMessages(int lastReceivedIndex) throws RemoteException {
        List<String> newMessages = new ArrayList<>();
        for (int i = lastReceivedIndex + 1; i < messages.size(); i++) {
            newMessages.add(messages.get(i));
        }
        return newMessages;
    }
    @Override
    public synchronized void joinChat(String userName) throws RemoteException {
        connectedUsers.add(userName);
        System.out.println(userName + " has joined the chat.");
        broadcastMessage(userName + " has joined the chat,Refresh your list :)", "Server");
    }
    @Override
    public synchronized void disconnect(String userName) throws RemoteException {
        connectedUsers.remove(userName);
        System.out.println(userName + " has disconnected.");
        broadcastMessage(userName + " has disconnected,Refresh your list :)", "Server");
    }
    private void broadcastMessage(String message, String sender) {
            try {
                sendMessage(message, sender);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }
    @Override
    public synchronized List<String> getConnectedUsers() throws RemoteException {
        return connectedUsers;
    }
}
