package edu.naukma.reshet.core.lexicography;

import edu.naukma.reshet.core.algorithm.lexicography.MatchRule;
import edu.naukma.reshet.core.algorithm.lexicography.NounPhraseMatch;
import edu.naukma.reshet.core.algorithm.lexicography.POSTag;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.ExactWordElement;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.MatchPattern;
import edu.naukma.reshet.core.algorithm.lexicography.pattern.NounPhraseElement;
import edu.naukma.reshet.model.TermRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Ukrainian;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = NounPhraseExtractTest.class)
public class DictionaryRelationPatternTest {
    @Test
    public void dictionary_relations_test(){
        String sentences =
                "АВТОРИТЕТ (від лат. autoritas - влада, вплив) - загальновизнаний вплив окремої людини або колективу, організацій в різних сферах суспільного життя. " +
                "АВТОФІЛІЯ (від гр. autos - сам і phileo - люблю) - самолюбування. " +
                "АГАМІЯ (гр. agamia) - 1) безшлюб'я; 2) біол. відсутність статі. " +
                "АГНОЗІЯ (від гр. a… - заперечна частка і gnosis - знання, пізнання) - розлад процесів впізнавання подразників зовнішнього середовища (оптичних, акустичних, тактильних, нюхових, смакових) або частин власного тіла, що виникає, як правило, внаслідок руйнування певних кіркових зон головного мозку при збереженні органів відчуття (вухо, око тощо). " +
                "АГОРАФОБІЯ (від гр. agora - площа і phobos - страх) - страх простору, один з видів психоневрозу. " +
                "АГРАМАТИЗМ (від гр. agrammatos - нерозбірливий) - порушення мовної діяльності людини. Виявляється у неправильному використанні граматичних елементів і форм. " +
                "АГРАФІЯ (від гр. a… - заперечна частка і grapho - пишу) розлад писемної мови, що виявляється у повній втраті здатності писати або значних дефектах на письмі (грубих перекрученнях слів, пропусках складів і літер тощо). " +
                "АГРЕСИВНІСТЬ (вiд лат. aggredior - нападаю) - емоцiйний стан i pиса хаpактеpу людини. Характеризується імпульсивною активністю поведінки, афективними переживаннями - гніву, злості, прагненням заподіяти іншому травму (фізично чи морально). " +
                "АДАПТАЦІЯ (від лат. adapto - пристосовую) - зміна параметрів чутливості аналізаторів, пристосування їх до подразників (наприклад, око адаптується при сприйманні віддаленого об'єкта завдяки механізмам акомодації та конвергенції). Соціально-психологічна А. особистості в групі чи колективі забезпечується завдяки функціонуванню певної системи механізмів (рефлексії, емпатії, прийому соціального зворотного зв'язку тощо).";

        MatchPattern pattern = new MatchPattern(
                new NounPhraseElement(true, new MatchRule(1, POSTag.ADJ, POSTag.NOUN), new MatchRule(0, POSTag.NOUN)),
                new ExactWordElement("—","-"),
                new NounPhraseElement(false, new MatchRule(1,POSTag.ADJ,POSTag.NOUN), new MatchRule(0,POSTag.NOUN,POSTag.NOUN), new MatchRule(0, POSTag.NOUN))
        );

        BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale("uk-UA"));
        iterator.setText(sentences);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = sentences.substring(start,end);
            System.out.println(sentence);
            List<NounPhraseMatch> match = pattern.matchFirst(sentence);
            List<TermRelation> relations = pattern.getRelations(match);
            System.out.println(relations);
        }
    }
}
