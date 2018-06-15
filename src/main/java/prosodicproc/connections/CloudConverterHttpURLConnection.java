package prosodicproc.connections;

import org.aioobe.cloudconvert.CloudConvertService;
import org.aioobe.cloudconvert.ConvertProcess;
import org.aioobe.cloudconvert.ProcessStatus;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import static org.apache.commons.io.FilenameUtils.removeExtension;

public class CloudConverterHttpURLConnection {
    private final String USER_AGENT = "Mozilla";
    File inputFile;
    String outputFile;


    public CloudConverterHttpURLConnection(File input) throws URISyntaxException, IOException, ParseException, InterruptedException {
        this.inputFile=input;


        CloudConvertService service = new CloudConvertService("F5rqdk8CVnt0mljVkhtME9xTzq7AmuFGdisGMq6eHYDly2AXO9CLVCY3SDlyXMZV");
        // Create conversion process
        ConvertProcess process = service.startProcess("mp3", "wav");
// Perform conversion

        process.startConversion(inputFile);


        // Wait for result
        ProcessStatus status;
        waitLoop: while (true) {
            status = process.getStatus();

            switch (status.step) {
                case FINISHED: break waitLoop;
                case ERROR: throw new RuntimeException(status.message);
            }

            // Be gentle
            Thread.sleep(200);
        }
        // Download result
       // String newName = inputFile.getName().
        if(removeExtension(inputFile.getName()).contains("a")){
            service.download(status.output.url, new File(inputFile.getParent()+"\\argument\\"+removeExtension(inputFile.getName())+".wav"));
            this.outputFile = inputFile.getParent()+"\\argument\\"+removeExtension(inputFile.getName())+".wav";
        }
       else if(removeExtension(inputFile.getName()).contains("o")){
            service.download(status.output.url, new File(inputFile.getParent()+"\\opinion\\"+removeExtension(inputFile.getName())+".wav"));
            this.outputFile = inputFile.getParent()+"\\opinion\\"+removeExtension(inputFile.getName())+".wav";
        }

// Clean up
        process.delete();
    }
public String getOutputFile(){
        return this.outputFile;
}
    }


