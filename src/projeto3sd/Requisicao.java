package projeto3sd;

import java.nio.ByteBuffer;

public class Requisicao {

    String metodo = null;
    String path = null;
    int versao = -1;
    ByteBuffer dados = null;
    
    public Requisicao(){
    }
    
    public Requisicao(String metodo){
        this.metodo = metodo;
    }
    
    public Requisicao(String metodo, String path){
        this.metodo = metodo;
        this.path = path;
    }
    
    public Requisicao(String metodo, String path, ByteBuffer dados){
        this.metodo = metodo;
        this.path = path;
        this.dados = dados;
    }
    
    public Requisicao(String metodo, String path, ByteBuffer dados, int versao){
        this.metodo = metodo;
        this.path = path;
        this.dados = dados;
        this.versao = versao;
    }
    
    public Requisicao(String metodo, String path, int versao){
        this.metodo = metodo;
        this.path = path;
        this.versao = versao;
    }
           
}
