package com.redhat.photogallery.query;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class QueryItem extends PanacheEntityBase {

    @Id
    public Long id;
    public String name;
    public String category;
    public int likes;

    @Override
    public String toString() {
        return "QueryItem [id=" + id + ", name=" + name + ", category=" + category + ", likes=" + likes + "]";
    }

}
