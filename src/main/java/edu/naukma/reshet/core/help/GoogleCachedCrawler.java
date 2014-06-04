package edu.naukma.reshet.core.help;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("cached")
public class GoogleCachedCrawler extends GoogleCrawler {

  @Inject
  TerminRepository cacher;

  @Override
  public Double getDocumentaryFrequency(String term) {
    Termin termin = cacher.findByText(term);
    System.out.println("Looking for cached docFreq of term: "+term);
    if (termin == null){
      Double docFreq = super.getDocumentaryFrequency(term);
      termin = new Termin(term, docFreq);
      cacher.save(termin);
    }
    return termin.getDocFrequency();
  }
}