import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
    private ServerSocket serverSocket; // ServerSocket object for the server's socket
    private Socket clientSocket; // Socket object for the client's socket
    private PrintWriter out; // PrintWriter object for the output stream to the client
    private BufferedReader in; // BufferedReader object for the input stream from the client

    /* Start the server on the given port */
    public void start(int port) {
        try {
            // Create a new server socket on the given port
            serverSocket = new ServerSocket(port);
            System.out.println("Server: listening on port " + port);

            // Wait for a client to connect
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Create input and output streams for the client socket
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read input from the client until "terminate" is received
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("terminate")) {
                    System.out.println("Client: terminating");
                    break;
                }
                System.out.println("Client: " + inputLine);
            }

            // Close the connection with the client
            System.out.println("Client: disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    /* Stop the server and close all connections */
    public void stop() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (clientSocket != null)
                clientSocket.close();
            if (serverSocket != null)
                serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // check if the command line arguments are valid
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }

        // get the port number from the command line arguments
        int port = Integer.parseInt(args[0]);

        // create a new server object and start the server
        Server server = new Server();
        server.start(port);
    }
}
