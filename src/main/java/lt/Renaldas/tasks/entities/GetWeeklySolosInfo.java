package lt.Renaldas.tasks.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

//            "SELECT (CASE WHEN t.username IS NULL THEN p.discord_user_name ELSE p.discord_user_name END) AS zaidejai,\n" +
//                    "       SUM(CASE WHEN t.points::FLOAT IS NOT NULL THEN t.points::FLOAT ELSE 0.0 END)         AS points,\n" +
//                    "       COALESCE(string_agg(t.matchid, ', '), '')                                            AS zaidimai,\n" +
//                    "       (CASE WHEN t.username IS NULL THEN t.username ELSE t.username END) AS activisionuser\n" +
//                    "FROM weekly_solos b\n" +
//                    "         LEFT JOIN player_matches t ON b.uno = t.uno\n" +
//                    "         LEFT JOIN wzregistration p ON b.uno = p.uno\n" +
//                    "GROUP BY zaidejai, activisionuser\n" +
//                    "ORDER BY points DESC;"
                    "SELECT (CASE WHEN t.username IS NULL THEN p.discord_user_name ELSE p.discord_user_name END) AS zaidejai,\n" +
                            "       SUM(CASE WHEN t.points::FLOAT IS NOT NULL THEN t.points::FLOAT ELSE 0.0 END)         AS points,\n" +
                            "       COALESCE(string_agg(t.matchid, ', '), '')                                            AS zaidimai,\n" +
                            "       (CASE WHEN t.username IS NULL THEN t.username ELSE t.username END)                   AS activisionuser\n" +
                            "FROM weekly_solos b\n" +
                            "         LEFT JOIN player_matches t ON b.uno = t.uno\n" +
                            "         LEFT JOIN wzregistration p ON b.uno = p.uno\n" +
                            "GROUP BY zaidejai, activisionuser\n" +
                            "UNION\n" +
                            "SELECT (CASE WHEN t.username IS NULL THEN p.discord_user_name ELSE p.discord_user_name END) AS zaidejai,\n" +
                            "       SUM(CASE WHEN t.points::FLOAT IS NOT NULL THEN t.points::FLOAT ELSE 0.0 END)         AS points,\n" +
                            "       COALESCE(string_agg(t.matchid, ', '), '')                                            AS zaidimai,\n" +
                            "       (CASE WHEN t.username IS NULL THEN t.username ELSE t.username END)                   AS activisionuser\n" +
                            "FROM weekly_solos_archives b\n" +
                            "         LEFT JOIN player_matches_archives t ON b.uno = t.uno\n" +
                            "         LEFT JOIN wzregistration p ON b.uno = p.uno\n" +
                            "GROUP BY zaidejai, activisionuser\n" +
                            "ORDER BY points DESC;")
            ) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Zaidejai zaidejas = new Zaidejai(++vieta, rs.getString("zaidejai"), rs.getString("points"), rs.getString("zaidimai"), rs.getString("activisionuser"));
                    zaidejaiList.add(zaidejas);

                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }

        return zaidejaiList;
    }

    public static String getWeeklyTime() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm");
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Vilnius");
        calendar.setTimeZone(timeZone);
//        calendar.
        long start = 0;
        long end = 0;

        try (Connection connection = getConnection()) {
            try (PreparedStatement st = connection.prepareStatement(
                    "SELECT startinmillis, endinmillis FROM weekly_solos_status;")) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {

                    start = rs.getLong("startinmillis");
                    end = rs.getLong("endinmillis");
                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }
        String weeklyTime = " / ";
        calendar.setTimeInMillis(start);
        weeklyTime += sdf.format(calendar.getTime()) + " - ";
        calendar.setTimeInMillis(end);
//        weeklyTime += sdf.format(calendar.getTime()) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " \\";
        weeklyTime += sdf.format(calendar.getTime()) + " \\";
        return weeklyTime;
    }

    public static List<String> getHistory(){

        List<String> historyList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement st = connection.prepareStatement(
                    "SELECT table_name AS istorija " +
                            "FROM information_schema.tables " +
                            "WHERE table_type = 'BASE TABLE' " +
                            "  AND table_schema = 'public' " +
                            "  AND table_name LIKE 'wsq_%';")) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {

                    String istorija = rs.getString("istorija");
//                    end = rs.getLong("endinmillis");
                    historyList.add(istorija);
                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }
        return historyList;
    }

    public static class Zaidejai {
        public Zaidejai(int vieta, String nickname, String taskai, String zaidimai, String activisionuser) {
            this.vieta = vieta;
            this.nickname = nickname;
            this.taskai = taskai;
            this.zaidimai = zaidimai;
            this.activisionuser = activisionuser;
        }

        public int vieta;
        public String nickname;
        public String taskai;
        public String zaidimai;
        public String activisionuser;
    }
}
