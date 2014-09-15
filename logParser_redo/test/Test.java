import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
    public static void main(String[] args) {
        String sql = "insert into TYQ(col1) values (?)";
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.27.178:1521:NLDW", "LOGSRC", "LOGSRC");
            Connection con = DriverManager.getConnection("jdbc:mysql://10.0.64.137:3306/rssp?useUnicode=true&amp;characterEncoding=utf8", "hitv", "hitv");
            
            Statement ps = null;
            ps = con.createStatement();
//            for (int i = 2; i < 6; i++) {
//                ps.setString(1, String.valueOf(i));
//                ps.addBatch();
//            }
//            ps.setString(1, "12345126351263");
//            ps.addBatch("insert into TYQ(col1,col2) values ('12', seq_viewlogid.nextval)");
            ps.addBatch("insert into TYQ(col1) values ('11')");
            ps.executeBatch();
            ResultSet rs = ps.getGeneratedKeys();
            System.out.println(rs);
            con.commit(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
