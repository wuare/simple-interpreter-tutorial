part15
#### goal
- Improve error reporting in the lexer, parser, and semantic analyzer. Instead of stack traces with very generic messages like “Invalid syntax”, we would like to see something more useful like “SyntaxError: Unexpected token -> Token(TokenType.SEMI, ‘;’, position=23:13)”
