package edu.naukma.reshet.integration.single;

import com.google.common.collect.FluentIterable;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.IndexerFactory;
import edu.naukma.reshet.core.PdfDirectoryIndexer;
import edu.naukma.reshet.core.SimpleTextSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = ExtractDocFreqTermsApplication.class)
public class ExtractDocFreqTermsApplication {
    static String path = "/Users/user/naukma/";

    public static void main(String args[]){
      System.out.println("Reference collection documentary frequency extractor application");
      SimpleTextSearcher searcher = new SimpleTextSearcher(path + "index/science/lucene/");
      Map<String, Double> map = searcher.getTermsIDFs();
      List<String> list = sortByValue(map);
      List<String> top = FluentIterable.from(list).limit(500).toList();
      for(String key: top){
          System.out.println(map.get(key)+" : "+key);
      }
      System.out.println("IDFs calculated.");

    }

    public static List sortByValue(final Map m) {
        List<String> keys = new ArrayList<String>();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                } else {
                    return 0;
                }
            }
        });
        return keys;
    }
}
