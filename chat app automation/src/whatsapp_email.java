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

import javax.swing.plaf.synth.SynthSpinnerUI;

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

public class whatsapp_email {
	
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
		
		public	static	String grp_name	=	"false"; 
		public	static	String grp_msg	=	"false"; 
		public	static	String new_grp_name;	
		public	static	String new_grp_msg; 
		
		
		public static void main(String[] args){
			
			setUp();
			//sendAllChats();
		
			
			TimerTask task = new TimerTask() {		      
				  
				@Override
			      public void run() {
					
					//handle pin lock
					/*try {
						Runtime.getRuntime().exec("adb shell input keyevent 82");
						//Change below 4 digit number acc your device's pin
						Runtime.getRuntime().exec("adb shell input text 1234");
						driver.findElement(By.id("com.android.systemui:id/key_enter")).click();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Issue occured while inputting pin lock");
						e.printStackTrace();
					}*/

						//Call latest chats function
					  	sendLatestChats();
				  }
			};
			
			Timer timer = new Timer();
		    long delay = 0;
		    
		    //Set interval in milliseconds
		    //Currently set to 15 minutes
		    long intevalPeriod = 1 * 60000; 
		    
		    // schedules the task to be run in an interval 
		    timer.scheduleAtFixedRate(task, delay, intevalPeriod);
		}
		
		
		public static void setUp(){
			
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("platformname", "Android");
			cap.setCapability("deviceName", "ZY22283HSQ"); //****Changed device name from ****
			cap.setCapability("platformVersion", "6.0.1"); //****Changed android device version*****
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
		
		
		public static void sendAllChats(){
	
		try{

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
							whatsappsendEmail(row);
							
							//Wait
							wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.gm:id/to")));
										
							//Call sendEmail function
							try{
								gmail();
								
							}catch(Exception e){
								
								//Handle typing issue or incorrect emailId exception
								if(!driver.findElements(By.name("OK")).isEmpty())
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("OK")));
									driver.findElement(By.name("OK")).click();
									gmail();							
								}
								else{
									e.printStackTrace();
								}
							}
							
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
		} 
		catch (Exception e) {
			System.out.println("Issue while sending all chats");					
		}

	}

		
	//Send latest chats	
	public static void sendLatestChats()
	{
	        // task to run goes here
	        System.out.println("Hello !!");
	        
	        //Restart whatsapp activity
	        driver.startActivity("com.whatsapp", "com.whatsapp.HomeActivity", null, null);

	        //check condition for new chats
	        List<MobileElement>	list	= driver.findElements(By.id("com.whatsapp:id/badge"));	
	        
	        if(list.size()	!=	0)
	        {	
	        	
	        	//Get number of new chats
	        	String t	=	driver.findElement(By.id("com.whatsapp:id/badge")).getText();
	        	int chatNumber	=	Integer.parseInt(t);
	        	
	        	try{

	    			//Initializing counter
	    			int counter	=	1;
	    			int	counter1	=	1;
	    			boolean finished = false;
	    			
	    			//Storing all chats in a list
	    			rowList = driver.findElements(By.id("com.whatsapp:id/contact_row_container"));
	    			
	    			//For loop
	    			while(!finished) {
	    						
	    						for (WebElement row : rowList) {
	    							
	    							List<MobileElement> l	=	driver.findElements(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'com.whatsapp:id/contact_row_container')]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.TextView[contains(@resource-id,'com.whatsapp:id/conversations_row_contact_name')]"));
	    							List<MobileElement>	m	=	driver.findElements(By.id("com.whatsapp:id/single_msg_tv"));	 
	    							
	    							new_grp_name	=	l.get(0).getText();
	    							new_grp_msg	=	m.get(0).getText();
	    							
	    							//CASE - TO CHECK SAME CHAT CASE
	    							if(grp_name.equals(new_grp_name)	&&	grp_msg.equals(new_grp_msg))
	    							{
	    								grp_name	=	"true";
	    								continue;	
	    							}
	    							
	    							//Call whatsapp code
	    							whatsappsendEmail(row);
	    							
	    							//Wait
	    							
	    							wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.gm:id/to")));
	    										
	    							//Call sendEmail function
	    							try{
	    								gmail();
	    								
	    							}catch(Exception e){
	    								
	    								//Handle typing issue or incorrect emailId exception
	    								if(!driver.findElements(By.name("OK")).isEmpty())
	    								{
	    									wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("OK")));
	    									driver.findElement(By.name("OK")).click();
	    									gmail();							
	    								}
	    								else{
	    									e.printStackTrace();
	    								}
	    							}
	    							
	    							//Go back, out of the current group chat 
	    							driver.findElement(By.className("android.widget.ImageView")).click();
	    							
	    							
	    							//If all new chats are sent then stop script
	    							if(chatNumber	==	0)
	    							{
	    								System.out.println("Number of chats:"+counter);
	    								finished	=	true;
	    								break;
	    							}
	    							else{
	    								counter++;
	    								chatNumber --;
	    							}
	    							
	    							//Increment row counter
	    							counter1++;
	    							
	    							if(counter1 == rowList.size()) 
	    							{
	    								grp_name	=	l.get(l.size()-1).getText();
	    								grp_msg	=	m.get(m.size()-1).getText();
	    								
	    								swipeVertically();
	    								counter1	= 0;
	    								break;
	    							}
	    
	    						}
	    				}
	    		} 
	    		catch (Exception e) {
	    			System.out.println("Issue while sending latest chats");					
	    		}

	        	
	        }
	        else{
	        	System.out.println("No new chats");
	        }
	    
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


	//Send Email Function
	public static void gmail()
	{
		//Clear field
		driver.findElement(By.id("com.google.android.gm:id/to")).clear();
		
		//Type recipient email id
		driver.findElement(By.id("com.google.android.gm:id/to")).sendKeys("devsh2694@gmail.com ");

		//Send email
		driver.findElement(By.id("com.google.android.gm:id/send")).click();
	
		//**Added below one loc****
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.ImageView")));
	
	}	

	
	public static void whatsappsendEmail(WebElement row){
		
		//Click on whatsapp chat
		row.click();
	
		//Wait
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("More options")));
		
		//Initiate email
		driver.findElement(By.name("More options")).click();
		driver.findElement(By.name("More")).click();
		driver.findElement(By.name("Email chat")).click();
		
		//***HANDLE ATTACHMENT POPUP
		if(driver.findElements(By.id("android:id/button3")).isEmpty())
		{}
		else
		{
			driver.findElement(By.id("android:id/button3")).click();
		}
		
		//Wait for progress bar
		if(!driver.findElements(By.id("android:id/progress")).isEmpty())
		{
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress")));
		}
		else{}
	
	}
	
}