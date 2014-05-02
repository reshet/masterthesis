package edu.naukma.reshet.core;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
import edu.naukma.reshet.model.Page;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import edu.naukma.reshet.shared.MongoCache;
import jersey.repackaged.com.google.common.base.Optional;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component(value = "cached")
public class GoogleCachedCrawler extends GoogleCrawler{

  @Autowired
  MongoCache cacher;
  @Override
  public Long getDocumentaryFrequency(String term) {
    Termin termin = cacher.findTerm(term);
    System.out.println("Looking for cached docFreq of term: "+term);
    if (termin == null){
      Long docFreq = super.getDocumentaryFrequency(term);
      termin = new Termin(term, docFreq);
      cacher.saveTerm(termin);
    }
    return termin.getDocFrequency();
  }
}