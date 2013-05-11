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

public class ServerApp {

    public static void main(String[] args) {
        System.out.println("Iniciando Servidor...");
        try {
            ServerSocket servidor = new ServerSocket(1234);
            InputStream entrada;
            BufferedReader read;

            OutputStream saida;
            PrintStream ps;
            
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket socket = servidor.accept();

                System.out.println("Cliente Conectado: " + socket.getRemoteSocketAddress());

                //objetos para entrada dos dados
                entrada = socket.getInputStream();
                read = new BufferedReader( new InputStreamReader(entrada));

                String msgDoCliente = read.readLine();
                System.out.println("TIPO: "+msgDoCliente);
                
                //objetos para seída dos dados
                saida = socket.getOutputStream();
                ps = new PrintStream(saida);
                
                if("DATA_HORA".equals(msgDoCliente)){
                    ps.println(new Date().toString());
                }
                
         
                System.out.println("Servidor Finalizado!");
                
                //fecha conexões
                read.close();
                entrada.close();
                socket.close();

            }
        } catch (IOException ex) {
            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
