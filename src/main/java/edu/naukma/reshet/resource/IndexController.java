package edu.naukma.reshet.resource;

import edu.naukma.reshet.model.dto.IndexDTO;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import edu.naukma.reshet.shared.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class IndexController {
  @Autowired
  private IndexManager indexManager;
  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public HttpEntity<IndexDTO> getIndexGen(){
    IndexDTO index = new IndexDTO(0L, "generic");
    index.add(linkTo(methodOn(IndexController.class).getIndexGen()).withSelfRel());
    return new ResponseEntity<IndexDTO>(index, HttpStatus.OK);
  }

  @RequestMapping(value = "/{name}", method = RequestMethod.POST)
  @ResponseBody
  public HttpEntity<IndexDTO> createIndex(@PathVariable String name){
    IndexDTO index = new IndexDTO(0L, name);
    index.add(linkTo(methodOn(IndexController.class).getIndexGen()).withSelfRel());
    if(indexManager.createIndex(name)){
      return new ResponseEntity<IndexDTO>(index, HttpStatus.CREATED);
    }
    return new ResponseEntity<IndexDTO>(index, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
