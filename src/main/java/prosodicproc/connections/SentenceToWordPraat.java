package prosodicproc.connections;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;

import static prosodicproc.connections.ReadXML.nameCorrespondingAudioFile;

public class SentenceToWordPraat {
   private static String path;
    private static File praat_output;
    private static File wordsFile;
    private static File outputParent;
    private static String  outputText;//the folder where the csv with frequency of each word is stored for each sentence



    //public SentenceToWordPraat(File input, File output) throws InterruptedException, IOException, URISyntaxException {
  public SentenceToWordPraat(File input,File wordsFile) throws Exception {
      //
    //  File input= new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\OpinionFolder\\ListOfSentenceFiles");
     // File output= new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\OpinionFolder\\Words");
this.wordsFile=wordsFile;
this.outputParent=wordsFile.getParentFile();
    File[] listOfFiles = input.listFiles();
    for (File f:listOfFiles) {
        //this.
                path=f.getPath();

        //System.out.println("path "+path);
        //this.
                praat_output=new File(wordsFile+"\\"+FilenameUtils.removeExtension(f.getName()));
        if(!praat_output.exists()){
            praat_output.mkdirs();
        }
//        System.out.println("praat output: "+praat_output);
        scriptRun_SentenceToWordPraat(path,praat_output);
        getMaxPitch(praat_output);



//        GetTimesForEachWord gt = new GetTimesForEachWord(new File(returnedF),f);
    }

}
    public static void scriptRun_SentenceToWordPraat(String path, File poutput) throws IOException, URISyntaxException, InterruptedException {
        String[] command = {"cmd.exe", "/c", "Praat.exe --run ExtractFromLongSound_fromdirectory_Words.praat \"" + path + "\""+" \"" + poutput+ "\"" };
        ProcessBuilder probuilder = new ProcessBuilder(command);
        probuilder.directory(new File("C:\\Users\\Sibora\\Desktop\\"));
        Process process = probuilder.start();
        process.waitFor();
        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
//        System.out.printf("Output of scriptRun_SentenceToWordPraat running %s is:\n",
//                Arrays.toString(command));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            try {
                int exitValue = process.waitFor();
                System.out.println("\n\n Sentencet per word Exit Value is " + exitValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           // Praat praat = new Praat();
            //praat.insertToPraat("SentenceToWord.praat", path, praat_output.toString());
        }
       //
        //nameCorrespondingAudioFile(new File((praat_output + "\\\\")));
        //praat_output is the directory where audio files are stored. The directory of argument audio files is different
        //from the directory of opinion audio files.


    }
    public File getWordsFile(){
        return praat_output;
    }

    public static void getMaxPitch(File output) throws Exception {


        //create the folder to output the csv with highest frequency(pitch value) of each word in each sentence
         File outputFolder= new File(outputParent+"\\"+"pitchForWords"+"\\"+FilenameUtils.removeExtension(output.getName()));
    if(!outputFolder.getParentFile().exists()) {
        outputFolder.getParentFile().mkdirs();
    }


        if(!outputFolder.exists()){
            outputFolder.mkdirs();
    }


       //File outputfile = new File(outputFolder+"\\"+FilenameUtils.removeExtension(output.getName())+".csv");
//        if (!outputfile.exists()) {
       // BufferedReader bReader = new BufferedReader(new FileReader(outputFolder+"\\"+FilenameUtils.removeExtension(output.getName())+".csv"));
        outputText=outputFolder+"\\"+FilenameUtils.removeExtension(output.getName())+".csv";

//        System.out.println("outputText_getMaxPitch"+outputText);

        //  String[] command = {"cmd.exe", "/c", "Praat.exe --run ExtractFromLongSound_fromdirectory_Words.praat \"" + path + "\""+" \"" + praat_output+ "\"" };
        String[] command = {"cmd.exe", "/c", "Praat.exe --run scriptWithFilePath_AllCSV.praat \"" +output +"\" "+"\"" +outputText+"\""};
        ProcessBuilder probuilder1 = new ProcessBuilder(command);
        probuilder1.directory(new File("C:\\Users\\Sibora\\Desktop\\"));
        Process process = probuilder1.start();
        process.waitFor();
        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running %s is:\n",
                Arrays.toString(command));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            try {
                int exitValue = process.waitFor();
//                System.out.println("\n\nExit Value is " + exitValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
//        GetTimesForEachWord gt = new GetTimesForEachWord(new File( outputText),wordsFile);
//        System.out.println("text sended "+ outputText);
//        System.out.println("wordsFile "+ wordsFile);
    }

    public static File getcsvPathofWordsPitch(){
      return  new File(outputText);//Path of csv
    }
//    public static void main(String argv[]) throws InterruptedException, IOException, URISyntaxException {
//        new SentenceToWordPraat();
//    }
    }
