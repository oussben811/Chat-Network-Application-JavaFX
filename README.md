# Javacast Connect: Chat Network Application in Java/JavaFX

## Overview

Javacast Connect is a real-time chat application developed using Java and JavaFX, leveraging Java RMI (Remote Method Invocation) for seamless communication. Users can create chat rooms, send secure messages, and share files effortlessly.

![image](https://github.com/oussben811/Javacast-Connect-Chat-Network-Application-in-Java-JavaFX/assets/78149349/5b53ebd4-f6cd-4764-bce3-f1a5c9af455f)

## Features

- **Create Chat Rooms:** Users can easily create and join chat rooms.
- **Real-time Messaging:** Send and receive messages in real-time.
- **Secure Communication:** Ensure privacy and data protection.
- **File Sharing:** Share files with other users.
- **User Management:** Manage connected users and handle user disconnections.

## Components

### Server (ChatServer)

The server-side application uses RMI to handle communication with clients. It creates an RMI registry, instantiates the chat service (`ChatServiceImpl`), and binds it to the name "chat" in the registry.

### Chat Service Interface (ChatService)

Defines the methods for the chat service:
- `sendMessage(String message, String sender)`: Send a message with the sender's name.
- `receiveNewMessages(int lastReceivedIndex)`: Retrieve new messages since the given index.
- `joinChat(String userName)`: Allow a user to join the chat.
- `getConnectedUsers()`: Retrieve the list of connected users.
- `disconnect(String userName)`: Allow a user to disconnect.

### Chat Service Implementation (ChatServiceImpl)

Implements the `ChatService` interface, providing the core functionalities for message handling and user management.

### Client (Main)

The client-side application initializes the chat service, loads the UI, and handles user interactions:
- Connects to the chat service via RMI.
- Loads the UI from an FXML file.
- Allows users to send and receive messages.
- Manages user connection and disconnection.

### User Interface Controller (MainController)

Controls the chat UI, updating elements based on user actions and service responses:
- Displays chat messages and user lists.
- Manages user inputs and interactions.
- Updates the UI with the current time.

## UI Layout (Main.fxml)

Defines the chat application's user interface:
- Vertical layout (VBox) with labels for chat and client names.
- TextArea for displaying messages and TextField for message input.
- ListView for displaying connected users.
- Buttons for refreshing the user list and disconnecting.
- Icons for buttons and absolute positioning for elements.

## Execution

### Server Side

To start the server, run the `ChatServer` class.

### Client Side

To start the client, run the `Main` class. Test cases include:
- Non-empty name input.
- Empty name input defaults to "Anonymous".
- Handling existing names.

## Testing

- Ensure the server is running before starting clients.
- Test with multiple clients to verify real-time updates and user management.
- Use the refresh button to update the list of connected users.

Enhance your chat experience with Javacast Connect!
