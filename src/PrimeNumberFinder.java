class PrimeNumberFinder {
    private boolean[] isPrimeArray;
    private int maxNumber;

    public PrimeNumberFinder(int maxNumber) {
        this.maxNumber = maxNumber;
        this.isPrimeArray = new boolean[maxNumber + 1];
        sieveOfEratosthenes();
    }

    private void sieveOfEratosthenes() {
        Arrays.fill(isPrimeArray, true);
        isPrimeArray[0] = isPrimeArray[1] = false;

        for (int i = 2; i * i <= maxNumber; i++) {
            if (isPrimeArray[i]) {
                for (int j = i * i; j <= maxNumber; j += i) {
                    isPrimeArray[j] = false;
                }
            }
        }
    }
    public boolean isPrime(int number) {
        if (number < 0 || number > maxNumber) return false;
        return isPrimeArray[number];
    }

    public Ladder findNearestLadder(int position, List<Ladder> ladders) {
        int minDistance = Integer.MAX_VALUE;
        Ladder nearest = null;

        for (Ladder ladder : ladders) {
            if (ladder.getStart() >= position) {
                int distance = ladder.getStart() - position;
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = ladder;
                }
            }
        }

        return nearest;
    }
}