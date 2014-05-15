package edu.naukma.reshet.resource;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import edu.naukma.reshet.model.dto.IndexDTO;
import edu.naukma.reshet.model.dto.RdfGraph;
import edu.naukma.reshet.orchestration.IndexFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping("/rdf")
public class RDFController {

  @RequestMapping(value = "/{name}", method = RequestMethod.GET)
  @ResponseBody
  public HttpEntity<RdfGraph> getRDF(@PathVariable String name){
    RdfGraph graph = new RdfGraph("content");
    //graph.add(linkTo(methodOn(RDFController.class).getRDF(name)).withSelfRel());
    return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
  }

  @RequestMapping(value = "/{name}/ld", method = RequestMethod.GET)
  @ResponseBody
  public String getRDFLD(@PathVariable String name) throws IOException, JsonLdError {
    RdfGraph graph = new RdfGraph("content");
    Map context = new HashMap();
// Customise context...
// Create an instance of JsonLdOptions with the standard JSON-LD options
    JsonLdOptions options = new JsonLdOptions();
    options.setProduceGeneralizedRdf(true);
// Customise options...
// Call whichever JSONLD function you want! (e.g. compact)
    Object compact = JsonLdProcessor.compact(graph, context, options);
// Print out the result (or don't, it's your call!)
//    System.out.println(JsonUtils.toPrettyString(compact));
    return JsonUtils.toPrettyString(compact);
  }

  @RequestMapping(value = "/{name}/ldmy", method = RequestMethod.GET)
  @ResponseBody
  public RdfGraph getRDFLDmy(@PathVariable String name) {
    RdfGraph graph = new RdfGraph(name);
    return graph;
  }


}
