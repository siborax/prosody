package prosodicproc.connections;

public class DataFormat {
    private String word;
    private String start;
    private String end;
    private String pitch;
    public DataFormat(){

    }
    public DataFormat(String word, String start, String end, String pitch){
        this.word= word;
        this.start=start;
        this.end=end;
        this.pitch=pitch;
    }
    public String getWord(){return word;}
    public String getStart(){return start;}
    public String getEnd(){return end;}
    public String getPitch(){return pitch;}
    public void setWord(String word){this.word=word;}
    public void setStart(String start){this.start=start;}
    public void setEnd(String end){this.end=end;}
    public void setPitch(String pitch){this.pitch=pitch;}


    public DataFormat(String word, String start, String end){
        this.word=word;
        this.start=start;
        this.end=end;
    }
    public DataFormat(String pitch){
        this.pitch=pitch;
    }
}
