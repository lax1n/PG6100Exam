package com.westerdals.hauaug13.restweb;

import com.westerdals.hauaug13.entity.Event;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by August on 28.05.2016.
 */
@XmlRootElement
@XmlSeeAlso(Event.class)
public class EventList extends ArrayList<EventDTO> {
    public EventList(){
        super();
    }
    public EventList(Collection<? extends EventDTO> c){
        super(c);
    }

    @XmlElement(name= "event")
    public List<EventDTO> getEventList(){
        return this;
    }

    public void setEventList(List<EventDTO> eventList){
        this.addAll(eventList);
    }
}
