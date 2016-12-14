package migemo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CharacterConverterTest {
    @Test
    public void hira2kata() throws Exception {
        for (char c = 1; c < Character.MAX_VALUE; c++) {
            String actual = CharacterConverter.hira2kata(Character.toString(c));
            String expected = Character.toString(hira2kata.getOrDefault(c, c));
            assertEquals(expected, actual);
        }
    }

    private static final Map<Character, Character> hira2kata = new HashMap<Character, Character>() {{
        put('あ', 'ア');
        put('い', 'イ');
        put('う', 'ウ');
        put('え', 'エ');
        put('お', 'オ');
        put('か', 'カ');
        put('き', 'キ');
        put('く', 'ク');
        put('け', 'ケ');
        put('こ', 'コ');
        put('さ', 'サ');
        put('し', 'シ');
        put('す', 'ス');
        put('せ', 'セ');
        put('そ', 'ソ');
        put('た', 'タ');
        put('ち', 'チ');
        put('つ', 'ツ');
        put('て', 'テ');
        put('と', 'ト');
        put('な', 'ナ');
        put('に', 'ニ');
        put('ぬ', 'ヌ');
        put('ね', 'ネ');
        put('の', 'ノ');
        put('は', 'ハ');
        put('ひ', 'ヒ');
        put('ふ', 'フ');
        put('へ', 'ヘ');
        put('ほ', 'ホ');
        put('ま', 'マ');
        put('み', 'ミ');
        put('む', 'ム');
        put('め', 'メ');
        put('も', 'モ');
        put('や', 'ヤ');
        put('ゆ', 'ユ');
        put('よ', 'ヨ');
        put('ら', 'ラ');
        put('り', 'リ');
        put('る', 'ル');
        put('れ', 'レ');
        put('ろ', 'ロ');
        put('わ', 'ワ');
        put('ゐ', 'ヰ');
        put('ゑ', 'ヱ');
        put('を', 'ヲ');
        put('が', 'ガ');
        put('ぎ', 'ギ');
        put('ぐ', 'グ');
        put('げ', 'ゲ');
        put('ご', 'ゴ');
        put('ざ', 'ザ');
        put('じ', 'ジ');
        put('ず', 'ズ');
        put('ぜ', 'ゼ');
        put('ぞ', 'ゾ');
        put('だ', 'ダ');
        put('ぢ', 'ヂ');
        put('づ', 'ヅ');
        put('で', 'デ');
        put('ど', 'ド');
        put('ば', 'バ');
        put('び', 'ビ');
        put('ぶ', 'ブ');
        put('べ', 'ベ');
        put('ぼ', 'ボ');
        put('ぱ', 'パ');
        put('ぴ', 'ピ');
        put('ぷ', 'プ');
        put('ぺ', 'ペ');
        put('ぽ', 'ポ');
        put('ぁ', 'ァ');
        put('ぃ', 'ィ');
        put('ぅ', 'ゥ');
        put('ぇ', 'ェ');
        put('ぉ', 'ォ');
        put('ゃ', 'ャ');
        put('ゅ', 'ュ');
        put('ょ', 'ョ');
        put('ん', 'ン');
        put('っ', 'ッ');
        put('ゎ', 'ヮ');
    }};
}