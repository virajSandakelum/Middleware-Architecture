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


    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Client connected to " + host + ":" + port);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            if(isPublisher){
                out.println("Publisher");
                String inputLine;
                while ((inputLine = stdIn.readLine()) != null) {
                    out.println(inputLine);
                }
            } else {
                out.println("subcriber");
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
                out.close();
            if (in != null)
                in.close();
            if (stdIn != null)
                stdIn.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }



}