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

    public void start(){
        clientSocket = new Socket(host, port);
        System.out.println("Client connected to "+host+":"+port);

        out = new PrintWrite(clientSocket.getOutputStream(), autoFlush:true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        if(isPublisher){
            out.println("Publisher");
            String inputLine;
            while ((inputLine = stdIn.readLine()) != null) {
                out.println(inputLine);
            }
        }

    }

}