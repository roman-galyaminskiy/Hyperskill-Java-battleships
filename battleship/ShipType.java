package battleship;

class ShipType {
    public ShipType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    private final String name;
    private final int size;
}
