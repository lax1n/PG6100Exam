package com.westerdals.hauaug13.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by August on 27.05.2016.
 */
public class LoginPageObject {    private WebDriver driver;

    public LoginPageObject(WebDriver driver){
        this.driver = driver;
    }

    public void goToLoginPage(){
        HomePageObject homeObj = new HomePageObject(driver);
        homeObj.goToHomePage(); //Go to home

        goFromHomeToLoginPage(); //Go to login
    }

    public void goFromHomeToLoginPage(){
        driver.findElement(By.xpath("//*[contains(@id,'signInBtn')]")).click();
    }

    public void goDirectlyToLoginPage(){
        driver.get("localhost:8080/pg6100_exam/user/login.faces");
    }

    public void fillForm(String username, String password){
        WebElement usrField = driver.findElement(By.xpath("//*[contains(@id,'usernameField')]")); //Use xpath over id due to partly generated ids
        WebElement pwdField = driver.findElement(By.xpath("//*[contains(@id,'passwordField')]"));

        usrField.clear();
        pwdField.clear();
        usrField.sendKeys(username);
        pwdField.sendKeys(password);
    }

    public void submitForm(){
        driver.findElement(By.xpath("//*[contains(@id,'loginBtn')]")).click();
    }

    public void logOut(){
        driver.findElement(By.xpath("//*[contains(@id,'signOutBtn')]")).click();
    }
}
