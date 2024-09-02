package practice;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class Working_with_VerificationOfNewContactWithSupportDate {

	@Test
	public void contact() throws Throwable {
		FileInputStream fis = new FileInputStream("./TestData/commondata.properties");
		Properties p= new Properties();
		p.load(fis);
		String browser = p.getProperty("browser");
		String url = p.getProperty("url");
		String username = p.getProperty("username");
		String password = p.getProperty("password");


		Random random = new Random();
		int randomint = random.nextInt(1000);

		FileInputStream fis1 = new FileInputStream("./TestData/TestScript.xlsx");
		Workbook wb = WorkbookFactory.create(fis1);
		Sheet sheet = wb.getSheet("Contact");
		Row row = sheet.getRow(1);
		Cell cell = row.getCell(2);
		//System.out.println(cell.getStringCellValue()+ randomint);
		String data = cell.getStringCellValue()+ randomint;

		WebDriver driver;
		if(browser.equalsIgnoreCase("chrome")) {
			driver= new ChromeDriver();
		}else if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
		}else if(browser.equalsIgnoreCase("edge")){
			driver = new EdgeDriver();
		}else {
			driver= new ChromeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.get(url);
		driver.findElement(By.xpath("//input[@name='user_name']")).sendKeys(username);
		driver.findElement(By.xpath("//input[@name='user_password']")).sendKeys(password);
		driver.findElement(By.id("submitButton")).click();
		driver.findElement(By.xpath("(//a[text()='Contacts'])[1]")).click();
		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click();
		driver.findElement(By.name("lastname")).sendKeys(data);
		
		Date dateobj = new Date();
		SimpleDateFormat sim= new SimpleDateFormat("yyyy-MM-dd");
		String actdate = sim.format(dateobj);
		Calendar cal = sim.getCalendar();
		cal.add(Calendar.DAY_OF_MONTH,30);
		String daterequired = sim.format(cal.getTime());
		
		//Support start date
		driver.findElement(By.name("support_start_date")).sendKeys(actdate);
		System.out.println(actdate);
		
		//Support end date
		driver.findElement(By.name("support_end_date")).sendKeys(daterequired);
		System.out.println(daterequired);
		Thread.sleep(3000);
		
		
		try {
			driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[2]")).click();
			String startdate = driver.findElement(By.id("dtlview_Support Start Date")).getText();
			String enddate = driver.findElement(By.id("dtlview_Support End Date")).getText();

			if(startdate.contains(actdate)) {
				System.out.println("new contact with support start date "+startdate+" is created ==== pass");
			}else
			{
				System.out.println("new contact with support start date "+startdate+"not created ==== failed");
			}
			
			if(enddate.contains(daterequired)) {
				System.out.println("new contact with support end date "+daterequired+" is created ==== pass");
			}else
			{
				System.out.println("new contact with support end date "+daterequired+" is not created ==== failed");
			}
		}catch (Exception e) {
			Alert alertText = driver.switchTo().alert();
			String text1 = alertText.getText();
			alertText.accept();
			System.out.println("New organsiaction is not created as " + text1 + " -Please enter different Organsiaction Name-");
		}

		driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']")).click();
		driver.findElement(By.xpath("//a[text()='Sign Out']")).click();

		driver.quit();

	}
}
