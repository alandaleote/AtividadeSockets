import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static final int PORT = 6789;

    private ServerSocket serverSocket;

    private final List<ClientSocket> clientSocketList;

   public Server() {
       clientSocketList = new LinkedList<>();
   }

    private void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println(
                "Servidor iniciado " + serverSocket.getInetAddress().getHostAddress() + " : " + PORT);

        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        try {
            while (true) {
                System.out.println("Aguardando conexão...");
                final ClientSocket clientSocket;
                try {
                    clientSocket = new ClientSocket(serverSocket.accept());
                    System.out.println("Cliente " + clientSocket.getRemoteSocketAddress() + " conectado");
                } catch (SocketException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
                try {
                    new Thread(() -> clientMessageLoop(clientSocket)).start();
                    clientSocketList.add(clientSocket);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.getMessage());
                    clientSocket.close();
                }
            }
        } finally {
            stop();
        }
    }

    private void stop() {
        try {
            System.out.println("Finalizando servidor");
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void clientMessageLoop(final  ClientSocket clientSocket) {
       try {
           String msg;
           while((msg = clientSocket.getMessage()) != null){
               System.out.println(clientSocket.getRemoteSocketAddress() +": " + msg);

//               como fechar a conversa
               if("sair".equalsIgnoreCase(msg)){
                   return;
               }

               sendMsgToAll(clientSocket, msg);
           }
       } finally {
           clientSocket.close();
       }

    }

    private void sendMsgToAll(final ClientSocket sender, String msg) {
        for()
    }


} catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void sendToAll(String msg) {
        for(PrintWriter e: chatClients) {
            e.println(msg);
            e.flush();
        }
    }

    private class ListenClient implements Runnable {
        private Scanner reader;
        public ListenClient(Socket client) {
            try {
                reader = new Scanner(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String text;
            while ((text = reader.nextLine()) != null) {
                System.out.println(text);
                Server.this.sendToAll(text);
            }
        }
    }

    public static void main(String args[]) {
        new Server();
    }
}


