import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private Socket socket; // socket object to connect to the server
    private PrintWriter out; // output stream to send data to the server
    private BufferedReader in; // input stream to receive data from the server
    private BufferedReader stdIn; // input stream to read data from the console

    /* Method to start the client */
    public void start(String host, int port) {
        try {
            // create a new socket object to connect to the server
            socket = new Socket(host, port);
            System.out.println("Client: connected to " + host + ":" + port);

            // create a new output stream to send data to the server
            out = new PrintWriter(socket.getOutputStream(), true);
            // create a new input stream to receive data from the server
            // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // create a new input stream to read data from the console
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            String inputLine;
            // read data from the console and send it to the server
            while ((inputLine = stdIn.readLine()) != null) {
                out.println(inputLine);
                // if the input is "terminate", break the loop
                if (inputLine.equals("terminate")) {
                    System.out.println("Client: terminating");
                    break;
                }
            }

            System.out.println("Client: disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    /* Method to stop the client */
    public void stop() {
        try {
            // close the input stream to receive data from the server
            if (in != null)
                in.close();
            // close the output stream to send data to the server
            if (out != null)
                out.close();
            // close the input stream to read data from the console
            if (stdIn != null)
                stdIn.close();
            // close the socket object
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // check if the command line arguments are valid
        if (args.length != 2) {
            System.err.println("Usage: java Client <host name> <port number>");
            System.exit(1);
        }

        // get the hostname and port number from the command line arguments
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // create a new client object and start the client
        Client client = new Client();
        client.start(host, port);
    }
}