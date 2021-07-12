package top.wuare.part16;

import top.wuare.part16.exception.LexerError;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Lexer {
    private final String text;
    private int pos;
    private int lineno = 1;
    private int column = 1;
    private Character curCh;
    private final Map<String, Token> RESERVED_KEYWORDS = new HashMap<>();

    {
        RESERVED_KEYWORDS.put("BEGIN", new Token(Token.BEGIN, "BEGIN"));
        RESERVED_KEYWORDS.put("END", new Token(Token.END, "END"));
        RESERVED_KEYWORDS.put("PROGRAM", new Token(Token.PROGRAM, "PROGRAM"));
        RESERVED_KEYWORDS.put("VAR", new Token(Token.VAR, "VAR"));
        RESERVED_KEYWORDS.put("DIV", new Token(Token.INTEGER_DIV, "DIV"));
        RESERVED_KEYWORDS.put("INTEGER", new Token(Token.INTEGER, "INTEGER"));
        RESERVED_KEYWORDS.put("REAL", new Token(Token.REAL, "REAL"));
        RESERVED_KEYWORDS.put("PROCEDURE", new Token(Token.PROCEDURE, "PROCEDURE"));
    }

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.curCh = text.charAt(this.pos);
    }

    public void error() {
        String s = String.format("Lexer error on '%s' line: %d column: %d", curCh, lineno, column);
        throw new LexerError(s);
    }

    public void advance() {
        // Advance the 'pos' pointer and set the 'current_char' variable
        if (curCh == '\n') {
            lineno++;
            column = 0;
        }
        this.pos++;
        if (pos > len(text) - 1) {
            curCh = null;
        } else {
            curCh = text.charAt(pos);
            column++;
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

    public void skipComment() {
        while (curCh != '}') {
            advance();
        }
        advance();
    }

    public Token number() {
        // Return a (multi-digit) integer or float consumed from the input
        StringBuilder builder = new StringBuilder();
        while (curCh != null && Character.isDigit(curCh)) {
            builder.append(curCh);
            advance();
        }
        if (curCh != null && curCh == '.') {
            builder.append(curCh);
            advance();
            while (curCh != null && Character.isDigit(curCh)) {
                builder.append(curCh);
                advance();
            }
            return new Token(Token.REAL_CONST, builder.toString());
        }
        return new Token(Token.INTEGER_CONST, builder.toString());
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
            if (curCh == '{') {
                advance();
                skipComment();
                continue;
            }
            if (Character.isLetter(curCh)) {
                return id();
            }
            if (curCh == ':' && peek() == '=') {
                Token token = new Token(Token.ASSIGN, ":=", lineno, column);
                advance();
                advance();
                return token;
            }
            if (curCh == ';') {
                Token token = new Token(Token.SEMI, ";", lineno, column);
                advance();
                return token;
            }
            if (curCh == '.') {
                Token token = new Token(Token.DOT, ".", lineno, column);
                advance();
                return token;
            }
            if (Character.isDigit(curCh)) {
                return number();
            }
            if (curCh == '+') {
                Token token = new Token(Token.PLUS, "+", lineno, column);
                advance();
                return token;
            }
            if (curCh == '-') {
                Token token = new Token(Token.MINUS, "-", lineno, column);
                advance();
                return token;
            }
            if (curCh == '*') {
                Token token = new Token(Token.MUL, "*", lineno, column);
                advance();
                return token;
            }
            if (curCh == '(') {
                Token token = new Token(Token.LPAREN, "(", lineno, column);
                advance();
                return token;
            }
            if (curCh == ')') {
                Token token = new Token(Token.RPAREN, ")", lineno, column);
                advance();
                return token;
            }
            if (curCh == ':') {
                Token token = new Token(Token.COLON, ":", lineno, column);
                advance();
                return token;
            }
            if (curCh == ',') {
                Token token = new Token(Token.COMMA, ",", lineno, column);
                advance();
                return token;
            }
            if (curCh == '/') {
                Token token = new Token(Token.FLOAT_DIV, "/", lineno, column);
                advance();
                return token;
            }
            error();
        }
        return new Token(Token.EOF, null);
    }

    public int getLineno() {
        return lineno;
    }

    public void setLineno(int lineno) {
        this.lineno = lineno;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Character getCurCh() {
        return curCh;
    }
}
