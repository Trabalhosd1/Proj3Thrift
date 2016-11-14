package projeto3sd;


import java.nio.ByteBuffer;
import java.util.List;
import org.apache.thrift.TException;


/**
 *
 * @author fabio
 */
public class ServiceHandler implements FSService.Iface {

    @Override
    public TPage GetFile(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPage> ListFiles(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean AddFile(String path, ByteBuffer data) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TPage UpdateFile(String path, ByteBuffer data) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TPage DeleteFile(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TPage UpdateVersion(String path, ByteBuffer data, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TPage DeleteVersion(String path, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
