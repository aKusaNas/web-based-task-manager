package lt.Renaldas.tasks.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Formatter;

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
                            "ORDER BY points DESC;")) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Zaidejai zaidejas = new Zaidejai(++vieta, rs.getString("zaidejai"), rs.getString("points"), rs.getString("zaidimai"));
                    zaidejaiList.add(zaidejas);

                }
            }

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }

        return zaidejaiList;
    }

    public static String getWeeklyTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfhour = new SimpleDateFormat("HH:mm:ss");

//        for (String availableID : TimeZone.getAvailableIDs()) {
//            System.out.println(availableID);
//        }

        System.out.println(calendar.getTimeZone());
        System.out.println(sdf.format(calendar.getTime()));
        TimeZone tz = TimeZone.getTimeZone("Europe/Vilnius");
//        TimeZone tz = TimeZone.getTimeZone("Europe/Vilnius");
        System.out.println(":///////////////:");
        calendar.setTimeZone(tz);

        System.out.println(tz.useDaylightTime());
        System.out.println(calendar.getTimeZone());
        System.out.println(sdf.format(calendar.getTime()));


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
        weeklyTime += sdf.format(calendar.getTime()) + " - " + sdfhour.format(calendar.getTime()) + " X \\";
        return weeklyTime;
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
