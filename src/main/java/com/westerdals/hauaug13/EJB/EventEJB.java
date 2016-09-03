package com.westerdals.hauaug13.EJB;

import com.westerdals.hauaug13.entity.Event;
import com.westerdals.hauaug13.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;

/**
 * Created by August on 28.05.2016.
 */
@Stateless
public class EventEJB {

    @PersistenceContext(name = "PG6100_exam")
    private EntityManager entityManager;

    public EventEJB(){
    }
    public EventEJB(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Event createEvent(Event event) {
        try {
            entityManager.persist(event);
            return event;
        } catch (Exception e) {
            return null;
        }
    }

    public Event getEventById(long id) {
        try {
            Query findById = entityManager.createNamedQuery(Event.query_getEventById, Event.class).setParameter("id", id);
            return (Event) findById.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Event> getAllEvents() {
        try {
            Query findById = entityManager.createNamedQuery(Event.query_getAllEvents, Event.class);
            return findById.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public List<Event> getAllEventsByCountry(String country) {
        try {
            Query findById = entityManager.createNamedQuery(Event.query_getAllEventsByCountry, Event.class).setParameter("country", country);
            return findById.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Event updateEvent(Event event){
        return entityManager.merge(event);
    }

    public boolean deleteEvent(Event event) {
        try {
            entityManager.remove(entityManager.contains(event) ? event : entityManager.merge(event));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
