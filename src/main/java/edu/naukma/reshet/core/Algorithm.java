package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.Indexer;
import edu.naukma.reshet.shared.TextExtractor;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/17/14
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Algorithm {
  public Indexer getIndexer() {
    return indexer;
  }

  public TextExtractor getExtractor() {
    return extractor;
  }

  static class AlgorithmBuilder{
     private Algorithm alg;
     private AlgorithmBuilder(){
       alg = new Algorithm();
     }
    public AlgorithmBuilder withIndexer(Indexer indexer){
       alg.indexer = indexer;
       return this;
    }
    public AlgorithmBuilder withTextExtractor(TextExtractor extractor){
      alg.extractor = extractor;
      return this;
    }
    public Algorithm build(){
      return alg;
    }
  }

  private Indexer indexer;
  private TextExtractor extractor;
  private Algorithm(){}
  public static AlgorithmBuilder builder(){
    return new AlgorithmBuilder();
  }

  public void run(){
     System.out.println("Running text extractor "+extractor.getClass().getName());
     System.out.println("Running indexer "+indexer.getClass().getName());
  }

}


