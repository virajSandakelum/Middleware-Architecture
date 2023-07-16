import java.io.BufferedReader; //BufferedReader reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
import java.io.InputStreamReader; //An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
import java.io.PrintWriter; //PrintWriter prints formatted representations of objects to a text-output stream.
import java.net.Socket; //This class implements client sockets (also called just "sockets"). A socket is an endpoint for communication between two machines.

public class Client {

    private Socket clientSocket; 
    private PrintWriter out; //out is a PrintWriter object which is used to write the output to the socket
    private BufferedReader in; //in is a BufferedReader object which is used to read the input from the socket
    private BufferedReader stdIn; //stdIn is a BufferedReader object which is used to read the input from the console
    private static boolean isPublisher = false; //isPublisher is a boolean variable which is used to check whether the client is a publisher or a subscriber
    private static String topic = ""; //topic is the subject of the message

    public void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port); //Creates a stream socket and connects it to the specified port number on the named host.
            System.out.println("Client connected to " + host + ":" + port); //Prints the message on the console

            out = new PrintWriter(clientSocket.getOutputStream(), true); //Creates a new PrintWriter, without automatic line flushing, from an existing OutputStream.
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates a buffering character-input stream that uses a default-sized input buffer.
            stdIn = new BufferedReader(new InputStreamReader(System.in)); //Creates a buffering character-input stream that uses a default-sized input buffer.

            String inputLine; 
            if (isPublisher) {   //if the client is a publisher
                out.println("publisher\n" + topic); //Prints the message on the socket
                while ((inputLine = stdIn.readLine()) != null) { //Reads the input from the console
                    out.println(inputLine); //Prints the message on the socket
                }
            } else {
                out.println("subscriber\n" + topic); //Prints the message on the socket
                while ((inputLine = in.readLine()) != null) { //Reads the input from the socket
                    System.out.println(inputLine);  //Prints the message on the console
                }
            }
        } catch (Exception e) { //catches the exception
            e.printStackTrace(); //prints the stack trace
            System.out.println("Client failed to connect");  //Prints the message on the console
        } finally { //finally block is executed whether an exception is handled or not
            stop();  
        }
    }

    public void stop() {
        try {
            if (clientSocket != null) //if the client socket is not null
                clientSocket.close(); //closes the client socket
            if (out != null)  //if the PrintWriter object is not null
                out.close();  //closes the PrintWriter object
            if (in != null)  //if the BufferedReader object is not null
                in.close();  //closes the BufferedReader object
            if (stdIn != null)  //if the BufferedReader object is not null
                stdIn.close();  //closes the BufferedReader object
        } catch (Exception e) { //catches the exception
            e.printStackTrace(); //prints the stack trace
        }
    }

    public static void main(String[] args) {  //main method
        if (args.length != 4) {  //if the number of arguments is not equal to 4
            System.out.println("Usage: java Client <host> <port> <client-type>");
            System.exit(1);  //exits the program
        }

        String host = args[0];  //host is the IP address of the server
        int port = Integer.parseInt(args[1]);  //port is the port number of the server

        switch (args[2].toLowerCase()) {  //switch case
            case "publisher":    //if the client is a publisher
                isPublisher = true;
                break;
            case "subscriber":   //if the client is a subscriber
                isPublisher = false;
                break;
            default:  //if the client is neither a publisher nor a subscriber
                System.out.println("Usage: java Client <host> <port> <client-type>");
                System.exit(1);  //exits the program
                return;
        }

        topic = args[3].toLowerCase();  //topic is the subject of the message

        Client client = new Client();  //creates a new Client object
        client.start(host, port);   //calls the start method
    }
}