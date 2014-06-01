package edu.naukma.reshet.integration.single;


import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Sets;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.core.algorithm.TopTfIdfInitialTerminologyNounExtractor;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = ExtractDocFreqTermsApplication.class)
@ComponentScan(basePackages = {
        "edu.naukma.reshet.core.model",
        "edu.naukma.reshet.core.algorithm",
        "edu.naukma.reshet.core.help",
        "edu.naukma.reshet.repositories"
})
@EnableAutoConfiguration
public class ExtractDocFreqTermsApplication {
    static String path = "/Users/user/naukma/";


    @Autowired
    TerminRepository repo;
    @Autowired
    TermInDocRepository repoTermInDoc;



    @Autowired
    TopTfIdfInitialTerminologyNounExtractor extractor;

    private Map<String, Double> getCollectionTerms(String indexPath){
        System.out.println("Reference collection documentary frequency extractor application");
        SimpleTextSearcher searcher = new SimpleTextSearcher(path + indexPath, "science");
        Map<String, Double> map = searcher.getTermsIDFs();
        Map<String, Double> filtered_map = filterTerms(map);
        System.out.println("Original map size:" + map.size());
        System.out.println("Filtered map size:" + filtered_map.size());
        return filtered_map;
    }
    private void saveReferenceTerms(){
        Map<String, Double> map = getCollectionTerms("index/science/lucene/");
        List<String> list = sortByValue(map);
        for(String key: list){
            repo.save(new Termin(key, map.get(key)));
        }
        System.out.println("IDFs calculated.");
    }

    private void extractCollectionTerms(String indexPath, String indexName){
        SimpleTextSearcher searcher = new SimpleTextSearcher(path + indexPath, indexName);
        int totalDocs = searcher.getIndexReader().numDocs();
        List<TermInDoc> allTerms = Lists.newLinkedList();
        for(int i = 0; i < totalDocs; i++){
           List<TermInDoc> terms =  extractor.extractValuableTerms(searcher, i);
           System.out.println("Terms count: " + terms.size());
           allTerms.addAll(terms);
        }
        List<TermInDoc> top = Ordering
                .natural()
                .reverse()
                .immutableSortedCopy(allTerms);
        System.out.println("All Terms count: " + top.size());
        List<TermInDoc> topSmall = limitList(top, 0.3);
        System.out.println("Small terms count: " + topSmall.size());
        repoTermInDoc.save(topSmall);
//        for(TermInDoc tInDoc: topSmall){
//            System.out.println(tInDoc.getTermin().getText());
//        }
    }
    private static List<TermInDoc> limitList(List<TermInDoc> list, double factor){
        final Set<String> uniqueTerms = Sets.newHashSet();
        List<TermInDoc> uniqueList = FluentIterable.from(list)
            .filter(new Predicate<TermInDoc>() {
                @Override
                public boolean apply(@Nullable TermInDoc termInDoc) {
                    if (!uniqueTerms.contains(termInDoc.getTermin().getText())) {
                        uniqueTerms.add(termInDoc.getTermin().getText());
                        return true;
                    }
                    return false;
                }
            }).toList();
        return FluentIterable.from(uniqueList)
                .limit((int) Math.round(list.size() * factor))
                .toList();

    }
    @PostConstruct
    public void application(){
       //saveReferenceTerms();
       extractCollectionTerms("index/computerscience/lucene/","computerscience");
       extractCollectionTerms("index/philosophy/lucene/","philosophy");
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

    public static List<String> sortByValue(final Map m) {
        List<String> keys = new ArrayList<String>();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
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
