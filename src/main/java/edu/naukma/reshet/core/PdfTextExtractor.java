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
import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfTextExtractor implements TextExtractor{
  private final File file;
  public PdfTextExtractor(File file){
     this.file = file;
  }
  @Override
  public Document getDocument() {
    if(file.exists()){
      //Document luceneDocument = LucenePDFDocument.getDocument(f);
      try {
        FileInputStream fi = new FileInputStream(file);
        PDFParser parser = new PDFParser(fi);
        parser.parse();
        COSDocument cd = parser.getDocument();
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(new PDDocument(cd));
        Document doc = new Document();
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexed(true);
        type.setTokenized(true);
        type.setStoreTermVectors(true);
        type.setStoreTermVectorPositions(true);
        type.setStoreTermVectorOffsets(true);
        doc.add(new Field("content", text, type));
        return doc;
      } catch (IOException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
    return null;
  }
}
