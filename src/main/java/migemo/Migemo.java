package migemo;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Migemo {
    public final static String VERSION = "1.3";

    private final TreeMap<String, String> tree = new TreeMap<>();
    private RegexOperator operator = RegexOperator.DEFAULT;

    /**
     * Migemoオブジェクトを作成する。
     * dictで指定したファイルがmigemo-dict辞書として
     * オブジェクト作成時に読み込まれる。
     */
    public Migemo() {
        try (InputStream is = Migemo.class.getResourceAsStream("/migemo-dict");
             InputStreamReader isr = new InputStreamReader(is)) {
            load(isr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Migemo(File dict) throws IOException {
        this.load(dict);
    }

    public void load(File file) throws IOException {
        Objects.requireNonNull(file);
        this.load(new FileReader(file));
    }

    public void load(Reader reader) throws IOException {
        Objects.requireNonNull(reader);
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#") && !line.isEmpty()) {
                    String[] split = line.split("\t", 2);
                    this.tree.put(split[0], split[1]);
                }
            }
        }
    }

    /**
     * queryを文節に分解する。
     * 文節の切れ目は通常アルファベットの大文字。
     * 文節が複数文字の大文字で始まった文節は非大文字を区切りとする。
     */
    static List<String> parseQuery(String query) {
        Objects.requireNonNull(query);
        if (query.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for (char c : query.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                throw new IllegalArgumentException();
            }
        }
        Pattern pattern = Pattern.compile("[^A-Z]+|[A-Z]{2}|[A-Z][^A-Z]+");
        List<String> queries = new ArrayList<>();
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            queries.add(matcher.group());
        }
        return queries;
    }

    /**
     * 1つの単語をmigemo変換。
     * 引数のチェックは行なわない。
     */
    private String queryAWord(String query) {
        RegexGenerator generator = new RegexGenerator(this.operator);
        // query自信はもちろん候補に加える
        generator.add(query);
        // queryそのものでの辞書引き
        String lower = query.toLowerCase();
        for (String words : this.searchStartsWith(lower).values()) {
            for (String word : words.split("\t")) {
                generator.add(word);
            }
        }
        // queryを全角にして候補に加える
        String zen = CharacterConverter.han2zen(query);
        generator.add(zen);
        // queryを半角にして候補に加える
        String han = CharacterConverter.zen2han(query);
        generator.add(han);
        // 平仮名、カタカナ、及びそれによる辞書引き追加
        String hira = RomajiConverter.roma2hira(query);
        if (hira.matches("[^a-z]$")) {
            for (String a : RomajiConverter.roma2hiraDubiously(query)) {
                generator.add(a);
                // 平仮名による辞書引き
                for (String words : this.searchStartsWith(a).values()) {
                    for (String word : words.split("\t")) {
                        generator.add(word);
                    }
                }
                // 片仮名文字列を生成し候補に加える
                String kata = CharacterConverter.hira2kata(a);
                generator.add(kata);
                // 半角カナを生成し候補に加える
                generator.add(CharacterConverter.zen2han(kata));
                // カタカナによる辞書引き
                for (String words : this.searchStartsWith(kata).values()) {
                    for (String word : words.split("\t")) {
                        generator.add(word);
                    }
                }
            }
        } else {
            generator.add(hira);
            // 平仮名による辞書引き
            for (String words : this.searchStartsWith(hira).values()) {
                for (String word : words.split("\t")) {
                    generator.add(word);
                }
            }
            // 片仮名文字列を生成し候補に加える
            String kata = CharacterConverter.hira2kata(hira);
            generator.add(kata);
            // 半角カナを生成し候補に加える
            generator.add(CharacterConverter.zen2han(kata));
            // カタカナによる辞書引き
            for (String words : this.searchStartsWith(kata).values()) {
                for (String word : words.split("\t")) {
                    generator.add(word);
                }
            }
        }
        return generator.generate();
    }

    /**
     * queryで与えられた文字列(ローマ字)を日本語検索のための正規表現へ変換する。
     * 戻り値は変換された結果の文字列(正規表現)。
     * @param query 問い合わせ文字列
     * @return 正規表現文字列。
     */
    public String query(String query) {
        // 引数チェック
        if (query == null) {
            return null;
        }
        if (query.isEmpty()) {
            return query;
        }

        // 文節に分解する
        List<String> words = parseQuery(query);

        // 単語群をrxgenオブジェクトに入力し正規表現を得る
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            String answer = queryAWord(word);
            result.append(answer);
        }
        return result.toString();
    }

    /**
     * 指定したキーで始まる単語のよみと漢字リストのエントリを検索しそのビューを返す
     * @param hiragana キー
     * @return 検索結果のエントリへのビュー
     */
    private SortedMap<String, String> searchStartsWith(String hiragana) {
        String stop = hiragana.substring(0, hiragana.length() - 1) + (char)(hiragana.charAt(hiragana.length() - 1) + 1);
        return this.tree.subMap(hiragana, stop);
    }

    public void setOperator(RegexOperator operator) {
        this.operator = operator;
    }
}
