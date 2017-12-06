import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.util.concurrent.TimeUnit;

public class whatsapp_message_send_csv{
		
	
	public static AndroidDriver driver;
	private static	WebDriverWait wait;
	public	static	String	msg	=	"Test";
	public static void main(String[] args){
		setUp();
		reader();
	}
	
	
	@BeforeTest
	public static void setUp(){
		System.out.println("setup");
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
	public static void searchAndSend(String number, String text){
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/menuitem_search")));
		
		//Click on search
		driver.findElement(By.id("com.whatsapp:id/menuitem_search")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/search_src_text")));
		
		//Search number
		driver.findElement(By.id("com.whatsapp:id/search_src_text")).sendKeys(number);
		
		if(driver.findElements(By.id("com.whatsapp:id/search_no_matches")).isEmpty()){
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/contact_row_container")));
		
			//Click on first row
			List<MobileElement>	l = driver.findElements(By.id("com.whatsapp:id/contact_row_container"));
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
				l.get(0).click();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			//Type message
			driver.findElement(By.id("com.whatsapp:id/entry")).sendKeys(text);
			
			//Wait and Send
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
	public static void reader(){
		//String csvFile = "/Users/aadityajain/Documents/Plabro/Tech/BitBucket/whatsapp/cdata-olxquikr-yowsup/whatsapp-scripts/file-csv/csv-file-message.csv";
		String csvFile = "/Users/aadityajain/Downloads/real_contacts_ver1.0.csv";
		String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                //hardcoded the message because of the newline issue
                //final String message = "Super Deal\nPlot near Golf Course Road, Gurgaon, @65000 persqyd \nMarket Price atleast @75000 persqyd \nWhatsapp me or Plabro me for details. http://plabro.com/app";
                final String message = "Hello Bro,\n\nI started Plabro-Platform for Real Estate Agents 2 years back. I am messaging you personally, so I can connect with you directly. \n\nWe have come up with new feature, where we will update you on latest requirements and distress-Deals through push notifications. \n\nYou will love this feature and will have latest requirements. \n\nRequest you to update the app by going on to playstore/iOS - www.plabro.com/app \n\nAnd if you need anything specific which makes your life easy, you can ping me directly. \n\nWarm Regards \nAaditya \nFounder, Plabro";
                searchAndSend(country[0],message);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	
}
