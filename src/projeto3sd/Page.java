/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto3sd;

import java.util.Date;

/**
 *
 * @author Dri
 */
public class Page {
    private long creation;
    private long modification;
    private int version = -1;
    private byte[] data;
    
    Page(long creation, long modification, byte[] data){
        this.creation = creation;
        this.modification = modification;
        this.data = data;
    }

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public long getModification() {
        return modification;
    }

    public void setModification(long modification) {
        this.modification = modification;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    
    
    
}
