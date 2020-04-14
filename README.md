# jmigemo

jmigemoは、ローマ字のまま日本語をインクリメンタル検索するためのツールであるMigemoを、Javaで実装したものです。

| package | |
| ---- | ---- |
| jmigemo | Migemo本体 |
| jmigemo-dict | Migemo用の辞書 |
| jmigemo-cli | Migemo本体と辞書を同梱 |

## 使い方

Migemoの実行には辞書ファイルが必要です。
[jmigemo-dict](https://github.com/oguna/jmigemo-dict/tree/v0.1.0) から辞書ファイルを含んだJarファイルをダウンロードできます。

Javaプロジェクトの依存関係にJarファイルを追加します。
以下のコードはGradleの場合です。

```gradle
...
dependencies {
    ...
    compile files('jmigemo-dict-0.1.0.jar')
}
```

依存関係に追加後、Javaプログラムでは以下のようにして使用します。

```java
Migemo migemo = new Migemo();
MigemoCompactDictionary dictionary;
try (InputStream is = MigemoDefaultCompactDictionary.getStream() ) {
    dictionary = new MigemoCompactDictionary(is);
} catch (IOException e) {
    throw new RuntimeException(e);
}
migemo.setDictionary(dictionary);
migemo.setOperator(RegexOperator.DEFAULT);
String regex = migemo.query("kikai");
// => "(kikai|きかい|キカイ|喜界|器械|奇怪|既会員|棋界|機[会械]|毀壊|気塊|貴会|ｋｉｋａｉ|ｷｶｲ)"
```

## JAR for CLI

辞書ファイルを同梱したJARファイルは、以下のコマンドで作成できます。

```
> ./gradlew cliJar
> java -jar .\build\libs\jmigemo-cli-0.1.0-SNAPSHOT.jar -q -w kensaku
(kensaku|けんさく|ケンサク|建策|憲[作冊]|検索|献策|研削|羂索|ｋｅｎｓａｋｕ|ｹﾝｻｸ)
```

なお、GPLライセンスのjmigemo-dictを含んでいるため、作成されたJARファイルはコピーレフトによりGPLライセンスとなります。