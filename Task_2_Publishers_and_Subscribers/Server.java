import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int clientCount = 0;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("Server failed to start on port " + port);
        }

        while(true){
            try{
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                ClientHandler client = new ClientHandler(clientSocket,clientCount);
                clients.add(client);

                Thread thread = new Thread(client);
                thread.start();
            }catch(Exception error){
                error.printStackTrace();
                System.out.println("Client failed to connect");
            }
        }
    }

    private class ClientHandler implements Runnable{
        private int id;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private boolean isPublisher = false;

        public ClientHandler(Socket clientSocket, int id){
            this.clientSocket = clientSocket;
            this.id = id;
            System.out.println("Client"+ id+" connected");
        }

        public void run(){
            try{
                out = new PrintWriter(clientSocket.getOutputStream(),true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine = in.readLine();
                System.out.println("Client"+id+" is a "+inputLine.toLowerCase());
                isPublisher = inputLine.equalsIgnoreCase("PUBLISHER");

                while((inputLine = in.readLine()) != null){
                    String message = "Client"+id+" sent: "+ inputLine;
                    System.out.println(message);

                    if(isPublisher){
                        for(ClientHandler client: clients){
                            if(!client.isPublisher){
                                client.sendMessage(message);
                            }
                        }
                    }
                }
            }catch(Exception error){
                error.printStackTrace();
            }finally{
                
            }
        }

        public void sendMessage(String message){
            out.println(message);
        }
    }
}


