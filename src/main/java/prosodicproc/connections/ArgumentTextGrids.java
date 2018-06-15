//package prosodicproc.connections;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class ArgumentTextGrids {
//    public void insertArgumentTextGridToDB(String xml_Id, InputStream fis, StringBuffer argumentsb, File outputFile, String path) {
//        Database db = new Database();
//        try {
//
//            System.out.println("argument function called");
//            PreparedStatement ps = db.connect().prepareStatement("CREATE TABLE IF NOT EXISTS Argument (xml_Id text PRIMARY KEY , textGrid bytea, Audio_outputDirectory text, path text);\n" +
//                    "INSERT INTO Argument VALUES(?,?,?,?)");
//            System.out.println("ARGUMENT TEXTGRIDS");
//            ps.setString(1, xml_Id);
//            ps.setBinaryStream(2, fis, argumentsb.length());
//            ps.setString(3,outputFile.toString());
//            ps.setString(4,path);
//            System.out.println("argument: "+ ps.toString());
//            ps.executeUpdate();
//            ps.close();
//            fis.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}