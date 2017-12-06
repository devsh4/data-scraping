

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class dynamic_message {
		
	
	public static AndroidDriver driver;
	private static	WebDriverWait wait;
	public	static	String	msg	=	"Test";
	
	@BeforeTest
	public static void setUp(){
		
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformname", "Android");
		cap.setCapability("deviceName", "ZY22283HSQ"); 
		cap.setCapability("platformVersion", "6.0.1");
		cap.setCapability("appPackage", "com.whatsapp");
		cap.setCapability("appActivity", "com.whatsapp.HomeActivity");
		cap.setCapability("newCommandTimeout", false);
		
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		wait = new WebDriverWait(driver, 180);
	}
	
	
	@Test(dataProvider="number")
	public void searchAndSend(String number, String text){
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/menuitem_search")));
		
		//Click on search
		driver.findElement(By.id("com.whatsapp:id/menuitem_search")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/search_src_text")));
		
		//Search number
		driver.findElement(By.id("com.whatsapp:id/search_src_text")).sendKeys(number);
		
		if(driver.findElements(By.id("com.whatsapp:id/search_no_matches")).isEmpty()){
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/contact_row_container")));
		
			//Click on first row
			List<MobileElement>	l	=	driver.findElements(By.id("com.whatsapp:id/contact_row_container"));
			l.get(0).click();
			
			//Type message			
			driver.findElement(By.id("com.whatsapp:id/entry")).sendKeys(text);

			System.out.println("Test");
			
			//Wait and Send
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/send")));
			driver.findElement(By.id("com.whatsapp:id/send")).click();
			
			//COPY PASTE
			MobileElement e = (MobileElement) driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Good')]"));
			
			//Long Press
			TouchAction action = new TouchAction(driver);
			action.longPress(e).release().perform();
			
			//Click on copy
			driver.findElement(By.xpath("//android.widget.ImageButton[1]")).click();
			
			//Paste
			MobileElement e1 = (MobileElement) driver.findElement(By.id("com.whatsapp:id/entry"));
			action.longPress(e1).release().perform();
			
			//Click on paste
			driver.findElement(By.xpath ("//android.view.ViewGroup[@bounds='[0,2300][1440,2454]']"));
			
			//Send
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/send")));
			driver.findElement(By.id("com.whatsapp:id/send")).click();
			
			//Go back
			driver.findElement(By.className("android.widget.ImageView")).click();
			
		}
		else{
			System.out.println("No matches in contacts for : "+number);
			
			//Go back
			driver.findElement(By.id("com.whatsapp:id/search_back")).click();
		}
			
	}
	
	
	@DataProvider(name="number")
	public Object[][] data(){
		
		ExcelDataConfig data = new ExcelDataConfig("./Dependencies/Data.xlsx");
		int rows=data.getRowCount("Sheet1");
		
		Object arr[][]= new Object[rows][2];
		
			for(int i=0;i<rows;i++)
				for(int	j=0;j<=1;j++)
				{
					{
						arr[i][j]=data.getData("Sheet1", i, j);
					}
				}

			return arr;	
			
	}
	
	
}
