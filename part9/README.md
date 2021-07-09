part9

#### goal
1. How to parse and interpret a Pascal program definition.
2. How to parse and interpret compound(复合) statements.
3. How to parse and interpret assignment statements, including variables.
4. A bit about symbol tables and how to store and lookup variables.

#### sample Pascal-like program
```pascal
BEGIN
    BEGIN
        number := 2;
        a := number;
        b := 10 * a + 10 * number / 4;
        c := a - - b
    END;
    x := 11;
END.
```
#### syntax diagrams and grammar rules
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part9/images/lsbasi_part9_syntax_diagram_01.png)  
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part9/images/lsbasi_part9_syntax_diagram_02.png)  
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part9/images/lsbasi_part9_syntax_diagram_03.png)  
(the image from [Ruslan' Blog](https://ruslanspivak.com/lsbasi-part9/))  

#### complete grammar
```grammar
program : compound_statement DOT

    compound_statement : BEGIN statement_list END

    statement_list : statement
                   | statement SEMI statement_list

    statement : compound_statement
              | assignment_statement
              | empty

    assignment_statement : variable ASSIGN expr

    empty :

    expr: term ((PLUS | MINUS) term)*

    term: factor ((MUL | DIV) factor)*

    factor : PLUS factor
           | MINUS factor
           | INTEGER
           | LPAREN expr RPAREN
           | variable

    variable: ID
```
