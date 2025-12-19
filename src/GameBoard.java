class GameBoard {
    private static final int SIZE = 8;
    private static final int TOTAL_CELLS = SIZE * SIZE;
    private Cell[] cells;
    private List<Ladder> ladders;
    private PrimeNumberFinder primeFinder;
    private Random random;

    public GameBoard() {
        this.cells = new Cell[TOTAL_CELLS + 1];
        this.ladders = new ArrayList<>();
        this.primeFinder = new PrimeNumberFinder(TOTAL_CELLS);
        this.random = new Random();

        initializeCells();
        initializeLadders();
        initializeRandomScores();
    }

    private void initializeCells() {
        for (int i = 0; i <= TOTAL_CELLS; i++) {
            cells[i] = new Cell(i);
        }
    }

    private void initializeLadders() {
        ladders.add(new Ladder(3, 22));
        ladders.add(new Ladder(5, 14));
        ladders.add(new Ladder(11, 26));
        ladders.add(new Ladder(17, 38));
        ladders.add(new Ladder(20, 42));
        ladders.add(new Ladder(28, 50));
        ladders.add(new Ladder(36, 55));
        ladders.add(new Ladder(43, 62));
        ladders.add(new Ladder(51, 67));
    }

    private void initializeRandomScores() {
        int[] randomNodes = {7, 15, 23, 31, 39, 47, 55, 63};
        for (int node : randomNodes) {
            if (node <= TOTAL_CELLS) {
                cells[node].setScoreBonus(random.nextInt(50) + 10);
            }
        }
    }

    public int getTotalCells() { return TOTAL_CELLS; }

    public Cell getCell(int position) {
        if (position >= 0 && position <= TOTAL_CELLS) {
            return cells[position];
        }
        return null;
    }

    public Ladder getLadderAt(int position) {
        for (Ladder ladder : ladders) {
            if (ladder.getStart() == position) {
                return ladder;
            }
        }
        return null;
    }

    public List<Ladder> getLadders() { return ladders; }

    public boolean isPrime(int number) {
        return primeFinder.isPrime(number);
    }

    public Ladder getNearestLadder(int position) {
        return primeFinder.findNearestLadder(position, ladders);
    }
}