package projeto3sd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class FileSystem {
    
    
    public static Map arquivos;
    
    public FileSystem(){
        this.arquivos = new HashMap();
    }
    
    
    public void AddArquivo(Page p, String path){
        
        arquivos.put(Math.abs(path.hashCode()), p);
        
    }
    
    public Page GetArquivo(String path){
        return (Page) arquivos.get( Math.abs(path.hashCode()) );
        
        
    }
    
    public void DeletaArquivo(String path){
        if( arquivos.get( Math.abs(path.hashCode()) ) != null ) arquivos.remove( Math.abs(path.hashCode()) );
    }
    
    public ArrayList<TPage> ListSubFiles(String path){
        
        ArrayList<TPage> retorno = new ArrayList<>();
        
        for(Object key : arquivos.keySet()){
            if(key==Integer.valueOf(path.hashCode())) retorno.add((Page) arquivos.get(key));
        }
        return retorno;
    }
    
    
}
