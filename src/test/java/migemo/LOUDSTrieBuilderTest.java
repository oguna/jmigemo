package migemo;

import org.junit.Assert;
import org.junit.Test;

public class LOUDSTrieBuilderTest {
    @Test
    public void SimpleWords1() {
        String[] words = new String[] {
                "baby",
                "bad",
                "bank",
                "box",
                "dad",
                "dance",
        };
        LOUDSTrie trie = LOUDSTrieBuilder.build(words);
        Assert.assertEquals(10, trie.get("box"));
    }

    @Test
    public void SimpleWords2() {
        String[] words = new String[] {
                "a",
                "aa",
                "ab",
                "bb",
        };
        LOUDSTrie trie = LOUDSTrieBuilder.build(words);
        Assert.assertEquals(1, trie.get(""));
        Assert.assertEquals(2, trie.get("a"));
        Assert.assertEquals(3, trie.get("b"));
        Assert.assertEquals(4, trie.get("aa"));
        Assert.assertEquals(5, trie.get("ab"));
        Assert.assertEquals(6, trie.get("bb"));
        Assert.assertEquals(-1, trie.get("bbb"));
        Assert.assertEquals(-1, trie.get("c"));
    }
}
