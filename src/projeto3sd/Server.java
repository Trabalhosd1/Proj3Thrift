package projeto3sd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Server {

    private int port;
    private int servers[];

    public Server(int port, int servers[]) {
        this.port = port;
        this.servers = servers;
    }

    public void start() throws IOException {
        TreeMap<String, Page> sa = new TreeMap<>();
        
        String s = "Pagina Raiz !";
        Page p = new Page(new Date().getTime(), new Date().getTime(), s.getBytes());
        p.setVersion(p.getVersion() + 1);
        sa.put("/", p);
        
        
        ServerSocket socketServidor = null;
        System.out.println("Servidor iniciando ...");

        try {
            System.out.println("Alocando porta ...");
            socketServidor = new ServerSocket(port);

        } catch (Exception e) {
            System.out.println("Erro!!!" + e.getMessage());
        }

        System.out.println("Servidor iniciado.");

        while (true) {
            System.out.println("Servidor aguardando requisição.");
            Socket socket = null;

            try {
                socket = socketServidor.accept();
                InetAddress infoCliente = socket.getInetAddress();
                System.out.println("Cliente" + infoCliente.getHostName() + " conectou ao servidor.");
            } catch (IOException e) {
                System.out.println("Erro de conexão ! " + e.getMessage());
                break;
            } finally {
                while (socket.getInputStream() == null) {
                    System.out.println("Aguardando requisição...");
                }

                InputStream is = socket.getInputStream();
                DataInputStream input = new DataInputStream(is);

                int temp = 0;
                temp = input.available();

                String linha = null;
                String metodo = null;
                String caminhoArquivo = null;
                String protocolo = null;
                String[] dadosReq = null;
                byte[] dados = null;
                int linhaReq = 1;
                int contador = 0;
                int tam = 0;
                boolean flag = true;

                while (true) {
                    linha = input.readLine();
                    if (linhaReq == 1) {
                        dadosReq = linha.split(" ");

                        metodo = dadosReq[0];
                        caminhoArquivo = dadosReq[1];
                        protocolo = dadosReq[2];
                    }
                    System.out.println(linhaReq + " " + linha);
                    linhaReq++;
                    if (linha.contains("Content-Length")) {
                        tam = Integer.parseInt(linha.substring(16));
                    }

                    if (tam > 1000) {
                        OutputStream resposta = socket.getOutputStream();
                        resposta.write("Arquivo maior que 1KB, tente novamente.".getBytes());
                        resposta.flush();
                        System.out.println("Arquivo maior que 1KB, tente novamente.");
                        flag = false;
                        break;
                    }

                    if (linha.isEmpty()) {
                        dados = new byte[tam];
                        for (int i = 0; i < tam && i < 1000; i++) {
                            dados[i] = input.readByte();
                        }
                        break;
                    }

                }

                if(flag){
                    Responder resposta = new Responder(metodo, caminhoArquivo, protocolo, socket, sa, dados);
                    Thread respostaTH = new Thread (resposta);                
                    respostaTH.start();
                }
               
            }
        }
    }
   
    public void thriftGetReq (){

}
    public void thriftTestReq(){
        
    }
   
}

 