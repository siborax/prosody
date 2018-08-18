package prosodicproc.connections;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fillTheDb {
    public String sentenceId;
    public String word;
    public String start;
    public String end;
    public String pitch;
//    public DataFormat format;
//    public ArrayList<DataFormat> sentenceFormat;
public fillTheDb(File inputFile) throws IOException, JSONException {
    if(ReadXML.category.equals("argument")){
        new fillTheDb(null,inputFile);
    }
    if(ReadXML.category.equals("opinion")){
        new fillTheDb(inputFile,null);
    }
}

    public fillTheDb(File opinionFile, File argumentFile) throws IOException, JSONException {
        mongodb mdb = new mongodb();
      //  mdb.setCategory(ReadXML.category);
        mdb.SetDb();


        JSONObject fileName = new JSONObject();//takes the name of the xmlID
        String xmlId = ReadXML.xml_Id;
        fileName.put("filename", xmlId);
        fileName.put("category",ReadXML.category);

//opinion Files
        if(opinionFile!=null){
        File[] sentences = opinionFile.listFiles();
        JSONObject sentencesArray = new JSONObject(); // takes the name of the sentence folder, example: 1_john_g_roberts_jr
        JSONArray sentencesOpinion = new JSONArray();
        for (File sentence : sentences) {//sentence is C:\Users\Sibora\Desktop\Oral_Argument\Edited_Oral_Argument\07_542\OpinionFolder\pitchForWords\1_john_g_roberts_jr
            System.out.println("sent:" + sentence);
            ArrayList<File> csvFiles = new ArrayList<>();
            JSONObject sentenceObject = new JSONObject();
            sentenceObject.put("sentenceId", sentence.getName());


            //    ArrayList<DataFormat> df = new ArrayList<DataFormat>();
            File[] insideSentence = sentence.listFiles();// insideSentence is min_max.csv or 1_john_g_roberts_jr.csv
            if (insideSentence != null) {
                System.out.println("0" + insideSentence[0]);
                System.out.println("1" + insideSentence[1]);
                sentenceObject.put("words", combineFiles(insideSentence[1], insideSentence[0]));
                //insideSentence[1] ->min_max.csv
                //insideSentence[0] ->1_john_g_roberts_jr.csv

                //  sentenceObject.put("sentences", sentencesArray);
                sentencesOpinion.put(sentenceObject);
            }

        }

        fileName.put("sentencesOpinion", sentencesOpinion);
    }
if(argumentFile!=null) {
    File[] sentencesArgument = argumentFile.listFiles();
    JSONObject sentencesArrayArgument = new JSONObject(); // takes the name of the sentence folder, example: 1_john_g_roberts_jr
    JSONArray sentencessArgument = new JSONArray();
    for (File sentence : sentencesArgument) {//sentence is C:\Users\Sibora\Desktop\Oral_Argument\Edited_Oral_Argument\07_542\OpinionFolder\pitchForWords\1_john_g_roberts_jr
        System.out.println("sent:" + sentence);
        ArrayList<File> csvFiles = new ArrayList<>();
        JSONObject sentenceObject = new JSONObject();
        sentenceObject.put("sentenceId", sentence.getName());


        //    ArrayList<DataFormat> df = new ArrayList<DataFormat>();
        File[] insideSentence = sentence.listFiles();// insideSentence is min_max.csv or 1_john_g_roberts_jr.csv
        if (insideSentence != null) {
            System.out.println("0" + insideSentence[0]);
            System.out.println("1" + insideSentence[1]);
            sentenceObject.put("words", combineFiles(insideSentence[1], insideSentence[0]));
//                fileName.put("sentences", sentencesArrayArgument);
            sentencessArgument.put(sentenceObject);

        }

    }

    fileName.put("sentencesArgument", sentencessArgument);
}

        mdb.storeDocument(fileName);
    }

    private JSONArray combineFiles(File minMaxFile, File pitchFile) throws IOException, JSONException {
      //  BufferedReader minMaxReader = new BufferedReader(new FileReader(minMaxFile.getPath()));//minMaxReader
       // minMaxReader.readLine();
//        Iterable <CSVRecord>linesMinMax = CSVFormat.EXCEL.parse(minMaxReader);
        BufferedReader pitchReader = new BufferedReader(new FileReader(pitchFile.getPath()));
        JSONArray arrayWords = new JSONArray();
        List<String> lines = Files.readAllLines(Paths.get(pitchFile.getPath()), StandardCharsets.UTF_8);//pitch without header
        List<String> linesMinMax = Files.readAllLines(Paths.get(minMaxFile.getPath()), StandardCharsets.UTF_8);//min max with header
        int i = 0;

        // the @lines has smaller number of words since some words are not properly detected from praat
        for (int c = 0; c < lines.size(); c++) {
//            linesMinMax.iterator()
            // DataFormat format = new DataFormat();
            JSONObject arrayWord = new JSONObject();
            String line = lines.get(c);//pitch
            System.out.println("line" + line);
            String[] array = line.split(",");
            System.out.println("c " + (c));

           // System.out.println("array min_max:"+ arrayMinMax.length);
            for (int secondArray = 0; secondArray < linesMinMax.size(); secondArray++) {
                System.out.println(linesMinMax.size());
                String lineMinMax = linesMinMax.get(secondArray);
                System.out.println("length line "+lineMinMax);
               // System.out.println("min max" + linesMinMax.get(c + 1));
                String[] arrayMinMax = lineMinMax.split(",");
                if(array[0].equals(arrayMinMax[0])){
//            if(array[0].contains(minMaxWord.get(0))){
//String pValue = array[2];
                if(array.length>=2){
                        pitch = array[2];
                    System.out.println("array0:" + array[0]);
                    System.out.println(array[0] + " " + array[2]);
                    System.out.println("word " + arrayMinMax[0]);

                    System.out.println("pitch" + array[2]);

                    arrayWord.put("word", arrayMinMax[0]);
                    arrayWord.put("pitch", array[2]);
                    arrayWord.put("start", arrayMinMax[1]);
                    arrayWord.put("end", arrayMinMax[2]);
                }
                else{

                    pitch = String.valueOf(0.0);
                    System.out.println("array0:" + array[0]);
                    System.out.println(array[0] + " " + pitch);
                    System.out.println("word " + arrayMinMax[0]);

                    System.out.println("pitch" + pitch);

                    arrayWord.put("word", arrayMinMax[0]);
                    arrayWord.put("pitch", pitch);
                    arrayWord.put("start", arrayMinMax[1]);
                }
                }
            }

                arrayWords.put(arrayWord);


         //   i++;
        }

        return arrayWords;
    }


//


    //       if (FilenameUtils.removeExtension(file.getName()).equals(FilenameUtils.removeExtension(sentence.getName()))) {
//            System.out.println("1?"+ FilenameUtils.removeExtension(file.getName()));
//            System.out.println("2?"+ FilenameUtils.removeExtension(sentence.getName()));


//    private Collection<? extends DataFormat> pitchValue(File in) throws IOException {
//      //  File pitch_file = new File();
//        List<String> lines = Files.readAllLines(Paths.get(in.getPath()), StandardCharsets.UTF_8);
//        for (String line : lines) {
//            String[] array = line.split(",");
//            if (array[0].contains(format.getWord())) {
//                System.out.println("array0:" + array[0]);
//                System.out.println(array[0] + " " + array[2]);
//                pitch = array[2];
//                // System.out.println("what:"+pitch);
//                format.setPitch(array[2]);
//            }
//        }
//        System.out.println("formatPitch: "+format);
//        return Collections.singleton(format);
//    }

//    private Collection<? extends DataFormat> start_end(BufferedReader in) throws IOException {
//        // DataFormat dataFormat = new DataFormat();
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
//        for (CSVRecord record : records) {
//            word = record.get(0);
//            format.setWord(word);
//            start = record.get(1);
//            format.setStart(start);
//            end = record.get(2);
//            format.setEnd(end);
//        }
//        System.out.println("format: "+format);
//            return Collections.singleton(format);
//
//    }

//        pitch= getPitch(speakerCsv);
//
//

    // }
    //}
    // mdb.closeDB();


    //  }


//    public static void main(String argv[]) throws IOException, JSONException {
//        new fillTheDb(new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\OpinionFolder\\pitchForWords"),new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\ArgumentFolder\\pitchForWords"));
//
//
//    }


}

