import java.io.BufferedReader; //BufferedReader reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
import java.io.InputStreamReader; //An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
import java.io.PrintWriter; //PrintWriter prints formatted representations of objects to a text-output stream.
import java.net.Socket; //This class implements client sockets (also called just "sockets"). A socket is an endpoint for communication between two machines.

public class Client {

    private Socket clientSocket; 
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private static boolean isPublisher = false;
    private static String topic = "";

    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port); //Creates a stream socket and connects it to the specified port number on the named host.
            System.out.println("Client connected to " + host + ":" + port); //Prints the message on the console

            out = new PrintWriter(clientSocket.getOutputStream(), true); //Creates a new PrintWriter, without automatic line flushing, from an existing OutputStream.
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates a buffering character-input stream that uses a default-sized input buffer.
            stdIn = new BufferedReader(new InputStreamReader(System.in)); //Creates a buffering character-input stream that uses a default-sized input buffer.

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