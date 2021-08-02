package lt.Renaldas.tasks.entities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetStats {
    private static Logger logger = LoggerFactory.getLogger(GetStats.class);

    final private String playertag;
    final private String platform;

    public GetStats(String playertag, String platform) {
        this.playertag = playertag.replaceAll("#", "%23");
        this.platform = platform.replaceAll("#", "%23");
    }

    public Player getPlayerStats() {

        Player player = new Player();



        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://app.wzstats.gg/v2/player?username="+ playertag +"&platform=" + platform)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBodyString = response.body().string();
            JSONObject Jobject = new JSONObject(responseBodyString);
            player.lifetimeKD = Math.round(Double.parseDouble(Jobject.getJSONObject("data").getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get("kdRatio").toString()) * 100.0) / 100.0;
            player.winKD = Math.round(Double.parseDouble(Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("all").getJSONObject("properties").get("kdRatio").toString()) * 100.0) / 100.0;
            player.kills = Jobject.getJSONObject("data").getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get("kills").toString();
            player.wins = Jobject.getJSONObject("data").getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get("wins").toString();

            if (Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").has("br_brtrios"))
                player.brAll += Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").getJSONObject("br_brtrios").getJSONObject("properties").getInt("kills");

            if (Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").has("br_brduos"))
                player.brAll += Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").getJSONObject("br_brduos").getJSONObject("properties").getInt("kills");

            if (Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").has("br_brquads"))
                player.brAll += Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").getJSONObject("br_brquads").getJSONObject("properties").getInt("kills");

            if (Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").has("br_brsolo"))
                player.brAll += Jobject.getJSONObject("data").getJSONObject("weekly").getJSONObject("mode").getJSONObject("br_brsolo").getJSONObject("properties").getInt("kills");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return player;
    }

    public static class Player {
        public double lifetimeKD;
        public double winKD;
        public String kills;
        public String wins;
        public int brAll;
    }
    
}