# simple-fasttext-server

You send a REST GET with some text, and get a classification for it.  
Work In Progress.

## Installation

1. install [leiningen](https://leiningen.org/)
2. clone this repo
3. change directory to the included directory `fasttext`.
4. git clone [fasttext](https://github.com/facebookresearch/fastText) here.
3. place your trained fasttext model here as well (should be named `classifier.bin`).
4. `lein run` (takes few seconds to start up)

## Usage

```
curl 'http://localhost:3000/predict?text=classify%20me'
```

Or your whatever http get API.
