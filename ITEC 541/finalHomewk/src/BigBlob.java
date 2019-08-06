import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.io.*;

public class BigBlob
{
    public  String model, DBstatus;

    public Blob modelpic;

    public ImageIcon blobIcon;


    private Connection conn;
    private Statement stmt;
    private int connected;
    private String user, pass;
    private String query;





    public BigBlob(String _model) throws ClassNotFoundException {

        model = _model;
        user = "jcaldwell2";
        pass = "132435Ac$";



        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            // Class.forName ("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@Picard2:1521:itec2",user,pass);
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
            query = "Select model, modelpic "
                    + "from Auto.CARS "
                    + "where model = '" + model + "'" ;

            System.out.println(query);

            ResultSet rset = stmt.executeQuery(query);
            if(rset.next())
            {
                model = rset.getString("model");
                modelpic = rset.getBlob("modelpic");

                DBstatus = "Found";

                BufferedImage im = ImageIO.read(modelpic.getBinaryStream());
                blobIcon = new ImageIcon(im);
            }
            conn.close();
        }
        catch(SQLException e)
        {
            DBstatus = "Error connecting to the Database";
            System.out.println(e);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File cannot be found");
        }
        catch(java.io.IOException e)
        {
            System.out.println(e);
        }
    }

}