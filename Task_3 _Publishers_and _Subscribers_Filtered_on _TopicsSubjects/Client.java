import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private static boolean isPublisher = false;
    private static String topic = "";

    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Client connected to " + host + ":" + port);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            String inputLine;
            if (isPublisher) {
                out.println("publisher\n" + topic);
                while ((inputLine = stdIn.readLine()) != null) {
                    out.println(inputLine);
                }
            } else {
                out.println("subscriber\n" + topic);
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Client failed to connect");
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            if (clientSocket != null)
                clientSocket.close();
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (stdIn != null)
                stdIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Client <host> <port> <client-type>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        switch (args[2].toLowerCase()) {
            case "publisher":
                isPublisher = true;
                break;
            case "subscriber":
                isPublisher = false;
                break;
            default:
                System.out.println("Usage: java Client <host> <port> <client-type>");
                System.exit(1);
                return;
        }

        topic = args[3].toLowerCase();

        Client client = new Client();
        client.start(host, port);
    }
}