package edu.naukma.reshet.resource;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.collect.FluentIterable;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.dto.IndexDTO;
import edu.naukma.reshet.model.dto.RdfGraph;
import edu.naukma.reshet.model.dto.graph.Thesaurus;
import edu.naukma.reshet.orchestration.IndexFacade;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TermRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping("/rdf")
public class RDFController {
  @Autowired
  TermInDocRepository repoTerms;
  @Autowired
  TermRelationRepository repoRelations;

  @RequestMapping(value = "/{name}", method = RequestMethod.GET)
  @ResponseBody
  public HttpEntity<RdfGraph> getRDF(@PathVariable String name){
    List<TermInDoc> terms = FluentIterable
            .from(repoTerms.findByIndex(name))
            //.limit(10)
            .toList();
    List<TermRelation> relations = FluentIterable
            .from(repoRelations.findAll(new PageRequest(0,200)).getContent())
            //.limit(20)
            .toList();
    RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.BASIC);

    //graph.add(linkTo(methodOn(RDFController.class).getRDF(name)).withSelfRel());
    return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
  }
    @RequestMapping(value = "/{name}/compact", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<RdfGraph> getRDFcompact(@PathVariable String name){
        List<TermInDoc> terms = FluentIterable
                .from(repoTerms.findByIndex(name))
                        //.limit(10)
                .toList();
        List<TermRelation> relations = FluentIterable
                .from(repoRelations.findByIndex(name))
                        //.limit(20)
                .toList();
        RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.COMPACT);

        //graph.add(linkTo(methodOn(RDFController.class).getRDF(name)).withSelfRel());
        return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
    }
    @RequestMapping(value = "/{name}/human", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<RdfGraph> getRDFhuman(@PathVariable String name){
        List<TermInDoc> terms = FluentIterable
                .from(repoTerms.findByIndex(name))
                        //.limit(10)
                .toList();
        List<TermRelation> relations = FluentIterable
                .from(repoRelations.findByIndex(name))
                        //.limit(20)
                .toList();
        RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.HUMAN);

        //graph.add(linkTo(methodOn(RDFController.class).getRDF(name)).withSelfRel());
        return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
    }
//
//  @RequestMapping(value = "/{name}/ld", method = RequestMethod.GET)
//  @ResponseBody
//  public String getRDFLD(@PathVariable String name) throws IOException, JsonLdError {
//    RdfGraph graph = new RdfGraph(Lists.newArrayList(),Lists.newArrayList());
//    Map context = new HashMap();
//// Customise context...
//// Create an instance of JsonLdOptions with the standard JSON-LD options
//    JsonLdOptions options = new JsonLdOptions();
//    options.setProduceGeneralizedRdf(true);
//// Customise options...
//// Call whichever JSONLD function you want! (e.g. compact)
//    Object compact = JsonLdProcessor.compact(graph, context, options);
//// Print out the result (or don't, it's your call!)
////    System.out.println(JsonUtils.toPrettyString(compact));
//    return JsonUtils.toPrettyString(compact);
//  }
//
///*  @RequestMapping(value = "/{name}/ldmy", method = RequestMethod.GET)
//  @ResponseBody
//  public RdfGraph getRDFLDmy(@PathVariable String name) {
//    List<TermInDoc> terms =
//    RdfGraph graph = new RdfGraph(name);
//    return graph;
//  }*/


}
