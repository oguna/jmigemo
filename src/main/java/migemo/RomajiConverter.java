package migemo;

import java.util.Arrays;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RomajiConverter {

    private final static String FIXKEY_NONXTU = "aiueon";

    public static String roma2hira(String source) {
        if (source == null) {
            return null;
        }
        if (source.isEmpty()) {
            return "";
        }
        for (int i = Math.min(3, source.length()); i > 0; i--) {
            String key = source.substring(0, i);
            String value = roma2hira.get(key);
            if (value != null) {
                return value + roma2hira(source.substring(i));
            }
        }
        if (source.length() >= 2 && source.charAt(0) == source.charAt(1) && !FIXKEY_NONXTU.contains(source.substring(0, 1))) {
            // 「っ」の判定
            return "っ" + roma2hira(source.substring(1));
        } else if (source.charAt(0) == 'n') {
            // 「n(子音)」を「ん(子音)」に変換
            return "ん" + roma2hira(source.substring(1));
        } else {
            return source.charAt(0) + roma2hira(source.substring(1));
        }
    }

    public static String[] roma2hiraDubiously(String source) {
        if (source == null) {
            return null;
        }
        if (source.isEmpty()) {
            return new String[] {""};
        }
        for (int i = Math.min(3, source.length()); i > 0; i--) {
            String key = source.substring(0, i);
            String value = roma2hira.get(key);
            if (value != null) {
                return concat(value, roma2hiraDubiously(source.substring(i)));
            }
        }
        if (source.length() >= 2 && source.charAt(0) == source.charAt(1) && !FIXKEY_NONXTU.contains(source.substring(0, 1))) {
            // 「っ」の判定
            return concat("っ", roma2hiraDubiously(source.substring(1)));
        } else if (source.charAt(0) == 'n') {
            // 「n(子音)」を「ん(子音)」に変換
            return concat("ん", roma2hiraDubiously(source.substring(1)));
        } else if (source.length() <= 2) {
            SortedMap<String, String> subtree = getTreeStartsWith(source);
            if (subtree.isEmpty()) {
                return concat(source.substring(0, 1), roma2hiraDubiously(source.substring(1)));
            } else {
                return subtree.values().toArray(new String[0]);
            }
        }
        return concat(source.substring(0, 1), roma2hiraDubiously(source.substring(1)));
    }

    private static String[] concat(String prefix, String[] sources) {
        return Arrays.stream(sources).map(e -> prefix + e).toArray(String[]::new);
    }

    private static SortedMap<String, String> getTreeStartsWith(String key) {
        char[] stopChars = key.toCharArray();
        int lastIndex = stopChars.length - 1;
        stopChars[lastIndex] = (char)(stopChars[lastIndex] + 1);
        String stop = new String(stopChars);
        return roma2hira.subMap(key, stop);
    }

    private static final TreeMap<String, String> roma2hira = new TreeMap<String, String>() {{
        put("a", "あ");
        put("i", "い");
        put("u", "う");
        put("e", "え");
        put("o", "お");
        put("ka", "か");
        put("ki", "き");
        put("ku", "く");
        put("ke", "け");
        put("ko", "こ");
        put("sa", "さ");
        put("si", "し");
        put("su", "す");
        put("se", "せ");
        put("so", "そ");
        put("ta", "た");
        put("ti", "ち");
        put("tu", "つ");
        put("te", "て");
        put("to", "と");
        put("na", "な");
        put("ni", "に");
        put("nu", "ぬ");
        put("ne", "ね");
        put("no", "の");
        put("ha", "は");
        put("hi", "ひ");
        put("hu", "ふ");
        put("he", "へ");
        put("ho", "ほ");
        put("ma", "ま");
        put("mi", "み");
        put("mu", "む");
        put("me", "め");
        put("mo", "も");
        put("ya", "や");
        put("yi", "い");
        put("yu", "ゆ");
        put("ye", "いぇ");
        put("yo", "よ");
        put("ra", "ら");
        put("ri", "り");
        put("ru", "る");
        put("re", "れ");
        put("ro", "ろ");
        put("wa", "わ");
        put("wi", "ゐ");
        put("wu", "う");
        put("we", "ゑ");
        put("wo", "を");
        put("ga", "が");
        put("gi", "ぎ");
        put("gu", "ぐ");
        put("ge", "げ");
        put("go", "ご");
        put("za", "ざ");
        put("zi", "じ");
        put("zu", "ず");
        put("ze", "ぜ");
        put("zo", "ぞ");
        put("da", "だ");
        put("di", "ぢ");
        put("du", "づ");
        put("de", "で");
        put("do", "ど");
        put("ba", "ば");
        put("bi", "び");
        put("bu", "ぶ");
        put("be", "べ");
        put("bo", "ぼ");
        put("pa", "ぱ");
        put("pi", "ぴ");
        put("pu", "ぷ");
        put("pe", "ぺ");
        put("po", "ぽ");
        put("la", "ぁ");
        put("li", "ぃ");
        put("lu", "ぅ");
        put("le", "ぇ");
        put("lo", "ぉ");
        put("lya", "ゃ");
        put("lyi", "ぃ");
        put("lyu", "ゅ");
        put("lye", "ぇ");
        put("lyo", "ょ");
        put("xa", "ぁ");
        put("xi", "ぃ");
        put("xu", "ぅ");
        put("xe", "ぇ");
        put("xo", "ぉ");
        put("xya", "ゃ");
        put("xyi", "ぃ");
        put("xyu", "ゅ");
        put("xye", "ぇ");
        put("xyo", "ょ");
        put("kya", "きゃ");
        put("kyi", "きぃ");
        put("kyu", "きゅ");
        put("kye", "きぇ");
        put("kyo", "きょ");
        put("gwa", "ぐぁ");
        put("gwi", "ぐぃ");
        put("gwu", "ぐぅ");
        put("gwe", "ぐぇ");
        put("gwo", "ぐぉ");
        put("gya", "ぎゃ");
        put("gyi", "ぎぃ");
        put("gyu", "ぎゅ");
        put("gye", "ぎぇ");
        put("gyo", "ぎょ");
        put("sha", "しゃ");
        put("shi", "し");
        put("shu", "しゅ");
        put("she", "しぇ");
        put("sho", "しょ");
        put("swa", "すぁ");
        put("swi", "すぃ");
        put("swu", "すぅ");
        put("swe", "すぇ");
        put("swo", "すぉ");
        put("sya", "しゃ");
        put("syi", "しぃ");
        put("syu", "しゅ");
        put("sye", "しぇ");
        put("syo", "しょ");
        put("tha", "てゃ");
        put("thi", "てぃ");
        put("thu", "てゅ");
        put("the", "てぇ");
        put("tho", "てょ");
        put("tsa", "つぁ");
        put("tsi", "つぃ");
        put("tsu", "つ");
        put("tse", "つぇ");
        put("tso", "つぉ");
        put("twa", "とぁ");
        put("twi", "とぃ");
        put("twu", "とぅ");
        put("twe", "とぇ");
        put("two", "とぉ");
        put("tya", "ちゃ");
        put("tyi", "ちぃ");
        put("tyu", "ちゅ");
        put("tye", "ちぇ");
        put("tyo", "ちょ");
        put("dha", "でゃ");
        put("dhi", "でぃ");
        put("dhu", "でゅ");
        put("dhe", "でぇ");
        put("dho", "でょ");
        put("nya", "にゃ");
        put("nyi", "にぃ");
        put("nyu", "にゅ");
        put("nye", "にぇ");
        put("nyo", "にょ");
        put("hya", "ひゃ");
        put("hyi", "ひぃ");
        put("hyu", "ひゅ");
        put("hye", "ひぇ");
        put("hyo", "ひょ");
        put("bya", "びゃ");
        put("byi", "びぃ");
        put("byu", "びゅ");
        put("bye", "びぇ");
        put("byo", "びょ");
        put("pya", "ぴゃ");
        put("pyi", "ぴぃ");
        put("pyu", "ぴゅ");
        put("pye", "ぴぇ");
        put("pyo", "ぴょ");
        put("mya", "みゃ");
        put("myi", "みぃ");
        put("myu", "みゅ");
        put("mye", "みぇ");
        put("myo", "みょ");
        put("rya", "りゃ");
        put("ryi", "りぃ");
        put("ryu", "りゅ");
        put("rye", "りぇ");
        put("ryo", "りょ");
        put("ca", "か");
        put("ci", "し");
        put("cu", "く");
        put("ce", "せ");
        put("co", "こ");
        put("cha", "ちゃ");
        put("chi", "ち");
        put("chu", "ちゅ");
        put("che", "ちぇ");
        put("cho", "ちょ");
        put("fa", "ふぁ");
        put("fi", "ふぃ");
        put("fu", "ふ");
        put("fe", "ふぇ");
        put("fo", "ふぉ");
        put("fwa", "ふぁ");
        put("fwi", "ふぃ");
        put("fwu", "ふぅ");
        put("fwe", "ふぇ");
        put("fwo", "ふぉ");
        put("fya", "ふゃ");
        put("fyi", "ふぃ");
        put("fyu", "ふゅ");
        put("fye", "ふぇ");
        put("fyo", "ふょ");
        put("ja", "じゃ");
        put("ji", "じ");
        put("ju", "じゅ");
        put("je", "じぇ");
        put("jo", "じょ");
        put("jya", "じゃ");
        put("jyi", "じぃ");
        put("jyu", "じゅ");
        put("jye", "じぇ");
        put("jyo", "じょ");
        put("qa", "くぁ");
        put("qi", "くぃ");
        put("qu", "く");
        put("qe", "くぇ");
        put("qo", "くぉ");
        put("qwa", "くぁ");
        put("qwi", "くぃ");
        put("qwu", "くぅ");
        put("qwe", "くぇ");
        put("qwo", "くぉ");
        put("qya", "くゃ");
        put("qyi", "くぃ");
        put("qyu", "くゅ");
        put("qye", "くぇ");
        put("qyo", "くょ");
        put("va", "ヴぁ");
        put("vi", "ヴぃ");
        put("vu", "ヴ");
        put("ve", "ヴぇ");
        put("vo", "ヴぉ");
        put("vya", "ヴゃ");
        put("vyi", "ヴぃ");
        put("vyu", "ヴゅ");
        put("vye", "ヴぇ");
        put("vyo", "ヴょ");
        put("nn", "ん");
        put("n'", "ん");
        put("xn", "ん");
        put("ltu", "っ");
        put("xtu", "っ");
        put("lwa", "ゎ");
        put("xwa", "ゎ");
        put("lka", "ヵ");
        put("xka", "ヵ");
        put("lke", "ヶ");
        put("xke", "ヶ");
        put("kwa", "くぁ");
        put("-", "ー");
        put("~", "～");
        put("mba", "んば");
        put("mbi", "んび");
        put("mbu", "んぶ");
        put("mbe", "んべ");
        put("mbo", "んぼ");
        put("mpa", "んぱ");
        put("mpi", "んぴ");
        put("mpu", "んぷ");
        put("mpe", "んぺ");
        put("mpo", "んぽ");
        put("mma", "んま");
        put("mmi", "んみ");
        put("mmu", "んむ");
        put("mme", "んめ");
        put("mmo", "んも");
        put("tcha", "っちゃ");
        put("tchi", "っち");
        put("tchu", "っちゅ");
        put("tche", "っちぇ");
        put("tcho", "っちょ");
    }};
}
