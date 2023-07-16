import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public void start(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Client: connected to " + host + ":" + port);

            out = new PrintWriter(socket.getOutputStream(), true);
            // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            String inputLine;
            while ((inputLine = stdIn.readLine()) != null) {
                out.println(inputLine);
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

    public void stop() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (stdIn != null)
                stdIn.close();
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Client <host name> <port number>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Client client = new Client();
        client.start(host, port);
    }
}