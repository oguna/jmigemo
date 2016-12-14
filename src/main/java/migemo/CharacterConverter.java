package migemo;

import java.util.HashMap;
import java.util.Map;

public class CharacterConverter {
    public static String han2zen(String source) {
        StringBuilder sb = new StringBuilder(source.length());
        for (char c : source.toCharArray()) {
            sb.append(han2zen.getOrDefault(c, c));
        }
        return sb.toString();
    }

    public static String hira2kata(String source) {
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            chars[i] = 'ぁ' <= c && c <= 'ん' ? (char) (c - 'ぁ' + 'ァ') : c;
        }
        return new String(chars);
    }

    public static String zen2han(String source) {
        StringBuilder sb = new StringBuilder(source.length());
        for (char c : source.toCharArray()) {
            sb.append(zen2han.getOrDefault(c, Character.toString(c)));
        }
        return sb.toString();
    }

    private static final Map<Character, Character> han2zen = new HashMap<Character, Character>() {{
        put('$', '＄');
        put('%', '％');
        put('&', '＆');
        put('\'', '’');
        put('(', '（');
        put(')', '）');
        put('*', '＊');
        put('+', '＋');
        put(',', '，');
        put('-', '－');
        put('.', '．');
        put('/', '／');
        put('0', '０');
        put('1', '１');
        put('2', '２');
        put('3', '３');
        put('4', '４');
        put('5', '５');
        put('6', '６');
        put('7', '７');
        put('8', '８');
        put('9', '９');
        put(':', '：');
        put(';', '；');
        put('<', '＜');
        put('=', '＝');
        put('>', '＞');
        put('?', '？');
        put('@', '＠');
        put('A', 'Ａ');
        put('B', 'Ｂ');
        put('C', 'Ｃ');
        put('D', 'Ｄ');
        put('E', 'Ｅ');
        put('F', 'Ｆ');
        put('G', 'Ｇ');
        put('H', 'Ｈ');
        put('I', 'Ｉ');
        put('J', 'Ｊ');
        put('K', 'Ｋ');
        put('L', 'Ｌ');
        put('M', 'Ｍ');
        put('N', 'Ｎ');
        put('O', 'Ｏ');
        put('P', 'Ｐ');
        put('Q', 'Ｑ');
        put('R', 'Ｒ');
        put('S', 'Ｓ');
        put('T', 'Ｔ');
        put('U', 'Ｕ');
        put('V', 'Ｖ');
        put('W', 'Ｗ');
        put('X', 'Ｘ');
        put('Y', 'Ｙ');
        put('Z', 'Ｚ');
        put('[', '［');
        put('\\', '￥');
        put(']', '］');
        put('^', '＾');
        put('_', '＿');
        put('`', '‘');
        put('a', 'ａ');
        put('b', 'ｂ');
        put('c', 'ｃ');
        put('d', 'ｄ');
        put('e', 'ｅ');
        put('f', 'ｆ');
        put('g', 'ｇ');
        put('h', 'ｈ');
        put('i', 'ｉ');
        put('j', 'ｊ');
        put('k', 'ｋ');
        put('l', 'ｌ');
        put('m', 'ｍ');
        put('n', 'ｎ');
        put('o', 'ｏ');
        put('p', 'ｐ');
        put('q', 'ｑ');
        put('r', 'ｒ');
        put('s', 'ｓ');
        put('t', 'ｔ');
        put('u', 'ｕ');
        put('v', 'ｖ');
        put('w', 'ｗ');
        put('x', 'ｘ');
        put('y', 'ｙ');
        put('z', 'ｚ');
        put('{', '｛');
        put('|', '｜');
        put('}', '｝');
        put('~', '～');
        put('｡', '。');
        put('｢', '「');
        put('｣', '」');
        put('､', '、');
        put('･', '・');
        put('ｦ', 'ヲ');
        put('ｧ', 'ァ');
        put('ｨ', 'ィ');
        put('ｩ', 'ゥ');
        put('ｪ', 'ェ');
        put('ｫ', 'ォ');
        put('ｬ', 'ャ');
        put('ｭ', 'ュ');
        put('ｮ', 'ョ');
        put('ｯ', 'ッ');
        put('ｰ', 'ー');
        put('ｱ', 'ア');
        put('ｲ', 'イ');
        put('ｳ', 'ウ');
        put('ｴ', 'エ');
        put('ｵ', 'オ');
        put('ｶ', 'カ');
        put('ｷ', 'キ');
        put('ｸ', 'ク');
        put('ｹ', 'ケ');
        put('ｺ', 'コ');
        put('ｻ', 'サ');
        put('ｼ', 'シ');
        put('ｽ', 'ス');
        put('ｾ', 'セ');
        put('ｿ', 'ソ');
        put('ﾀ', 'タ');
        put('ﾁ', 'チ');
        put('ﾂ', 'ツ');
        put('ﾃ', 'テ');
        put('ﾄ', 'ト');
        put('ﾅ', 'ナ');
        put('ﾆ', 'ニ');
        put('ﾇ', 'ヌ');
        put('ﾈ', 'ネ');
        put('ﾉ', 'ノ');
        put('ﾊ', 'ハ');
        put('ﾋ', 'ヒ');
        put('ﾌ', 'フ');
        put('ﾍ', 'ヘ');
        put('ﾎ', 'ホ');
        put('ﾏ', 'マ');
        put('ﾐ', 'ミ');
        put('ﾑ', 'ム');
        put('ﾒ', 'メ');
        put('ﾓ', 'モ');
        put('ﾔ', 'ヤ');
        put('ﾕ', 'ユ');
        put('ﾖ', 'ヨ');
        put('ﾗ', 'ラ');
        put('ﾘ', 'リ');
        put('ﾙ', 'ル');
        put('ﾚ', 'レ');
        put('ﾛ', 'ロ');
        put('ﾜ', 'ワ');
        put('ﾝ', 'ン');
        put('ﾞ', '゛');
        put('ﾟ', '゜');
    }};

    private static final Map<Character, String> zen2han = new HashMap<Character, String>() {{
        put('！', "!");
        put('”', "\"");
        put('＃', "#");
        put('＄', "$");
        put('％', "%");
        put('＆', "&");
        put('’', "'");
        put('（', "(");
        put('）', ")");
        put('＊', "*");
        put('＋', "+");
        put('，', ",");
        put('－', "-");
        put('．', ".");
        put('／', "/");
        put('０', "0");
        put('１', "1");
        put('２', "2");
        put('３', "3");
        put('４', "4");
        put('５', "5");
        put('６', "6");
        put('７', "7");
        put('８', "8");
        put('９', "9");
        put('：', ":");
        put('；', ";");
        put('＜', "<");
        put('＝', "=");
        put('＞', ">");
        put('？', "?");
        put('＠', "@");
        put('Ａ', "A");
        put('Ｂ', "B");
        put('Ｃ', "C");
        put('Ｄ', "D");
        put('Ｅ', "E");
        put('Ｆ', "F");
        put('Ｇ', "G");
        put('Ｈ', "H");
        put('Ｉ', "I");
        put('Ｊ', "J");
        put('Ｋ', "K");
        put('Ｌ', "L");
        put('Ｍ', "M");
        put('Ｎ', "N");
        put('Ｏ', "O");
        put('Ｐ', "P");
        put('Ｑ', "Q");
        put('Ｒ', "R");
        put('Ｓ', "S");
        put('Ｔ', "T");
        put('Ｕ', "U");
        put('Ｖ', "V");
        put('Ｗ', "W");
        put('Ｘ', "X");
        put('Ｙ', "Y");
        put('Ｚ', "Z");
        put('［', "[");
        put('￥', "\\");
        put('］', "]");
        put('＾', "^");
        put('＿', "_");
        put('‘', "`");
        put('ａ', "a");
        put('ｂ', "b");
        put('ｃ', "c");
        put('ｄ', "d");
        put('ｅ', "e");
        put('ｆ', "f");
        put('ｇ', "g");
        put('ｈ', "h");
        put('ｉ', "i");
        put('ｊ', "j");
        put('ｋ', "k");
        put('ｌ', "l");
        put('ｍ', "m");
        put('ｎ', "n");
        put('ｏ', "o");
        put('ｐ', "p");
        put('ｑ', "q");
        put('ｒ', "r");
        put('ｓ', "s");
        put('ｔ', "t");
        put('ｕ', "u");
        put('ｖ', "v");
        put('ｗ', "w");
        put('ｘ', "x");
        put('ｙ', "y");
        put('ｚ', "z");
        put('｛', "{");
        put('｜', "|");
        put('｝', "}");
        put('～', "~");
        put('。', "｡");
        put('「', "｢");
        put('」', "｣");
        put('、', "､");
        put('・', "･");
        put('ヲ', "ｦ");
        put('ァ', "ｧ");
        put('ィ', "ｨ");
        put('ゥ', "ｩ");
        put('ェ', "ｪ");
        put('ォ', "ｫ");
        put('ャ', "ｬ");
        put('ュ', "ｭ");
        put('ョ', "ｮ");
        put('ッ', "ｯ");
        put('ー', "ｰ");
        put('ア', "ｱ");
        put('イ', "ｲ");
        put('ウ', "ｳ");
        put('エ', "ｴ");
        put('オ', "ｵ");
        put('カ', "ｶ");
        put('キ', "ｷ");
        put('ク', "ｸ");
        put('ケ', "ｹ");
        put('コ', "ｺ");
        put('サ', "ｻ");
        put('シ', "ｼ");
        put('ス', "ｽ");
        put('セ', "ｾ");
        put('ソ', "ｿ");
        put('タ', "ﾀ");
        put('チ', "ﾁ");
        put('ツ', "ﾂ");
        put('テ', "ﾃ");
        put('ト', "ﾄ");
        put('ナ', "ﾅ");
        put('ニ', "ﾆ");
        put('ヌ', "ﾇ");
        put('ネ', "ﾈ");
        put('ノ', "ﾉ");
        put('ハ', "ﾊ");
        put('ヒ', "ﾋ");
        put('フ', "ﾌ");
        put('ヘ', "ﾍ");
        put('ホ', "ﾎ");
        put('マ', "ﾏ");
        put('ミ', "ﾐ");
        put('ム', "ﾑ");
        put('メ', "ﾒ");
        put('モ', "ﾓ");
        put('ヤ', "ﾔ");
        put('ユ', "ﾕ");
        put('ヨ', "ﾖ");
        put('ラ', "ﾗ");
        put('リ', "ﾘ");
        put('ル', "ﾙ");
        put('レ', "ﾚ");
        put('ロ', "ﾛ");
        put('ワ', "ﾜ");
        put('ン', "ﾝ");
        put('゛', "ﾞ");
        put('゜', "ﾟ");
        put('ヴ', "ｳﾞ");
        put('ガ', "ｶﾞ");
        put('ギ', "ｷﾞ");
        put('グ', "ｸﾞ");
        put('ゲ', "ｹﾞ");
        put('ゴ', "ｺﾞ");
        put('ザ', "ｻﾞ");
        put('ジ', "ｼﾞ");
        put('ズ', "ｽﾞ");
        put('ゼ', "ｾﾞ");
        put('ゾ', "ｿﾞ");
        put('ダ', "ﾀﾞ");
        put('ヂ', "ﾁﾞ");
        put('ヅ', "ﾂﾞ");
        put('デ', "ﾃﾞ");
        put('ド', "ﾄﾞ");
        put('バ', "ﾊﾞ");
        put('ビ', "ﾋﾞ");
        put('ブ', "ﾌﾞ");
        put('ベ', "ﾍﾞ");
        put('ボ', "ﾎﾞ");
        put('パ', "ﾊﾟ");
        put('ピ', "ﾋﾟ");
        put('プ', "ﾌﾟ");
        put('ペ', "ﾍﾟ");
        put('ポ', "ﾎﾟ");
    }};
}
