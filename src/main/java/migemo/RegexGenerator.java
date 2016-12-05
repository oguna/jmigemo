package migemo;

import java.util.Objects;

public class RegexGenerator {
    private static class Node {
        final char code;
        Node child;
        Node next;

        private Node(char code) {
            this.code = code;
        }

        Node add(String query, int offset) {
            Objects.requireNonNull(query);
            char code = query.charAt(offset);
            Node node = this;
            // codeを持つノードを探す
            while (node != null && node.code != code) {
                node = node.next;
            }
            // codeを持つノードが無い場合、作成追加する
            boolean a = false;
            if (node == null) {
                node = new Node(code);
                node.next = this;
                a = true;
            }
            if (query.length() == offset + 1) {
                // 入力パターンが尽きたら終了
                // 入力パターンよりも長い既存パターンは破棄する
                node.child = null;
            } else {
                if (node.child == null) {
                    node.child = new Node(query.charAt(1 + offset));
                }
                node.child = node.child.add(query, offset + 1);
            }
            return a ? node : this;
        }
    }

    private Node node;

    private final RegexOperator operator;

    public RegexGenerator(RegexOperator operator) {
        this.operator = Objects.requireNonNull(operator);
    }

    public RegexGenerator() {
        this(RegexOperator.DEFAULT);
    }

    public void add(String word) {
        Objects.requireNonNull(word);
        if (word.isEmpty()) {
            return;
        }
        if (this.node == null) {
            this.node = new Node(word.charAt(0));
        }
        this.node = this.node.add(word, 0);
    }

    private void generateStub(StringBuilder buf, Node node) {
        // 現在の階層の特性(兄弟の数、子供の数)をチェックする
        int brother = 1;
        int haschild = 0;
        for (Node tmp = node; tmp != null; tmp = tmp.next) {
            if (tmp.next != null) {
                brother++;
            }
            if (tmp.child != null) {
                haschild++;
            }
        }
        int nochild = brother - haschild;

        // 必要ならば()によるグルーピング
        if (brother > 1 && haschild > 0) {
            buf.append(this.operator.nestIn);
        }

        // 子の無いノードを先に[]によりグルーピング
        if (nochild > 0) {
            if (nochild > 1) {
                buf.append(this.operator.selectIn);
            }
            for (Node tmp = node; tmp != null; tmp = tmp.next) {
                if (tmp.child != null) {
                    continue;
                }
                buf.append(tmp.code);
            }
            if (nochild > 1) {
                buf.append(this.operator.selectOut);
            }
        }

        // 子のあるノードを出力
        if (haschild > 0) {
            // グループを出力済みならORで繋ぐ
            if (nochild > 0) {
                buf.append(this.operator.or);
            }
            Node tmp;
            for (tmp = node; tmp.child == null; tmp = tmp.next) {
            }
            while (true) {
                buf.append(tmp.code);
                // 空白・改行飛ばしのパターンを挿入
                if (this.operator.newline != null) {
                    buf.append(this.operator.newline);
                }
                generateStub(buf, tmp.child);
                for (tmp = tmp.next; tmp != null && tmp.child == null; tmp = tmp.next) {
                }
                if (tmp == null) {
                    break;
                }
                if (haschild > 1) {
                    buf.append(this.operator.or);
                }
            }
        }
        // 必要ならば()によるグルーピング
        if (brother > 1 && haschild > 0) {
            buf.append(this.operator.nestOut);
        }
    }

    public String generate() {
        if (this.node == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            generateStub(sb, this.node);
            return sb.toString();
        }
    }
}
