package com.westerdals.hauaug13.EJB;

/**
 * Created by August on 27.05.2016.
 */
import org.apache.commons.codec.digest.*;
import com.westerdals.hauaug13.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;


@Stateless
public class UserEJB {

    @PersistenceContext(name = "PG6100_exam")
    private EntityManager entityManager;

    public UserEJB() {
    }

    public UserEJB(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User createUser(User user, String password) {
        if (user == null || password == null || password.isEmpty()) {
            return null;
        }
        if (password.length() > 64) {
            return null;
        }
        if (getUserByUsername(user.getUsername()) != null) {
            return null;
        }

        String hash = getHash(user, password);

        user.setHash(hash);

        try {
            entityManager.persist(user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean login(String id, String password){
        if (id == null || id.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User userDetails = getUser(id);
        if (userDetails == null) {
            return false;
        }

        String hash = generateHash(password, userDetails.getSalt());

        return hash.equals(userDetails.getHash());
    }
    public User getUser(String id){
        return entityManager.find(User.class, id);
    }

    public String getHash(User user, String password){
        String salt = getSalt();
        user.setSalt(salt);

        return generateHash(password, user.getSalt());
    }

    public User getUserById(long id) {
        try {
            Query findById = entityManager.createNamedQuery(User.query_getUserById, User.class).setParameter("id", id);
            return (User) findById.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            Query findById = entityManager.createNamedQuery(User.query_getAllUsers, User.class);
            return findById.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            Query findById = entityManager.createNamedQuery(User.query_getUserByUsername, User.class).setParameter("username", username);
            return (User) findById.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteUser(User user) {
        try {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User updateUser(User user) {
        entityManager.merge(user);
        return user;
    }

    @NotNull
    public static String generateHash(String password, String salt){
        return DigestUtils.sha256Hex(password + salt);
    }


    @NotNull
    protected String getSalt() {
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        return salt;
    }
}

