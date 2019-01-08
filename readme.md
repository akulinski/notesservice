# Notes Service

This is REST API for notes service written in spring boot 

### Prerequisites
``` 
1. Install jdk 1.8 https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
```
```
2. Install maven https://maven.apache.org/install.html
```
```
3. Install docker https://docs.docker.com/docker-for-windows/install/
```


### Installing

```
1. Open project root folder in terminal
```

```
2. docker build -t eg_postgresql .
```
This makes postgres run on port 5433 so i won`t interfere with possible existing local installation
```
3. docker run --rm -P -p 5433:5432 --name pg_test eg_postgresql
```

```
4. mvn clean install
```

During start application creates mock data 
```
5. mvn spring-boot:run    
```

```$xslt
6. To close press ctrl + c (this may cause BUILD FAILURE because application finishes with code 1)
 
```

## Running the tests

```$xslt
    mvn clean test
```

## Authors

* **Albert Kuliński** 
