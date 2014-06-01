package edu.naukma.reshet.core.help;

//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import edu.naukma.reshet.shared.DocumentaryFrequencyCrawler;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class GoogleCrawler implements DocumentaryFrequencyCrawler {
  private Double httpPlainCrawler(String term){
    try {
      String query = "https://www.googleapis.com/customsearch/v1?q="+
              term+
              "&cx="+URLEncoder.encode("002125048308312569436:ezbjh_ejg_e", "UTF-8")+
              "&fields="+URLEncoder.encode("searchInformation/totalResults", "UTF-8")+
              "&key=AIzaSyDVmwTtdEmD7fQN2Jt2pTIifYI5WaLPnUM";
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
      return new Double(Long.parseLong(str));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0D;
  }
  @Override
  public Double getDocumentaryFrequency(String term) {
    System.out.println("Searching google for doc freq of term " + term + "...");
    return httpPlainCrawler(term);
  }
}