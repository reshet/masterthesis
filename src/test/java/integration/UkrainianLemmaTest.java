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

import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.api.Lemmatizer;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michal Hlavac <hlavki@hlavki.eu>
 */


public class UkrainianLemmaTest {

    public UkrainianLemmaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void longUkrainianText() {
        try {
            Lemmatizer lm = LemmatizerFactory.getPrebuilt("mlteast-uk");
            String text = "З іншого боку, інфлекційні парадигми, "
                    + "або списки інфлективних форм типічних слів (таких як пісня, співати, "
                    + "співав, пісні, співак, співаки, спів, пісні, "
                    + ") повинні бути проаналізовні згідно критериїв "
                    + "задля викриття прихованих лексичних стемів.";
            String[] words = text.split("(?=[,.])|\\s+");
            for (String word : words) {
                if (word.trim().length() > 1) {
                    CharSequence lemma = lm.lemmatize(word.trim());
                    if (!word.equals(lemma)) {
                        System.out.println(word + " -> " + lemma);
                    }
                }
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shortUkrainianText() {
        try {
            Lemmatizer lm = LemmatizerFactory.getPrebuilt("mlteast-uk");
            assertEquals("бути", lm.lemmatize("є"));
            assertEquals("співати", lm.lemmatize("співають"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
