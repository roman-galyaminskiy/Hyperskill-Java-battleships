package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Player {
    private final Ship[] ships = new Ship[5];
    private enum ShotType {HIT, MISS, NONE};
    private final ShotType[] shots = new ShotType[100];
    private int ships_number = 0;
    private int ships_destroyed = 0;
    private int[] lastShotIndexes = new int[2];

    public Player() {
        System.out.println("DEBUG: Player created");
        Arrays.fill(ships, null);
        Arrays.fill(shots, ShotType.NONE);
    }

//  Drawing your and opponent's board

    public void drawPrepare() {
        drawShips();
    }

    public void drawBattle() {
        drawShots();
        drawSeparator();
        drawShips();
    }

    private void drawShips() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        char[][] buf = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buf[i][j] = '~';
            }
        }

        for (int i = 0; i < ships_number; i++) {
            if (ships[i].getX1() == ships[i].getX2()) {
                for (int j = ships[i].getY1(); j <= ships[i].getY2(); j++) {
                    buf[j][ships[i].getX1()] = ships[i].getCell(j - ships[i].getY1());;
                }
            }
            else if (ships[i].getY1() == ships[i].getY2()) {
                for (int j = ships[i].getX1(); j <= ships[i].getX2(); j++) {
                    buf[ships[i].getY1()][j] = ships[i].getCell(j - ships[i].getX1());
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            System.out.printf("%c", 'A' + i);

            for (int j = 0; j < 10; j++) {
                System.out.printf(" %c", buf[i][j]);
            }
            System.out.println();
        }
    }

    private void drawShots() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        char[][] buf = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                switch(shots[(i + 1) * (j + 1) - 1]) {
                    case HIT:
                        buf[i][j] = 'X';
                        break;
                    case MISS:
                        buf[i][j] = 'M';
                        break;
                    default:
                        buf[i][j] = '~';
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            System.out.printf("%c", 'A' + i);

            for (int j = 0; j < 10; j++) {
                System.out.printf(" %c", buf[i][j]);
            }
            System.out.println();
        }
    }

    private void drawSeparator() {
        System.out.println("---------------------");
    }

//  Preparation

    public void placeShips() {
        ShipType[] shipTypes = {
                new ShipType("Aircraft Carrier", 5),
                new ShipType("Battleship", 4),
                new ShipType("Submarine", 3),
                new ShipType("Cruiser", 3),
                new ShipType("Destroyer", 2)};

        Scanner scanner = new Scanner(System.in);

        for (var shipType: shipTypes) {
            drawPrepare();

            while (true) {

                System.out.printf("\nEnter the coordinates of the %s (%d cells):\n\n> ", shipType.getName(), shipType.getSize());

                try {
                    this.addShip(shipType.getSize(), parsePosition(scanner.next(), scanner.next()));
                    this.drawPrepare();
                    break;
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void addShip(int size, int[] indexes) {
        checkSize(size, indexes);
        for (int i = 0; i < ships_number; i++) {
            checkIntersection(this.ships[i], indexes);
        }
        this.ships[ships_number] = new Ship(size, indexes);
        ships_number++;
    }

    private int[] parsePosition(String begin, String end) {
//      F3 F7 -> [2,5], [6,5]
//      J10 J8 -> [9, 7], [9, 9]
//      '1' = '1' - '1' = 0
//      '10' = '0' - '1' + '1' - '0' * 10 = 10 - 1 = 9

        int[] indexes = new int[4];

        if (begin.length() == 3) {
            indexes[0] = (begin.charAt(1) - '0') * 10 + (begin.charAt(2) - '1');
        } else {
            indexes[0] = begin.charAt(1) - '1';
        }

        indexes[1] = begin.charAt(0) - 'A';

        if (end.length() == 3) {
            indexes[2] = (end.charAt(1) - '0') * 10 + (end.charAt(2) - '1');
        } else {
            indexes[2] = end.charAt(1) - '1';
        }

        indexes[3] = end.charAt(0) - 'A';

        for (int index : indexes) {
            if (index < 0 || index > 9) {
                throw new RuntimeException("Error! checkSize");
            }
        }

//      First point is upper left corner, swap points if needed
        if (indexes[0] > indexes[2] || indexes[1] > indexes[3]) {
            int buff = indexes[0];
            indexes[0] = indexes[2];
            indexes[2] = buff;
            buff = indexes[1];
            indexes[1] = indexes[3];
            indexes[3] = buff;
        }

        return indexes;
    }

    private void checkIntersection(Ship ship, int[] indexes) {
        int x1 = indexes[0];
        int y1 = indexes[1];
        int x2 = indexes[2];
        int y2 = indexes[3];

//      new ship if higher/lower or on left/right side
//        System.out.printf("DEBUG: old ship: [%d, %d], [%d, %d]\n", ship.getX1(), ship.getY1(), ship.getX2(), ship.getY2());
//        System.out.printf("DEBUG: new ship: [%d, %d], [%d, %d]\n", x1, y1, x2, y2);
        if ( !(y2 < (ship.getY1() - 1) || y1 > (ship.getY2() + 1) || x2 < (ship.getX1() - 1 )|| x1 > (ship.getX2() + 1)) ) {
            throw new RuntimeException("Error! checkIntersection");
        }
    }

    private void checkSize (int size, int[] indexes) {
        int x1 = indexes[0];
        int y1 = indexes[1];
        int x2 = indexes[2];
        int y2 = indexes[3];

        if (x2 - x1 + y2 - y1 != size - 1) {
            throw new RuntimeException("Error! checkSize");
        }
    }

//  Battle

    public int[] shoot() {
        Scanner scanner = new Scanner(System.in);
        String shot;
        int[] indexes = new int[2];

        drawBattle();

        while (true) {
            System.out.println("Take a shot!\n> ");
            shot = scanner.next();

            if (shot.length() == 3) {
                indexes[0] = (shot.charAt(1) - '0') * 10 + (shot.charAt(2) - '1');
            } else {
                indexes[0] = shot.charAt(1) - '1';
            }

            indexes[1] = shot.charAt(0) - 'A';

            if (indexes[0] < 0 || indexes[0] > 9 || indexes[1] < 0 || indexes[1] > 9) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n\n>");
                continue;
            }
            break;
        }

        lastShotIndexes = indexes;
        return indexes;
    }

    public void lastShotResults(boolean hit) {
        if (hit) {
            shots[(lastShotIndexes[0] + 1) * (lastShotIndexes[1] + 1) - 1] = ShotType.HIT;
        }
        else {
            shots[(lastShotIndexes[0] + 1) * (lastShotIndexes[1] + 1) - 1] = ShotType.MISS;
        }
    }

    public int checkHit(int[] indexes) {
        for (var ship : ships) {
            if (!ship.isDestroyed()) {
                if (ship.shot(indexes[0], indexes[1])) {
                    if (ship.isDestroyed()) {
                        ++ships_destroyed;

                        if (ships_destroyed == ships_number) {
                            return 3;
                        }
                        return 2;
                    }
                    return 1;
                }
            }
        }

        return 0;
    }

}
