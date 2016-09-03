package com.westerdals.hauaug13.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by August on 28.05.2016.
 */
public class CreateUserPageObject {
    private WebDriver driver;

    public CreateUserPageObject(WebDriver driver){
        this.driver = driver;
    }

    public void goToNewUserPage(){
        LoginPageObject loginObj = new LoginPageObject(driver);
        loginObj.goToLoginPage(); //Go to login

        goFromLoginToNewUserPage(); //Go to new user
    }

    public void goFromLoginToNewUserPage(){
        driver.findElement(By.xpath("//*[contains(@id,'signUpBtn')]")).click();
    }

    public void goDirectlyToNewUserPage(){
        driver.get("localhost:8080/pg6100_exam/user/newUser.faces");
    }

    public void fillForm(String username, String password, String passwordConfirm, String firstName, String lastName, String country){
        WebElement usernameElem = driver.findElement(By.xpath("//*[contains(@id,'username')]"));
        usernameElem.clear();
        usernameElem.sendKeys(username);

        WebElement pwdElem = driver.findElement(By.xpath("//*[contains(@id,'password')]"));
        pwdElem.clear();
        pwdElem.sendKeys(password);

        WebElement pwdConfirmElem = driver.findElement(By.xpath("//*[contains(@id,'confirmPassword')]"));
        pwdConfirmElem.clear();
        pwdConfirmElem.sendKeys(passwordConfirm);

        WebElement firstNameElem = driver.findElement(By.xpath("//*[contains(@id,'firstName')]"));
        firstNameElem.clear();
        firstNameElem.sendKeys(firstName);

        WebElement lastNameElem = driver.findElement(By.xpath("//*[contains(@id,'lastName')]"));
        lastNameElem.clear();
        lastNameElem.sendKeys(lastName);

        new Select(driver.findElement(By.xpath("//*[contains(@id,'country')]"))).selectByVisibleText(country);

    }

    public void fillFormWithMiddleName(String username, String password, String passwordConfirm, String firstName, String middleName, String lastName, String country){
        fillForm(username, password, passwordConfirm, firstName, lastName, country);
        WebElement middleNameElem = driver.findElement(By.xpath("//*[contains(@id,'middleName')]"));
        middleNameElem.clear();
        middleNameElem.sendKeys(middleName);
    }

    public void submitForm(){
        driver.findElement(By.xpath("//*[contains(@id,'createUserBtn')]")).click();
    }

    public void logOut(){
        driver.findElement(By.xpath("//*[contains(@id,'signOutBtn')]")).click();
    }
}