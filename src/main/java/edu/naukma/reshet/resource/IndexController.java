package edu.naukma.reshet.resource;

import edu.naukma.reshet.model.dto.IndexDTO;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import edu.naukma.reshet.orchestration.IndexFacade;
import edu.naukma.reshet.shared.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping("/index")
public class IndexController {
  @Autowired
  private IndexFacade indexFacade;

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
    if(indexFacade.createTermIndex(name) != null){
      return new ResponseEntity<IndexDTO>(index, HttpStatus.CREATED);
    }
    return new ResponseEntity<IndexDTO>(index, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
  @ResponseBody
  public HttpEntity<IndexDTO> deleteIndex(@PathVariable String name){
    IndexDTO index = new IndexDTO(0L, name);
    index.add(linkTo(methodOn(IndexController.class).getIndexGen()).withSelfRel());
    if(indexFacade.deleteTermIndex(name)){
      return new ResponseEntity<IndexDTO>(index, HttpStatus.OK);
    }
    return new ResponseEntity<IndexDTO>(index, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @RequestMapping(value="/{name}/upload", method=RequestMethod.GET)
  public @ResponseBody String provideUploadInfo(@PathVariable String name) {
    return "You can upload a file to index " + name + " by posting to this same URL.";
  }

  @RequestMapping(value="/{indexname}/upload", method=RequestMethod.POST)
  public @ResponseBody String handleFileUpload(@PathVariable String indexname, @RequestParam("name") String name,
                                               @RequestParam("file") MultipartFile file){
    if (!file.isEmpty()) {
      try {
        byte[] bytes = file.getBytes();
        if(indexFacade.addDocument(indexname, name, bytes)){
          return "You successfully uploaded " + name + " into " + indexname + "!";
        } else {
          return "You failed to upload " + name + "";
        }
      } catch (Exception e) {
        return "You failed to upload " + name + "";
      }
    } else {
      return "You failed to upload " + name + " because the file was empty.";
    }
  }

}
