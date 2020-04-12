package migemo;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class MigemoTest {
    @Test
    public void parseQuery_abc() {
        Iterator<String> iterator = Migemo.parseQuery("abc");
        assertTrue(iterator.hasNext());
        assertEquals("abc", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void parseQuery_aBc() {
        Iterator<String> iterator = Migemo.parseQuery("aBc");
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Bc", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void parseQuery_ABc() {
        Iterator<String> iterator = Migemo.parseQuery("ABc");
        assertTrue(iterator.hasNext());
        assertEquals("AB", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void parseQuery_renbunsetu() {
        Iterator<String> iterator = Migemo.parseQuery("renbunsetuNoKensaku");
        assertTrue(iterator.hasNext());
        assertEquals("renbunsetu", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("No", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Kensaku", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void parseQuery_aA() {
        Iterator<String> iterator = Migemo.parseQuery("aA");
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void queryKikai() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kikai");
        String expected = "(kikai|きかい|キカイ|喜界|器械|奇怪|既会員|棋界|機[会械]|毀壊|気塊|貴会|ｋｉｋａｉ|ｷｶｲ)";
        assertEquals(expected, regex);
    }

    @Test
    public void queryKika() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kika");
        String expected = "(kika|きか|キカ|亀鑑|企[劃画]|利かん[坊気]|喜(界|歌劇)|器[官械管]|基幹|奇([怪禍観貨]|関数)|季[刊夏]|帰[化艦還館]|幾何|旗[下艦]|既(刊|会員)|期間|木川|机下|棋界|機[会械関]|毀壊|気[化塊管]|汽罐|着[換方替飾]|聞かん坊|規格|貴[下会官家簡艦課]|軌間|飢渇|饋還|麾下|ｋｉｋａ|ｷｶ)";
        assertEquals(expected, regex);
    }

    @Test
    public void queryKik() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kik");
        String expected = "([〔〕掬效椈樵菊鞠鞫]|Asterales|Campanulales|kik|き([かきくけこ]|っ[かきくけこ])|キ([カキクケコ]|ッ[カキクケコ])|乞巧奠|亀[甲鑑]|企[劃及画]|佶屈|冀求|切っ掛|利([目腕酒]|かん[坊気]|き[所手目耳腕足酒]|け者)|効[き目]|危[局急懼機険]|吃[緊驚]|吉[凶川]|喜([々国気界]|久[井子治泉男里雄]|歌劇)|喫[緊驚]|器[官局械機管]|基[幹金]|奇([功効奇巧形怪景気矯禍行観計貨骨鰭]|々(妙々|怪々)|っ怪|関数)|嬉[々嬉]|季[候刊夏]|寄[居港稿航金]|崎嶇|希[求覯]|帰([京休化国敬期港結航艦還郷館]|去来)|幾何|忌諱|拮[抗据]|旗[下艦鼓]|既([刊婚決]|会員)|暉暉|期間|木([屑川耳越]|久蔵|古内|形式|構造)|机下|枳[棘穀]|桔梗|棄[却教権]|棊局|棋[局界]|樹木希林|橘花|機[会器工巧根械構甲関]|毀[壊棄]|気([候功化圏塊孔根気球管胸配骨]|くばり)|汽[機罐]|畸[型形計]|着([換方替込飾]|こなし|崩れ)|稀覯|窺基|紀([子行]|久男)|聞([え取書耳逃]|かん坊|き([付入出分及取咎始届役忘惚慣所手捨損方易書様比洩流漏物直知終続耳苦落覚誤較込返逃通違難飽馴齧]|上手|下手|伝え|心地|応え|納め|間違)|く(耳|ならく)|こ(え|し召)|分け|合せ)|聴(き([入取始惚所手比流直終続良較込通飽]|もの)|取り)|規[格矩]|記[旗紀]|詭計|諱忌|貴([下会兄公君国官家局校簡艦課顕]|久子|研究室|金属)|起([句居工期稿]|久子)|軌間|輝輝|飢[渇饉鬼]|饋還|饑饉|騎[甲虎行]|騏(驥|鬼翁)|鬼[哭気]|鰭(棘|脚類)|麹塵|麾下|ｋｉｋ|ｷ([ｶｷｸｹｺ]|ｯ[ｶｷｸｹｺ]))";
        assertEquals(expected, regex);
    }

    @Test
    public void testRenbunsetsu() {
        Migemo migemo = createMigemo();
        String regex = migemo.query("renbunsetuNoKensaku");
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("連文節の検索").matches());
    }

    @Test
    public void testRenbunsetsu2() {
        Migemo migemo = createMigemo();
        String regex = migemo.query("renbunsetu no kensaku");
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("連文節の検索").matches());
    }

    @Test
    public void testNihonN() {
        Migemo migemo = createMigemo();
        String regex = migemo.query("nihon n");
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("日本の").matches());
    }

    private static Migemo createMigemo() {
        Migemo migemo = new Migemo();
        MigemoDictionary dictionary = new MigemoDictionary();
        dictionary.loadDefault();
        dictionary.build();
        migemo.setDictionary(dictionary);
        return migemo;
    }
}