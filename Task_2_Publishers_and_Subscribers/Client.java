import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

    private Socket clientSocket; // TO STORE THE CLIENT SOCKET TO CONNECT TO THE SERVER
    private PrintWriter reqout;  // TO STORE THE OBJECT TO SEND DATA TO THE SERVER

    private BufferedReader stdIn;   // TO STORE THE OBJECT THAT READ THE DATA FROM STANDARD INPUT

    private static boolean isPublisher = false; // TO STORE THE WHETHER THE CLIENT IS PUBLISHER OR SUBCRIBER


    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Client connected to " + host + ":" + port);

            reqout = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            // IF THE CLIENT IS PUBLISHER, GET THE INPUTS
            if(isPublisher){
                reqout.println("Publisher");
                String inputLine;
                while ((inputLine = stdIn.readLine()) != null) {
                    out.println(inputLine);
                }
            // IF THE CLIENT IS SUBCRIBER DISPLAY THE MESSAGES
            } else {
                reqout.println("subcriber");
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("Client Failed to connect");
        } finally {
            terminateConnection()
        }
    }


    // TERMINATE THE CONNECTION
    public void terminateConnection() {
        try {
            if (clientSocket != null)
                clientSocket.close();
            if (out != null)
                reqout.close();
            if (in != null)
                in.close();
            if (stdIn != null)
                stdIn.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    // MAIN FUNCTION
    public static void main(String[] args) {

        // IF THE USER INPUT IS NOT COMPLETE
        if (args.length != 3) {
            System.out.println("Usage: java Client <host> <port> <client-type>");
            System.exit(1);
        }

        // GET THE HOST ADDRESS AND PORT NUMBER
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // CHECK WHETHER THE USER IS A PUBLISHER OR A SUBCRIBER
        switch (args[2].toUpperCase()) {
            case "PUBLISHER":
                isPublisher = true;
                break;
            case "SUBSCRIBER":
                isPublisher = false;
                break;
            default:
                System.out.println("Usage: java Client <host> <port> <client-type>");
                System.exit(1);
                return;
        }

        // CREATE THE CLIENT OBJECT
        Client client = new Client();
        client.start(host, port);
    }

}