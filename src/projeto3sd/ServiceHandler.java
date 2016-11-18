package projeto3sd;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import static projeto3sd.Client.perform;

/**
 *
 * @author fabio
 */
public class ServiceHandler implements FSService.Iface {

    FileSystem fs;
    int[] servers;
    int nroServ;
    Requisicao req;

    public ServiceHandler(FileSystem fs, int[] servers, int nroServ, Requisicao req) {
        this.fs = fs;
        this.servers = servers;
        this.nroServ = nroServ;
        this.req = req;
    }

    @Override
    public TPage GetFile(String path) throws TException {
        int teste;
        TPage pagina = null;

        teste = testaServidor();

        if (teste == this.nroServ) {
            pagina = fs.GetArquivo(path);
        } else {
            pagina = this.startaServerClient(req, servers[teste]);
        }

        return pagina;
    }

    @Override
    public List<TPage> ListFiles(String path) throws TException {
        
        return fs.ListSubFiles(path);
    
    }

    @Override
    public boolean AddFile(String path, ByteBuffer data) throws TException {
        Page pagina = null;        
        pagina = new Page(Integer.valueOf(new Date().toString()), Integer.valueOf(new Date().toString()), data.array() );

        int teste;
        teste = testaServidor();
        
        try {

            if (teste == this.nroServ) {
                fs.AddArquivo(  pagina , path);
                return true;
            } else {
                pagina = this.startaServerClient(req, servers[teste]);
                return true;
            }

        } catch (Exception e) {
            System.out.println("erro !!! " + e);
            return false;
        }
    
    }

    @Override
    public TPage UpdateFile(String path, ByteBuffer data) throws TException {
        
        // verifica se o arquivo existe, se existe seta data dele, se não existe, ignora operação
        TPage pagina = null;
        if((pagina = fs.GetArquivo(path))!=null){
            pagina.setData(data.array());
        }
        return pagina;
     }

    @Override
    public TPage DeleteFile(String path) throws TException {
        fs.DeletaArquivo(path);
        return null;
    }

    @Override
    public TPage UpdateVersion(String path, ByteBuffer data, int version) throws TException {
        // verifica se o arquivo existe, se existe e versão atual menor que version, seta data dele, se não, ignora operação
        TPage pagina = null;
        if((pagina = fs.GetArquivo(path))!=null && pagina.getVersion() < version){
            pagina.setData(data.array());
        }
        return pagina;
    }

    @Override
    public TPage DeleteVersion(String path, int version) throws TException {
        
        TPage pagina = null;
        if((pagina = fs.GetArquivo(path))!=null && pagina.getVersion() < version)
            fs.DeletaArquivo(path);
        return null;
    
    }


    public int testaServidor() {
        int nroTeste = 0;

        //testa se o arquivo pertence a esse servidor, se pertencer retorna o número do próprio servidor, se não retorna o número do servidor a que o arquivo pertence
        if (1 == 1) {
            return nroTeste;
        } else {
            return nroTeste;
        }

    }

    private Page startaServerClient(Requisicao req, int server) {
        Page pagina = null;

        try {
            TTransport transport;
            transport = new TSocket("localhost", server);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FSService.Client client = new FSService.Client(protocol);
            pagina = perform(client, req);
            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
        return pagina;
    }
}


