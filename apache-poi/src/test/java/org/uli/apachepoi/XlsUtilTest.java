/**
 * 
 */
package org.uli.apachepoi;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;


/**
 * @author uli
 *
 */
public class XlsUtilTest {
    @Test
    public void testWidth() throws IOException {
        XlsUtil xu = XlsUtil.getInstance();
        HSSFWorkbook wb = xu.getWorkbook();
        HSSFSheet sheet = wb.createSheet("uli");
        Row row = sheet.createRow(0);
        row.setHeight((short) 500);
        sheet.setColumnWidth(3, 300);
        Cell cell = row.createCell(3);
        xu.write(wb,  "uli.xls");
    }
}
