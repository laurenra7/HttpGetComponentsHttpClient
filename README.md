# HttpGetComponentsHttpClient
Test HTTP GET using the newer Apache HttpComponents [HttpClient](http://hc.apache.org/httpcomponents-client-4.5.x/index.html) which replaced the older Apache Commons [HttpClient](http://hc.apache.org/httpclient-3.x/).

This newer HttpClient is compatible with the latest SSL and TLS.  The older commons HttpClient is not.  The Spring Framework [RestTemplate](https://docs.spring.io/spring/docs/current/javadoc-api/index.html?org/springframework/web/client/RestTemplate.html) uses HttpComponents under the covers.

### Build

Build with [Maven](https://maven.apache.org/).

```
mvn clean install
```

Produces an executable .jar file

```
/target/HttpGetComponentsHttpClient.jar
```


### Run

```
java -jar HttpGetComponentsHttpClient.jar
```


### Options

```
usage: java -jar HttpGetComponentsHttpClient.jar url [-h] [-o <filename>] [-v]

Do an HTTP GET using the Apache HttpComponents HttpClient.

 -h,--help                Show this help
 -o,--output <filename>   output file
 -v,--verbose             show HTTP headers/footers and processing messages

Examples:

  java -jar HttpGetComponentsHttpClient.jar https://someurl.com/get/stuff

  java -jar HttpGetComponentsHttpClient.jar -o myfile.txt https://someurl.com/get/stuff

  java -jar HttpGetComponentsHttpClient.jar -v https://someurl.com/get/stuff
```
