class Cell {
    private int position;
    private int scoreBonus;

    public Cell(int position) {
        this.position = position;
        this.scoreBonus = 0;
    }

    public int getPosition() { return position; }
    public int getScoreBonus() { return scoreBonus; }
    public void setScoreBonus(int bonus) { this.scoreBonus = bonus; }
}
