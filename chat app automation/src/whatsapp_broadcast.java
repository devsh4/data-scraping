import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @description Whatsapp Script
 * @author Dev Shah
 */

public class whatsapp_broadcast {
	
		public static AndroidDriver<MobileElement> driver;
		private static	WebDriverWait wait;
		
		//initilaize variables
		Dimension	screenSize;
		static	int	screenWidth;
		static	int	screenHeight;
		public static	String roww;
		public static	String row1;
		public	static	String row2;
		public	static	String row_msg;
		public	static	String row1_msg;
		public	static	String row2_msg;
		public	static	String row_new;
		public	static	String row1_new;
		public	static	String row2_new;
		public	static	String row_new_msg;
		public	static	String row1_new_msg;
		public	static	String row2_new_msg;
		public static	List<MobileElement> rowList; 
		public static 	String	deviceId	=	"ZY22283HSQ";
		public static void main(String[] args){
			
			setUp();
			sendMsgToAllChats();
		}
		
		
		public static void setUp(){
			
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("platformname", "Android");
			cap.setCapability("deviceName", deviceId ); //****Changed device name from ****
			cap.setCapability("platformVersion", "6.0.1"); //****Changed android device version*****
			cap.setCapability("appPackage", "com.whatsapp");
			cap.setCapability("appActivity", "com.whatsapp.HomeActivity");
			
			try {
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			wait = new WebDriverWait(driver, 180);
		}
		
		
		public static void sendMsgToAllChats(){
	
		//try{

			//Initializing counter
			int counter = 1;
				
			//Storing all chats in a list
			rowList = driver.findElements(By.id("com.whatsapp:id/contact_row_container"));
			boolean finished = false;
			
			//loop
			while(!finished){
					
						List<MobileElement> li	=	driver.findElements(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'com.whatsapp:id/contact_row_container')]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.TextView[contains(@resource-id,'com.whatsapp:id/conversations_row_contact_name')]"));
						List<MobileElement>	msg	=	driver.findElements(By.id("com.whatsapp:id/single_msg_tv"));
						roww	=	li.get(li.size()-3).getText();
						row_msg	=	msg.get(msg.size()-3).getText();
						row1	=	li.get(li.size()-2).getText();
						row1_msg	=	msg.get(msg.size()-2).getText();
						row2	=	li.get(li.size()-1).getText();
						row2_msg	=	msg.get(msg.size()-1).getText();
						
						for (WebElement row : rowList) {
						
							//Call whatsapp code
							whatsapp_sendMsg(row);
										
							//Go back, out of the current group chat 
							driver.findElement(By.className("android.widget.ImageView")).click();
							
							//Increment counter
							counter++;
							
							if(counter == rowList.size()) 
							{
								swipeVertically();
								
								//Get name of last chat row
								wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'com.whatsapp:id/contact_row_container')]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.TextView[contains(@resource-id,'com.whatsapp:id/conversations_row_contact_name')]")));
								
								List<MobileElement> l	=	driver.findElements(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'com.whatsapp:id/contact_row_container')]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.TextView[contains(@resource-id,'com.whatsapp:id/conversations_row_contact_name')]"));
								List<MobileElement>	msg1	=	driver.findElements(By.id("com.whatsapp:id/single_msg_tv"));
								
								row_new	=	l.get(l.size()-3).getText();
								row_new_msg	=	msg1.get(msg1.size()-3).getText();
								row1_new	=	l.get(l.size()-2).getText();
								row1_new_msg	=	msg1.get(msg1.size()-2).getText();
								row2_new	=	l.get(l.size()-1).getText();
								row2_new_msg	=	msg1.get(msg1.size()-1).getText();
								
					
								if(roww.equals(row_new)	&&	row1.equals(row1_new)	&&	row2.equals(row2_new)){				
									
									System.out.println("****going to next check****");
									
										if(row_msg.equals(row_new_msg)	&&	row1_msg.equals(row1_new_msg)	&&	row2_msg.equals(row2_new_msg))
											{
												System.out.println("All chats history sent!");
												finished	=	true;
												break;	
											}
									
									}
								
								counter = 0;
								break;
							}
						}
				}
		/*} 
		catch (Exception e) {
			System.out.println("Issue while broadcasting all chats");					
		}*/

	}
		
	
		//Swipe vertically function
		public static void swipeVertically() {
		
		Dimension screenSize = driver.manage().window().getSize();
		
		int screenWidth = screenSize.getWidth() / 2;
		int screenHeight = screenSize.getHeight();
		screenHeight = screenHeight * 70 / 100;

		//TRY & CATCH
		try{
			for(int i=0;i<=3;i++)
				{
					driver.swipe(screenWidth, screenHeight, screenWidth, 0, 1000);
				}}
		catch(Exception e)
		{
			e.printStackTrace();
			
			System.out.println("Try swiping again");
			for(int i=0;i<=3;i++)
				{
					driver.swipe(screenWidth, screenHeight, screenWidth, 0, 1000);
				}
		}
	}

		public static void whatsapp_sendMsg(WebElement row){
		
		//Click on whatsapp chat
		row.click();
		
		//TO DO
		//Send text
		MobileElement e	=	driver.findElement(By.id("com.whatsapp:id/entry"));
		e.sendKeys(Constants.message);
		/*try {
		     e.click();  
		     new ProcessBuilder(new String[]{"adb", "-s", deviceId, "shell", "input", "text", Constants.message})
		       .redirectErrorStream(true)
		       .start();
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}*/
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/send")));
		//Click 
		driver.findElement(By.id("com.whatsapp:id/send")).click();
		
		String temp	=	driver.findElement(By.id("com.whatsapp:id/conversation_contact_name")).getText();
		System.out.println(temp);
	}
	
}