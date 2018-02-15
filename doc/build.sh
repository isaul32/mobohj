#!/bin/sh

DIR=build

pdflatex -aux-directory=$DIR -output-directory=$DIR diary.tex
