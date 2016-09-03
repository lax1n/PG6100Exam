package com.westerdals.hauaug13.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by August on 28.05.2016.
 */
public class CreateEventPageObject {
    private WebDriver driver;

    public CreateEventPageObject(WebDriver driver){
        this.driver = driver;
    }

    public void goToNewEventPage(){
        HomePageObject homeObj = new HomePageObject(driver);
        homeObj.goToHomePage(); //Go to home

        goFromHomeToNewEventPage(); //Go to new event
    }

    public void goFromHomeToNewEventPage(){
        driver.findElement(By.xpath("//*[contains(@id,'newEventBtn')]")).click();
    }

    public void goDirectlyToNewEventPage(){
        driver.get("localhost:8080/pg6100_exam/user/newEvent.faces");
    }

    public void fillForm(String title, String country, String location, String description){

        try {
            WebElement titleElem = driver.findElement(By.xpath("//*[contains(@id,'title')]"));
            titleElem.clear();
            titleElem.sendKeys(title);

            new Select(driver.findElement(By.xpath("//*[contains(@id,'country')]"))).selectByVisibleText(country);

            WebElement locationElem = driver.findElement(By.xpath("//*[contains(@id,'location')]"));
            locationElem.clear();
            locationElem.sendKeys(location);

            WebElement descElem = driver.findElement(By.xpath("//*[contains(@id,'description')]"));
            descElem.clear();
            descElem.sendKeys(description);
        }catch(NoSuchElementException e){

        }
    }

    public void submitForm(){
        driver.findElement(By.xpath("//*[contains(@id,'createEventBtn')]")).click();
    }
}