coding-expercise-interpreter
=====================

[![Build Status](https://api.travis-ci.org/expercise/expercise-interpreter.png?branch=master)](https://travis-ci.org/expercise/expercise-interpreter)

# Sandboxed Interpreter for expercise
_**P.S. README is outdated, will be updated soon!**_

## Interpreter Docker Image

### Step 1: Install Docker Engine

https://docs.docker.com/engine/installation/ubuntulinux/

### Step 2: Build Image

``
$ sudo docker build -t expercise/interpreter -f InterpreterDockerfile .
``

### Step 3: Run Interpreter API manually on port 8080 (for example)

``
$ sudo docker run -d -p 8080:4567 expercise/interpreter
``

Run with memory constraint (128 MB):

``
$ sudo docker run -m 128M -d -p 8080:4567 expercise/interpreter
``
