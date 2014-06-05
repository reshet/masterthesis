package edu.naukma.runnable.single;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Sets;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.AdvancedTextSearcher;
import edu.naukma.reshet.core.PdfDirectoryIndexer;
import edu.naukma.reshet.core.SimpleTextSearcher;
import edu.naukma.reshet.core.algorithm.RelationFinder;
import edu.naukma.reshet.core.algorithm.SnippetsFinder;
import edu.naukma.reshet.core.algorithm.TopTfIdfInitialTerminologyNounExtractor;
import edu.naukma.reshet.model.Snippet;
import edu.naukma.reshet.model.TermInDoc;
import edu.naukma.reshet.model.TermRelation;
import edu.naukma.reshet.model.Termin;
import edu.naukma.reshet.repositories.TermInDocRepository;
import edu.naukma.reshet.repositories.TermRelationRepository;
import edu.naukma.reshet.repositories.TerminRepository;
import edu.naukma.reshet.shared.Searcher2;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = FullProcessCollectionApplication.class)
@ComponentScan(basePackages = {
        "edu.naukma.reshet.core.model",
        "edu.naukma.reshet.core.algorithm",
        "edu.naukma.reshet.core.help",
        "edu.naukma.reshet.repositories"
})
@EnableAutoConfiguration
public class FullProcessCollectionApplication implements CommandLineRunner{
    static String path = "/Users/user/naukma/";
    @Bean
    public static PropertySourcesPlaceholderConfigurer getConfigurator(){
        return new PropertySourcesPlaceholderConfigurer();
    }
    public static PdfDirectoryIndexer indexer(String collectionPath, String name){
        return new PdfDirectoryIndexer(path + collectionPath, path + "index/", name);
    }

    public static void main(String args[]){
        SpringApplication.run(FullProcessCollectionApplication.class, args);
    }

    @Autowired
    TerminRepository repo;
    @Autowired
    TermInDocRepository repoTermInDoc;
    @Autowired
    TermRelationRepository repoRelation;
    @Autowired
    TopTfIdfInitialTerminologyNounExtractor extractor;

    private Lemmatizer lemmatizer;

    private void extractCollectionRelations(String indexName){
        long startTime = System.currentTimeMillis();
        Searcher2 searcher = new AdvancedTextSearcher(path + "index/"+indexName+"/lucene/");
        SnippetsFinder snipFinder = new SnippetsFinder();
        RelationFinder relFinder = new RelationFinder();
        relFinder.setLm(lemmatizer);
        snipFinder.setLm(lemmatizer);
        snipFinder.setSearcher(searcher);

        List<TermInDoc> termsAll = repoTermInDoc.findByIndex(indexName);
        List<Snippet> snippets = snipFinder.findSnippets(termsAll);
        Set<TermRelation> relations = relFinder.getRelations(termsAll, snippets);
        Set<TermRelation> validRelations = prepareRelations(relations, indexName);
        repoRelation.save(validRelations);
        long endTime = System.currentTimeMillis();
        System.out.println("Time spent: " + (endTime - startTime) / 1000 + " sec.");
    }
    private Set<TermRelation> prepareRelations(Set<TermRelation> relations, String index){
        final Set<String> uniqueRelationsPool = Sets.newHashSet();
        final Set<TermRelation> validRelations = Sets.newHashSet();
        for(TermRelation relation: relations){
            String hash = relation.getRelationType() + "|" +
                    relation.getTerm1().getTermin().getText() + "|" +
                    relation.getTerm2().getTermin().getText();
            if (!uniqueRelationsPool.contains(hash)) {
                uniqueRelationsPool.add(hash);
                String termin1 = relation.getTerm1().getTermin().getText();
                String termin2 = relation.getTerm2().getTermin().getText();
                Termin term1 = repo.findByText(termin1);
                Termin term2 = repo.findByText(termin2);
                if (term1 == null) {
                    repo.save(relation.getTerm1().getTermin());
                    term1 = repo.findByText(termin1);
                }
                if (term2 == null) {
                    repo.save(relation.getTerm2().getTermin());
                    term2 = repo.findByText(termin2);
                }
                relation.getTerm1().setTermin(term1);
                relation.getTerm2().setTermin(term2);
                TermInDoc t1 = repoTermInDoc.findByTermin(term1);
                TermInDoc t2 = repoTermInDoc.findByTermin(term2);
                if(t1 == null){
                    repoTermInDoc.save(relation.getTerm1());
                    t1 = repoTermInDoc.findByTermin(term1);
                }
                if(t2 == null){
                    repoTermInDoc.save(relation.getTerm2());
                    t2 = repoTermInDoc.findByTermin(term2);
                }


                TermRelation validRelation = new TermRelation(
                        t1,
                        t2,
                        relation.getRelationType(), index);
                validRelations.add(validRelation);
            }
        }
        return validRelations;
    }

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

    private void extractCollectionTerms(String indexName){
        SimpleTextSearcher searcher = new SimpleTextSearcher(path + "index/"+indexName+"/lucene/", indexName);
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
                        if(termInDoc == null) return false;
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

    private static Map<String, Double> filterTerms(Map<String, Double> map){
        return ImmutableMap.copyOf(Maps.filterEntries(map, new Predicate<Map.Entry<String, Double>>() {
            @Override
            public boolean apply(@Nullable Map.Entry<String, Double> entry) {
                if (entry.getKey().length() <= 2) return false;
                if (!entry.getKey().matches("^[А-ЯІа-яі\u0027-]+$")) return false;
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

    private void indexPdfCollection(String collection){
        System.out.println(collection + " collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("thesauri/"+collection+"/", collection);
        indexer.indexDirectoryWithPdfs();
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            lemmatizer = LemmatizerFactory.getPrebuilt("mlteast-uk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(strings.length>0){
            String collection = strings[0];
            indexPdfCollection(collection);
            extractCollectionTerms(collection);
            extractCollectionRelations(collection);
        } else {
            System.out.println("Abort: please enter colleciton name");
        }
    }
}
