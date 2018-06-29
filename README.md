# Css Minification application

## Required Prerequisites

* JDK 8
* Chrome v65-67 

## Building and Running

```
./mvnw  package spring-boot:run 
```
Default cache ttl value is equal to 60 seconds.
This can be configured over 'cache.ttl' system property:
e.g:
```
./mvnw  package spring-boot:run -Dcache.ttl=30
```

## Usage Instructions

### Css Minification

```
curl -v -H "Content-Type: application/json" -d "[<url_list>]" localhost:8080/minify
```
e.g.:
```
curl -v -H "Content-Type: application/json" -d "[\"https://linkedin.com/\", "\"https://translate.google.com.ua/\""]" localhost:8080/minify
```

In order to avoid cached values - query parameter 'noCache' can be used:
```
curl -v -H "Content-Type: application/json" -d "[<url_list>]" localhost:8080/minify?noCache=true
```

### Entire Cache Invalidation

```
curl -v localhost:8080/cache/css-minification/remove
```

### Certain Url Cache Invalidation

```
curl -v localhost:8080/cache/css-minification/remove?key=<url>
```
e.g.:
```
curl -v localhost:8080/cache/css-minification/remove?key=https://linkedin.com/
```


