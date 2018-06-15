package prosodicproc.connections;

import com.google.common.io.Files;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;

public class Audio {
File inputFile;
File outputFile;
    public void readAudio(){


        File audioFile = null;
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose audio file to convert: ");
            int returnValue = chooser.showOpenDialog(null);
            File fileCase = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fileCase = chooser.getSelectedFile();
            }
            if (fileCase != null) {
                String filePath = fileCase.getPath();
                audioFile = new File(filePath);
                this.inputFile=audioFile;

            }
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public byte[] convertMp3_toWave() throws Exception {
//        InputStream targetStream = Files.asByteSource(inputFile).openStream();
//        byte[] bytes= org.apache.commons.io.IOUtils.toByteArray(targetStream);
//
//        AudioInputStream in = AudioSystem.getAudioInputStream(inputFile);
//       // AudioInputStream din = null;
//        AudioFormat baseFormat = in.getFormat();
//        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
//                                                    baseFormat.getSampleRate(),
//                                                    16,
//                                                    baseFormat.getChannels(),
//                                                    baseFormat.getChannels()*2,
//                                                    baseFormat.getSampleRate(),
//                                                    false
//                                                        );
//       // din =AudioSystem.getAudioInputStream(decodedFormat,in);
//        System.out.println(mp3ToWav.getAudioDataBytes(bytes,decodedFormat));
//        byte[] bytesArray = mp3ToWav.getAudioDataBytes(bytes,decodedFormat);
//
//        return bytesArray;
//
//    }
//    public void convertAudio() throws InterruptedException, IOException, ParseException, URISyntaxException {
//        CloudConverterHttpURLConnection converter = new CloudConverterHttpURLConnection(inputFile);
//        System.out.println("output"+converter.toString());
//      // this.outputFile= new File(converter.getOutputFile());
//    }
    public File getInputFile (){
        return this.inputFile;
    }
}
