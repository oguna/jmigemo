package migemo;

import java.util.Objects;

public class RegexGenerator {
    private static class Node {
        private final char code;
        private Node child;
        private Node next;

        private Node(char code) {
            this.code = code;
        }
    }

    private Node root;

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
        this.root = add(this.root, word, 0);
    }

    private static Node add(Node node, String word, int offset) {
        if (node == null) {
            if (offset >= word.length()) {
                return null;
            }
            node = new Node(word.charAt(offset));
            if (offset < word.length() - 1) {
                node.child = add(null, word, offset + 1);
            }
            return node;
        }
        Node thisNode = node;
        char code = word.charAt(offset);
        // 兄弟ノードから同じcodeのノードを探す
        if (code < node.code) {
            // 先頭ノードがcodeより大きいので、先頭に挿入
            Node newNode = new Node(code);
            newNode.next = node;
            node = newNode;
            if (offset < word.length()) {
                node.child = add(null, word, offset + 1);
            }
            thisNode = node;
        } else {
            // codeと同じか、codeより小さい最大のノードを探す
            while (node.next != null && node.next.code <= code) {
                node = node.next;
            }
            if (node.code == code) {
                // 同じcodeのノードを見つけた
                if (node.child == null) {
                    // 以降の文字は追加しない
                    return thisNode;
                }
            } else {
                // codeより小さい最大のノードを見つけた
                Node newNode = new Node(code);
                newNode.next = node.next;
                node.next = newNode;
                node = newNode;
            }
            if (word.length() == offset + 1) {
                // 入力パターンが尽きたら終了
                // 入力パターンよりも長い既存パターンは破棄する
                node.child = null;
                return thisNode;
            }
            node.child = add(node.child, word, offset + 1);
        }
        return thisNode;
    }

    private void generateStub(StringBuilder buf, Node node) {
        final String ESCAPE_CHARACTERS = "\\.[]{}()*+-?^$|";
        final char ESCAPE = '\\';
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
            buf.append(this.operator.beginGroup);
        }

        // 子の無いノードを先に[]によりグルーピング
        if (nochild > 0) {
            if (nochild > 1) {
                buf.append(this.operator.beginClass);
            }
            for (Node tmp = node; tmp != null; tmp = tmp.next) {
                if (tmp.child != null) {
                    continue;
                }
                if (ESCAPE_CHARACTERS.indexOf(tmp.code) != -1) {
                    buf.append(ESCAPE);
                }
                buf.append(tmp.code);
            }
            if (nochild > 1) {
                buf.append(this.operator.endClass);
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
                if (ESCAPE_CHARACTERS.indexOf(tmp.code) != -1) {
                    buf.append(ESCAPE);
                }
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
            buf.append(this.operator.endGroup);
        }
    }

    public String generate() {
        if (this.root == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            generateStub(sb, this.root);
            return sb.toString();
        }
    }
}
