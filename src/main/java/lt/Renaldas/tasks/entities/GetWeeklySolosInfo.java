package lt.Renaldas.tasks.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetWeeklySolosInfo {
    public static final String DBKEY = System.getenv("DATABASE_URL");
    private static final Logger logger = LoggerFactory.getLogger(GetWeeklySolosInfo.class);

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
            try (PreparedStatement st = connection.prepareStatement(
//                    "SELECT username, string_agg(matchid, ', ') AS zaidimai, SUM (points::FLOAT) AS points\n" +
//                    "FROM   player_matches\n" +
//                    "GROUP  BY 1\n" +
//                    "ORDER BY points DESC;")
                    "SELECT (CASE WHEN t.username IS NULL THEN p.discord_user_name ELSE p.discord_user_name END) AS zaidejai,\n" +
                            "       SUM(CASE WHEN t.points::FLOAT IS NOT NULL THEN t.points::FLOAT ELSE 0.0 END)         AS points,\n" +
                            "       COALESCE(string_agg(t.matchid, ', '), '')                                            AS zaidimai\n" +
                            "FROM weekly_solos b\n" +
                            "         LEFT JOIN player_matches t ON b.uno = t.uno\n" +
                            "         LEFT JOIN wzregistration p ON b.uno = p.uno\n" +
                            "GROUP BY zaidejai\n" +
                            "ORDER BY points DESC;"))
             {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Zaidejai zaidejas = new Zaidejai(++vieta, rs.getString("zaidejai"), rs.getString("points"), rs.getString("zaidimai"));
                    zaidejaiList.add(zaidejas);

                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }

//        Calendar calendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1636539819492L);
        String pradetas = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        calendar.add(Calendar.DATE, 7);
        String baigiasi = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        logger.info(pradetas + "  /// " + baigiasi + " timezone: " + calendar.getTimeZone());
        ;

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
