//package prosodicproc.connections;
//
//import prosodicproc.connections.Database;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class Praat {
//
//    public void insertToPraat(String script_name, String inputD,String outputFile){
//        Database db = new Database();
//        try {
//
//            System.out.println("argument function called");
//            PreparedStatement ps = db.connect().prepareStatement("CREATE TABLE IF NOT EXISTS praat_scripts (script_name text , inputD text, outputDirectory text);\n" +
//                    "INSERT INTO praat_scripts VALUES(?,?,?)");
//
//            ps.setString(1, script_name);
//            ps.setString(2, inputD);
//            ps.setString(3,outputFile);
//            System.out.println("argument: "+ ps.toString());
//            ps.executeUpdate();
//            ps.close();
//
//    } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
