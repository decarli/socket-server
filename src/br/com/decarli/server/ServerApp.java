package br.com.decarli.server;

import br.com.decarli.Mensagem;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.windows.ThemeReader;

public class ServerApp {

    public static void main(String[] args) {
        new ServerApp().start();
    }

    public void start() {
        System.out.println("Iniciando Servidor...");
        try {
            ServerSocket servidor = new ServerSocket(1234);


            while (true) {
                System.out.println("Aguardando conexão...");
                Socket socket = servidor.accept();

                new Thread(new SocketServer(socket)).start();

            }
        } catch (Exception ex) {
            System.out.println("Porta já está sendo utilizada!");
        }
    }

    class SocketServer implements Runnable {

        Socket socket;

        public SocketServer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            
            
            InputStream entrada;
            BufferedReader read;

            OutputStream saida;
            PrintStream ps;
            System.out.println("Cliente Conectado: " + socket.getRemoteSocketAddress());
            try {

                System.out.println("Thread: "+this);
                //objetos para entrada dos dados
                entrada = socket.getInputStream();
                read = new BufferedReader(new InputStreamReader(entrada));

                String msgDoCliente = read.readLine();
                System.out.println("TIPO: " + msgDoCliente);

                //objetos para seída dos dados
                saida = socket.getOutputStream();
                ps = new PrintStream(saida);

                if ("DATA_HORA".equals(msgDoCliente)) {
                    ps.println(new Date().toString());
                }

                msgDoCliente = read.readLine();
                System.out.println("TIPO: " + msgDoCliente);
                if ("MSG".equals(msgDoCliente)) {
                    String json = read.readLine();
                    System.out.println("JSON: " + json);

                    Gson gson = new Gson();

                    Mensagem ms = gson.fromJson(json, Mensagem.class);
                    System.out.println("Destinatario: " + ms.getDestino());

                    ps.println("Ok");
                }


                System.out.println("Servidor Finalizado!");

                //fecha conexões
                read.close();
                entrada.close();
                socket.close();
            } catch (Exception ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
