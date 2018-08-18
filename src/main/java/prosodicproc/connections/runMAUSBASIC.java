package prosodicproc.connections;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.toilelibre.libe.curl.Curl.curl;
import static org.toilelibre.libe.curl.Curl.$;

//@RestController
public class runMAUSBASIC {
    private static File text;
    private static File audio;
    public runMAUSBASIC(File text, File audio){
        this.text=text;
        this.audio=audio;
    }

    // private static String rsp;

    public static void getTextGrid() throws IOException, URISyntaxException, InterruptedException {
        String url = "http://clarin.phonetik.uni-muenchen.de/BASWebServices/services/runMAUSBasic";
        String username = "sibora.xhema@uni-konstanz.de";
        String password = "Xhihenaj1994";
        String textFile = "@"+text.toString();
       System.out.println("tfile from runmouse: "+textFile);
       //String textFile = "@C:\\Users\\Sibora\\IdeaProjects\\prosodic_Processing\\1_john_g_roberts_jr.txt";
        String audioFile = "@"+audio.toString();
       //String audioFile = "@C:\\Users\\Sibora\\IdeaProjects\\prosodic_Processing\\1_john_g_roberts_jr.wav";
        // String url="https://www.example.com/xyz/abc";
        System.out.println("audiofile form runmouse:"+ audioFile);
        String[] command = {"curl", "-k", "-X", "POST", "-H", "'content-type:multipart/form-data'", "-u " + username + ":" + password, "-F " + "LANGUAGE=eng-US", "-F TEXT=" + textFile, "-F SIGNAL=" + audioFile, url};
        ProcessBuilder probuilder = new ProcessBuilder(command);
        probuilder.redirectErrorStream(true);
        Process process = probuilder.start();
        process.waitFor();
        // Process p;
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running %s is:\n",
                Arrays.toString(command));
        String TextGridName = null;
        StringBuffer list = new StringBuffer();
        StringBuffer stringB= new StringBuffer();
        System.out.println("null or not: "+br.readLine());
        while ((line = br.readLine()) != null) {

            System.out.println("line: "+line);
            // TextGridName = null;
            try {
                int exitValue = process.waitFor();
                System.out.println("\n\nExit Value is " + exitValue);
                stringB.append(line);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            Document doc = Jsoup.parse(stringB.toString());
//            System.out.println("output doc: "+doc.select(":containsOwn(text)").first().ownText());
            String link = doc.select(":containsOwn(text)").first().ownText();
            System.out.println(link);

            //get the name of the file returned by the runBasicMaus
            URI uri = new URI(link);
            String[] segments = uri.getPath().split("/");

            TextGridName = segments[segments.length - 1];
        System.out.println("tgn: "+ TextGridName);
            URL url0 = new URL(link);
            URLConnection yc = url0.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                list.append(inputLine);
                list.append("\n");
            }
            System.out.println("textgriddd:"+ list);
            in.close();


       // Path file = null;
        //System.out.println("name:"+TextGridName);
        if (TextGridName != null) {
           Path file = Paths.get(TextGridName);
            Files.write(Paths.get(text.getParent() +"\\" +file.getFileName()), Collections.singleton(list), Charset.forName("UTF-8"));
        }

    }
    public File get_runMAUSBASICFile(){
        return text.getParentFile();//return sentence file ex: 1_john_g_roberts_jr
    }
}