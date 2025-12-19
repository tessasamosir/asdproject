class DiceResult {
    private int value;
    private boolean isGreen;

    public DiceResult(int value, boolean isGreen) {
        this.value = value;
        this.isGreen = isGreen;
    }

    public int getValue() { return value; }
    public boolean isGreen() { return isGreen; }
}