
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataConfig {

		// TODO Auto-generated method stub
		XSSFWorkbook wb;
		XSSFSheet sheet1;
		DataFormatter formatter = new DataFormatter();
		
		public ExcelDataConfig(String excelPath) {
				
				try {
					File src=new File(excelPath);
					
					FileInputStream fis= new FileInputStream(src);
					
					wb= new XSSFWorkbook(fis);
					
					sheet1=wb.getSheetAt(0);
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
				
			}
				
			public String getData(String sheetname,int row,int column)
			{
				//sheet1=wb.getSheet(sheetname);
				
				Cell cell	=	sheet1.getRow(row).getCell(column);
				String data = formatter.formatCellValue(cell);
				
				return data;	
			}
			
			public String getnumData(String sheetname,int row,int column)
			{
				sheet1=wb.getSheet(sheetname);
				
				String data=sheet1.getRow(row).getCell(column).getRawValue();

				return data;	
			}
			
			
			public int getRowCount(String sheetname)
			{
				 int count=wb.getSheet(sheetname).getLastRowNum();
				 count=count+1;
						 
				 return count;	
			}
			
		}


