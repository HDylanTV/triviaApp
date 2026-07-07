package nl.hdylantv;

import java.util.Map;

public record QuizConfig(
    int amount,
    String category,
    String difficulty,
    String type
) {
    public static Map<String, Integer> allowedCategories = Map.ofEntries(
        Map.entry("General Knowledge", 9),
        Map.entry("Entertainment: Books", 10),
        Map.entry("Entertainment: Film", 11),
        Map.entry("Entertainment: Music", 12),
        Map.entry("Entertainment: Musicals & Theatres", 13),
        Map.entry("Entertainment: Television", 14),
        Map.entry("Entertainment: Video Games", 15),
        Map.entry("Entertainment: Board Games", 16),
        Map.entry("Science & Nature", 17),
        Map.entry("Science: Computers", 18),
        Map.entry("Science: Mathematics", 19),
        Map.entry("Mythology", 20),
        Map.entry("Sports", 21),
        Map.entry("Geography", 22),
        Map.entry("History", 23),
        Map.entry("Politics", 24),
        Map.entry("Art", 25),
        Map.entry("Celebrities", 26),
        Map.entry("Animals", 27),
        Map.entry("Vehicles", 28),
        Map.entry("Entertainment: Comics", 29),
        Map.entry("Science: Gadgets", 30),
        Map.entry("Entertainment: Japanese Anime & Manga", 31),
        Map.entry("Entertainment: Cartoon & Animations", 32)
    );

    public enum Difficulty {
        ANY(null),
        EASY("easy"),
        MEDIUM("medium"),
        HARD("hard");

        private final String value;

        Difficulty(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum Type {
        ANY(null),
        MULTIPLE_CHOICE("multiple"),
        TRUE_OR_FALSE("boolean");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public boolean isValid() {
        return 1 <= this.amount && this.amount <= 50;
    }

    public int getAmount() {
        return this.amount;
    }

    public Integer getCategoryId() {
        if (this.category == null) {
            return null;
        }
        if (allowedCategories.containsKey(this.category)) {
            return allowedCategories.get(this.category);
        }

        return null;
    }

    public Difficulty getDifficulty() {
        if (this.difficulty == null) {
            return Difficulty.ANY;
        }
        switch (this.difficulty) {
            case "easy" -> {
                return Difficulty.EASY;
            }
            case "medium" -> {
                return Difficulty.MEDIUM;
            }
            case "hard" -> {
                return Difficulty.HARD;
            }
            default -> {
                return Difficulty.ANY;
            }
        }
    }

    public Type getType() {
        if (this.type == null) {
            return Type.ANY;
        }
        switch (this.type) {
            case "multiple" -> {
                return Type.MULTIPLE_CHOICE;
            }
            case "boolean" -> {
                return Type.TRUE_OR_FALSE;
            }
            default -> {
                return Type.ANY;
            }
        }
    }
}
