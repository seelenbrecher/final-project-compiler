#!/bin/bash
java JLex.Main Scanner.lex
java java_cup.Main Parser.cup
mv Scanner.lex.java Yylex.java
javac *.java

# test
TEST_FOLDER="$PWD/tests/"


for testcase in `ls $TEST_FOLDER`; do 
    echo "====================================================="
    echo "testing testcase $testcase"
    echo "====================================================="
    java parser < $TEST_FOLDER/$testcase
done
