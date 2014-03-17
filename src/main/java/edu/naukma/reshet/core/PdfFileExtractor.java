package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.TextExtractor;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.PDFBox;
import org.apache.pdfbox.lucene.LucenePDFDocument;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/16/14
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfFileExtractor implements TextExtractor{
  private final String fileName;
  public PdfFileExtractor(String filename){
     this.fileName = filename;
  }
  @Override
  public Document getDocument() throws IOException {
    File f = new File(fileName);
    f.exists();
    Document luceneDocument = LucenePDFDocument.getDocument(f);
    return luceneDocument;
  }
}
