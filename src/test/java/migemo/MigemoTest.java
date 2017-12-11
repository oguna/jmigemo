package migemo;

import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class MigemoTest {
    @Test
    public void parseQuery_abc() throws Exception {
        List<String> parsedQuery = Migemo.parseQuery("abc");
        assertEquals(1, parsedQuery.size());
        assertEquals("abc", parsedQuery.get(0));
    }

    @Test
    public void parseQuery_aBc() throws Exception {
        List<String> parsedQuery = Migemo.parseQuery("aBc");
        assertEquals(2, parsedQuery.size());
        assertEquals("a", parsedQuery.get(0));
        assertEquals("Bc", parsedQuery.get(1));
    }

    @Test
    public void parseQuery_ABc() throws Exception {
        List<String> parsedQuery = Migemo.parseQuery("ABc");
        assertEquals(2, parsedQuery.size());
        assertEquals("AB", parsedQuery.get(0));
        assertEquals("c", parsedQuery.get(1));
    }

    @Test
    public void parseQuery_renbunsetu() {
        List<String> parsedQuery = Migemo.parseQuery("renbunsetuNoKensaku");
        assertEquals(3, parsedQuery.size());
        assertEquals("renbunsetu", parsedQuery.get(0));
        assertEquals("No", parsedQuery.get(1));
        assertEquals("Kensaku", parsedQuery.get(2));
    }

    @Test
    public void parseQuery_aA() {
        List<String> parsedQuery = Migemo.parseQuery("aA");
        assertEquals(2, parsedQuery.size());
        assertEquals("a", parsedQuery.get(0));
        assertEquals("A", parsedQuery.get(1));
    }

    @Test
    public void queryKikai() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kikai");
        String expected = "(ｷｶｲ|キカイ|既会員|棋界|奇怪|喜界|毀壊|機[会械]|貴会|器械|気塊|きかい|ｋｉｋａｉ|kikai)";
        assertEquals(expected, regex);
    }

    @Test
    public void queryKika() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kika");
        String expected = "(ｷｶ|キカ|木川|飢渇|規格|企[劃画]|軌間|亀鑑|汽罐|聞かん坊|利かん[気坊]|饋還|基幹|期間|棋界|喜(界|歌劇)|毀壊|既( 刊|会員)|器[管官械]|機[関会械]|旗[艦下]|麾下|気[管塊化]|季[刊夏]|着[方飾換替]|机下|帰[館艦還化]|幾何|奇([観怪貨禍]|関数)|貴[艦官簡会下家課]|きか|ｋｉｋａ|kika)";
        assertEquals(expected, regex);
    }

    @Test
    public void queryKik() throws Exception {
        Migemo migemo = createMigemo();
        String regex = migemo.query("kik");
        String expected = "([〕〔樵掬鞠鞫椈菊效]|佶屈|拮[抗据]|喫[緊驚]|吃[緊驚]|吉[凶川]|切っ掛|橘花|稀覯|乞巧奠|騎[行甲虎]|詭計|畸[計形 型]|紀([行子]|久男)|Asterales|麹塵|崎嶇|饑饉|樹木希林|寄[航港稿金居]|棊局|枳[穀棘]|Campanulales|桔梗|鰭(棘|脚類)|棄[権教却]|冀求|希[覯求]|聴(取り|き([惚良始流直較比所続終飽入込通取手]|もの))|鬼[哭気]|危[険懼局急機]|輝輝|効[目き]|騏(驥|鬼翁)|諱忌|窺基|暉暉|嬉[々嬉]|起([稿工句居期]|久子)|記[旗紀]|忌諱|ｷ([ｺｹｸｷｶ]|ｯ[ｺｹｸｷｶ])|木([越耳屑川]|古内|構造|形式|久蔵)|飢[ 饉鬼渇]|規[矩格]|企[及劃画]|軌間|亀[甲鑑]|汽[機罐]|聞([え逃耳取書]|こ(え|し召)|く(耳|ならく)|分け|合せ|き([惚忘分様易役 漏洩物始逃難馴慣流直損捨苦較比書所付続違終及落覚飽誤入出込耳届咎通取知方齧返手]|下手|伝え|応え|心地|納め|間違|上手)|かん坊)|利([目酒腕]|け者|き[目酒腕所足耳手]|かん[気坊])|饋還|基[金幹]|期間|棋[局界]|喜([国々気界]|久[子里男雄泉井治]|歌劇)| 毀[棄壊]|既([婚決刊]|会員)|器[局機管官械]|機[根工巧構甲器関会械]|旗[鼓艦下]|麾下|気([骨根功孔候圏配胸球気管塊化]|くばり)|季[候刊夏]|着([込方飾換替]|こなし|崩れ)|机下|帰([国港航結郷京敬休期館艦還化]|去来)|幾何|奇([骨功巧効行計景形矯鰭気奇観 怪貨禍]|っ怪|々(妙々|怪々)|関数)|貴([国校公兄顕君局艦官簡会下家課]|研究室|久子|金属)|き([こけくきか]|っ[こけくきか])|ｋ ｉｋ|キ([コケクカキ]|ッ[コケクキカ])|kik)";
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

    private static Migemo createMigemo() {
        Migemo migemo = new Migemo();
        MigemoDictionary dictionary = new MigemoDictionary();
        dictionary.loadDefault();
        dictionary.build();
        migemo.setDictionary(dictionary);
        return migemo;
    }
}