package edu.naukma.reshet.core;

import edu.naukma.reshet.shared.TextExtractor;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/16/14
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PdfFileExtractor implements TextExtractor{
  private String fileName;
  public PdfFileExtractor(){}
  public PdfFileExtractor(String filename){
     this.setFileName(filename);
  }
  @Override
  public Document getDocument() throws IOException {
    File f = new File(getFileName());

    if(f.exists()){
      //Document luceneDocument = LucenePDFDocument.getDocument(f);
      FileInputStream fi = new FileInputStream(f);

      PDFParser parser = new PDFParser(fi);
      parser.parse();
      COSDocument cd = parser.getDocument();
      PDFTextStripper stripper = new PDFTextStripper();
      String text = stripper.getText(new PDDocument(cd));
      Document doc = new Document();
      FieldType type = new FieldType();
      type.setStored(false);
      type.setIndexed(true);
      type.setTokenized(true);
      type.setStoreTermVectors(true);
      type.setStoreTermVectorPositions(true);
      type.setStoreTermVectorOffsets(true);
      doc.add(new Field("content", text, type));
      return doc;
    }
    return null;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
