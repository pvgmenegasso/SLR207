#!/bin/sh
num=0
for file in split*
do
	outfile="split${num}"
	num=$(( $num+ 1))
	mv "$file" "$outfile"
done
