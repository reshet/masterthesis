package edu.naukma.reshet.shared;

import org.apache.lucene.document.Document;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/16/14
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TextExtractor {
   Document getDocument() throws IOException;
}
