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
        teste = testaServidor(servers, path);

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
        //pagina = new Page(Integer.valueOf(new Date().toString()), Integer.valueOf(new Date().toString()), data.array());
        pagina = new Page(new Date().getTime(), new Date().getTime(), data.array());
        
        int teste;
        teste = testaServidor(servers, path);

        try {

            if (teste == this.nroServ) {
                fs.AddArquivo(pagina, path);
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
        TPage pagina = null;

        int teste;
        teste = testaServidor(servers, path);

        if (teste == nroServ) {
            // verifica se o arquivo existe, se existe seta data dele, se não existe, ignora operação
            if ((pagina = fs.GetArquivo(path)) != null) {
                pagina.setData(data.array());
            }
        } else {
            pagina = this.startaServerClient(req, servers[teste]);
        }
        return pagina;
    }

    @Override
    public TPage DeleteFile(String path) throws TException {

        int teste;
        TPage pagina = null;

        teste = testaServidor(servers, path);

        if (teste == nroServ) {

            if ((pagina = fs.GetArquivo(path)) != null) {
                fs.DeletaArquivo(path);
            }
        } else {
            pagina = this.startaServerClient(req, servers[teste]);
        }
        return pagina;
    }

    @Override
    public TPage UpdateVersion(String path, ByteBuffer data, int version) throws TException {

        int teste;
        TPage pagina = null;

        teste = testaServidor(servers, path);

        if (teste == nroServ) {
            if ((pagina = fs.GetArquivo(path)) != null && pagina.getVersion() == version) {
                pagina.setData(data.array());
            }
        } else {
            pagina = this.startaServerClient(req, servers[teste]);
        }
        return pagina;
    }

    @Override
    public TPage DeleteVersion(String path, int version) throws TException {

        int teste;
        TPage pagina = null;

        teste = testaServidor(servers, path);

        if (teste == nroServ) {
            if ((pagina = fs.GetArquivo(path)) != null && pagina.getVersion() < version) {
                fs.DeletaArquivo(path);
            }
        } else {
            pagina = this.startaServerClient(req, servers[teste]);
        }
        return pagina;
    }

    public int testaServidor(int servers[], String path) {
        int nroTeste = 0;
        int quociente;

        quociente = servers.length;
        nroTeste = Math.abs(path.hashCode());
        //nroTeste = Integer.parseInt(path)%quociente;

        //testa se o arquivo pertence a esse servidor, se pertencer retorna o número do próprio servidor, se não retorna o número do servidor a que o arquivo pertence
        return nroTeste;

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
