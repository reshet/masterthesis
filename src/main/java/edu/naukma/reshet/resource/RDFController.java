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
            .toList();
    List<TermRelation> relations = FluentIterable
            .from(repoRelations.findAll(new PageRequest(0,200)).getContent())
            .toList();
    RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.BASIC);

    return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
  }
    @RequestMapping(value = "/{name}/compact", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<RdfGraph> getRDFcompact(@PathVariable String name){
        List<TermInDoc> terms = FluentIterable
                .from(repoTerms.findByIndex(name))
                .toList();
        List<TermRelation> relations = FluentIterable
                .from(repoRelations.findByIndex(name))
                .toList();
        RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.COMPACT);
        return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
    }
    @RequestMapping(value = "/{name}/human", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<RdfGraph> getRDFhuman(@PathVariable String name){
        List<TermInDoc> terms = FluentIterable
                .from(repoTerms.findByIndex(name))
                .toList();
        List<TermRelation> relations = FluentIterable
                .from(repoRelations.findByIndex(name))
                .toList();
        RdfGraph graph = new RdfGraph(terms,relations, Thesaurus.Type.HUMAN);
        return new ResponseEntity<RdfGraph>(graph, HttpStatus.OK);
    }
}
