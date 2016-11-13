
package projeto3sd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Projeto1SD {

    /**
     * @param args the command line arguments
     */
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
        
        
        try {
            Server servidor = new Server(servers[idServ], servers);

            servidor.start();
        }
        catch(IOException e){
            System.out.println("Erro ao iniciar o servidor ! " + e.getMessage());
        }
        finally{
            System.out.println("Programa encerrado.");
        }
        
       
    }
    
}
