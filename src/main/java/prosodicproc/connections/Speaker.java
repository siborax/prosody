package prosodicproc.connections;

public class Speaker {
    public String name;
    public String type;
    public String id;
    public String index;

    public Speaker(){

    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type=type;
    }

    public void setId(String id) {
        this.id=id;
    }
    public void setIndex(String index){
        this.index=index;
    }
}
