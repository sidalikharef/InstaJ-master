
package instaj;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException; 


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class SendMeSSages {
	int countfollows=0;
	int countcomments=0;
    WebDriver driver;
    Utils utils = new Utils(); 
    int timeBetweenLikes;
    PrintWriter fw = null;
    public SendMeSSages(ArrayList<String> links, TextField timeBetweenLikes, TextArea logbox, WebDriver driver) throws InterruptedException {
        this.timeBetweenLikes = Integer.parseInt(timeBetweenLikes.getText());
        this.driver = driver;
        startMessagesThread(logbox, links);
    }
    
    public void goToPage(String link) {
        this.driver.get(link);
    } 
    
    public void SendMeSSages(TextArea logbox, ArrayList<String> links) throws InterruptedException, IOException {
        for (int x = 0; x < links.size(); x++) {
            goToPage(links.get(x));
    		PrintWriter writer = new PrintWriter("logs.txt", "UTF-8");
            if (isAvailable()) {    	
                WebElement fav = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/header/div[2]/div[1]/div[1]/span/a"), driver);
                fav.click();
                Thread.sleep(2000);
        		driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/div[1]/div[1]/div/div/div/span/span[1]/button")).click();
        		Thread.sleep(2000);
        		driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/div[1]/div[1]/div/div[1]/div/button")).click();
        		Thread.sleep(3000);
        		driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[3]/button[1]")).click();
         		Thread.sleep(1000);
        		driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div/div[2]/textarea")).click();
        		Thread.sleep(500);
        	    driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div/div[2]/textarea")).sendKeys("hey, please follow me and i will folow back :p");
        	    Thread.sleep(1000);
        		driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div/div[3]/button")).click();
        		
        		
        		writer.println(links.get(x)+",");

        		   
                utils.addLogLine(logbox, "[+] Followed User : : " + links.get(x));
                utils.addLogLine(logbox, "[+] Message send to : " + links.get(x));
                utils.waitGivenSeconds(timeBetweenLikes);
            } else {
                utils.addLogLine(logbox, "[+] PAGE NOT AVAILABLE! CONTINUING WITH THE NEXT ");
            }
    		writer.close();
        }
    }
    
    public Task<Void> MessagesTask(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        return new Task<Void>() {
            
            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                utils.addLogLine(logbox, "[+] Links loaded! Sending Messages ...");
                SendMeSSages(logbox, links);
                return null;
            }
        };
    }
    
    public void startMessagesThread(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        Task<Void> downloaderTask = MessagesTask(logbox, links);
        
        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            utils.addLogLine(logbox, "[+] Done!");
            downloaderTask.cancel();
        });
        
        downloaderTask.setOnFailed((WorkerStateEvent t) -> {
            // Code to run once runFactory() **fails**
        });
        
        new Thread(downloaderTask).start();
    }
    
    public boolean isAvailable() {
        boolean isAvailable = true;
        WebElement pageBody = utils.fluentWait(By.tagName("body"), driver);
        String bodyClass = pageBody.getAttribute("class");
        if (bodyClass.equals(" p-error dialog-404")) {
            isAvailable = false;
        }
        return isAvailable;
    }
    
}
