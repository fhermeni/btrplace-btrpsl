#!/bin/sh
#Compile the document written in Markdown to HTML

EXE=Markdown.pl
#README
$EXE README.txt > README.html

IN="src/main/md"
OUT="src/main/doc"
for d in `ls $IN/`; do
    name=`basename $d .md`
    $EXE $IN/$d > $OUT/$name.html
done
