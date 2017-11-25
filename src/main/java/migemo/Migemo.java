package migemo;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Migemo {
    public final static String VERSION = "1.3";

    private final String[] keys;
    private final String[] values;
    private RegexOperator operator = RegexOperator.DEFAULT;

    /**
     * Migemoオブジェクトを作成する。
     * dictで指定したファイルがmigemo-dict辞書として
     * オブジェクト作成時に読み込まれる。
     */
    public Migemo() throws IOException {
        this(Migemo.class.getResourceAsStream("/migemo-dict"));
    }

    public Migemo(File dict) throws IOException {
        this(new FileInputStream(Objects.requireNonNull(dict)));
    }

    public Migemo(InputStream is) {
        Objects.requireNonNull(is);
        List<Map.Entry<String, String>> dict = new ArrayList<>();
        try (InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(";") && !line.isEmpty()) {
                    String[] split = line.split("\t", 2);
                    dict.add(new AbstractMap.SimpleImmutableEntry<>(split[0], split[1]));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        dict.sort(Comparator.comparing(Map.Entry::getKey));
        this.keys = dict.stream().map(Map.Entry::getKey).toArray(String[]::new);
        this.values = dict.stream().map(Map.Entry::getValue).toArray(String[]::new);
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
        for (String words : this.predictiveSearch(lower)) {
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
        String[] hira = RomajiConverter.roma2hiraDubiously(query);
        for (String a : hira) {
            generator.add(a);
            // 平仮名による辞書引き
            for (String words : this.predictiveSearch(a)) {
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
            for (String words : this.predictiveSearch(kata)) {
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
     * 指定したキーで始まる単語のよみを持つ漢字リストのエントリを検索しそのビューを返す
     * @param hiragana キー
     * @return 配列で表した検索結果の漢字リスト
     */
    private String[] predictiveSearch(String hiragana) {
        String stop = hiragana.substring(0, hiragana.length() - 1) + (char)(hiragana.charAt(hiragana.length() - 1) + 1);
        int startPos = Arrays.binarySearch(this.keys, hiragana);
        if (startPos < 0) {
            startPos = -(startPos + 1);
        }
        int endPos = Arrays.binarySearch(this.keys, stop);
        if (endPos < 0) {
            endPos = -(endPos + 1);
        }
        return Arrays.copyOfRange(this.values, startPos, endPos);
    }

    public void setOperator(RegexOperator operator) {
        this.operator = operator;
    }
}
