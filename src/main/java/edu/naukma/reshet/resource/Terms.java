package edu.naukma.reshet.resource;

import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Terms {
  @RequestMapping("/all")
  public @ResponseBody String getAll(){
    return "all terms resp";
  }

}
