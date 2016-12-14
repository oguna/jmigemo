package migemo;

import org.junit.Test;

import static org.junit.Assert.*;

public class RomajiConverterTest {
    @Test
    public void roma2hiraDubiously() throws Exception {
        String[] expected = new String[]{"きか", "きき", "きく", "きけ", "きこ", "きくぁ", "ききゃ", "ききぇ", "ききぃ", "ききょ", "ききゅ"};
        String[] actual = RomajiConverter.roma2hiraDubiously("kik");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void roma2hiraDubiously1() throws Exception {
        String[] expected = new String[]{"あいうえお"};
        assertArrayEquals(expected, RomajiConverter.roma2hiraDubiously("aiueo"));
    }

    @Test
    public void roma2hiraDubiously2() throws Exception {
        String[] expected = new String[]{"っか"};
        assertArrayEquals(expected, RomajiConverter.roma2hiraDubiously("kka"));
    }

    @Test
    public void roma2hiraDubiously3() throws Exception {
        String[] expected = new String[]{"な", "に", "ぬ","ね", "の", "にゃ", "にぃ", "にゅ", "にぇ", "にょ", "ん"};
        String[] actual = RomajiConverter.roma2hiraDubiously("n");
        assertArrayEquals(expected, actual);
    }
}