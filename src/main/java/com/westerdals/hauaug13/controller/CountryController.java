package com.westerdals.hauaug13.controller;

import com.westerdals.hauaug13.EJB.CountryEJB;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by August on 27.05.2016.
 */
@Named
@ApplicationScoped
public class CountryController implements Serializable{

    @Inject
    private CountryEJB countryEJB;

    private List<String> countries;

    @PostConstruct
    public void loadCountries(){
        countries = countryEJB.getCountries();
    }

    public List<String> getCountries(){
        return countries;
    }
}
