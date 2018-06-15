package prosodicproc.connections;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import prosodicproc.connections.praat.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static prosodicproc.connections.praat.PraatFile.read;
import static prosodicproc.connections.praat.PraatFile.readFromFile;

public class GetTimesForEachWord {
    private  String textGrid;
    public File min_max;
    public GetTimesForEachWord(File inputCsvPath, File ListOfSentenceFiles) throws Exception {
        File[] listOfFiles = ListOfSentenceFiles.listFiles();
        File [] csvFiles =inputCsvPath.listFiles();
        for (File f:listOfFiles) {
File[] arrayOfFiles = f.listFiles();

for(File insideFile:arrayOfFiles) {
    textGrid = insideFile.getName();
    if (textGrid.endsWith(".TextGrid")) {
        System.out.println("TextGrid: ");
        for (File speakersFiles : csvFiles) {
            File[] listOfSpeakerSentences = speakersFiles.listFiles();
            for(File csv: listOfSpeakerSentences){
                System.out.println("textgrid: " +textGrid);
                System.out.println("csv: " + csv.getName());

                if (FilenameUtils.removeExtension(textGrid).equals(FilenameUtils.removeExtension(csv.getName()))) {

                min_max = new File(inputCsvPath + "\\" + FilenameUtils.removeExtension(csv.getName()) + "\\" + "min_max.csv");
                System.out.println("minMax" + min_max);
                String[] command = {"cmd.exe", "/c", "Praat.exe --run getStartEndTime.praat \"" + insideFile + "\"" + " \"" + min_max + "\" 1"};
                ProcessBuilder probuilder = new ProcessBuilder(command);
                probuilder.directory(new File("C:\\Users\\Sibora\\Desktop"));
                Process process = probuilder.start();
                //Read out dir output
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                System.out.printf("Output of running %s is:\n",
                        Arrays.toString(command));
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                    try {
//                        int exitValue = process.waitFor();
//                        System.out.println("\n\nExit Value is " + exitValue);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//            }
//
            }
        }}
    }
}
    }
    }
//    public static void main (String argv[]) throws Exception {
//        new GetTimesForEachWord(new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\OpinionFolder\\pitchForWords"),new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\OpinionFolder\\ListOfSentenceFiles"));
//    }

}
