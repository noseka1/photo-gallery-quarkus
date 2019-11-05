package com.redhat.photogallery.query;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/query")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QueryService {

    private static final Logger LOG = LoggerFactory.getLogger(QueryService.class);

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readCategoryOrderedByLikes(@QueryParam("category") String category) {
        Query query = em.createQuery(
                "SELECT pi.id, pi.name, pi.category, li.likes " +
                "FROM PhotoItem pi LEFT JOIN LikesItem li ON pi.id = li.id " +
                "WHERE pi.category = :category " +
                "ORDER BY li.likes DESC"
        );
        @SuppressWarnings("unchecked")
        List<QueryItem> items = query.setParameter("category",  category).getResultList();
        LOG.info("Returned {} items in category {}", items.size(), category);
        return Response.ok(new GenericEntity<List<QueryItem>>(items){}).build();
    }
}