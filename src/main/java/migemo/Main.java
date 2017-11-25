package migemo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String ABOUT = "jmigemo - J/Migemo Library " + Migemo.VERSION + " Driver";
    private static final String DICT_NAME = "migemo-dict";
    private static final int SUBDICT_MAX = 8;

    private static void queryLoop(Migemo migemo, boolean quiet) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            if (!quiet) {
                System.out.print("QUERY: ");
            }
            while ((line = br.readLine()) != null) {
                String answer = migemo.query(line);
                if (answer != null) {
                    System.out.printf(quiet ? "%s\n" : "PATTERN: %s\n", answer);
                    System.out.flush();
                }
                if (!quiet) {
                    System.out.print("QUERY: ");
                }
            }
        }
    }

    private static void help(String name) {
        System.out.printf("%s \n" +
                        "\n" +
                        "USAGE: %s [OPTIONS]\n" +
                        "\n" +
                        "OPTIONS:\n" +
                        "  -d --dict <dict>	Use a file <dict> for dictionary.\n" +
                        "  -s --subdict <dict>	Sub dictionary files. (MAX %d times)\n" +
                        "  -q --quiet		Show no message except results.\n" +
                        "  -v --vim		Use vim style regexp.\n" +
                        "  -e --emacs		Use emacs style regexp.\n" +
                        "  -n --nonewline	Don't use newline match.\n" +
                        "  -w --word <word>	Expand a <word> and soon exit.\n" +
                        "  -h --help		Show this message.\n"
                , ABOUT, name, SUBDICT_MAX);
    }

    public static void main(String[] args) throws IOException {
        boolean mode_vim = false;
        boolean mode_emacs = false;
        boolean mode_nonewline = false;
        boolean mode_quiet = false;
        List<String> subdicts = new ArrayList<>();
        String dict = null;
        String word = null;
        Migemo migemo;
        PrintStream fplog = System.out;
        String prgname = Migemo.class.getSimpleName();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--vim":
                case "-v":
                    mode_vim = true;
                    break;
                case "--emacs":
                case "-e":
                    mode_emacs = true;
                    break;
                case "--nonewline":
                case "-n":
                    mode_nonewline = true;
                    break;
                case "--dict":
                case "-d":
                    dict = args[++i];
                    break;
                case "--subdict":
                case "-s":
                    if (subdicts.size() < SUBDICT_MAX) {
                        subdicts.add(arg);
                    }
                case "--word":
                case "-w":
                    word = args[++i];
                    break;
                case "--quiet":
                case "-q":
                    mode_quiet = true;
                    break;
                case "--help":
                case "-h":
                    help(prgname);
                    return;
            }
        }

        // 辞書をカレントディレクトリと1つ上のディレクトリから捜す
        if (dict == null) {
            File currentDirDict = new File("./dict/");
            File parentDirDict = new File("../dict/");
            if (currentDirDict.exists()) {
                migemo = new Migemo(currentDirDict);
                if (word == null && !mode_quiet) {
                    fplog.printf("migemo_open(%s)=%s\n", "./dict/" + DICT_NAME, migemo);
                }
            } else if (parentDirDict.exists()) {
                migemo = new Migemo(parentDirDict);
                if (word == null && !mode_quiet) {
                    fplog.printf("migemo_open(\"%s\")=%s\n", "../dict/" + DICT_NAME, migemo);
                }
            } else {
                migemo = new Migemo();
            }
        } else {
            migemo = new Migemo(new File(dict));
            if (word == null && !mode_quiet) {
                fplog.printf("migemo_open(%s)=%s\n", dict, migemo);
            }
        }
        // サブ辞書を読み込む
        if (subdicts.size() > 0) {
            throw new UnsupportedOperationException();
            //for (String subdict : subdicts) {
            //    migemo.load(new File(subdict));
            //    if (word != null && !mode_quiet) {
            //        fplog.printf("migemo_load(%s, \"%s\")\n", migemo, subdict);
            //    }
            //}
        }

        if (mode_vim) {
            if (mode_nonewline) {
                migemo.setOperator(RegexOperator.VIM_NONEWLINE);
            } else {
                migemo.setOperator(RegexOperator.VIM_NEWLINE);
            }
        } else if (mode_emacs) {
            if (mode_nonewline) {
                migemo.setOperator(RegexOperator.EMACS_NONEWLINE);
            } else {
                migemo.setOperator(RegexOperator.EMACS_NEWLINE);
            }
        }
        if (word != null) {
            String ans = migemo.query(word);
            if (ans != null) {
                fplog.printf(mode_vim ? "%s" : "%s\n", ans);
            }
        } else {
            if (!mode_quiet) {
                System.out.printf("clock()=%f\n", System.currentTimeMillis() / 1000d);
            }
            queryLoop(migemo, mode_quiet);
        }
    }
}
