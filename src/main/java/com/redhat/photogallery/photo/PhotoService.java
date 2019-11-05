package com.redhat.photogallery.photo;

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

@Path("/photos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoService.class);

    @POST
    @Transactional
    public Long createPhoto(PhotoItem item) {
        item.persist();
        LOG.info("Added {} into the data store", item);
        return item.id;
    }

    @GET
    public Response readAllPhotos() {
        List<PhotoItem> items = PhotoItem.listAll();
        LOG.info("Returned all {} items", items.size());
        return Response.ok(new GenericEntity<List<PhotoItem>>(items){}).build();
    }
}