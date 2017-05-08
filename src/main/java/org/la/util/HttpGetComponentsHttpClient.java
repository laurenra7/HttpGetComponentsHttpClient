package org.la.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by laurenra on 5/31/16.
 */
public class HttpGetComponentsHttpClient {

    private static boolean modeVerbose;

    public static void main(String[] args) throws Exception {

        int exitStatus = 0;
        modeVerbose = false;

        // Build command line options
        Options clOptions = new Options();
        clOptions.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Show this help")
                .build());
        clOptions.addOption(Option.builder("o")
                .longOpt("output")
                .desc("output file")
                .hasArg()
                .argName("filename")
                .build());
        clOptions.addOption(Option.builder("v")
                .longOpt("verbose")
                .desc("show HTTP headers/footers and processing messages")
                .build());

        if(args.length == 0) {
            showCommandHelp(clOptions);
        }
        else {
            exitStatus = processCommandLine(args, clOptions);
        }

        System.exit(exitStatus);
    }


    private static int processCommandLine(String[] args, Options clOptions) {
        int executeStatus = 0;
        String url = "";
        String outputJson = "";

        CommandLineParser clParser = new DefaultParser();


        try {
            CommandLine line = clParser.parse(clOptions, args);

            if (line.hasOption("help")) {
                showCommandHelp(clOptions);
            }
            else {
                if (line.hasOption("verbose")) {
                    modeVerbose = true;
                }

                // Remaining command line parameter(s), if any, is URL
                List<String> cmdLineUrl = line.getArgList();
                if(cmdLineUrl.size() > 0) {
                    url = cmdLineUrl.get(0); // Get only the first parameter as URL, ignore others

                    String response = httpComponentsGet(url);
                    if (response != null) {

                        if (line.hasOption("output")) {
                            // Write response to output file
                            executeStatus = writeStringToFile(line.getOptionValue("output"), response);
                        }
                        else {
                            // Write response to console
                            System.out.println(response);
                        }
                    }
                }
                else {
                    System.out.println("Error: no URL");
                    showCommandHelp(clOptions);
                }
            }
        }
        catch (ParseException e) {
            System.err.println("Command line parsing failed. Error: " + e.getMessage() + "\n");
            showCommandHelp(clOptions);
            executeStatus = 1;
        }

        return executeStatus;
    }



    private static String httpComponentsGet(String url) {
        String result = "";

        if (modeVerbose) {
            System.out.println("Http GET from URL: " + url);
        }

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("Accept","text/plain");
        httpGet.addHeader("Accept-Charset", "utf-8");

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();

            if (modeVerbose) {
                result = result + "HTTP response code: " + httpResponse.getStatusLine().getStatusCode() + "\n";
            }

            if (responseCode == HttpStatus.SC_OK) {
                if (modeVerbose) {
                    result = result + "---------- Request Header ----------\n";
                    org.apache.http.Header[] requestHeaders = httpGet.getAllHeaders();
                    for (org.apache.http.Header reqHeader : requestHeaders) {
                        result = result + reqHeader.getName() + ": " + reqHeader.getValue() + "\n";
                    }

                    result = result + "---------- Response Header ----------\n";
                    org.apache.http.Header[] responseHeaders = httpResponse.getAllHeaders();
                    for (org.apache.http.Header respHeader : responseHeaders) {
                        result = result + respHeader.getName() + ": " + respHeader.getValue() + "\n";
                    }
                }

                try (BufferedReader bufferedReader =
                             new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {

                    StringBuffer responseBody = new StringBuffer();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        responseBody.append(line);
                    }

                    if (modeVerbose) {
                        result = result + "---------- Response Body ----------\n";
                    }

                    result = result + responseBody;
                }

            }
            else {
                System.out.println("Problem with request. HTTP status code: " + responseCode);
            }

        }
        catch (IOException e) {
            System.out.println("Error fetching request: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    private static int writeStringToFile(String outputFilename, String outputString) {
        int status = 0;
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        if (modeVerbose) {
            System.out.println("Output file: " + outputFilename);
        }

        try {
            fileWriter = new FileWriter(outputFilename);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(outputString);

        }
        catch (IOException e) {
            System.out.println("Problem writing to file. Error: " + e.getMessage());
            status = 1;
        }
        finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }
            catch (IOException ioErr) {
                System.out.println("Problem closing file. Error: " + ioErr.getMessage());
                status = 1;
            }
        }

        return status;
    }


    private static void showCommandHelp(Options options) {
        String commandHelpHeader = "\nDo an HTTP GET using the Apache HttpComponents HttpClient.\n\n";
        String commandHelpFooter = "\nExamples:\n\n" +
                "  java -jar HttpGetComponentsHttpClient.jar https://someurl.com/get/stuff\n\n" +
                "  java -jar HttpGetComponentsHttpClient.jar -o myfile.txt https://someurl.com/get/stuff\n\n" +
                "  java -jar HttpGetComponentsHttpClient.jar -v https://someurl.com/get/stuff\n\n";

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(88,"java -jar HttpGetComponentsHttpClient.jar url", commandHelpHeader, options, commandHelpFooter, true);
    }


}
