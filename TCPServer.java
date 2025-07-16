import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static final List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Get Port number from env
        String portEnv = System.getenv("PORT");
        int port = portEnv != null ? Integer.parseInt(portEnv) : 10000;

        ServerSocket serverSocket = new ServerSocket(port, 20, InetAddress.getByName("0.0.0.0"));
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket);

            // Create thread for client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            clients.add(out);

            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received message: " + message);
                        broadcast(message, out);
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected: " + e);
                    clients.remove(out);
                }
            }).start();
        }
    }

    private static void broadcast(String message, PrintWriter source) {
        System.out.println("Broadcasting message: " + message);
        for (PrintWriter client : clients) {
            if (client != source) {
                client.print(message);
                System.out.println("Sent message to " + client);
            }
        }
    }
}