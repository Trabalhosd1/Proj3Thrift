package projeto3sd;

import java.util.ArrayList;

public class FileSystem {
    
    
    public static ArrayList<Page> arquivos;
    
    public FileSystem(){
        this.arquivos = new ArrayList<>();
    }
    
    
    public void AddArquivo(Page p, String path){
        arquivos.add( Math.abs(path.hashCode()) , p);
    }
    
    public Page GetArquivo(String path){
        return arquivos.get( Math.abs(path.hashCode()) );
    }
    
    
}
