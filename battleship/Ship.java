package battleship;

class Ship {
    public Ship(int size, int[] indexes) {
        this.x1 = indexes[0];
        this.y1 = indexes[1];
        this.x2 = indexes[2];
        this.y2 = indexes[3];
        this.size = size;
        this.cells_damaged = 0;
        cells = new char[size];

        for (int i = 0; i < size; i++) {
            cells[i] = 'O';
        }
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public boolean shot(int x, int y) {
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
            if (x1 == x2) {
                cells[y - y1] = 'X';
            } else {
                cells[x - x1] = 'X';
            }
            ++cells_damaged;
            System.out.printf("cells_damaged: %d, size: %d\n", cells_damaged, size);
            return true;
        } else {
            return false;
        }
    }

    public boolean isDestroyed() {
        return (cells_damaged == size);
    }

    public char getCell(int cell_index) {
        return cells[cell_index];
    }

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int size;
    private char cells[];
    private int cells_damaged = 0;
}