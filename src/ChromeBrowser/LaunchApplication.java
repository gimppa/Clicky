package ChromeBrowser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LaunchApplication {
	
	public static void main(String[] args) {
		
		Random r = new Random(System.currentTimeMillis());
		
		HashMap<String, String> answers = new HashMap<String, String>();
		
		try {
			
	         FileInputStream fileIn = new FileInputStream("answers.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         answers = (HashMap<String, String>) in.readObject();
	         in.close();
	         fileIn.close();
	         System.out.println(answers);
			
		}catch (Exception e) {
			System.out.println(e);
		}
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\L. X. Ander\\Desktop\\selenyymi\\chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--log-level=OFF");
		WebDriver driver = new ChromeDriver(options);
		
		driver.get("http://benz.me/hextech-chest-quiz");
		
		driver.manage().window().maximize();
		
		driver.switchTo().frame(0);
		
		Actions builder = new Actions(driver);
		
		boolean tosFlag = false;
		boolean playFlag = false;
		boolean lastQuestion = false;
		boolean nameFlag = false;
		
		String currentQuestion = "";
		
		float speedMod = 0.8f;
		
		while(true) {
			
			try {

				Thread.sleep(100);
				
				if (playFlag) {
					
					
					WebDriverWait wait = new WebDriverWait(driver, 10);
					
					WebElement questionText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='question-text']")));
					

					
					if (!questionText.getText().equals(currentQuestion)) {
						
						WebElement qNum = driver.findElement(By.xpath("//span[@class='question-info']"));
						
						System.out.println(qNum.getText());
						
						if (qNum.getText().equals("Question 7/ 7")) {
							System.out.println("Last question");
							lastQuestion = true;
						}
						
						currentQuestion = questionText.getText();
						
						System.out.println(currentQuestion);
						
						String answer = answers.get(currentQuestion);
						
						if (answer != null) {
							
							System.out.println("Known answer is:" + answer);
							
							List<WebElement> answerButtons = driver.findElements(By.xpath("//div[@class='choice']"));
							
							for (WebElement button : answerButtons) {
								if (button.getText().equals(answer)) {
									click(button, builder, (int)((300+r.nextInt(200)*speedMod)*speedMod) , (int)(50+r.nextInt(200)*speedMod) , 90+r.nextInt(30));
								}
							}
							
						}
						else {
							
							List<WebElement> answerButtons = driver.findElements(By.xpath("//div[@class='choice']"));
							click(answerButtons.get(r.nextInt(3)), builder, 1491+r.nextInt(492) , 125+r.nextInt(88) , 125+r.nextInt(88));
							
							WebDriverWait answerWait = new WebDriverWait(driver, 10);
							
							WebElement correctAnswer = answerWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='choice selected disabled is-correct']|//div[@class='choice disabled is-correct']")));
							
							String correctText = correctAnswer.getText();
							
							answers.put(currentQuestion, correctText);
							
							System.out.println("Unknown answer is:" + correctText);
						}
						
						if (lastQuestion) {
							playFlag = false;
							lastQuestion = false;
							speedMod -= 0.05f;
							speedMod *= 0.95f;
							System.out.println("Current delay modifier: "+speedMod);
							try {
								
						         FileOutputStream fileOut = new FileOutputStream("answers.ser");
				                 ObjectOutputStream out = new ObjectOutputStream(fileOut);
				                 out.writeObject(answers);
				                 out.close();
				                 fileOut.close();
								
							}catch (Exception e) {
								System.out.println(e);
							}
							
						}
						
					}
					
				}
				
				else {
					
					
					if(!tosFlag) {
						WebDriverWait wait = new WebDriverWait(driver, 10);
						WebElement checkmark = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='mc-checkmark']")));
						System.out.println("Agree to TOS");
						
						click( checkmark , builder , 895+r.nextInt(500) , 333+r.nextInt(127) , 125+r.nextInt(88) );
						
						WebElement letsGo = driver.findElement(By.xpath("//button[@class='main-button']"));
						
						click( letsGo , builder , 488+r.nextInt(239) , 233+r.nextInt(127) , 125+r.nextInt(88) );
						
						tosFlag = true;
						
					}
					
					if(!nameFlag) {
						nameFlag = true;
						
						WebDriverWait wait = new WebDriverWait(driver, 10);
						WebElement nameBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='name-input']")));
						System.out.println("Input name");
						
						click( nameBox , builder , 488+r.nextInt(239) , 233+r.nextInt(127) , 125+r.nextInt(88) );
						
						builder.sendKeys(nameBox, "DefinitelyNotABot").build().perform();
						
						Thread.sleep(500);
						
						playFlag = true;
						
					}
					
					/*
					if (!playFlag) {
						
						WebDriverWait wait = new WebDriverWait(driver, 10);
						WebElement closeBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='close-button']")));
						System.out.println("Get rid of popup");
						
						click( closeBox , builder , 895+r.nextInt(239) , 233+r.nextInt(127) , 125+r.nextInt(88) );
		
					}
					*/
					
					WebDriverWait wait = new WebDriverWait(driver, 10);
					WebElement playButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='custom-button']")));
					if (playButton != null) {
						System.out.println("Play quiz");
						
						click( playButton , builder , 488+r.nextInt(239) , 233+r.nextInt(127) , 125+r.nextInt(88) );
						playFlag = true;
						
					}
				}
				
				
			} catch(Exception e) {System.out.println(e);}
		}
		
	}
	
	private static void click(WebElement target, Actions builder, int delay, int clickdelay, int clicklength) {
		try {
			Random r = new Random(System.currentTimeMillis());
			
			Thread.sleep(delay);
			
			Dimension size = target.getSize();

			int xrand = r.nextInt(size.width) - size.width/2;
			xrand *= r.nextInt(100)/100;
			int yrand = r.nextInt(size.height) - size.height/2;
			yrand *= r.nextInt(100)/100;
			
			builder.moveToElement(target, xrand, yrand).perform();
			
			Thread.sleep(clickdelay);
			
			builder.clickAndHold().build().perform();
			
			Thread.sleep(clicklength);
			
			builder.release().build().perform();
			
		} catch (InterruptedException e) {
			System.out.print(e);
		}
	}
	
	
}
