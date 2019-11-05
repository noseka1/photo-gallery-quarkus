package com.redhat.photogallery.like;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/likes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LikeService {

    private static final Logger LOG = LoggerFactory.getLogger(LikeService.class);

    @POST
    @Transactional
    public void addLikes(LikesItem item) {
        LikesItem savedItem = LikesItem.findById(item.id);
        if (savedItem == null) {
            savedItem = item;
        }
        else {
            int likes = savedItem.likes + item.likes;
            savedItem.likes = likes;
        }
        savedItem.persist();
        LOG.info("Updated in data store {}", savedItem);
    }

    @GET
    public Response readAllLikes() {
        List<LikesItem> items = LikesItem.listAll();
        LOG.info("Returned all {} items", items.size());
        return Response.ok(new GenericEntity<List<LikesItem>>(items){}).build();
    }
}