#### goal  

learn how to parse and interpret arithmetic expressions that have any number of addition, subtraction, 
multiplication, and division operators.  
write an interpreter that will be able to evaluate expressions like `14 + 2 * 3 - 6 / 2`.  

#### note  

Here are the rules for how to construct a grammar from the precedence table:  

For each level of precedence define a non-terminal. The body of a production for the non-terminal should contain arithmetic operators from that level and non-terminals for the next higher level of precedence.  
Create an additional non-terminal factor for basic units of expression, in our case, integers. The general rule is that if you have N levels of precedence, you will need N + 1 non-terminals in total: one non-terminal for each level plus one non-terminal for basic units of expression.  

#### grammar
![grammar](https://github.com/wuare/simple-interpreter-tutorial/blob/master/part5/images/lsbasi_part5_grammar.png)  
(the image from [Ruslan' Blog](https://ruslanspivak.com/lsbasi-part5/))  
