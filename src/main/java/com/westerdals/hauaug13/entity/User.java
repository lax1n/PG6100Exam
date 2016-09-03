package com.westerdals.hauaug13.entity;

/**
 * Created by August on 27.05.2016.
 */

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.List;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = User.query_getUserById, query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = User.query_getAllUsers, query = "SELECT u FROM User u"),
        @NamedQuery(name = User.query_getUserByUsername, query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User {

    public static final String query_getUserById = "User.getUserById";
    public static final String query_getAllUsers = "User.getAllUsers";
    public static final String query_getUserByUsername = "User.getUserByUsername";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    @Size(min = 1, max = 64)
    @NotBlank
    private String username;

    @XmlTransient
    @NotNull
    @Size(min = 0, max = 32)
    private String firstName;

    @XmlTransient
    @Size(min = 0, max = 32)
    private String middleName;

    @XmlTransient
    @NotNull
    @Size(min = 0, max = 32)
    private String lastName;

    @XmlTransient
    @NotNull
    private String hash;

    @XmlTransient
    @NotNull @Size(max = 26)
    private String salt;

    @XmlTransient
    @NotNull
    private String country;

    @XmlTransient
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "event_attended")
    private List<Event> eventsAttended;

    public User(){
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String firstName, String middleName, String lastName, String country) {
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.country = country;
    }

    public List<Event> getEventsAttended() {
        return eventsAttended;
    }

    public void setEventsAttended(List<Event> eventsAttended) {
        this.eventsAttended = eventsAttended;
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}