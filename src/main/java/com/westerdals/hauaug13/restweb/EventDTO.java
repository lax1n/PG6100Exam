package com.westerdals.hauaug13.restweb;

import com.westerdals.hauaug13.entity.Event;
import com.westerdals.hauaug13.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by August on 29.05.2016.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDTO {

    @XmlElement(required = true)
    private long id;

    @XmlElement(required = true)
    private String title;

    @XmlElement(required = true)
    private String country;

    @XmlElement
    private List<User> usersAttended;

    public EventDTO() {
    }

    public EventDTO(long id, String title, String country) {
        this.id = id;
        this.title = title;
        this.country = country;
    }

    public void setUsersAttended(List<User> users) {
        this.usersAttended = users;
    }
}
