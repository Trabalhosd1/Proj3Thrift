/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto3sd;


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
    
    public Client(){
    
    }
    
    
    
    public static void main(String[] args){
        try{
                TTransport transport;
                transport = new TSocket("localhost", Integer.valueOf(args[0]));
                transport.open();
                TProtocol protocol = new  TBinaryProtocol(transport);
                FSService.Client client =   new FSService.Client(protocol);
                GetFile(client);
                transport.close();


            }catch (TException x) {
                x.printStackTrace();
            }
    
    
    
    
    }
    
    
    public static TPage GetFile(FSService.Client client) throws org.apache.thrift.TException{
    
        
        
        return null;
    }
    
    
}
