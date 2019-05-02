
package org.unlitrodeluzcolombia.mediamanager.ws;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
@Path("main")
public class LitroDeLuzMediaManagerResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LitroDeLuzMediaManagerResource
     */
    public LitroDeLuzMediaManagerResource() {
    }

    /**
     * Retrieves representation of an instance of org.unlitrodeluzcolombia.mediamanager.ws.LitroDeLuzMediaManagerResource
     * @return an instance of java.lang.String
     */
    @GET
//    @Produces(MediaType.)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of LitroDeLuzMediaManagerResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
