package com.westerdals.hauaug13.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by August on 28.05.2016.
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = Event.query_getEventById, query = "SELECT e FROM Event e WHERE e.id = :id"),
        @NamedQuery(name = Event.query_getAllEvents, query = "SELECT e FROM Event e"),
        @NamedQuery(name = Event.query_getAllEventsByCountry, query = "SELECT e FROM Event e WHERE e.country = :country")
})
public class Event {

    public static final String query_getEventById = "Event.getEventById";
    public static final String query_getAllEvents = "Event.getAllEvents";
    public static final String query_getAllEventsByCountry = "Event.getAllEventsByCountry";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 64)
    private String title;

    @NotNull
    private String country;

    @NotNull
    private String location;

    @Size(max = 150)
    private String description;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name ="user_attended")
    private List<User> usersAttended;

    public Event() {
    }

    public Event(String title, String country, String location, String description) {
        this.title = title;
        this.country = country;
        this.location = location;
        this.description = description;
    }

    @Transient
    @XmlTransient
    private boolean hasParticipant;

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
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

    public List<User> getUsersAttended() {
        return usersAttended;
    }

    public void setUsersAttended(List<User> users) {
        this.usersAttended = users;
    }

    public void addParticipant(User user){
        if(usersAttended == null) {
            usersAttended = new ArrayList<User>();
        }
        usersAttended.add(user);
    }

    public void removeParticipant(User currentUser){
        if(usersAttended == null)
            return;

        for(int i=0; i < usersAttended.size(); i++){
            if(currentUser.getUsername().equals(usersAttended.get(i).getUsername())){
                usersAttended.remove(i);
            }
        }
    }

    public boolean isHasParticipant() {
        return hasParticipant;
    }

    public void setHasParticipant(boolean hasParticipant) {
        this.hasParticipant = hasParticipant;
    }


    public boolean containsParticipant(User currentUser){
        if(usersAttended == null)
            return false;

        for(User user : usersAttended){
            if(currentUser.getUsername().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }
}

