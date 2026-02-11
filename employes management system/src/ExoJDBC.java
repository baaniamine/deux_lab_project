import java.sql.*;
public class ExoJDBC {
        private static final String url = "jdbc:mysql://localhost:3306/exo_lab1?serverTimezone=UTC";
        private static final String username = "root";
        private static final String password = "";
    public static void main(String[] args) {
        try{
             // loading the druver before applyin any connection
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
System.out.println("loading failed : class not found ");
e.printStackTrace();
return;
        }
        try{
Connection conn = DriverManager.getConnection(url, username, password);
Statement sttmnt = conn.createStatement();
System.out.println("connection is successeful");
sttmnt.executeUpdate("DROP TABLE IF EXISTS DevDATA");
sttmnt.executeUpdate("CREATE TABLE DevData(" +
        "devs varchar(32) not null, "+
                   " jours varchar(16) not null," +
                    "scripts int not null" + ");");
            System.out.println("table got created is successeful");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('AMINE', 'Lundi', 1)");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('HASSAN', 'Lundi', 2)");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('OTHMANE', 'Mardi', 9)");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('AMINE', 'Mardi', 3)");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('OTHMANE', 'Mardi', 4)");
            sttmnt.executeUpdate("INSERT INTO DevData VALUES ('HASSAN', 'Mercredi', 2)");
            System.out.println("data got created is successefuly");
            System.out.println("\n-------max script par jour-----------");
            try(ResultSet RS = sttmnt.executeQuery("SELECT devs,jours, MAX(scripts) AS MAXSCRIPTS" + " FROM DevData GROUP BY devs,jours ")){
                while(RS.next()){
                    String devs = RS.getString("devs");
                    String jours = RS.getString("jours");
                    int MAXSCRIPTS = RS.getInt("MAXSCRIPTS");
                    System.out.println(devs + '|' + jours +'|' +MAXSCRIPTS);
                }
            }
            System.out.println("\n----- Classement des développeurs (total scripts) ---");
            try(ResultSet RS = sttmnt.executeQuery("SELECT devs, SUM(scripts) AS Total " +
                    "FROM DevData GROUP BY devs ORDER BY Total DESC")){
                while(RS.next()){
                    String devs = RS.getString("devs");
                    int Sumscripts = RS.getInt("Total");
                    System.out.println(devs + '|' + Sumscripts);
                }

            }
            System.out.println("\n--- Total scripts semaine ---");
            try(ResultSet RS = sttmnt.executeQuery("SELECT SUM(scripts) AS TOTAL_SEMAINE FROM DevData")){
                if (RS.next()){
                    int Total_semaine  = RS.getInt("TOTAL_SEMAINE");
                    System.out.println("total semaaine scripts " + '|' + Total_semaine);
                }
            }

            System.out.println("\n--- Total scripts pour un développeur  ---"); //prepared steatemnt
            String sql = "SELECT SUM(scripts) AS totalscripts FROM DevData WHERE devs = ?";
String devrecherche = "HASSAN";
try(PreparedStatement ps = conn.prepareStatement(sql)){
    ps.setString(1,devrecherche);
    try(ResultSet rs = ps.executeQuery()){
        if (rs.next()){
            int totalscripts = rs.getInt("totalscripts");
            System.out.println("total pour un dev recherche :" + devrecherche + '|' + totalscripts);
        }
    }
}


        }catch(SQLException E){
            E.printStackTrace();
System.out.println("man i guess there's a problem go pray and come back");
        }
    }

}