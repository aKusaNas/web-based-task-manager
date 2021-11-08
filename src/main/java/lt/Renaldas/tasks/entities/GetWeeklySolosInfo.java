package lt.Renaldas.tasks.entities;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetWeeklySolosInfo {
    public static final String DBKEY = System.getenv("DATABASE_URL");

    public static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(DBKEY);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static List<Zaidejai> getWeeklySolosZaidejus() {


        List<Zaidejai> zaidejaiList = new ArrayList<>();
        int vieta = 0;
        try (Connection connection = getConnection()) {
//            // IMAMI 5 ZAIDEJAI IS MATCHES LENTELES SU SUMUOTAIS TASKAIS
            try (PreparedStatement st = connection.prepareStatement("SELECT username, string_agg(matchid, ', ') AS zaidimai, SUM (points::FLOAT) AS points\n" +
                    "FROM   player_matches\n" +
                    "GROUP  BY 1\n" +
                    "ORDER BY points DESC;")) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Zaidejai zaidejas = new Zaidejai(++vieta, rs.getString("username"), rs.getString("points"), rs.getString("zaidimai"));
                    zaidejaiList.add(zaidejas);

                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }
        return zaidejaiList;
    }

    public static class Zaidejai {
        public Zaidejai(int vieta, String nickname, String taskai, String zaidimai) {
            this.vieta = vieta;
            this.nickname = nickname;
            this.taskai = taskai;
            this.zaidimai = zaidimai;
        }

        public int vieta;
        public String nickname;
        public String taskai;
        public String zaidimai;
    }

}
