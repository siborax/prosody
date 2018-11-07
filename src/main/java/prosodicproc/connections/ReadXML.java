package prosodicproc.connections; /**
 * Created by Hiwi on 6/28/2017.
 */


import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.removeExtension;


public class ReadXML {
    public static List<String> IndexedSenteces;
    static HashMap<String, ArrayList<String>> speakers = new HashMap<>();
    static ArrayList<String> currentText;
    static File[] listOfSentences;
    static File file;
    public static String category;
    public static String xml_Id;

    private static File ListOfSentenceFiles;
    public static File csvPathofWordsPitch;
    public static File wordsFile;
    public static File TextSentenceFile;
    public static ArrayList<Speaker> speakersPerCase;

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
            speakersPerCase = new ArrayList<>();
            // filePath
            InputStream inputStream= new FileInputStream(fXmlFile);
            Reader reader = new InputStreamReader(inputStream,"UTF-8");
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            StringBuffer out = null;
            StringBuffer out0 = null;
            StringBuffer sb = null;
            StringBuffer opinionsb = null;
            StringBuffer total_offset = null;
            doc.getDocumentElement().normalize();
            Audio audio = new Audio();
            audio.readAudio();
            File fileName = fXmlFile;
            NodeList caseFolderNL = doc.getElementsByTagName("caseFolder");
            Node CaseFolderNode = caseFolderNL.item(0);//starting from argument case index of the item needs to be changed later for the opinion case item=1
            Element CaseFolder = (Element) CaseFolderNode;
            xml_Id = CaseFolder.getAttribute("id");
            PrintWriter writer;
//            NodeList caseJustices = doc.getElementsByTagName("caseJustice");
            getSpeakersInCase(doc.getElementsByTagName("caseJustice"));
            getSpeakersInCase(doc.getElementsByTagName("caseAdvocate"));



            NodeList NL = doc.getElementsByTagName("caseMedia");
            for (int counter = 0; counter < NL.getLength(); counter++) { //big for open again
                Node CaseNode = NL.item(counter);
                Element Case = (Element) CaseNode;
//decide which interval is being read in the stringbuffer
                if (Case.getAttribute("category").equals("argument")) {
                    out = new StringBuffer();
                    out0 = new StringBuffer();
                    sb = new StringBuffer();
                    total_offset = new StringBuffer();
                    file = new File(fileName.getParentFile() + "\\" + "ArgumentFolder\\sentencesPerSpeaker\\");
                    out.append("File type = \"ooTextFile\"" + "\n");
                    out.append("Object class = \"TextGrid\"" + "\n");
                } else if (Case.getAttribute("category").equals("opinion")) {
                    //  System.out.println("opinion interval");
                    out = new StringBuffer();
                    out0 = new StringBuffer();
                    sb = new StringBuffer();
                    total_offset = new StringBuffer();
                    opinionsb = new StringBuffer();
                    out.append("File type = \"ooTextFile\"" + "\n");
                    out.append("Object class = \"TextGrid\"" + "\n");
                    file = new File(fileName.getParentFile() + "\\" + "OpinionFolder\\sentencesPerSpeaker\\");
                }
                //The following for loop(s) create a hashmap of all the sentences of each speaker in the case.xml file
                int c = 0;
                NodeList turnNodeList = Case.getElementsByTagName("turn");
                Node firstTurnNode = turnNodeList.item(0);
                Element FirstTurnElement = (Element) firstTurnNode;
                Element ActualTurnElement = (Element) firstTurnNode;
                IndexedSenteces = new ArrayList<>();// Used for storing all the sentences of all speakers in a map
                for (int turnCounter = 0; turnCounter < turnNodeList.getLength(); turnCounter++) {
                    if (turnNodeList.item(turnCounter).hasChildNodes()) {
                        String SpeakerName = "";//take the name of the speaker in the turn
                        Element CurrentNode = (Element) turnNodeList.item(turnCounter);
                        SpeakerName = CurrentNode.getAttribute("speaker");
                        //looping for all sentences of 1 turn(speaker)
                        for (int count = 0; count < turnNodeList.item(turnCounter).getChildNodes().getLength(); count++) {
                            Node tempNode = turnNodeList.item(turnCounter).getChildNodes().item(count);
                            if (tempNode.getNodeName().equals("text")) {
                                //cannot use counter because it increments even when the node is not text
                                File file2 = new File(file + "\\" + (c + 1) + "_" + SpeakerName + "\\" + (c + 1) + "_" + SpeakerName + ".txt");
                                if (!file2.getParentFile().exists()) {
                                    file2.getParentFile().mkdirs();
                                }
                                PrintWriter writer2 = new PrintWriter(file2);
                                writer2.append(tempNode.getTextContent());
                                writer2.flush();
                                IndexedSenteces.add(SpeakerName);
                                c++;
                            }
                        }
                    }
                }
                listOfSentences = new File[0];
                listOfSentences = file.listFiles();
                //nameCorrespondingAudioFile();
                NodeList textNodeList = Case.getElementsByTagName("text");
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
                            out0.append(out);
                            out.setLength(0);
                        } else {
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
                        argumentFolder.mkdirs();
                    }
                    PrintWriter textGrid = new PrintWriter(argumentFolder + "\\" + removeExtension(audio.getInputFile().getName()) + ".TextGrid");
                    textGrid.append(out0);
                    textGrid.close();
                    total_offset.setLength(0);
                    audio.getInputFile().renameTo(new File(argumentFolder + "\\" + audio.getInputFile().getName()));
                    if (!new File(fileName.getParentFile() + "\\argumentSentences\\" + "\\").exists()) {
                        new File(fileName.getParentFile() + "\\argumentSentences\\" + "\\").mkdirs();
                    }
                    praatFuncAudioToSentence(argumentFolder + "\\", new File(file.getParentFile().getParentFile() + "\\argumentSentences\\" + "\\"));
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
                    if (!new File(fileName.getParentFile() + "\\opinionSentences\\" + "\\").exists()) {
                        new File(fileName.getParentFile() + "\\opinionSentences\\" + "\\").mkdirs();
                    }
                    total_offset.setLength(0);
                    audio.getInputFile().renameTo(new File(opinionFolder + "\\" + audio.getInputFile().getName()));
                    praatFuncAudioToSentence(opinionFolder + "\\", new File(file.getParentFile().getParentFile() + "\\opinionSentences\\" + "\\"));
                }
            } //big for open again

            wordsFile = new File(TextSentenceFile.getParentFile().getParentFile().getPath() + "\\" + "WordsPerSentence" + "\\");
            if (!wordsFile.exists()) {
                wordsFile.mkdirs();
            }
            SentenceToWordPraat stw = new SentenceToWordPraat(ListOfSentenceFiles, wordsFile);
            csvPathofWordsPitch = stw.getcsvPathofWordsPitch();
            GetTimesForEachWord gt = new GetTimesForEachWord(csvPathofWordsPitch.getParentFile().getParentFile(), ListOfSentenceFiles);

            fillTheDb fbd = new fillTheDb(csvPathofWordsPitch.getParentFile().getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getSpeakersInCase(NodeList caseSpeakerList) {
        for (int pos = 0; pos < caseSpeakerList.getLength(); pos++) {
            Speaker speaker = new Speaker();

            Node speakerNode = caseSpeakerList.item(pos);
            Element justiceElement = (Element) speakerNode;
            if(speakerNode.getNodeName().equals("caseJustice")){
            speaker.setType("justice");
            for (int childNodes = 0; childNodes < caseSpeakerList.item(pos).getChildNodes().getLength(); childNodes++) {
                Node caseJusticeChildNode = caseSpeakerList.item(pos).getChildNodes().item(childNodes);
                if (caseJusticeChildNode.getNodeName().equals("caseJusticeName")) {
                    speaker.setName(caseJusticeChildNode.getTextContent());
                }
            }
            }
            if(speakerNode.getNodeName().equals("caseAdvocate")){
                speaker.setType("advocate");
                for (int childNodes = 0; childNodes < caseSpeakerList.item(pos).getChildNodes().getLength(); childNodes++) {
                    Node caseJusticeChildNode = caseSpeakerList.item(pos).getChildNodes().item(childNodes);
                    if (caseJusticeChildNode.getNodeName().equals("caseAdvocateName")) {
                        speaker.setName(caseJusticeChildNode.getTextContent());
                    }
                }
            }
                speaker.setId(justiceElement.getAttribute("id"));
//                speaker.setIndex(justiceElement.getAttribute("index"));
            speakersPerCase.add(speaker);
        }
    }
//in order to run praat scripts Processbuilder object's directory should be directed to the same place as praat.exe is

    public static void praatFuncAudioToSentence(String path, File praat_output) throws Exception {
        String[] command = {"cmd.exe", "/c", "Praat.exe --run ExtractFromLongSound_fromdirectory.praat \"" + path + "\\\"" + " \"" + praat_output + "\\\\\""};
        ProcessBuilder probuilder = new ProcessBuilder(command);
        probuilder.directory(new File("C:\\Users\\sibora\\Desktop\\"));
        Process process = probuilder.start();
        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line;
//         System.out.printf("Output of running %s is:\n",
//         Arrays.toString(command));
        while ((line = br.readLine()) != null) {
            try {
                int exitValue = process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nameCorrespondingAudioFile(new File((praat_output + "\\\\")));
        //praat_output is the directory where audio files are stored. The directory of argument audio files is different
        //from the directory of opinion audio files.
    }

    public static void nameCorrespondingAudioFile(File audioSegments) throws Exception {
        List<File> fileList = new ArrayList<>();
        File[] listOfAudioSegments = audioSegments.listFiles();
        for (int a = 0; a < listOfAudioSegments.length; a++) {
            for (File f : listOfSentences) {
                if (Integer.parseInt(f.getName().replaceAll("[a-zA-Z_.]", "")) == Integer.parseInt(listOfAudioSegments[(a)].getName().replaceAll("[a-zA-Z_.]", ""))) {
                    listOfAudioSegments[a].renameTo(new File(f.getAbsoluteFile() + "\\" + f.getName() + ".wav"));
                    getWordsTextGrid(new File(f.getAbsoluteFile() + "\\" + f.getName() + ".txt"), new File(f.getAbsoluteFile() + "\\" + f.getName() + ".wav"));
                }
            }
        }
    }

    public static void getWordsTextGrid(File textFile, File audioFile) throws Exception {

        runMAUSBASIC rb = new runMAUSBASIC(textFile, audioFile);
        rb.getTextGrid();
        if (rb.get_runMAUSBASICFile().exists()) {
            sendFilesTo_SentenceToWord(rb.get_runMAUSBASICFile());
        }
    }

    public static void sendFilesTo_SentenceToWord(File FileDirectory) throws Exception {
        TextSentenceFile = FileDirectory;

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
                }
                if (f.getName().endsWith("TextGrid")) {
                    f.renameTo(new File(ListOfSentenceFiles.getPath() + "\\" + FilenameUtils.removeExtension(f.getName()) + "\\" + f.getName()));
                }
            }
        }

    }
}



