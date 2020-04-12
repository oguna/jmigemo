# jmigemo

jmigemoは、ローマ字のまま日本語をインクリメンタル検索するためのツールであるMigemoを、Javaで実装したものです。

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
MigemoDictionary dictionary = new MigemoDictionary();
dictionary.build();
migemo.setDictionary(dictionary);
migemo.setOperator(RegexOperator.DEFAULT);
String regex = migemo.query("kikai");
// => "(kikai|きかい|キカイ|喜界|器械|奇怪|既会員|棋界|機[会械]|毀壊|気塊|貴会|ｋｉｋａｉ|ｷｶｲ)"
```
