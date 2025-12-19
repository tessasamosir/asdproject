import java.util.*;

class Dice {
    private Random random;

    public Dice() {
        this.random = new Random();
    }

    public DiceResult roll() {
        int value = random.nextInt(6) + 1;
        boolean isGreen = random.nextInt(100) < 80;
        return new DiceResult(value, isGreen);
    }
}