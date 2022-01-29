/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * FXML Controller class
 *
 * @author Diego
 */
public class MainScreenFXMLController {

    Utils utils = new Utils();
    public static Boolean f=false;
    public static Boolean c=false;
    public static	int numfollows=0;
    public static   int numcomment=0;
    public static String elements;
    public static LinkedList<String> comments = new LinkedList<String>();

    WebDriver driver;

    @FXML
    ImageView profilePicture;

    @FXML
    Label username, currentFollowers, currentFollowing;

    @FXML
    TextArea logbox, tagsList,logMessages,Message;

    @FXML
    TextField numberOfLikes, timeBetweenLikes;
	@FXML
	TextField numberOfMessages,timesBetweenMessages;
	@FXML
	TextField numberOffollows;
	@FXML
	TextArea comment;
	@FXML
	TextArea commentone;
	@FXML
	TextArea commenttwo;
	@FXML
	TextField numberOfcomments;
	@FXML
	CheckBox likecheck;
	@FXML
	CheckBox followcheck;
	@FXML
	CheckBox commentscheck;
	@FXML
	RadioButton safemode;
	@FXML
	RadioButton custome;

    /**
     * Initializes the controller class.
     *
     * @param driver
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    final ToggleGroup group = new ToggleGroup();
    public void RadiobuttonBehavior() {
    	if (safemode.isSelected()) {
    		numberOfLikes.setDisable(true);
    		numberOffollows.setDisable(true);
    		numberOfcomments.setDisable(true);
    	}else if (custome.isSelected()) {
    		numberOfLikes.setDisable(false);
    		numberOffollows.setDisable(false);
    		numberOfcomments.setDisable(false);
    	}
    	
    }

    public void startBtnBehavior() throws InterruptedException {
    
    	if (followcheck.isSelected()) {
    		f=true;
    		numfollows=Integer.parseInt(numberOffollows.getText());

    	}
    	if(commentscheck.isSelected()) {
    		comments.clear();
    		c=true;
    		
    		numcomment=Integer.parseInt(numberOfcomments.getText());		
    		String one ='"'+comment.getText()+'"';
    		String two= '"'+commentone.getText()+'"';
    		String tree = '"'+commenttwo.getText()+'"';
    		comments.add(one);
    	    comments.add(two);
		    comments.add(tree);
     

        	
    	}
    	
        GetInstagramLinks getLinks = new GetInstagramLinks(driver, tagsList, numberOfLikes, logbox, timeBetweenLikes);


    }
    public void SendmessageBehavior() throws InterruptedException {
        
  
    	
    	GetInstagramProfils getLinks = new GetInstagramProfils(driver, tagsList, numberOfMessages, logMessages, timesBetweenMessages);


    }

    public void initGUI() {
        WebElement userLink = utils.fluentWait(By.xpath("//*//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div/div[5]/div[2]/div[2]/div[2]/a[1]/div"), driver);
        userLink.click();

        WebElement webUsername = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/div[1]/h2"), driver);
        WebElement webFollowers = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/ul/li[2]/a/span"), driver);
        WebElement webFollowing = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/ul/li[3]/a/span"), driver);
        this.username.setText(webUsername.getText());
        this.currentFollowers.setText(webFollowers.getText());
        this.currentFollowing.setText(webFollowing.getText());
        System.out.print("name"+webUsername.getText()+"\n followers"+webFollowers.getText()+"\n followin"+webFollowing.getText()+"\n");
        
        WebElement profilePictureElement = utils.fluentWait(By.cssSelector("#react-root > section > main > div > header > div > div > div > button > img"), driver);
        String profilePicLink = profilePictureElement.getAttribute("src");
        Image img = new Image(profilePicLink);
        profilePicture.setImage(img);
        safemode.setToggleGroup(group);
        custome.setToggleGroup(group);
        logbox.setText("*** InstaJ Bot by Sidali ***");
        autoScrollLogbox(logbox);
        numberOfLikes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                timeBetweenLikes.setDisable(false);
                timeBetweenLikes.setPromptText("");
            } else {
                timeBetweenLikes.setDisable(true);
                timeBetweenLikes.setPromptText("minutos");
             
            }
        });
       
    }

    public void autoScrollLogbox(TextArea logbox) {
        logbox.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                logbox.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }

}
