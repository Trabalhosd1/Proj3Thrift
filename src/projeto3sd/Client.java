/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto3sd;

import java.nio.ByteBuffer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author fabio
 */
public class Client {

    private int porta;

    public Client() {

    }

    public static void main(String[] args) {
        try {
            TTransport transport;
            transport = new TSocket("localhost", Integer.valueOf(args[0]));
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FSService.Client client = new FSService.Client(protocol);

//            String metodo = null;
//            String path = null;
//            ByteBuffer dados = null;
//            int versao = -1;
            Requisicao req = new Requisicao("GET");
            //adicionar uma nova instância para cada método selecionado

            perform(client, req);
            transport.close();

        } catch (TException x) {
            x.printStackTrace();
        }

    }

    public static TPage perform(FSService.Client client, Requisicao req) throws org.apache.thrift.TException {

        switch (req.metodo) {
            case "GET":
                client.GetFile(req.path);
                break;
            case "LIST":
                client.ListFiles(req.path);
                break;
            case "ADD":
                client.AddFile(req.path, req.dados);
                break;
            case "UPDATE":
                client.UpdateFile(req.path, req.dados);
                break;
            case "UPDATE+":
                client.UpdateVersion(req.path, req.dados, req.versao);
                break;
            case "DELETE":
                client.GetFile(req.path);
                break;
            case "DELETE+":
                client.DeleteVersion(req.path, req.versao);
                break;
        }

        return null;
    }

}
