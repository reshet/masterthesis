package edu.naukma.reshet.core.help;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component("local")
public class LocalReferenceCrawler implements DocumentaryFrequencyCrawler {
  @Autowired
  TerminRepository repo;
  @Override
  public Double getDocumentaryFrequency(String term) {
    //System.out.println("Searching local reference term index for doc freq of term " + term + "...");
    Termin t = repo.findByText(term);
    if(t == null) return Double.MIN_VALUE;
    return t.getDocFrequency();
  }
}