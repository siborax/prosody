package prosodicproc.connections; /**
 * Created by Hiwi on 6/28/2017.
 */


import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.apache.commons.io.FilenameUtils.removeExtension;


public class ReadXML {
    public static HashMap<Integer, String> IndexedSenteces;
    // static HashMap<String, ArrayList<String>> speakersFormatted= new HashMap<>();
    static HashMap<String, ArrayList<String>> speakers = new HashMap<>();
    static ArrayList<String> currentText;
    static File[] listOfSentences;
    static File file;
  public  static String category;
  public static String xml_Id;

    private static File ListOfSentenceFiles;

    public ReadXML() throws UnknownHostException {
    }

    public static void main(String argv[]) {

        File fXmlFile = null;
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose xml file: ");
            int returnValue = chooser.showOpenDialog(null);
            File fileCase = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fileCase = chooser.getSelectedFile();
            }
            if (fileCase != null) {
                String filePath = fileCase.getPath();
                fXmlFile = new File(filePath);
            }

            // File fXmlFile = new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\case.xml");
            // filePath
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //StringBUffers for the item[1] of the script - phrases
            StringBuffer out = null;
            StringBuffer out0 = null;
            StringBuffer sb = null;
            StringBuffer opinionsb = null;
            //StringBUffers for the item[2] of the script - offsets

            StringBuffer total_offset = null;

            doc.getDocumentElement().normalize();
            Audio audio = new Audio();
            audio.readAudio();


            String fileName = "C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\";
//            OpinonTextGrids opinionTG = new OpinonTextGrids();
//            ArgumentTextGrids argumentTG = new ArgumentTextGrids();

            NodeList caseFolderNL = doc.getElementsByTagName("caseFolder");
            Node CaseFolderNode = caseFolderNL.item(0);//starting from argument case index of the item needs to be changed later for the opinion case item=1
            Element CaseFolder = (Element) CaseFolderNode;
             xml_Id = CaseFolder.getAttribute("id");
            System.out.println("xml_Id: " + xml_Id);

            //
            PrintWriter writer;
            NodeList NL = doc.getElementsByTagName("caseMedia");
            //Node CaseNode = null;
            for (int counter = 0; counter < NL.getLength(); counter++) { //big for open again

                Node CaseNode = NL.item(counter);
                Element Case = (Element) CaseNode;

                System.out.println("Category:" + Case.getAttribute("category"));
//
//decide which interval is being read in the stringbuffer

                if (Case.getAttribute("category").equals("argument")) {

                    out = new StringBuffer();
                    out0 = new StringBuffer();
                    sb = new StringBuffer();
                    total_offset = new StringBuffer();
                    file = new File(fileName + "ArgumentFolder\\sentencesPerSpeaker\\");

                    out.append("File type = \"ooTextFile\"" + "\n");
                    out.append("Object class = \"TextGrid\"" + "\n");

                } else if (Case.getAttribute("category").equals("opinion")) {
                    System.out.println("opinion interval");
                    out = new StringBuffer();
                    out0 = new StringBuffer();
                    sb = new StringBuffer();
                    total_offset = new StringBuffer();
                    opinionsb = new StringBuffer();

                    out.append("File type = \"ooTextFile\"" + "\n");
                    out.append("Object class = \"TextGrid\"" + "\n");
                    file = new File(fileName + "OpinionFolder\\sentencesPerSpeaker\\");


                }

                //The following for loop(s) create a hashmap of all the sentences of each speaker in the case.xml file

                int c = 0;
                NodeList turnNodeList = Case.getElementsByTagName("turn");
                Node firstTurnNode = turnNodeList.item(0);
                Element FirstTurnElement = (Element) firstTurnNode;
                Element ActualTurnElement = (Element) firstTurnNode;
                // System.out.println("Length: "+turnNodeList.getLength());
                IndexedSenteces = new HashMap<>();// Used for storing all the sentences of all speakers in a map

                //
                for (int turnCounter = 0; turnCounter < turnNodeList.getLength(); turnCounter++) {
                    //System.out.println("Text content: "+turnNodeList.item(turnCounter).getTextContent());
                    if (turnNodeList.item(turnCounter).hasChildNodes()) {
                        String SpeakerName = "";//take the name of the speaker in the turn
                        Element CurrentNode = (Element) turnNodeList.item(turnCounter);
                        SpeakerName = CurrentNode.getAttribute("speaker");

                        //looping for all sentences of 1 turn(speaker)
                        for (int count = 0; count < turnNodeList.item(turnCounter).getChildNodes().getLength(); count++) {
                            Node tempNode = turnNodeList.item(turnCounter).getChildNodes().item(count);

                            if (tempNode.getNodeName() == "text") {
                                //cannot use counter because it increments even when the node is not text
                                // System.out.println("Node Value =" + tempNode.getTextContent());


                                File file2 = new File(file + "\\" + (c + 1) + "_" + SpeakerName + "\\" + (c + 1) + "_" + SpeakerName + ".txt");
                                System.out.println("file2" + file2.getParentFile());
                                if (file2.getParentFile().mkdir()) {
                                    file2.createNewFile();
                                    System.out.println("New file inside a file was created" + file2.getParentFile());
                                }
                                file.getParentFile().mkdirs();
                                file2.getParentFile().mkdirs();

                                //  writer = new PrintWriter(file);
                                PrintWriter writer2 = new PrintWriter(file2);
                                writer2.append(tempNode.getTextContent());
                                writer2.flush();

                                // writer.append(tempNode.getTextContent());
                                //System.out.println("inside writer: "+ writer.toString());
                                // writer.flush();

                                IndexedSenteces.put((c + 1), SpeakerName);
                                c++;
                            }
                        }
                    }
                }
                listOfSentences = new File[0];
                listOfSentences = file.listFiles();
//                for(Object o:listOfSentences){
//                    System.out.println("in list of sentences"+ o.toString());
//                }
                //nameCorrespondingAudioFile();

//for(int i =0; i< turnNodeList.getLength())

                System.out.println("After function");

                NodeList textNodeList = Case.getElementsByTagName("text");
                System.out.println("textNodelist length: " + textNodeList.getLength());
                System.out.println("");
                Node firstTextNode = textNodeList.item(0);
                Element FirstTextElement = (Element) firstTextNode;

                out.append("xmin= " + FirstTextElement.getAttribute("syncTime") + "\n");
                Node lastNode = turnNodeList.item(turnNodeList.getLength() - 1);//the length of the NewNodeList is 121 but since the counting starts from 0 the last element of the list is 120.
                Element lastStopTime = (Element) lastNode;
                out.append("xmax= " + lastStopTime.getAttribute("stopTime") + "\n");
                out.append("tiers? <exists>\n");
                out.append("size = 2\n");
                out.append("item []:\n");
                out.append("\t");
                int item = 1;
                if (item == 1) {

                    out.append("item [" + (item) + "]:" + "\n");
                    out.append("\t");
                    out.append("\t");
                    out.append("class = \"IntervalTier\"\n");
                    out.append("\t");
                    out.append("\t");
                    out.append("name = \"phrases\"\n");
                    out.append("\t");
                    out.append("\t");
                    out.append("xmin = " + FirstTextElement.getAttribute("syncTime").toString() + "\n");
                    out.append("\t");
                    out.append("\t");
                    out.append("xmax = " + lastStopTime.getAttribute("stopTime").toString() + "\n");
                    out.append("\t");
                    out.append("\t");
                    out.append("intervals: size= " + textNodeList.getLength() + "\n");
                    //out0.append(out);

                    for (int x = 0; x < (textNodeList.getLength()); x++) {
                        Node text_node = textNodeList.item(x);
                        Node text_nodeNext = textNodeList.item(x + 1);
                        Element texts = (Element) text_node;
                        Element textsN = (Element) text_nodeNext;
                        out.append("\n");
                        Node pNode = texts.getParentNode();
                        if (x != (textNodeList.getLength() - 1)) {

                            out.append("\t");
                            out.append("\t");
                            out.append("intervals [" + (x + 1) + "] :\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("xmin= " + texts.getAttribute("syncTime"));
                            out.append("\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("xmax= " + textsN.getAttribute("syncTime"));
                            out.append("\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("text= " + "\"" + texts.getTextContent().replaceAll("\"", "quote") + "\"");

                            out.append("\n");
                            //  System.out.println(out);
                            out0.append(out);

                            out.setLength(0);
                        } else

                        {
                            lastNode = turnNodeList.item(turnNodeList.getLength() - 1);//the length of the NewNodeList is 121 but since the counting starts from 0 the last element of the list is 120.
                            lastStopTime = (Element) lastNode;
                            out.append("\t");
                            out.append("\t");
                            out.append("intervals [" + textNodeList.getLength() + "] :\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("xmin= " + texts.getAttribute("syncTime"));
                            out.append("\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("xmax= " + lastStopTime.getAttribute("stopTime") + "\n");
                            out.append("\t");
                            out.append("\t");
                            out.append("text= " + "\"" + texts.getTextContent().replaceAll("\"", "") + "\"");

                            out0.append(out);
                            out.setLength(0);
                        }
                    }
                    item++;
                }
//read offsets
                if (item == 2) {
                    sb.setLength(0);
                    total_offset.setLength(0);

                    sb.append("\n");
                    sb.append("\t");
                    sb.append("item [" + (item) + "]:" + "\n");
                    sb.append("\t");
                    sb.append("\t");
                    sb.append("class = \"IntervalTier\"\n");
                    sb.append("\t");
                    sb.append("\t");
                    sb.append("name= \"offsets\"\n");
                    sb.append("\t");
                    sb.append("\t");
                    sb.append("xmin= " + FirstTextElement.getAttribute("syncTime") + "\n");
                    sb.append("\t");
                    sb.append("\t");
                    sb.append("xmax= " + lastStopTime.getAttribute("stopTime") + "\n");
                    sb.append("\t");
                    sb.append("\t");
                    sb.append("intervals: size= " + textNodeList.getLength() + "\n");

                    for (int x = 0; x < (textNodeList.getLength()); x++) {
                        Node text_node = textNodeList.item(x);
                        Node text_nodeNext = textNodeList.item(x + 1);
                        Element texts = (Element) text_node;
                        Element textsN = (Element) text_nodeNext;
                        if (x != (textNodeList.getLength() - 1)) {
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("intervals [" + (x + 1) + "] :\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("xmin= " + texts.getAttribute("syncTime"));
                            sb.append("\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("xmax= " + textsN.getAttribute("syncTime"));
                            sb.append("\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("offset= \"" + texts.getAttribute("offset") + "\"");
                            sb.append("\n");
                            sb.append("\n");
                            // System.out.println(sb);
                            // out0.append(sb);
                            total_offset.append(sb);
                            sb.setLength(0);


                        } else {

                            lastNode = turnNodeList.item(turnNodeList.getLength() - 1);//the length of the NewNodeList is 121 but since the counting starts from 0 the last element of the list is 120.
                            lastStopTime = (Element) lastNode;
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("intervals [" + textNodeList.getLength() + "] :\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("xmin= " + texts.getAttribute("syncTime"));
                            sb.append("\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("xmax= " + lastStopTime.getAttribute("stopTime") + "\n");
                            sb.append("\t");
                            sb.append("\t");
                            sb.append("offset= \"" + texts.getAttribute("offset") + "\"");
                            sb.append("\n");
                            // System.out.println(sb);
                            // out0.append(sb);
                            total_offset.append(sb);
                            sb.setLength(0);
                        }
                    }
                }
                out0.append(total_offset);
                if (counter == 0 && removeExtension(audio.getInputFile().getName()).contains("a")) {
                    category = "argument";
                    //if argument audio file is selected
                    File argumentFolder = new File(audio.getInputFile().getParent() + "\\argument\\");
                    if (!argumentFolder.exists()) {
                        argumentFolder.mkdir();
                    }
                    PrintWriter textGrid = new PrintWriter(argumentFolder + "\\" + removeExtension(audio.getInputFile().getName()) + ".TextGrid");
                    textGrid.append(out0);
                    textGrid.close();
//                    argumentTG.insertArgumentTextGridToDB(xml_Id, new ByteArrayInputStream(out0.toString().getBytes(StandardCharsets.UTF_8)), out0, audio.getInputFile(), argumentFolder + "\\");
                    total_offset.setLength(0);
                    praatFuncAudioToSentence(argumentFolder + "\\", new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\argumentSentences\\" + "\\"));
                    System.out.println("arguement folder: " + argumentFolder);

                } else if (counter == 1 && removeExtension(audio.getInputFile().getName()).contains("o")) //if opinion audio file is selected
                {
                    category = "opinion";
                    File opinionFolder = new File(audio.getInputFile().getParent() + "\\opinion");
                    if (!opinionFolder.exists()) {
                        opinionFolder.mkdirs();
                    }
                    PrintWriter textGrid = new PrintWriter(opinionFolder + "\\" + removeExtension(audio.getInputFile().getName()) + ".TextGrid");
                    textGrid.append(out0);
                    textGrid.close();
                  //  opinionTG.insertOpionionTextGridToDB(xml_Id, new ByteArrayInputStream(out0.toString().getBytes(StandardCharsets.UTF_8)), out0, audio.getInputFile(), opinionFolder + "\\");
                    praatFuncAudioToSentence(opinionFolder + "\\", new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\opinionSentences\\" + "\\"));
                    total_offset.setLength(0);
                    System.out.println("opinion FOlder: " + opinionFolder);
                }
            } //big for open again

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//in order to run praat scripts Processbuilder object's directory should be directed to the same place as praat.exe is

    public static void praatFuncAudioToSentence(String path, File praat_output) throws Exception {
        //File praat_output = new File("C:\\Users\\Sibora\\Desktop\\Oral_Argument\\Edited_Oral_Argument\\07_542\\AudioSntcPraat2\\");
        String[] command = {"cmd.exe", "/c", "Praat.exe --run ExtractFromLongSound_fromdirectory.praat \"" + path + "\\\"" + " \"" + praat_output + "\\\\\""};
        ProcessBuilder probuilder = new ProcessBuilder(command);
        probuilder.directory(new File("C:\\Users\\Sibora\\Desktop\\"));
        Process process = probuilder.start();
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
                System.out.println("\n\nExit Value is " + exitValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Praat praat = new Praat();
//            praat.insertToPraat("ExtractFromLongSound_fromdirectory.praat", path, praat_output.toString());
        }
        nameCorrespondingAudioFile(new File((praat_output + "\\\\")));
        //praat_output is the directory where audio files are stored. The directory of argument audio files is different
        //from the directory of opinion audio files.

    }

    public static void nameCorrespondingAudioFile(File audioSegments) throws Exception {
        System.out.println("audioSegments: " + audioSegments);
        File[] listOfAudioSegments = audioSegments.listFiles();
        //System.out.println("number of listofaudiosegments: " + listOfAudioSegments.length);
        if (audioSegments.mkdir()) {
            System.out.println("New file was created");
        }

        for (Map.Entry<Integer, String> entry : IndexedSenteces.entrySet()) {
            System.out.println("Name of audio file " + entry.getKey());

            for (int j = 1; j <= IndexedSenteces.size(); j++) {
                if (listOfSentences[(j - 1)].mkdir()) {
                    System.out.println("new audio file created  ");
                }
                if (entry.getKey().toString().equals(listOfAudioSegments[(j - 1)].getName().replaceAll("[a-zA-Z_.]", ""))) {
                    for (File f : listOfSentences) {

                        if (entry.getKey().toString().equals(f.getName().replaceAll("[a-zA-Z_.]", ""))) {
                            listOfAudioSegments[j - 1].renameTo(new File(f.getAbsoluteFile() + "\\" + entry.getKey() + "_" + entry.getValue() + ".wav"));
                            System.out.println("Writing audiossss" + f.getAbsoluteFile() + "\\" + entry.getKey() + "_" + entry.getValue() + ".wav");
                            getWordsTextGrid(new File(f.getAbsoluteFile() + "\\" + entry.getKey() + "_" + entry.getValue() + ".txt"), new File(f.getAbsoluteFile() + "\\" + entry.getKey() + "_" + entry.getValue() + ".wav"));
                           // fillSpeakersTable(category, f.getAbsoluteFile() + "\\" + entry.getKey() + "_" + entry.getValue() + ".wav", f.getAbsolutePath(), null, FilenameUtils.removeExtension(f.getName()));
                        }
                    }
                }
            }
        }
    }


    public static void getWordsTextGrid(File textFile, File audioFile) throws Exception {

        runMAUSBASIC rb = new runMAUSBASIC(textFile, audioFile);

        System.out.println("inside runMausbasic");
        rb.getTextGrid();
        sendFilesTo_SentenceToWord(rb.get_runMAUSBASICFile());
    }

    public static void sendFilesTo_SentenceToWord(File FileDirectory) throws Exception {
       // System.out.println("Parenttt: " + FileDirectory.toString());
         ListOfSentenceFiles = new File(FileDirectory.getParentFile().getParentFile().getPath() + "\\" + "ListOfSentenceFiles" + "\\");
        if (!ListOfSentenceFiles.exists()) {
            ListOfSentenceFiles.mkdirs();
        }
        File[] listOfFiles = FileDirectory.listFiles();//list of all files inside sentencesPerSpeaker folder
        // (all sentences of a specific category)
        if (listOfFiles != null) {
            for (File f : listOfFiles) {
                if (!new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName())).exists()) {
                    new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName())).mkdirs();
                }
                if (f.getName().endsWith(".wav")) {
                    f.renameTo(new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName()) + "\\" + f.getName()));
                    System.out.println("newoutput wav: " + (new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName()) + "\\" + f.getName())));
                }
                if (f.getName().endsWith("TextGrid")) {
                    f.renameTo(new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName()) + "\\" + f.getName()));
                    System.out.println("newoutput textgrid: " + (new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName()) + "\\" + f.getName())));
                }
            }
        }
        File wordsFile = new File(FileDirectory.getParentFile().getParentFile().getPath() + "\\" + "WordsPerSentence" + "\\");
        if (!wordsFile.exists()) {
            wordsFile.mkdirs();
        }
        SentenceToWordPraat stw = new SentenceToWordPraat(ListOfSentenceFiles, wordsFile);

        File csvPathofWordsPitch = stw.getcsvPathofWordsPitch();
        System.out.println("returned file "+ stw.getcsvPathofWordsPitch());
        GetTimesForEachWord gt = new GetTimesForEachWord(csvPathofWordsPitch.getParentFile().getParentFile(),ListOfSentenceFiles);
        System.out.println("csv path: "+ csvPathofWordsPitch.getParentFile().getParentFile());
        System.out.println("sentenceFiles path:  "+ ListOfSentenceFiles);

       //\m fillTheDb fbd= new fillTheDb(csvPathofWordsPitch.getParentFile().getParentFile());

    }

//    public static void fillSpeakersTable(String category, String audioPath, String txtPath, String[] words, String speakerID) {
//        Database db = new Database();
//        try {
//            PreparedStatement ps = db.connect().prepareStatement("CREATE TABLE IF NOT EXISTS sentence (category text , audiopath text PRIMARY KEY , txtPath text, words text, speakerid text );\n" +
//                    "INSERT INTO sentence VALUES(?,?,?,?,?)");
//            System.out.println("Sentence table");
//            ps.setString(1, category);
//            ps.setString(2, audioPath);
//            ps.setString(3, txtPath);
//            ps.setString(4, String.valueOf(words));
//            ps.setString(5, speakerID);
//            System.out.println("speaker: " + ps.toString());
//            ps.executeUpdate();
//            ps.close();
//            //fis.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


}



