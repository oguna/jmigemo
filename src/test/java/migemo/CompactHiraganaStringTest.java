package migemo;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompactHiraganaStringTest {
    private final static String hiragana = "ぁあぃいぅうぇえぉおかがきぎく" +
            "ぐけげこごさざしじすずせぜそぞた" +
            "だちぢっつづてでとどなにぬねのは" +
            "ばぱひびぴふぶぷへべぺほぼぽまみ" +
            "むめもゃやゅゆょよらりるれろゎわ" +
            "ゐゑをんゔゕゖ";

    @Test
    public void test() {
        byte[] encoded = CompactHiraganaString.encode(hiragana);
        String decoded = CompactHiraganaString.decode(encoded);
        assertEquals(hiragana, decoded);
    }

}