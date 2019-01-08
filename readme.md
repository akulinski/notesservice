# Notes Service

This is REST API for notes service written in spring boot 

Application uses postgres (resources/application.properties) and H2 database for testing (test/resources/application.properties) 

### Prerequisites
``` 
1. Install jdk 1.8 https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
```
```
2. Install maven https://maven.apache.org/install.html
```
```
3. Install docker https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-18-04
```


### Installing

If you want to turn off mocking of data set notes.mock.count = 0 in resources application.properties (do not change test application.properties )

```
1. Open project root folder in terminal
```
```$xslt
2. sudo docker login
```
If during this step docker build fails please retry
```
3.sudo docker build -t eg_postgresql .
```
This makes postgres run on port 5433 so i won`t interfere with possible existing local installation
```
4. sudo docker run --rm -P -p 5433:5432 --name pg_test eg_postgresql
```

```
5. mvn clean install
```

During start application creates mock data 
```
6. mvn spring-boot:run    
```

```$xslt
7. To close press ctrl + c (this may cause BUILD FAILURE because application finishes with code 1)
 
```

## Running the tests

```$xslt
    mvn clean test
```

## Authors

* **Albert Kuliński** 
