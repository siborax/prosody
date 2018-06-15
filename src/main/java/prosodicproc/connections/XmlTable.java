package prosodicproc.connections;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XmlTable {
    public void createXMLtable(String xml_Id, InputStream fis, File inputXml){
        try{
            Connection conn = null;
            PreparedStatement ps= conn.prepareStatement("CREATE TABLE IF NOT EXISTS xml_table (xml_Id xml_Id, textGrid bytea");
            ps.setString(1, xml_Id);
            ps.setBinaryStream(2, fis, (int)inputXml.length());
            ps.executeUpdate();
            ps.close();
            fis.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
