
package instaj;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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


public class StartLikes {
	int countfollows=0;
	int countcomments=0;
    WebDriver driver;
    Utils utils = new Utils(); 
    int timeBetweenLikes;
    PrintWriter fw = null;
    public StartLikes(ArrayList<String> links, TextField timeBetweenLikes, TextArea logbox, WebDriver driver) throws InterruptedException {
        this.timeBetweenLikes = Integer.parseInt(timeBetweenLikes.getText());
        this.driver = driver;
        startLikesThread(logbox, links);
    }
    
    public void goToPage(String link) {
        this.driver.get(link);
    } 
    
    public void likePosts(TextArea logbox, ArrayList<String> links) throws InterruptedException, IOException {
        for (int x = 0; x < links.size(); x++) {
            goToPage(links.get(x));
            if (isAvailable()) {
                WebElement fav = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/div[3]/section[1]/span[1]/button/div/span"), driver);
                fav.click();
                if (MainScreenFXMLController.f) {
                	if(countfollows<MainScreenFXMLController.numfollows) {
                		driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/header/div[2]/div[1]/div[2]/button")).click();
                        Thread.sleep(1000);
                        utils.addLogLine(logbox, "[+] FOLLOW a: " + links.get(x));
                        utils.waitGivenSeconds(timeBetweenLikes);
                        countfollows+=1;
                	}                	                      
                }
                if (MainScreenFXMLController.c) {
                	if(countcomments<MainScreenFXMLController.numcomment) {
                		int r=(int) ((Math.random() * (2 + 1 - 0)) + 0);
                    driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/div[3]/section[1]/span[2]")).click();
                    String z=MainScreenFXMLController.comments.get(r).replace("\"", "");
                    driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/div[3]/section[3]/div/form/textarea")).sendKeys(z);
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/div[3]/section[3]/div/form/button[2]")).click();
                    utils.addLogLine(logbox, "[+] COMMENT a: " + links.get(x));
                    utils.waitGivenSeconds(timeBetweenLikes);
                    countcomments+=1;
                	}
             }
            
            
                utils.addLogLine(logbox, "[+] like a: " + links.get(x));
                utils.waitGivenSeconds(timeBetweenLikes);
            } else {
                utils.addLogLine(logbox, "[+] PAGE NOT AVAILABLE! CONTINUING WITH THE NEXT ");
            }

        }
    }
    
    public Task<Void> likesTask(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        return new Task<Void>() {
            
            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                utils.addLogLine(logbox, "[+] Links loaded! Starting likes ...");
                likePosts(logbox, links);
                return null;
            }
        };
    }
    
    public void startLikesThread(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        Task<Void> downloaderTask = likesTask(logbox, links);
        
        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            utils.addLogLine(logbox, "[+] Likes finished!");
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
