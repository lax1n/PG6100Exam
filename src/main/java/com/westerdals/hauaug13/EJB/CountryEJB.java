package com.westerdals.hauaug13.EJB;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by August on 27.05.2016.
 */

@Stateless
public class CountryEJB {

    private final String webAddress;

    public CountryEJB(){
        webAddress = System.getProperty("fixerWebAddress", "restcountries.eu");
    }

    private List<String> countries;

    public List<String> getCountries(){
        URI uri = UriBuilder.fromUri("http://" + webAddress + "/rest/v1/all").build();
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request("application/json").get();

        String result = response.readEntity(String.class);
        Gson gson = new Gson();
        JsonArray data = gson.fromJson(result, JsonArray.class);

        countries = new ArrayList<String>();

        for(int i = 0; i < data.size(); i++){
            countries.add(data.get(i).getAsJsonObject().get("name").getAsString());
        }

        return countries;
    }
}
