package top.wuare.part9;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Lexer {
    private final String text;
    private int pos;
    private Character curCh;
    private final Map<String, Token> RESERVED_KEYWORDS = new HashMap<>();

    {
        RESERVED_KEYWORDS.put("BEGIN", new Token(Token.BEGIN, "BEGIN"));
        RESERVED_KEYWORDS.put("END", new Token(Token.END, "END"));
    }

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.curCh = text.charAt(this.pos);
    }

    public void error() {
        throw new RuntimeException("Invalid character");
    }

    public void advance() {
        // Advance the 'pos' pointer and set the 'current_char' variable
        this.pos++;
        if (pos > len(text) - 1) {
            curCh = null;
        } else {
            curCh = text.charAt(pos);
        }
    }
    public int len(String text) {
        return text != null ? text.length() : 0;
    }

    public Character peek() {
        int peekPos = pos + 1;
        if (peekPos > len(text) - 1) {
            return null;
        }
        return text.charAt(peekPos);
    }

    public void skipWhiteSpace() {
        while (curCh != null && Character.isWhitespace(curCh)) {
            advance();
        }
    }

    public int integer() {
        // Return a (multi-digit) integer consumed from the input
        StringBuilder builder = new StringBuilder();
        while (curCh != null && Character.isDigit(curCh)) {
            builder.append(curCh);
            advance();
        }
        return Integer.parseInt(builder.toString());
    }

    public Token id() {
        // Handle identifiers and reserved keywords
        StringBuilder builder = new StringBuilder();
        while (curCh != null && Character.isLetterOrDigit(curCh)) {
            builder.append(curCh);
            advance();
        }
        return RESERVED_KEYWORDS.getOrDefault(builder.toString(), new Token(Token.ID, builder.toString()));
    }

    public Token getNextToken() {
        // Lexical analyzer (also known as scanner or tokenizer)
        // This method is responsible for breaking a sentence apart into tokens. One token at a time
        while (curCh != null) {
            if (Character.isWhitespace(curCh)) {
                skipWhiteSpace();
                continue;
            }
            if (Character.isLetter(curCh)) {
                return id();
            }
            if (curCh == ':' && peek() == '=') {
                advance();
                advance();
                return new Token(Token.ASSIGN, ":=");
            }
            if (curCh == ';') {
                advance();
                return new Token(Token.SEMI, ";");
            }
            if (curCh == '.') {
                advance();
                return new Token(Token.DOT, ".");
            }
            if (Character.isDigit(curCh)) {
                return new Token(Token.INTEGER, String.valueOf(integer()));
            }
            if (curCh == '+') {
                advance();
                return new Token(Token.PLUS, "+");
            }
            if (curCh == '-') {
                advance();
                return new Token(Token.MINUS, "-");
            }
            if (curCh == '*') {
                advance();
                return new Token(Token.MUL, "*");
            }
            if (curCh == '/') {
                advance();
                return new Token(Token.DIV, "/");
            }
            if (curCh == '(') {
                advance();
                return new Token(Token.LPAREN, "(");
            }
            if (curCh == ')') {
                advance();
                return new Token(Token.RPAREN, ")");
            }
            error();
        }
        return new Token(Token.EOF, null);
    }
}
