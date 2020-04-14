package migemo;

import java.util.*;

public class Migemo {
    public final static String VERSION = "1.3";

    private RegexOperator operator = RegexOperator.DEFAULT;
    private MigemoCompactDictionary dictionary;

    /**
     * queryを文節に分解する。
     * 文節の切れ目は通常アルファベットの大文字。
     * 文節が複数文字の大文字で始まった文節は非大文字を区切りとする。
     */
    static Iterator<String> parseQuery(String query) {
        Objects.requireNonNull(query);
        int start = 0;
        while (start < query.length() && Character.isSpaceChar(query.charAt(start))) {
            start++;
        }
        final int _start = start;
        return new Iterator<String>() {
            int offset = _start;
            @Override
            public boolean hasNext() {
                return offset < query.length();
            }

            @Override
            public String next() {
                char c = query.charAt(offset);
                int len;
                if (Character.isUpperCase(c) && offset + 1 < query.length() && Character.isUpperCase(query.codePointAt(offset + 1))) {
                    len = 2;
                    while (offset + len < query.length() && Character.isUpperCase(query.charAt(offset + len))) {
                        len++;
                    }
                } else {
                    len = 1;
                    while (offset + len < query.length() && !Character.isSpaceChar(query.charAt(offset + len)) && !Character.isUpperCase(query.charAt(offset + len))) {
                        len++;
                    }
                }
                String result = query.substring(offset, offset + len);
                offset += len;
                // skip white space
                while (offset < query.length() && Character.isSpaceChar(query.charAt(offset))) {
                    offset++;
                }
                return result;
            }
        };
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
        for (String word : this.dictionary.predictiveSearch(lower)) {
            generator.add(word);
        }
        // queryを全角にして候補に加える
        String zen = CharacterConverter.han2zen(query);
        generator.add(zen);
        // queryを半角にして候補に加える
        String han = CharacterConverter.zen2han(query);
        generator.add(han);
        // 平仮名、カタカナ、及びそれによる辞書引き追加
        RomajiProcessor.RomajiPredictiveResult hiraganaResult = RomajiProcessor.romajiToHiraganaPredictively(query.toLowerCase());
        String[] hira = hiraganaResult.predictiveSuffixes.stream().map(e -> hiraganaResult.establishedHiragana + e).toArray(String[]::new);
        for (String a : hira) {
            generator.add(a);
            // 平仮名による辞書引き
            for (String word : this.dictionary.predictiveSearch(a)) {
                generator.add(word);
            }
            // 片仮名文字列を生成し候補に加える
            String kata = CharacterConverter.hira2kata(a);
            generator.add(kata);
            // 半角カナを生成し候補に加える
            generator.add(CharacterConverter.zen2han(kata));
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
        Iterator<String> words = parseQuery(query);

        // 単語群をrxgenオブジェクトに入力し正規表現を得る
        StringBuilder result = new StringBuilder();
        while (words.hasNext()) {
            String answer = queryAWord(words.next());
            result.append(answer);
        }
        return result.toString();
    }

    public void setOperator(RegexOperator operator) {
        this.operator = operator;
    }

    public void setDictionary(MigemoCompactDictionary dictionary) {
        this.dictionary = dictionary;
    }
}
