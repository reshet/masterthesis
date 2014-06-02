package edu.naukma.reshet.integration.single;


import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Sets;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.AdvancedTextSearcher;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
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
@ContextConfiguration(classes = FindRelationsInCollectionApplication.class)
@ComponentScan(basePackages = {
        "edu.naukma.reshet.core.model",
        "edu.naukma.reshet.core.algorithm",
        "edu.naukma.reshet.core.help",
        "edu.naukma.reshet.repositories"
})
@EnableAutoConfiguration
public class FindRelationsInCollectionApplication {
    static String path = "/Users/user/naukma/";

    @Autowired
    TerminRepository repo;
    @Autowired
    TermInDocRepository repoTermInDoc;
    @Autowired
    TermRelationRepository repoRelation;

    private Lemmatizer lemmatizer;

    private void extractCollectionRelations(String indexPath, String indexName){
        long startTime = System.currentTimeMillis();
        Searcher2 searcher = new AdvancedTextSearcher(path + indexPath);
        SnippetsFinder snipFinder = new SnippetsFinder();
        RelationFinder relFinder = new RelationFinder();
        relFinder.setLm(lemmatizer);
        snipFinder.setLm(lemmatizer);
        snipFinder.setSearcher(searcher);

        List<TermInDoc> termsAll = repoTermInDoc.findByIndex(indexName);
        List<Snippet> snippets = snipFinder.findSnippets(termsAll);
        Set<TermRelation> relations = relFinder.getRelations(termsAll, snippets);
        Set<TermRelation> validRelations = prepareRelations(relations);
        repoRelation.save(validRelations);
        long endTime = System.currentTimeMillis();
        System.out.println("Time spent: " + (endTime - startTime) / 1000 + " sec.");
    }
    private Set<TermRelation> prepareRelations(Set<TermRelation> relations){
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
                if (repo.findByText(termin1) == null) {
                    repo.save(relation.getTerm1().getTermin());
                }
                if (repo.findByText(termin2) == null) {
                    repo.save(relation.getTerm2().getTermin());
                }
                Termin term1 = repo.findByText(termin1);
                Termin term2 = repo.findByText(termin2);
                relation.getTerm1().setTermin(term1);
                relation.getTerm2().setTermin(term2);
                repoTermInDoc.save(relation.getTerm1());
                repoTermInDoc.save(relation.getTerm2());
                TermRelation validRelation = new TermRelation(
                        repoTermInDoc.findByTermin(term1),
                        repoTermInDoc.findByTermin(term2),
                        relation.getRelationType());
                validRelations.add(validRelation);
            }
        }
       return validRelations;
    }
    @PostConstruct
    public void application(){
        try {
            lemmatizer = LemmatizerFactory.getPrebuilt("mlteast-uk");
        } catch (IOException e) {
            e.printStackTrace();
        }
       extractCollectionRelations("index/computerscience/lucene/", "computerscience");
       //extractCollectionRelations("index/philosophy/lucene/", "philosophy");
    }
    public static void main(String args[]){
      SpringApplication.run(FindRelationsInCollectionApplication.class, args);
    }
}
