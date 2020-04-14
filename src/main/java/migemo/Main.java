package migemo;

import java.io.*;

public class Main {
    private static final String ABOUT = "jmigemo";

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
                        "  -q --quiet		Show no message except results.\n" +
                        "  -v --vim		Use vim style regexp.\n" +
                        "  -e --emacs		Use emacs style regexp.\n" +
                        "  -n --nonewline	Don't use newline match.\n" +
                        "  -w --word <word>	Expand a <word> and soon exit.\n" +
                        "  -h --help		Show this message.\n"
                , ABOUT, name);
    }

    public static void main(String[] args) throws IOException {
        boolean mode_vim = false;
        boolean mode_emacs = false;
        boolean mode_nonewline = false;
        boolean mode_quiet = false;
        String dict = null;
        String word = null;
        final PrintStream fplog = System.out;
        final String prgname = Migemo.class.getSimpleName();
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

        // 辞書ファイルの読み込み
        final MigemoCompactDictionary dictionary;
        if (dict == null) {
            try (InputStream is = MigemoDefaultCompactDictionary.getStream()) {
                dictionary = new MigemoCompactDictionary(is);
            }
        } else {
            try (InputStream is = new FileInputStream(dict);
                BufferedInputStream bis = new BufferedInputStream(is)) {
                dictionary = new MigemoCompactDictionary(bis);
            }
        }

        final Migemo migemo = new Migemo();
        migemo.setDictionary(dictionary);

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
            final String ans = migemo.query(word);
            if (ans != null) {
                fplog.printf(mode_vim ? "%s" : "%s\n", ans);
            }
        } else {
            queryLoop(migemo, mode_quiet);
        }
    }
}
