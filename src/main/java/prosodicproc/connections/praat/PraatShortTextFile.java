package prosodicproc.connections.praat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class PraatShortTextFile extends PraatTextFile {

    public PraatShortTextFile(File file, Charset charset, EOL eol) throws IOException {
        super(file, charset, eol);
    }

    @Override
    public void writeString(String decorator, String value) throws IOException {
        writer.write(String.format("\"%s\"", value));
        writeLine();
    }

    public void writeBareString(String value) throws IOException {
        writer.write(value);
        writeLine();
    }

    @Override
    public void writeInteger(String decorator, int value) throws IOException {
        writer.write(number.format(value));
        writeLine();
    }

    @Override
    public void writeDouble(String descriptor, double value) throws IOException {
        writer.write(number.format(value));
        writeLine();
    }

    @Override
    public void writeLine(String format, Object... args) throws IOException {
        // do nothing
    }

}