package edu.naukma.reshet.core;

import com.github.fakemongo.impl.index.IndexFactory;
import edu.naukma.reshet.shared.TextExtractor;
import org.apache.log4j.Appender;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PdfDirectoryIndexer{
  Logger logger = Logger.getLogger("IndexLog");
  IndexerFactory indexerFactory;
  private String pathToFiles;
    private String indexName;
    private String pathToIndex;
  private FileIndexManager indexManager;
  public PdfDirectoryIndexer(){}
  public PdfDirectoryIndexer(String pathToFiles, String pathToIndex, String indexName){
      this.pathToIndex = pathToIndex;
      this.pathToFiles = pathToFiles;
      this.indexName = indexName;
      this.indexManager = new FileIndexManager();
      this.indexManager.setIndexPath(pathToIndex);
      this.indexerFactory = new IndexerFactory();
      this.indexerFactory.setIndexPath(pathToIndex);
      try {
          FileHandler fh = new FileHandler( pathToIndex + "/index.log");
          SimpleFormatter formatter = new SimpleFormatter();
          fh.setFormatter(formatter);
          logger.addHandler(fh);
      } catch (SecurityException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  public void indexDirectoryWithPdfs(){
      long startTime = System.currentTimeMillis();
      logger.info("Started to index pdf files in directory " + pathToFiles);
      logger.info("Index path " + pathToIndex);
      System.out.println("Started to index pdf files in directory " + pathToFiles);
      System.out.println("Index path " + pathToIndex);
      indexManager.createIndex(indexName);
      final SimpleIndexer indexer = new SimpleIndexer(indexerFactory.getIndexWriter(this.indexName));
      Path directory = Paths.get(pathToFiles);
      try {
          Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                  logger.info("Document " + file.getParent()+"/"+file.getFileName() + " start parsing pdf.");
                  //System.out.println(new Date() + " __ Document " + file.getParent()+file.getFileName() + " start parsing pdf.");
                  Document doc = new PdfTextExtractor(file.toFile()).getDocument();
                  if(doc != null){
                      logger.info("Document " + file.getParent()+"/"+file.getFileName() + " start indexing.");
                      int docId = indexer.indexDocument(doc);
                      //System.out.println(new Date() + " __ Document " + file.getParent()+file.getFileName() + " indexed as doc " + docId + " in index " + indexName);
                      logger.info("Document " + file.getParent()+"/"+file.getFileName() + " indexed as doc " + docId + " in index " + indexName);
                  }
                  return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                  return FileVisitResult.CONTINUE;
              }

          });
      } catch (IOException e) {
          e.printStackTrace();
      }

      long endTime = System.currentTimeMillis();
      logger.info("Indexing finished. TIME SPENT: " + (endTime - startTime) / 1000.0 + " seconds");
      //System.out.println("Indexing finished. TIME SPENT: " + (endTime - startTime) / 1000.0 + " seconds");
    }
}
