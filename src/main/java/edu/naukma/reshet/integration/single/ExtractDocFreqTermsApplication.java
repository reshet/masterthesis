package edu.naukma.reshet.integration.single;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.IndexerFactory;
import edu.naukma.reshet.core.PdfDirectoryIndexer;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TerminRepository;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = ExtractDocFreqTermsApplication.class)
@ComponentScan(basePackages = {"edu.naukma.reshet.core.model", "edu.naukma.reshet.repositories"})
@EnableAutoConfiguration
public class ExtractDocFreqTermsApplication {
    static String path = "/Users/user/naukma/";
    @Autowired
    TerminRepository repo;

    @PostConstruct
    public void application(){
        System.out.println("Reference collection documentary frequency extractor application");
        SimpleTextSearcher searcher = new SimpleTextSearcher(path + "index/science/lucene/");
        Map<String, Double> map = searcher.getTermsIDFs();
        Map<String, Double> filtered_map = filterTerms(map);
        System.out.println("Original map size:" +map.size());
        System.out.println("Filtered map size:" + filtered_map.size());
        List<String> list = sortByValue(filtered_map);
        for(String key: list){
           repo.save(new Termin(key, map.get(key)));
        }
//        List<String> top = FluentIterable.from(list).limit(200).toList();
//        for(String key: top){
//            System.out.println(filtered_map.get(key)+" : "+key);
//        }
        System.out.println("IDFs calculated.");
    }
    public static void main(String args[]){
      SpringApplication.run(ExtractDocFreqTermsApplication.class, args);
    }
    private static Map<String, Double> filterTerms(Map<String, Double> map){
       return ImmutableMap.copyOf(Maps.filterEntries(map, new Predicate<Map.Entry<String, Double>>() {
           @Override
           public boolean apply(@Nullable Map.Entry<String, Double> entry) {
               if(entry.getKey().length() <= 2) return false;
               if(!entry.getKey().matches("^[А-ЯІа-яі\u0027-]+$")) return false;
               return true;
           }
       }));
    }

    public static List sortByValue(final Map m) {
        List<String> keys = new ArrayList<String>();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v2 = m.get(o1);
                Object v1 = m.get(o2);
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
