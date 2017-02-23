package FileStorage;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Orozco on 2/21/17.
 */
public interface FileStorage {
    void save(InputStream inputStream, String key);
    void save(File file, String key);
    InputStream get(String key);
    void delete(String key);
}