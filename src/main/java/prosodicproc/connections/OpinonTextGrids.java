//package prosodicproc.connections;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class OpinonTextGrids {
//
//public void insertOpionionTextGridToDB(String xml_Id, InputStream fis, StringBuffer opinionsb, File outputDirectory, String path ){
//     Database db= new Database();
//    //db.connect returns a conn  object
//
//    try{
//       // conn.connect();
//        System.out.println("opinion function called");
//
//       // PreparedStatement ps=  db.connect().prepareStatement("CREATE TABLE  OpinonsTextGrids (xml_Id VARCHAR , textGrid VARCHAR )");
//        PreparedStatement ps=  db.connect().prepareStatement("CREATE TABLE IF NOT EXISTS Opinon (xml_Id  text PRIMARY KEY , textGrid bytea, Audio_ouputDirectory text, path text) ;\n"
//                +"INSERT INTO Opinon VALUES(?,?,?,?)");
//        ps.setString(1, xml_Id);
//        ps.setBinaryStream(2, fis, opinionsb.length());
//        ps.setString(3,outputDirectory.toString());
//        ps.setString(4,path);
//        System.out.println("opinion: "+ ps.toString());
//        ps.executeUpdate();
//        ps.close();
//        fis.close();
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//}
//
//}
