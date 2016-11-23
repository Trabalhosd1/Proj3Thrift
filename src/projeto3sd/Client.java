/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto3sd;

import java.nio.ByteBuffer;
import java.util.Scanner;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
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
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            FSService.Client client = new FSService.Client(protocol);
            int op = 0;
            //while (op < 8) {

                System.out.println("Escolha a operação desejada: \n");
                System.out.println("1- GET\n2- List\n3- Add\n4- Update\n5- Update+Version\n6- Delete\n7- Delete+Version\n");
                Scanner in = new Scanner(System.in);
                op = in.nextInt();
                in.nextLine();
                Requisicao req = null;
                Page resp = null;

                switch (op) {
                    case 1:
                        System.out.println("Insira o caminho do arquivo: \n");
                        String path = in.nextLine();
                        req = new Requisicao("GET", path);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                    case 2:
                        System.out.println("Só lamento !\n");
                        //client.ListFiles(req.path);
                        break;
                    case 3:
                        System.out.println("Insira o caminho do arquivo: \n");
                        path = in.nextLine();
                        System.out.println("Insira os dados a serem adicionados: \n");
                        byte[] dados = in.nextLine().getBytes();
                        ByteBuffer data = ByteBuffer.wrap(dados);
                        req = new Requisicao("ADD", path, data);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                    case 4:
                        System.out.println("Insira o caminho do arquivo: \n");
                        path = in.nextLine();
                        System.out.println("Insira os dados a serem adicionados: \n");
                        data = ByteBuffer.wrap(in.nextLine().getBytes());
                        req = new Requisicao("UPDATE", path, data);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                    case 5:
                        System.out.println("Insira o caminho do arquivo: \n");
                        path = in.nextLine();
                        System.out.println("Insira os dados a serem adicionados: \n");
                        data = ByteBuffer.wrap(in.nextLine().getBytes());
                        System.out.println("Insira a versão desejada: \n");
                        int version = in.nextInt();
                        in.nextLine();
                        req = new Requisicao("UPDATE+VERSION", path, data, version);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                    case 6:
                        System.out.println("Insira o caminho do arquivo: \n");
                        path = in.nextLine();
                        req = new Requisicao("DELETE", path);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                    case 7:
                        System.out.println("Insira o caminho do arquivo: \n");
                        path = in.nextLine();
                        System.out.println("Insira a versão desejada: \n");
                        version = in.nextInt();
                        in.nextLine();
                        req = new Requisicao("DELETE+VERSION", path, version);
                        resp=perform(client, req);
                        OnPosRequisicao(resp);
                        break;
                }
                
            //}
            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        } finally {

        }
    }

    public static void OnPosRequisicao(Page resp){
        if(resp==null) System.out.println("Não é possível printar página nula");
        else resp.dump();
    }
    
    public static Page perform(FSService.Client client, Requisicao req) throws org.apache.thrift.TException {

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
