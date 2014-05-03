/*
 * Copyright 2013 Michal Hlavac <hlavki@hlavki.eu>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package integration;

import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.lemmagen.LemmagenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 *
 * @author Michal Hlavac <hlavki@hlavki.eu>
 */
public class LemmagenFilterTest extends BaseTokenStreamTestCase {

    private static final String[] ACTUAL_WORDS = new String[]{"respond", "are", "uninflected", "items", "underlying", "singing"};
    private static final String[] LEMMA_WORDS = new String[]{"respond", "be", "uninflect", "item", "underlie", "sing"};

    @Test
    public void doFilter() throws IOException {
        Analyzer analyzer = getAnalyzer();
        for (int idx = 0; idx < ACTUAL_WORDS.length; idx++) {
            assertAnalyzesTo(analyzer, ACTUAL_WORDS[idx], new String[] {LEMMA_WORDS[idx]});
        }
    }

    private Analyzer getAnalyzer() {
        return new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                StandardTokenizer source = new StandardTokenizer(Version.LUCENE_47, reader);
                LemmagenFilter filter = new LemmagenFilter(source, "mlteast-en", Version.LUCENE_47);
                return new TokenStreamComponents(source, filter);
            }
        };
    }

}
