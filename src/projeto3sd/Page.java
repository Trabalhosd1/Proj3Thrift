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
public class Page extends TPage {
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
    public long getModification() {
        return modification;
    }

    public int getVersion() {
        return version;
    }

    public byte[] getData() {
        return data;
    }

}
