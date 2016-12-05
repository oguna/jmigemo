package migemo;

import java.util.Objects;

public class RegexOperator {
    public final String or;
    public final String nestIn;
    public final String nestOut;
    public final String selectIn;
    public final String selectOut;
    public final String newline;

    public RegexOperator(String or, String nestIn, String nestOut, String selectIn, String selectOut, String newline) {
        this.or = Objects.requireNonNull(or);
        this.nestIn = Objects.requireNonNull(nestIn);
        this.nestOut = Objects.requireNonNull(nestOut);
        this.selectIn = Objects.requireNonNull(selectIn);
        this.selectOut = Objects.requireNonNull(selectOut);
        this.newline = newline;
    }

    public static final RegexOperator DEFAULT = new RegexOperator("|", "(", ")", "[", "]", null);
    public static final RegexOperator VIM_NONEWLINE = new RegexOperator("\\|", "\\%(", "\\)", "[", "]", null);
    public static final RegexOperator VIM_NEWLINE = new RegexOperator("\\|", "\\%(", "\\)", "[", "]", "\\_s*");
    public static final RegexOperator EMACS_NONEWLINE = new RegexOperator("\\|", "\\(", "\\)", "[", "]", null);
    public static final RegexOperator EMACS_NEWLINE = new RegexOperator("\\|", "\\(", "\\)", "[", "]", "\\s-*");
}
