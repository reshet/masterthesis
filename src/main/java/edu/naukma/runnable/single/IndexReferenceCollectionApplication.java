package edu.naukma.runnable.single;

import edu.naukma.reshet.configuration.MongoConfiguration;
import edu.naukma.reshet.core.PdfDirectoryIndexer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@PropertySource(value = "classpath:init.properties")
@Import(MongoConfiguration.class)
@ContextConfiguration(classes = IndexReferenceCollectionApplication.class)
@ComponentScan(basePackages = {"edu.naukma.reshet.core"})
public class IndexReferenceCollectionApplication {
    static String path = "/Users/user/naukma/";
    @Bean
    public static PropertySourcesPlaceholderConfigurer getConfigurator(){
        return new PropertySourcesPlaceholderConfigurer();
    }
    public static PdfDirectoryIndexer indexer(String collectionPath, String name){
        return new PdfDirectoryIndexer(path + collectionPath, path + "index/", name);
    }

    public static void main(String args[]){
       //buildReferenceCollection();
       //buildComputerScienceCollection();
       //buildPhilosophyCollection();
        //buildBioTechCollection();
       buildSociologyCollection();
    }
    private static void buildReferenceCollection(){
        System.out.println("Reference collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("pdfs/","science");
        indexer.indexDirectoryWithPdfs();
    }
    private static void buildComputerScienceCollection(){
        System.out.println("CS collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("thesauri/computerscience/", "computerscience");
        indexer.indexDirectoryWithPdfs();
    }
    private static void buildPhilosophyCollection(){
        System.out.println("Philosophy collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("thesauri/philosophy/", "philosophy");
        indexer.indexDirectoryWithPdfs();
    }
    private static void buildBioTechCollection(){
        System.out.println("Philosophy collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("thesauri/biotech/", "biotech");
        indexer.indexDirectoryWithPdfs();
    }
    private static void buildSociologyCollection(){
        System.out.println("Philosophy collection documentary frequency builder application");
        PdfDirectoryIndexer indexer = indexer("thesauri/sociology/", "sociology");
        indexer.indexDirectoryWithPdfs();
    }
}
