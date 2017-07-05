# simple-fasttext-server

You send a REST get with some text, and get a classification for it.  
Work In Progress.

## Installation

1. install [leiningen](https://leiningen.org/)
2. clone this repo
3. `lein run`

## Usage

```
curl 'http://localhost:3000/predict?text=classify%20me'
```

Or your whatever http get API.
