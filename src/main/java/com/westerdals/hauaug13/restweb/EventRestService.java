package com.westerdals.hauaug13.restweb;

import com.westerdals.hauaug13.EJB.EventEJB;
import com.westerdals.hauaug13.entity.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;

/**
 * Created by August on 28.05.2016.
 */
@Path("/events")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Stateless
public class EventRestService {

    @PersistenceContext(name = "PG6100_exam")
    private EntityManager entityManager;

    @Context
    private UriInfo uriInfo;

    private EventDTO eventDTO;

    @GET
    public Response getEvent(@QueryParam("id") String id, @QueryParam("attendees") String attendees){
        if (id == null) {
            return Response.status(400).build();
        }
        TypedQuery<Event> query;
        try {
            if(attendees != null && Boolean.parseBoolean(attendees)){
                //Try to return content with attendees
                query = entityManager.createNamedQuery(Event.query_getEventById, Event.class).setParameter("id", Long.parseLong(id));
                Event event = query.getSingleResult();
                if(event == null){
                    return Response.status(404).build();
                }
                eventDTO = new EventDTO(event.getid(), event.getTitle(), event.getCountry());
                eventDTO.setUsersAttended(event.getUsersAttended());
                return Response.ok(eventDTO).build();
            }else{
                query = entityManager.createNamedQuery(Event.query_getEventById, Event.class).setParameter("id", Long.parseLong(id));
                Event event = query.getSingleResult();
                if(event == null){
                    return Response.status(404).build();
                }
                eventDTO = new EventDTO(event.getid(), event.getTitle(), event.getCountry());
                return Response.ok(eventDTO).build();
            }
        }catch(Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/all")
    public Response getAllEventsByCountry(@QueryParam("country") String country, @QueryParam("attendees") String attendees){
        TypedQuery<Event> query;
        if(country == null){
            query = entityManager.createNamedQuery(Event.query_getAllEvents, Event.class);
        }else{
            try {
                query = entityManager.createNamedQuery(Event.query_getAllEventsByCountry, Event.class).setParameter("country", country);
            }catch(Exception e){
                return Response.status(400).build();
            }
        }
        List<Event> eventList = new ArrayList<Event>(query.getResultList());

        List<EventDTO> events = new ArrayList<EventDTO>();
        for(Event event : eventList){
            EventDTO eventDTO = new EventDTO(event.getid(), event.getTitle(), event.getCountry());
            if(attendees != null && Boolean.parseBoolean(attendees))
                eventDTO.setUsersAttended(event.getUsersAttended());
            events.add(eventDTO);
        }

        EventList eventListDTO = new EventList(events);
        return Response.ok(eventListDTO).build();
    }
}

