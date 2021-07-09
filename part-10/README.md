part10

#### goal
update interpreter to parse and interpret our very first complete Pascal program.  

#### program
```pascal
PROGRAM Part10;
VAR
   number     : INTEGER;
   a, b, c, x : INTEGER;
   y          : REAL;

BEGIN {Part10}
   BEGIN
      number := 2;
      a := number;
      b := 10 * a + 10 * number DIV 4;
      c := a - - b
   END;
   x := 11;
   y := 20 / 7 + 3.14;
   { writeln('a = ', a); }
   { writeln('b = ', b); }
   { writeln('c = ', c); }
   { writeln('number = ', number); }
   { writeln('x = ', x); }
   { writeln('y = ', y); }
END.  {Part10}
```

#### todo
1. We will learn how to parse and interpret the Pascal PROGRAM header
2. We will learn how to parse Pascal variable declarations
3. We will update our interpreter to use the DIV keyword for integer division and a forward slash / for float division
4. We will add support for Pascal comments

#### syntax diagrams and grammar rules
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part10/images/lsbasi_part10_grammar1.png)  
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part10/images/lsbasi_part10_grammar2.png)  
(the image from [Ruslan' Blog](https://ruslanspivak.com/lsbasi-part10/))  
