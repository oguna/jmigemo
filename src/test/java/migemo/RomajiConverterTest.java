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
    public void roma2hira() throws Exception {
        assertEquals("あいうえお", RomajiConverter.roma2hira("aiueo"));
        assertEquals("っか", RomajiConverter.roma2hira("kka"));
        assertEquals("ん", RomajiConverter.roma2hira("n"));
    }
}