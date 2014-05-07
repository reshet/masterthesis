package edu.naukma.reshet.core;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Component
public class GoogleCachedCrawler extends GoogleCrawler{

  @Inject
  TerminRepository cacher;

  @Override
  public Long getDocumentaryFrequency(String term) {
    Termin termin = cacher.findByText(term);
    System.out.println("Looking for cached docFreq of term: "+term);
    if (termin == null){
      Long docFreq = super.getDocumentaryFrequency(term);
      termin = new Termin(term, docFreq);
      cacher.save(termin);
    }
    return termin.getDocFrequency();
  }
}