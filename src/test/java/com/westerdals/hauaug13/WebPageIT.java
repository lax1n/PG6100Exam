package com.westerdals.hauaug13;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.westerdals.hauaug13.EJB.UserEJB;
import com.westerdals.hauaug13.PageObjects.CreateEventPageObject;
import com.westerdals.hauaug13.PageObjects.CreateUserPageObject;
import com.westerdals.hauaug13.PageObjects.HomePageObject;
import com.westerdals.hauaug13.PageObjects.LoginPageObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class WebPageIT {

    private static WebDriver driver;
    private static WireMockServer server;

    private final String testUsername = "LightYagami86";
    private final String testPassword = "misaamane";
    private final String testCountry = "Norway";

    private static HomePageObject homeObj;
    private static LoginPageObject loginObj;
    private static CreateUserPageObject createUserObj;
    private static CreateEventPageObject createEventObj;

    @BeforeClass
    public static void init() throws InterruptedException {
        //make sure to install in Firefox the plugin from
        //https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
        desiredCapabilities.setBrowserName("firefox");
        desiredCapabilities.setJavascriptEnabled(true);

        driver = new FirefoxDriver(desiredCapabilities);

        //Initiate page objects
        homeObj = new HomePageObject(driver);
        loginObj = new LoginPageObject(driver);
        createUserObj = new CreateUserPageObject(driver);
        createEventObj = new CreateEventPageObject(driver);

        /*
            When the integration tests in this class are run, it might be
            that WildFly is not ready yet, although it was started. So
            we need to wait till it is ready.
         */
        for(int i=0; i<30; i++){
            boolean ready = JBossUtil.isJBossUpAndRunning();
            if(!ready){
                Thread.sleep(2000); //check every second
                continue;
            } else {
                break;
            }
        }

        server = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        configureFor("localhost", 8099);
        server.start();
    }

    private String getAMockedJsonResponse(){
        String json = "[";
        json += "{'name': 'Norway'} , ";
        json += "{'name': 'Sweden'} , ";
        json += "{'name': 'Fake Land'} , ";
        json += "{'name': 'Afghanistan'} , ";
        json += "{'name': 'Ukraine'} ";
        json += "]";
        return json;
    }

    private void stubJsonResponse(String json){
        server.stubFor(
                get(urlMatching("/rest/v1/all"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type","text/json; charset=utf-8")
                                .withHeader("Content-Length",""+json.length())
                                .withBody(json)));
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        server.stop();
    }


    @Before
    public void startFromInitialPage(){
        //if for any reason WildFly is not running any more, do not fail the tests
        assumeTrue(JBossUtil.isJBossUpAndRunning());
    }

    @Test
    public void testHomePage() {
        homeObj.goToHomePage();
        String title = "Home";
        String sourceTitle = driver.getTitle();
        assertEquals(sourceTitle, title);
    }

    @Test
    public void testLoginLink() {
        loginObj.goToLoginPage();
        assertEquals(driver.getTitle(), "Login");
    }

    @Test
    public void testLoginWrongUser(){
        loginObj.goToLoginPage();
        loginObj.fillForm("Darude", "Sandstorm");
        loginObj.submitForm();
        assertEquals(driver.getTitle(), "Login");
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch(){
        createUserObj.goToNewUserPage();
        createUserObj.fillForm("DudeStorm37", "allhail", "lelouch", "LeLouch", "Lamperouge", testCountry);
        createUserObj.submitForm();
        assertEquals(driver.getTitle(), "Create User");
    }

    @Test
    public void testCreateValidUser(){
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername, testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();
        assertEquals(driver.getTitle(), "Home");

        //Log out for future tests' sake
        createUserObj.logOut();
    }

    @Test
    public void testLogin(){
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "2", testPassword, testPassword, "Misa", "Amane", testCountry);

        createUserObj.submitForm();
        createUserObj.logOut();
        loginObj.goToLoginPage();
        loginObj.fillForm(testUsername + "2", testPassword);
        loginObj.submitForm();
        assertEquals(driver.getTitle(), "Home");

        //Log out for future tests' sake
        loginObj.logOut();
    }

    @Test
    public void testCreateOneEvent(){
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "3", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event", testCountry, "South", "Event Description");
        createEventObj.submitForm();

        assertEquals(driver.getTitle(), "Home");

        //Log out for future tests' sake
        loginObj.logOut();
    }

    @Test
    public void testCreateEventInDifferentCountries(){
        homeObj.goToHomePage();
        int startEventCount = homeObj.getNumberOfEvents();
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "4", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event", testCountry, "South", "Event Description");
        createEventObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event", "Sweden", "South", "Event Description");
        createEventObj.submitForm();

        //Untick filter

        int newEventCount = homeObj.getNumberOfEvents();
        assertEquals(startEventCount + 2, newEventCount);

        //Log out for future tests' sake
        loginObj.logOut();
    }
    @Test
    public void testCreateEventsFromDifferentUsers(){
        homeObj.goToHomePage();
        int startEventCount = homeObj.getNumberOfEvents();
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "5", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event User One", testCountry, "South", "Event Description");
        createEventObj.submitForm();

        loginObj.logOut();

        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "6", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event User Two", "Sweden", "South", "Event Description");
        createEventObj.submitForm();

        //Untick filter

        int newEventCount = homeObj.getNumberOfEvents();
        assertEquals(startEventCount + 2, newEventCount);

        //Log out for future tests' sake
        loginObj.logOut();
    }

    /*
    * This test should be commented out before running this files' tests to avoid conflicts of mocking countries.
    * */
    @Test
    public void testCreateUserWithFakeLand(){
        homeObj.goToHomePage();

        String json = getAMockedJsonResponse();
        stubJsonResponse(json);

        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "7", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();


        createEventObj.goToNewEventPage();

        /*
         * This Try/Catch clause should be commented out while running the test to see that it actually works. In some cases the exception is still occurring.
         */
        try {
            createEventObj.fillForm("Test Event User One", "wrong", "South", "Event Description");
            createEventObj.submitForm();
        }catch(NoSuchElementException e){}


        createEventObj.fillForm("Test Event User One", "Fake Land", "South", "Event Description");
        createEventObj.submitForm();


        assertEquals(driver.getTitle(), "Home");

        //Log out for future tests' sake
        loginObj.logOut();
    }

    @Test
    public void testCreateAndAttendEvent(){
        String title = "Participatee";
        homeObj.goToHomePage();
        int startEventCount = homeObj.getNumberOfEvents();
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "8", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm(title, testCountry, "South", "Event Description");
        createEventObj.submitForm();

        //Find submitted event in list and check participant count
        String count = homeObj.getParticipantsByIndex(startEventCount);
        int numCount = Integer.parseInt(count);
        assertEquals(0, numCount);
        //Tick going
        homeObj.tickGoingByIndex(startEventCount);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Check participants again
        String updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        int updatedNumCount = Integer.parseInt(updatedCount);

        assertEquals(1, updatedNumCount);

        //Untick going
        homeObj.tickGoingByIndex(startEventCount);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Check participants again
        updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        updatedNumCount = Integer.parseInt(updatedCount);

        assertEquals(0, updatedNumCount);

        //Log out for future tests' sake
        loginObj.logOut();
    }

    @Test
    public void testTwoUsersAttendingSameEvent(){
        homeObj.goToHomePage();
        int startEventCount = homeObj.getNumberOfEvents();
        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "9", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Some title", testCountry, "South", "Event Description");
        createEventObj.submitForm();

        //Tick going
        homeObj.tickGoingByIndex(startEventCount);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Check participants again
        String updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        int updatedNumCount = Integer.parseInt(updatedCount);

        assertEquals(1, updatedNumCount);

        //Log out for future tests' sake
        loginObj.logOut();

        //Do the same with next user
        homeObj.goToHomePage();

        createUserObj.goToNewUserPage();
        createUserObj.fillForm(testUsername + "10", testPassword, testPassword, "Light", "Yagami", testCountry);
        createUserObj.submitForm();

        //Check participants again
        //Tick going
        homeObj.tickGoingByIndex(startEventCount);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Check participants again
        updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        updatedNumCount = Integer.parseInt(updatedCount);

        assertEquals(2, updatedNumCount);

        //Log out for future tests' sake
        loginObj.logOut();

        homeObj.goToHomePage();
        //Login first user
        loginObj.goToLoginPage();
        loginObj.fillForm(testUsername + "9", testPassword);
        loginObj.submitForm();

        updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        updatedNumCount = Integer.parseInt(updatedCount);

        //Verify count is still 2
        assertEquals(2, updatedNumCount);

        //Uncheck again with first user
        homeObj.tickGoingByIndex(startEventCount);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Check participants again
        updatedCount = homeObj.getParticipantsByIndex(startEventCount);
        updatedNumCount = Integer.parseInt(updatedCount);

        assertEquals(1, updatedNumCount);

        //Log out for future tests' sake
        loginObj.logOut();
    }
}
