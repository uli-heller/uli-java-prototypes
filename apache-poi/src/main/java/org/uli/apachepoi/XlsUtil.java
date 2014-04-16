/**
 * 
 */
package org.uli.apachepoi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * @author uli
 *
 */
public class XlsUtil {
    static class Holder {
        static XlsUtil instance = new XlsUtil();
    }
    
    static public XlsUtil getInstance() {
        return Holder.instance;
    }
    
    public HSSFWorkbook getWorkbook() {
        return new HSSFWorkbook();
    }
    
    public void write(HSSFWorkbook wb, String filename) throws IOException {
        OutputStream os = new FileOutputStream(filename);
        wb.write(os);
        os.close();
    }
}
