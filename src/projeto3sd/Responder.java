package projeto3sd;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Responder implements Runnable {

    String metodo;
    String caminhoArquivo;
    String protocolo;
    Socket socket;
    TreeMap<String, Page> sa;
    byte[] dados;

    public Responder(String metodo, String caminhoArquivo, String protocolo, Socket socket, TreeMap<String, Page> sa, byte[] dados) {
        this.metodo = metodo;
        this.caminhoArquivo = caminhoArquivo;
        this.protocolo = protocolo;
        this.socket = socket;
        this.sa = sa;
        this.dados = dados;
    }

    public void respond(String metodo, String caminhoArquivo, String protocolo, Socket socket, TreeMap<String, Page> sa, byte[] dados) throws IOException {
        Page pagina;

        synchronized (this) {
            pagina = sa.getOrDefault(caminhoArquivo, null);
        }

        if (pagina == null) {
            pagina = new Page(0, 0, null);
        }

        synchronized (pagina) {
            String header = null;
            String strTemp = null;
            byte[] conteudo;
            String status = protocolo + " 200 OK\r\n";
            StringBuilder sb = new StringBuilder();

            if (metodo.equals("GET")) {
                SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
                formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date data = new Date();
                String dataFormatada = formatador.format(data) + "GMT";

                if (pagina.getCreation() != 0) {
                    conteudo = pagina.getData();

                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + dataFormatada + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content-Length: " + conteudo.length + "\r\n"
                            + "Content creation: " + pagina.getCreation() + "\r\n"
                            + "Content modification: " + pagina.getModification() + "\r\n"
                            + "Content version: " + pagina.getVersion() + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                } else {
                    status = protocolo + " 404 NotFound \r\n";
                    strTemp = "Pagina nao encontrada!";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + dataFormatada + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                    conteudo = strTemp.getBytes();

                }

                OutputStream resposta = socket.getOutputStream();
                resposta.write(header.getBytes());
                resposta.write(conteudo);
                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.equals("UPDATE")) {
                //pagina = sa.getOrDefault(caminhoArquivo, null);
                if (pagina.getCreation() != 0) {
                    pagina.setData(dados);
                    pagina.setModification(new Date().getTime());
                    pagina.setVersion(pagina.getVersion() + 1);

                    conteudo = null;
                    strTemp = "Conteudo alterado!";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content creation: " + pagina.getCreation() + "\r\n"
                            + "Content modification: " + pagina.getModification() + "\r\n"
                            + "Content version: " + pagina.getVersion() + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";
                } else {
                    status = protocolo + " 404 NotFound \r\n";
                    strTemp = "Pagina nao encontrada !";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                    conteudo = null;

                    OutputStream resposta = socket.getOutputStream();
                    resposta = socket.getOutputStream();
                    sb.append(header);
                    sb.append(strTemp);
                    resposta.write(sb.toString().getBytes());

                    System.out.println("\nResposta: \n" + header);
                    resposta.flush();
                }
            } else if (metodo.contains("UPDATE+")) {
                //pagina = sa.getOrDefault(caminhoArquivo, null);
                String versao = metodo.substring(7);

                if (pagina.getCreation() != 0 && versao.equals(Integer.toString(pagina.getVersion()))) {
                    pagina.setData(dados);
                    pagina.setModification(new Date().getTime());
                    pagina.setVersion(pagina.getVersion() + 1);

                    conteudo = null;
                    strTemp = "Conteudo alterado!";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content creation: " + pagina.getCreation() + "\r\n"
                            + "Content modification: " + pagina.getModification() + "\r\n"
                            + "Content version: " + pagina.getVersion() + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";
                } else {
                    status = protocolo + " 409 Conflict \r\n";
                    strTemp = "Pagina nao encontrada !";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                    conteudo = null;
                }

                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                sb.append(header);
                sb.append(strTemp);
                resposta.write(sb.toString().getBytes());

                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.equals("HEAD")) {
                //pagina = sa.getOrDefault(caminhoArquivo, null);
                if (pagina.getCreation() != 0) {
                    conteudo = null;
                    strTemp = "Content creation: " + pagina.getCreation() + "\r\n"
                            + "Content modification: " + pagina.getModification() + "\r\n"
                            + "Content version: " + pagina.getVersion() + "\r\n";

                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content creation: " + pagina.getCreation() + "\r\n"
                            + "Content modification: " + pagina.getModification() + "\r\n"
                            + "Content version: " + pagina.getVersion() + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                } else {
                    strTemp = "Pagina nao encontrada !";
                    status = protocolo + " 404 NotFound \r\n";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";
                }

                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                sb.append(header);
                sb.append(strTemp);
                resposta.write(sb.toString().getBytes());
                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.equals("ADD")) {
                if (pagina.getCreation() == 0) {

                    status = protocolo + " 200 OK\r\n";
                    Page p1 = new Page(new Date().getTime(), new Date().getTime(), dados);
                    p1.setVersion(p1.getVersion() + 1);
                    sa.put(caminhoArquivo, p1);

                    strTemp = "Conteudo inserido !";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content creation: " + p1.getCreation() + "\r\n"
                            + "Content modification: " + p1.getModification() + "\r\n"
                            + "Content version: " + p1.getVersion() + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";
                } else {
                    status = protocolo + " 409 Conflict!\r\n";
                    strTemp = "ERRO ! Conteudo ja existe !";
                    header = status
                            + "Location http://localhost:1010/\r\n"
                            + "Date: " + new Date() + "\r\n"
                            + "Server: MeuServidor/1.1\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";
                }

                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                sb.append(header);
                sb.append(strTemp);
                resposta.write(sb.toString().getBytes());
                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.equals("DELETE")) {
                sb = new StringBuilder();
                status = null;
                conteudo = null;
                pagina = sa.getOrDefault(caminhoArquivo, null);
                if (pagina.getCreation() != 0) {
                    sa.remove(caminhoArquivo);
                    strTemp = "Pagina removida !";
                    conteudo = null;
                    status = protocolo + " 200 OK \r\n";
                } else {
                    conteudo = null;
                    strTemp = "Pagina nao encontrada !";
                    status = protocolo + " 404 NotFound \r\n";
                }

                header = status
                        + "Location http://localhost:1010/\r\n"
                        + "Date: " + new Date() + "\r\n"
                        + "Server: MeuServidor/1.1\r\n"
                        + "Content-Type: text/html\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                sb.append(header);
                sb.append(strTemp);
                resposta.write(sb.toString().getBytes());
                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.contains("UPDATE+")) {

                sb = new StringBuilder();
                status = null;
                conteudo = null;
                pagina = sa.getOrDefault(caminhoArquivo, null);
                String versao = metodo.substring(7);

                if (pagina.getCreation() != 0 && versao.equals(Integer.toString(pagina.getVersion()))) {
                    sa.remove(caminhoArquivo);
                    strTemp = "Pagina removida !";
                    conteudo = null;
                    status = protocolo + " 200 OK \r\n";
                } else {
                    conteudo = null;
                    strTemp = "Pagina nao encontrada !";
                    status = protocolo + " 404 NotFound \r\n";
                }

                header = status
                        + "Location http://localhost:1010/\r\n"
                        + "Date: " + new Date() + "\r\n"
                        + "Server: MeuServidor/1.1\r\n"
                        + "Content-Type: text/html\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                sb.append(header);
                sb.append(strTemp);
                resposta.write(sb.toString().getBytes());
                System.out.println("\nResposta: \n" + header);
                resposta.flush();
            } else if (metodo.equals("LIST")) {
                SortedMap sm = sa.headMap(caminhoArquivo);
                Set<String> filhos = sm.keySet();

                sb = new StringBuilder();
                
                for (String i : filhos) {
                    sb.append(i);
                    sb.append("\n");
                }
                OutputStream resposta = socket.getOutputStream();
                resposta = socket.getOutputStream();
                resposta.write(sb.toString().getBytes());
                System.out.println("\nResposta: \n");
                resposta.flush();
            }
        }
    }

    @Override
    public void run() {
        try {
            this.respond(metodo, caminhoArquivo, protocolo, socket, sa, dados);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Responder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
