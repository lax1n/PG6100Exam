package com.westerdals.hauaug13.controller;

import com.westerdals.hauaug13.EJB.EventEJB;
import com.westerdals.hauaug13.EJB.UserEJB;
import com.westerdals.hauaug13.entity.Event;
import com.westerdals.hauaug13.entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by August on 28.05.2016.
 */
@Named
@RequestScoped
public class EventController {

    @Inject
    private EventEJB eventEJB;

    @EJB
    private UserEJB userEJB;

    private Event event;

    private long id;

    private String title;

    private String country;

    private String location;

    private String description;

    private List<Event> events;

    private boolean countryFilter;


    @PostConstruct
    public void init() {
        event = new Event();
        countryFilter = false;
    }

    public void initEvent() {
        event = eventEJB.getEventById(id);
    }


    public String createEvent() {
        event.setTitle(title);
        event.setCountry(country);
        event.setLocation(location);
        event.setDescription(description);

        if (eventEJB.createEvent(event) != null) {
            return "/home.faces";
        } else {
            return "newEvent.xhtml";
        }
    }

    @Inject
    private UserController userController;

    public List<Event> getFilteredEvents() {
        country = userController.getCurrentUser().getCountry();
        events = getAllEventsByCountry(country);

        for(Event event : events){
            event.setHasParticipant(hasParticipant(event));
        }

        return events;
    }


    public List<Event> getAllEvents(){
        events = eventEJB.getAllEvents();

        for(Event event : events){
            event.setHasParticipant(hasParticipant(event));
        }
        return events;
    }

    public int getNumberOfEvents(){
        return getAllEvents().size();
    }

    public boolean anyEvents(){
        Boolean value = false;
        try{
            value = getNumberOfEvents() > 0;
        }catch(NullPointerException e){}
        return value;
    }

    public String toggleCountryFilter(){
        if(countryFilter){
            setCountryFilter(false);
        }else{
            setCountryFilter(true);
        }
        return "/home.faces";
    }


    public List<Event> getAllEventsByCountry(String country){
        return eventEJB.getAllEventsByCountry(country);
    }

    public void addParticipant(Event event){
        if(event == null){
            throw new NotFoundException();
        }
        User currentUser = userController.getCurrentUser();
        if(currentUser != null && event != null) {
            if(hasParticipant(event))
                event.removeParticipant(currentUser);
            else
                event.addParticipant(currentUser);
            eventEJB.updateEvent(event);
        }
    }

    public boolean hasParticipant(Event event){
        return event.containsParticipant(userController.getCurrentUser());
    }

    public int getNumberOfParticipants(Event event){
        return event.getUsersAttended().size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Event> getEvents(){
        return events;
    }

    public void setEvents(List<Event> events){
        this.events = events;
    }

    public boolean getCountryFilter(){
        return countryFilter;
    }

    public void setCountryFilter(boolean countryFilter){
        this.countryFilter = countryFilter;
    }
}
