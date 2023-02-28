#!/bin/bash 
file=textback.unhuf.txt
if test -f "$file"; then 
rm "$file" 
else echo "Executed" 
fi
