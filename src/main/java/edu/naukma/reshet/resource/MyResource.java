package edu.naukma.reshet.resource;

import edu.naukma.reshet.shared.Indexer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

  @Autowired
  Indexer indexer;
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getIt() {
      return "Got it!";
  }

  @GET
  @Path("/stepname")
  @Produces(MediaType.TEXT_PLAIN)
  public String getStepName() {
    return indexer.getStepName();
  }

  @PUT
  @Path("/index")
  @Produces(MediaType.TEXT_PLAIN)
  public String putIndexDocs() {
    return indexer.indexDocuments();
  }
}
