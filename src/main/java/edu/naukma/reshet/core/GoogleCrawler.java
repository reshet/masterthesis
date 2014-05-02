package edu.naukma.reshet.core;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.fasterxml.jackson.core.JsonFactory;
import edu.naukma.reshet.model.GoogleSearchResult;
import edu.naukma.reshet.model.Page;
import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class GoogleCrawler implements DocumentaryFrequencyCrawler {
  //private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private Long httpPlainCrawler(String term){
    try {
      String query = "https://www.googleapis.com/customsearch/v1?q="+
              term+
              "&cx="+URLEncoder.encode("002125048308312569436:ezbjh_ejg_e", "UTF-8")+
              "&fields="+URLEncoder.encode("searchInformation/totalResults", "UTF-8")+
              "&key=AIzaSyDVmwTtdEmD7fQN2Jt2pTIifYI5WaLPnUM";
     /* URL url = new URL(
              "https://www.googleapis.com/customsearch/v1?q=flowers&cx=002125048308312569436%3Aezbjh_ejg_e&key=AIzaSyDVmwTtdEmD7fQN2Jt2pTIifYI5WaLPnUM");
     */
      URL url = new URL(query);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      BufferedReader br = new BufferedReader(new InputStreamReader(
              (conn.getInputStream())));

      String output;
      System.out.println("Output from Server .... \n");
      StringBuilder strB = new StringBuilder();
      while ((output = br.readLine()) != null) {
        System.out.println(output);
        strB.append(output);
      }

      conn.disconnect();
      ObjectMapper mapper = new ObjectMapper();
      org.codehaus.jackson.JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use mapper.getFactory() instead
      org.codehaus.jackson.JsonParser jp = factory.createJsonParser(strB.toString());
      JsonNode actualObj = mapper.readTree(jp);
      String str = actualObj.get("searchInformation").get("totalResults").asText();
      System.out.print(str);
      return Long.parseLong(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0L;
  }
  private void restCrawler(String term){
    HttpComponentsClientHttpRequestFactory f = new HttpComponentsClientHttpRequestFactory();
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(f);
   /* String qu2ry = "https://www.googleapis.com/customsearch/v1?q="+term+"&cx=002125048308312569436:ezbjh_ejg_e"+
            "&fields=searchInformation/totalResults"+
            "&key=AIzaSyDVmwTtdEmD7fQN2Jt2pTIifYI5WaLPnUM";
   */
    String json = null;
    try {
      String query = "https://www.googleapis.com/customsearch/v1?q="+
              term+
              "&cx="+URLEncoder.encode("002125048308312569436:ezbjh_ejg_e", "UTF-8")+
              "&fields="+URLEncoder.encode("searchInformation/totalResults", "UTF-8")+
              "&key=AIzaSyDVmwTtdEmD7fQN2Jt2pTIifYI5WaLPnUM";
      json = restTemplate.getForObject(query, String.class);
      System.out.println(json);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    //GoogleSearchResult result = restTemplate.getForObject(query, GoogleSearchResult.class);
    //System.out.println(term+": "+result.getTotalResults());

  }

  private void crFB(){
    RestTemplate restTemplate = new RestTemplate();
    Page page = restTemplate.getForObject("http://graph.facebook.com/gopivotal", Page.class);
    System.out.println("Name:    " + page.getName());
    System.out.println("About:   " + page.getAbout());
    System.out.println("Phone:   " + page.getPhone());
    System.out.println("Website: " + page.getWebsite());
  }
  @Override
  public Long getDocumentaryFrequency(String term) {
    //restCrawler(term);
    //crFB();
    return httpPlainCrawler(term);
    //return 22L;
  }
}