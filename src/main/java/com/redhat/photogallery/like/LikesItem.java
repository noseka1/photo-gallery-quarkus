package com.redhat.photogallery.like;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class LikesItem extends PanacheEntityBase {

    @Id
    public Long id;
    public int likes;

    @Override
    public String toString() {
        return "LikesItem [id=" + id + ", likes=" + likes + "]";
    }

}
