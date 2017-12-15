package migemo;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class RomajiProcessorTest {

    @Test
    public void romajiToHiragana() {
        //assertEquals("ろーまじ", RomajiProcessor.romajiToHiragana("ro-maji"));
        //assertEquals("あっち", RomajiProcessor.romajiToHiragana("atti"));
        //assertEquals("あっt", RomajiProcessor.romajiToHiragana("att"));
        //assertEquals("wっw", RomajiProcessor.romajiToHiragana("www"));
        //assertEquals("っk", RomajiProcessor.romajiToHiragana("kk"));
        //assertEquals("ん", RomajiProcessor.romajiToHiragana("n"));
        assertEquals("けんさく", RomajiProcessor.romajiToHiragana("kensaku"));
    }

    @Test
    public void romajiToHiraganaPredictively() {
        RomajiProcessor.RomajiPredictiveResult kiku = RomajiProcessor.romajiToHiraganaPredictively("kiku");
        assertEquals("き", kiku.establishedHiragana);
        assertThat(kiku.predictiveSuffixes, hasItem("く"));

        RomajiProcessor.RomajiPredictiveResult ky = RomajiProcessor.romajiToHiraganaPredictively("ky");
        assertEquals("", ky.establishedHiragana);
        assertThat(ky.predictiveSuffixes, hasItems("きゃ", "きぃ", "きぇ", "きゅ", "きょ"));

        RomajiProcessor.RomajiPredictiveResult kky = RomajiProcessor.romajiToHiraganaPredictively("k");
        assertEquals("", kky.establishedHiragana);
        assertThat(kky.predictiveSuffixes, hasItems("っきゃ", "っきぃ", "っきぇ", "っきゅ", "っきょ"));

        RomajiProcessor.RomajiPredictiveResult n = RomajiProcessor.romajiToHiraganaPredictively("n");
        assertEquals("", n.establishedHiragana);
        assertThat(n.predictiveSuffixes, hasItems("ん", "な", "に", "ぬ", "ね", "の", "にゃ", "にゅ", "にょ"));
        assertThat(n.predictiveSuffixes, not(hasItem(containsString("っ"))));
    }
}