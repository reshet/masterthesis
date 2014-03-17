package edu.naukma.reshet.resource;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import edu.naukma.reshet.shared.FileManager;
import edu.naukma.reshet.shared.Indexer;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

  @Autowired
  Indexer indexer;

  @Autowired
  FileManager fileManager;
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

  @POST
  @Path("/index")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  public Response putIndexDocs(byte[] payload) {
    System.out.println("Payload: " + new String(payload));
    return Response.ok().build();
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response putIndexDocsForm(@FormDataParam("file") InputStream uploadedInputStream,
                                   @FormDataParam("file") FormDataContentDisposition fileDetail) {
    String uploadedFileLocation = "store/" + fileDetail.getFileName();
    fileManager.writeToFile(uploadedInputStream, uploadedFileLocation);
    String output = "File uploaded to : " + uploadedFileLocation;
    return Response.status(200).entity(output).build();
  }
}
