package prosodicproc.connections;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.Set;

public class mongodb {

    DB database;
    MongoClient mongoClient;
    String category;
    public void SetDb() throws UnknownHostException {
         mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("mongoDB");
        //boolean collectionExists = database.collectionExists("ProsodySentences");

       // if(!database.collectionExists("ProsodySentences")){
            database.createCollection("ProsodySentences" ,null );
            //new BasicDBObject("capped", false)


     //   }
        //emptyDB();





    }

    public void storeDocument(JSONObject json) throws UnknownHostException {
        //SetDb();
        Object  o = JSON.parse(json.toString());
        DBObject doc =(DBObject) o;

        if(!database.collectionExists("ProsodySentences")){

        }
        //if(database.getCollectionNames("ProsodySentences").contains()){}
         database.getCollection("ProsodySentences").insert(doc);
        System.out.println("db: "+database.getCollection("ProsodySentences").find(doc));
    }
    public void closeDB(){
        mongoClient.close();
    }
    public void emptyDB(){
        database.getCollection("ProsodySentences").drop();
    }

    public void setCategory(String category) {
       this.category= category;
    }
}
