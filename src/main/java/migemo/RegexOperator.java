package migemo;

import java.util.Objects;

public class RegexOperator {
    public final String or;
    public final String beginGroup;
    public final String endGroup;
    public final String beginClass;
    public final String endClass;
    public final String newline;

    public RegexOperator(String or, String beginGroup, String endGroup, String beginClass, String endClass, String newline) {
        this.or = Objects.requireNonNull(or);
        this.beginGroup = Objects.requireNonNull(beginGroup);
        this.endGroup = Objects.requireNonNull(endGroup);
        this.beginClass = Objects.requireNonNull(beginClass);
        this.endClass = Objects.requireNonNull(endClass);
        this.newline = newline;
    }

    public static final RegexOperator DEFAULT = new RegexOperator("|", "(", ")", "[", "]", null);
    public static final RegexOperator VIM_NONEWLINE = new RegexOperator("\\|", "\\%(", "\\)", "[", "]", null);
    public static final RegexOperator VIM_NEWLINE = new RegexOperator("\\|", "\\%(", "\\)", "[", "]", "\\_s*");
    public static final RegexOperator EMACS_NONEWLINE = new RegexOperator("\\|", "\\(", "\\)", "[", "]", null);
    public static final RegexOperator EMACS_NEWLINE = new RegexOperator("\\|", "\\(", "\\)", "[", "]", "\\s-*");
}
