# Sandboxed Interpreter for expercise

## Interpreter Docker Image

### Step 1: Install Docker Engine

https://docs.docker.com/engine/installation/ubuntulinux/

### Step 2: Build Image

``
$ sudo docker build -t expercise/interpreter -f InterpreterDockerfile .
``

### Step 3: Run Interpreter API on port 8080 (for example)

``
$ sudo docker run -d -p 8080:4567 expercise/interpreter
``

Run with memory constraint (128M):

``
sudo docker run -m 128M -d -p 8080:4567 expercise/interpreter
``