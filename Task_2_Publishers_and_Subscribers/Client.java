import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket clientSocket;
    private PrintWriter reqout;

    private BufferedReader in;
    private BufferedReader stdIn;

    private static boolean isPublisher = false;


    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Client connected to " + host + ":" + port);

            reqout = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            if(isPublisher){
                reqout.println("Publisher");
                String inputLine;
                while ((inputLine = stdIn.readLine()) != null) {
                    out.println(inputLine);
                }
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


    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <host> <port> <client-type>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

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

        Client client = new Client();
        client.start(host, port);
    }

}