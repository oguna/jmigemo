package migemo;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class RegexGeneratorTest {
    @Test
    public void test() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"baby", "bad", "bank", "box", "dad", "dance"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        Pattern pattern = Pattern.compile(regex);
        for (String word : words) {
            Matcher matcher = pattern.matcher(word);
            assertTrue("generated regex not find (regex:" + regex + " target:" + word + ")",  matcher.find());
        }
    }

    @Test
    public void test1() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"bad", "dad"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        assertEquals("(dad|bad)", regex);
    }

    @Test
    public void test2() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"bad", "bat"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        assertEquals("ba[td]", regex);
    }


    @Test
    public void test3() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"a", "b", "a"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        assertEquals("[ba]", regex);
    }

    @Test
    public void test4() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"あかい", "あかるい"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        assertEquals("あか", regex);
    }


    @Test
    public void test5() {
        RegexGenerator generator = new RegexGenerator();
        String[] words = new String[]{"たのしい", "たのしみ"};
        for (String word : words) {
            generator.add(word);
        }
        String regex = generator.generate();
        assertEquals("たのし", regex);
    }
}