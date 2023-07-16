import java.io.BufferedReader; // reading of text from a character input stream
import java.io.InputStreamReader; // bridge between byte streams and character streams
import java.io.PrintWriter; // put writing data into output stream
import java.net.ServerSocket; // make the server side for listing to the client connection
import java.net.Socket;     // connect the client-side socket, connect with the server
import java.util.ArrayList;
import java.util.List;    

public class Server {
    private int clientCount = 0;  // number of clients in the connection
    private ServerSocket serverSocket; // server socket will listen for incoming client connections
    private List<ClientHandler> clients = new ArrayList<>(); // store the multiple clients

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);  // handle the communication with client, perform on the client's socket
            System.out.println("Server started on port " + port); 
        } catch (Exception error) {
            System.out.println("Server failed to start on port " + port);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // accept incoming client connection and establish a socket connection with clients
                clientCount++;
                ClientHandler client = new ClientHandler(clientSocket, clientCount);
                clients.add(client);

                Thread thread = new Thread(client); // created client, running in Parallel manner
                thread.start();
            } catch (Exception error) {
                System.out.println("Client failed to connect");
            }
        }
    }

    private class ClientHandler implements Runnable {
        private int id;
        private Socket clientSocket;    
        private PrintWriter out;
        private BufferedReader in; 
        private boolean isPublisher = false; // determine the client is a publisher or subscriber
        private String topic; // store the topic

        public ClientHandler(Socket clientSocket, int id) { // ClientHandler constructor(initiate the client details)
            this.clientSocket = clientSocket;
            this.id = id;
            System.out.println("Client" + id + " connected");
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true); // associated with the client's output stream(send the data to the client over the socket connection)
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // associated with the client's input stream(read the data to the client over the socket connection)

                String inputLine = in.readLine();
                System.out.println("Client" + id + " is a " + inputLine.toLowerCase()); 
                isPublisher = inputLine.equalsIgnoreCase("PUBLISHER"); // determine the client is a publisher or subscriber

                topic = in.readLine(); // get the toipc from the client

                while ((inputLine = in.readLine()) != null) {
                    String message = "Publisher " + id + " sent: " + inputLine; 
                    System.out.println(message+" |=> topic : "+topic); // print the message and topic

                    if (isPublisher) { // if the client is a publisher then send the message to all the subscribers
                        for (ClientHandler client : clients) {
                            if (!client.isPublisher && client.topic.equalsIgnoreCase(topic)) { 
                                client.sendMessage(message);  // send the message to the subscriber
                            }
                        }
                    }
                }
            } catch (Exception error) {
                error.printStackTrace(); 
            } finally {
                terminalConnection(); // close the connection
            }
        }

        public void sendMessage(String message) {
            out.println(message); // send the message to the client
        }

        public void terminalConnection() {  // close the connection
            try {
                if (in != null)    
                    in.close(); 
                if (out != null)
                    out.close();
                if (clientSocket != null)
                    clientSocket.close(); // close the connection

                clients.remove(this); // remove the client from the list
                System.out.println("Client" + id + "disconnected");
            } catch (Exception error) { 
                system.out.println("Client" + id + "failed to disconnect");
            }
        }
    }

    public static void main(String[] args) { 
        if(args.length != 1){
            System.out.println("Usage: java Server <prot>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        Server server = new Server();   // create the server
        server.start(port); // start the server
    }

}
