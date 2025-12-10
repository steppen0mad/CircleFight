#!/bin/bash

set -e

SRC_DIR="src"
OUT_DIR="out"
CLASS_DIR="$OUT_DIR/cfs"
LIB_DIR="lib"
JAR="$LIB_DIR/jaylib-5.5.0-2.jar"

echo "Cleaning output directory..."
rm -rf "$OUT_DIR"
mkdir -p "$CLASS_DIR"

echo "Compiling..."
javac -cp "$JAR" -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")

echo "Running..."
java -cp "$OUT_DIR:$JAR" cfs.CFSApp

