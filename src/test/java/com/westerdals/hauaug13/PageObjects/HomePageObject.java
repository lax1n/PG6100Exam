package com.westerdals.hauaug13.PageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Created by August on 27.05.2016.
 */
public class HomePageObject {
    private WebDriver driver;

    public HomePageObject(WebDriver driver){
        this.driver = driver;
    }

    public void goToHomePage(){
        driver.get("localhost:8080/pg6100_exam");
        waitForPageToLoad();
    }

    public int getNumberOfEvents(){
        String id = "all_events";
        if(findElement("filtered_events")){
            id = "filtered_events";
        }else if(findElement("unfiltered_events")){
            id = "unfiltered_events";
        }else if(findElement("all_events")){
            id = "all_events";
        }else{
            return 0;
        }

        WebElement table = driver.findElement(By.xpath("//*[contains(@id,'" + id + "')]"));
        List<WebElement> eventsCollection = table.findElements(By.xpath("//*[contains(@id,'" + id + "')]/tbody/tr"));
        return eventsCollection.size();
    }

    public List<WebElement> getEventList(){
        String id = "all_events";
        if(findElement("filtered_events")){
            id = "filtered_events";
        }else if(findElement("unfiltered_events")){
            id = "unfiltered_events";
        }else if(findElement("all_events")){
            id = "all_events";
        }else{
            return null;
        }

        WebElement table = driver.findElement(By.xpath("//*[contains(@id,'" + id + "')]"));
        return table.findElements(By.xpath("//*[contains(@id,'" + id + "')]/tbody/tr"));
    }

    public void tickCountryFilter(){
        driver.findElement(By.xpath("//*[contains(@id,'countryFilter')]")).click();
    }

    public boolean filteredEvents(){
        if(findElement("countryFilter")){
            return driver.findElement(By.xpath("//*[contains(@id,'countryFilter')]")).isSelected();
        }else{
            return false;
        }
    }

    public String getParticipantsByIndex(int i){
        List<WebElement> goingElems = driver.findElements(By.xpath("//*[contains(@id,'participantsValue')]"));
        WebElement elem = goingElems.get(i);
        return elem.getText();
    }

    public void tickGoingByIndex(int i){
        List<WebElement> goingElems = driver.findElements(By.xpath("//*[contains(@id,'participateBox')]"));
        WebElement elem = goingElems.get(i);
        elem.click();
    }

    public boolean findElement(String id){
        try{
            driver.findElement(By.xpath("//*[contains(@id,'" + id + "')]"));
        }catch (NoSuchElementException e){
            return false;
        }
        return true;
    }


    public Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }
}
