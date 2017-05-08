# HttpGetCommonsHttpClient
Test HTTP GET using the old Apache Commons [HttpClient](http://hc.apache.org/httpclient-3.x/) which was widely used but has been replaced by [HttpClient](http://hc.apache.org/httpcomponents-client-ga/index.html) from [Apache HttpComponents](http://hc.apache.org/).  The Spring Framework uses HttpComponents under the covers.

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

Do an HTTP GET using the Apache Commons HttpClient.

 -h,--help                Show this help
 -o,--output <filename>   output file
 -v,--verbose             show HTTP headers/footers and processing messages

Examples:

  java -jar HttpGetComponentsHttpClient.jar https://someurl.com/get/stuff

  java -jar HttpGetComponentsHttpClient.jar -o myfile.txt https://someurl.com/get/stuff

  java -jar HttpGetComponentsHttpClient.jar -v https://someurl.com/get/stuff
```
