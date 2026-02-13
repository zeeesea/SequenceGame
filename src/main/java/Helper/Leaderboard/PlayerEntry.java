package Helper.Leaderboard;

import Helper.Comparable;
import Helper.GameMode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class PlayerEntry implements Helper.Comparable {
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