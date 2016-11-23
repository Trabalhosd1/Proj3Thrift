package projeto3sd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

class Server {

    /**
     * @param args the command line arguments
     */
    
    public static ServiceHandler handler; // terá que ser o nosso file system handler
    public static FSService.Processor processor;
    
    public static void main(String[] args) throws IOException {
        //abre o arquivo de servidores
        File in = new File("Servidores.txt");
        BufferedReader bf = new BufferedReader(new FileReader(in));
        
        //reconhece o número de servidores e os endereços
        int nroServ = Integer.parseInt(bf.readLine());
        int[] servers = new int[nroServ];
     
        for(int cont = 0; cont < nroServ; cont++){
            servers[cont] = Integer.parseInt(bf.readLine());
        }

        //identifica a instância de servidor
        int idServ = Integer.parseInt(args[0]);
        
        FileSystem fs = new FileSystem();
        
        try {
            handler = new ServiceHandler(fs, servers, nroServ, new Requisicao());
            processor = new FSService.Processor(handler);
        
            TServerTransport serverTransport = new TServerSocket(servers[idServ]);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            System.out.println("Starting the simple server...");
            server.serve();
        
        }catch(Exception x){
            x.printStackTrace();
        }
       
    }
}