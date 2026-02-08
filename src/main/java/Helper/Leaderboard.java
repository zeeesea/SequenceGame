package Helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;

/**
 * The Leaderboard class is a static Helper Class,
 * used to manage the leaderboard of the game, including savin and loading
 * the leaderboard data, and providing methods to add new scores and retrieve the top scores
 */
public class Leaderboard {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static List<PlayerEntry> leaderboard = new List<>();
    private static final String leaderboardPath = "assets/leaderboard.json";

    public static void init() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File file = new File(leaderboardPath);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                mapper.writeValue(file, new ArrayList<PlayerEntry>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            leaderboard = List.fromArrayList(mapper.readValue(
                    file,
                    new TypeReference<ArrayList<PlayerEntry>>() {}
            ));
        } catch (Exception e) {
            leaderboard = new List<>();
        }
    }

    /**
     * Updates the PlayerEntry if already existing or creates a new one
     * @param name The name of the player (doesn't matter if it already exists)
     * @param score The score the player reached this game
     * @param mode The GameMode the game was played in
     * @param timeStamp The start time of the game
     * @param playTime The total time the game was played
     * @param buttonCount Number of buttons this game was played with (e.g. 3)
     * @param averageClickSpeed The speed in which the buttons where pressed, the higher the better
     */
    public static void addEntry(String name, int score, GameMode mode, long timeStamp, double playTime, int buttonCount, double averageClickSpeed) {
        // Check if name already exists in the leaderboard
        for (PlayerEntry entry : leaderboard) {
            if (entry.name.equals(name)) {
                updateEntry(entry, score, mode, timeStamp, playTime, buttonCount, averageClickSpeed);
                updateJSONFile();
                return;
            }
        }
        // If name does not exist, add a new entry
        PlayerEntry entry = new PlayerEntry(name);
        leaderboard.append(entry);
        updateEntry(entry, score, mode, timeStamp, playTime, buttonCount, averageClickSpeed);
        updateJSONFile();
    }
    private static void updateEntry(PlayerEntry e, int score, GameMode mode, long timeStamp, double playTime, int buttonCount, double averageClickSpeed) {
        e.totalClicks += score;
        e.mistakes++;
        e.totalPlayTime += playTime;
        e.averageClickSpeed = (e.averageClickSpeed * e.games.size() + averageClickSpeed) / (e.games.size() + 1);

        e.addGameEntry(score, mode, timeStamp, playTime, buttonCount, averageClickSpeed);
    }
    public static void updateJSONFile() {
        try {
            // Konvertiere zu ArrayList, damit Jackson die Elemente richtig serialisiert
            mapper.writeValue(new File(leaderboardPath), leaderboard.toArrayList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<PlayerEntry> getLeaderboard() {
        return new List<>(leaderboard);
    }

    // ---------------------- DATA CLASSES ----------------------
    public static class PlayerEntry implements Comparable {
        public String name;
        public int totalClicks;
        public int mistakes;
        public double averageClickSpeed;
        public double totalPlayTime;

        public ArrayList<GameEntry> games = new ArrayList<>();

        public PlayerEntry() {}
        public PlayerEntry(String name) {
            this.name = name;
        }
        public void addGameEntry(int score, GameMode mode, long timeStamp, double playTime, int buttonCount, double averageClickSpeed) {
            games.add(new GameEntry(score, mode, timeStamp, playTime, buttonCount, averageClickSpeed));
        }
        @JsonIgnore
        public GameEntry getHighScoreGame() {
            if (games == null || games.isEmpty()) return null;
            GameEntry highest = games.get(0);
            for (GameEntry entry : games) {
                if (entry.score > highest.score) highest = entry;
            }
            return highest;
        }

        @Override
        public int compareTo(Comparable a) {
            return Integer.compare(a.getComparableSize(), getComparableSize());
        }

        @JsonIgnore
        @Override
        public int getComparableSize() {
            GameEntry highScore = getHighScoreGame();
            if (highScore == null) return 0;
            return highScore.score;
        }
    }

    public static class GameEntry {
        public int score;
        public GameMode gameMode;
        public long timestamp;
        public double playTime;
        public double averageClickSpeed;
        public int buttonCount;

        public GameEntry() {}

        public GameEntry(int score, GameMode gameMode, long timestamp, double playTime, int buttonCount, double averageClickSpeed) {
            this.score = score;
            this.gameMode = gameMode;
            this.timestamp = timestamp;
            this.buttonCount = buttonCount;
            this.playTime = playTime;
            this.averageClickSpeed = averageClickSpeed;
        }
    }
}
