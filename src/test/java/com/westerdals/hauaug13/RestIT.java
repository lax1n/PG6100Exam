package com.westerdals.hauaug13;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerdals.hauaug13.PageObjects.CreateEventPageObject;
import com.westerdals.hauaug13.PageObjects.CreateUserPageObject;
import com.westerdals.hauaug13.PageObjects.HomePageObject;
import com.westerdals.hauaug13.PageObjects.LoginPageObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by August on 29.05.2016.
 */
public class RestIT {

    private static WebDriver driver;

    private static HomePageObject homeObj;
    private static LoginPageObject loginObj;
    private static CreateUserPageObject createUserObj;
    private static CreateEventPageObject createEventObj;

    @BeforeClass
    public static void init() throws InterruptedException{
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

        //Set up testdata
        homeObj.goToHomePage();

        //User 1
        createUserObj.goToNewUserPage();
        createUserObj.fillForm("userA", "password", "password", "Light", "Yagami", "Ukraine");
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event User One", "Ukraine", "South", "Event Description");
        createEventObj.submitForm();

        //Tick going
        homeObj.tickGoingByIndex(0);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Log out for future tests' sake
        loginObj.logOut();

        //User 2
        createUserObj.goToNewUserPage();
        createUserObj.fillForm("userB", "password", "password", "Light", "Yagami", "Ukraine");
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event User Two", "Ukraine", "South", "Event Description");
        createEventObj.submitForm();
        //Tick going
        homeObj.tickGoingByIndex(1);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        homeObj.tickGoingByIndex(0);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Log out for future tests' sake
        loginObj.logOut();

        //User 3
        createUserObj.goToNewUserPage();
        createUserObj.fillForm("userC", "password", "password", "Light", "Yagami", "Afghanistan");
        createUserObj.submitForm();

        createEventObj.goToNewEventPage();
        createEventObj.fillForm("Test Event User Three", "Afghanistan", "South", "Event Description");
        createEventObj.submitForm();
        //Tick going
        homeObj.tickGoingByIndex(2);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        homeObj.tickGoingByIndex(1);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        homeObj.tickGoingByIndex(0);
        try {
            //Wait for ajax
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        //Log out for future tests' sake
        loginObj.logOut();
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
    }

    @Test
    public void testGetAllXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("event");

            assertTrue(nodeList.getLength() >= 3);
        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }

    @Test
    public void testGetCountryAXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all?country=Ukraine").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("event");

            assertEquals(nodeList.getLength(), 2);
        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }

    @Test
    public void testGetCountryBXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all?country=Afghanistan").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("event");

            assertEquals(nodeList.getLength(), 1);
        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }

    @Test
    public void testGetAllJson(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request("application/json").get();
        String result = response.readEntity(String.class);
        Gson gson = new Gson();
        JsonArray data = gson.fromJson(result, JsonArray.class);

        assertTrue(data.size() >= 3);
    }

    @Test
    public void testGetEventXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("event");

            String id = nodeList.item(0).getChildNodes().item(0).getTextContent();
            String title = nodeList.item(0).getChildNodes().item(1).getTextContent();

            URI currentUri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events").queryParam("id", id).build();
            Client currentClient = ClientBuilder.newClient();
            Response currentResponse = currentClient.target(currentUri).request(MediaType.APPLICATION_XML_TYPE).get();


            InputStream currentInputStream = currentResponse.readEntity(InputStream.class);
            DocumentBuilderFactory currentDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder currentDocumentBuilder = currentDocumentBuilderFactory.newDocumentBuilder();
            Document currentDocument = currentDocumentBuilder.parse(currentInputStream);
            NodeList currentNodeList = currentDocument.getElementsByTagName("eventDTO");
            NodeList currentNodes = currentNodeList.item(0).getChildNodes();

            String currentId = nodeList.item(0).getChildNodes().item(0).getTextContent();
            String currentTitle = nodeList.item(0).getChildNodes().item(1).getTextContent();

            assertTrue((id.equals(currentId)) && (title.equals(currentTitle)));
        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }

    @Test
    public void testGetEventJSon(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request("application/json").get();
        String result = response.readEntity(String.class);
        Gson gson = new Gson();
        JsonArray data = gson.fromJson(result, JsonArray.class);

        String id = data.get(0).getAsJsonObject().get("id").getAsString();
        String title = data.get(0).getAsJsonObject().get("title").getAsString();

        URI currentUri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events").queryParam("id", id).build();
        Client currentClient = ClientBuilder.newClient();
        Response currentResponse = currentClient.target(currentUri).request("application/json").get();
        String currentResult = currentResponse.readEntity(String.class);
        Gson currentGson = new Gson();
        JsonObject currentData = currentGson.fromJson(currentResult, JsonObject.class);

        String currentId = currentData.getAsJsonObject().get("id").getAsString();
        String currentTitle = currentData.getAsJsonObject().get("title").getAsString();

        assertTrue((id.equals(currentId)) && (title.equals(currentTitle)));
    }

    @Test
    public void testWrongId(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events").queryParam("id", 100).build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        assertTrue((response.getStatus() == 400) || (response.getStatus() == 404));
    }

    @Test
    public void testGetAllWithAttendeesXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events/all").queryParam("attendees", true).build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("event");

            Element element = (Element) nodeList.item(0);
            NodeList userNodeList = element.getElementsByTagName("usersAttended");
            assertEquals(3, userNodeList.getLength());

            Element elementTwo = (Element) nodeList.item(1);
            NodeList userNodeListTwo = elementTwo.getElementsByTagName("usersAttended");
            assertEquals(2, userNodeListTwo.getLength());

            Element elementThree = (Element) nodeList.item(2);
            NodeList userNodeListThree = elementThree.getElementsByTagName("usersAttended");
            assertEquals(1, userNodeListThree.getLength());

        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }

    @Test
    public void testGetEventWithAttendessXml(){
        URI uri = UriBuilder.fromUri("http://localhost:8080/pg6100_exam/rs/events").queryParam("id", 1).queryParam("attendees", true).build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_XML_TYPE).get();

        try{
            InputStream inputStream = response.readEntity(InputStream.class);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("eventDTO");

            Element element = (Element) nodeList.item(0);
            NodeList userNodeList = element.getElementsByTagName("usersAttended");
            assertEquals(3, userNodeList.getLength());

        }catch(ParserConfigurationException p){}catch(SAXException s){}catch(IOException i){}
    }
}
