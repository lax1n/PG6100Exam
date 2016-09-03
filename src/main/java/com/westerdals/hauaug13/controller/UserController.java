package com.westerdals.hauaug13.controller;

import com.westerdals.hauaug13.EJB.UserEJB;
import com.westerdals.hauaug13.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by August on 27.05.2016.
 */

@Named
@SessionScoped
public class UserController implements Serializable {

    @Inject
    private UserEJB userEJB;

    private User user;
    private User currentUser;
    private long id;

    private String username;
    private String password;
    private String passwordConfirm;

    private String firstName, middleName, lastName;

    private String country;

    @PostConstruct
    public void init(){
        user = new User();
    }

    public String createUser(){
        if(confirmPassword(password, passwordConfirm)){
            user.setFirstName(firstName);
            if((middleName != null) && (!middleName.equals("")))
                user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setCountry(country);


            //Attempt to persist user
            user = userEJB.createUser(user, password);
            password = null;
            passwordConfirm = null;
            if(user != null){
                currentUser = user;
                return "/home.faces";
            }
        }
        return "/user/newUser.faces";
    }

    private boolean confirmPassword(String password, String passwordConfirm){
        return (password != null && passwordConfirm != null && password.equals(passwordConfirm));
    }

    public void initUser() {
        user = userEJB.getUserById(id);
    }

    public String loginUser() {
        boolean loggedIn = login(username, password);
        if (loggedIn) {
            currentUser = userEJB.getUserByUsername(username);
            return "/home.faces";
        } else {
            return "/user/login.faces";
        }
    }

    private boolean login(String id, String password) {
        if (id == null || id.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User tempUser = userEJB.getUserByUsername(id);
        if (tempUser == null) {
            return false;
        }

        String hash = generateHash(password, tempUser.getSalt());

        boolean isOK = hash.equals(tempUser.getHash());
        if (isOK) {
            currentUser = tempUser;
        }
        return isOK;
    }

    public String logOut() {
        currentUser = null;
        user = new User();
        return "/home.faces";
    }


    public boolean isLoggedIn() {
        return currentUser != null;
    }


    public static String generateHash(String password, String salt){
        return DigestUtils.sha256Hex(password + salt);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){
        this.password = password;
    }

    public String getPasswordConfirm(){return passwordConfirm;}

    public void setPasswordConfirm(String passwordConfirm){
        this.passwordConfirm = passwordConfirm;
    }

    public Long getid() {
        return id;
    }

    public void setid(long id){
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getCountry(){return country; }

    public void setCountry(String country){
        this.country = country;
    }
}
