
// NEED TO DETERMINE IF PUZZLE IS SOLVABLE
// NEED TO CREATE PUZZLE TO SOLVE

import java.util.*;

public class FifteenPuzzle {

    private class tile_loc {
        public int x;
        public int y;

        public tile_loc(int x, int y) {
            this.x=x;
            this.y=y;
        }

    }

    public final static int ticker=4;
    private int[][] tiles;
    private int display_width;
    private tile_loc blank;

    public FifteenPuzzle() {
        tiles = new int[ticker][ticker];
        int count = 1;
        for(int i = 0; i < ticker; i++) {
            for(int j = 0; j < ticker; j++) {
                tiles[i][j] = count;
                count++;
            }
        }
        display_width=Integer.toString(count).length();

        // init blank
        blank = new tile_loc(ticker-1,ticker-1);
        tiles[blank.x][blank.y] = 0;
    }

    public final static FifteenPuzzle SOLVED = new FifteenPuzzle();


    public FifteenPuzzle(FifteenPuzzle toClone) {
        this();  // chain to basic init
        for(tile_loc p: alltile_loc()) {
            tiles[p.x][p.y] = toClone.tile(p);
        }
        blank = toClone.blanker();
    }

    public List<tile_loc> alltile_loc() {
        ArrayList<tile_loc> out = new ArrayList<tile_loc>();
        for(int i = 0; i < ticker; i++) {
            for(int j = 0; j < ticker; j++) {
                out.add(new tile_loc(i,j));
            }
        }
        return out;
    }


    public int tile(tile_loc p) {
        return tiles[p.x][p.y];
    }


    public tile_loc blanker() {
        return blank;
    }


    public tile_loc finder(int x) {
        for(tile_loc p: alltile_loc()) {
            if( tile(p) == x ) {
                return p;
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof FifteenPuzzle) {
            for(tile_loc p: alltile_loc()) {
                if( this.tile(p) != ((FifteenPuzzle) o).tile(p)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int out = 0;
        for(tile_loc p: alltile_loc()) {
            out = (out * ticker * ticker) + this.tile(p);
        }
        return out;
    }


    public void board() {
        System.out.println("=================");
        for(int i = 0; i < ticker; i++) {
            System.out.print("| ");
            for(int j = 0; j < ticker; j++) {
                int n = tiles[i][j];
                String spot;
                if( n > 0) {
                    spot = Integer.toString(n);
                } else {
                    spot = "";
                }
                while( spot.length() < display_width ) {
                    spot += " ";
                }
                System.out.print(spot + "| ");
            }
            System.out.print("\n");
        }
        System.out.print("=================\n\n");
    }


    public List<tile_loc> all_moves() {
        ArrayList<tile_loc> out = new ArrayList<tile_loc>();
        for(int dx =- 1; dx < 2; dx++) {
            for(int dy =- 1; dy < 2; dy++) {
                tile_loc tile_place = new tile_loc(blank.x + dx, blank.y + dy);
                if(legit_check(tile_place) ) {
                    out.add(tile_place);
                }
            }
        }
        return out;
    }

    public void move(tile_loc p) {
        if( !legit_check(p) ) {
            throw new RuntimeException("Move is not Legit");
        }
        assert tiles[blank.x][blank.y] == 0;
        tiles[blank.x][blank.y] = tiles[p.x][p.y];
        tiles[p.x][p.y] = 0;
        blank = p;
    }

    public FifteenPuzzle moveClone(tile_loc p) {
        FifteenPuzzle out = new FifteenPuzzle(this);
        out.move(p);
        return out;
    }

    public boolean legit_check(tile_loc p) {
        if( ( p.x < 0) || (p.x >= ticker) ) {
            return false;
        }
        if( ( p.y < 0) || (p.y >= ticker) ) {
            return false;
        }
        int dx = blank.x - p.x;
        int dy = blank.y - p.y;
        if( (Math.abs(dx) + Math.abs(dy) != 1 ) || (dx*dy != 0) ) {
            return false;
        }
        return true;
    }

    public int check_wrong_tiles() {
        int wrong = 0;
        for(int i = 0; i < ticker; i++) {
            for(int j = 0; j < ticker; j++) {
                if( (tiles[i][j] > 0) && ( tiles[i][j] != SOLVED.tiles[i][j] ) ){
                    wrong++;
                }
            }
        }
        return wrong;
    }

    public boolean check_solved() {
        return check_wrong_tiles() == 0;
    }

    public int man_distance() {
        int sum = 0;
        for(tile_loc p: alltile_loc()) {
            int val = tile(p);
            if( val > 0 ) {
                tile_loc correct = SOLVED.finder(val);
                sum += Math.abs( correct.x = p.x );
                sum += Math.abs( correct.y = p.y );
            }
        }
        return sum;
    }

    public void mixer() {
        mixer(ticker*ticker*ticker*ticker*ticker);
    }

    public void mixer(int howmany) {
        for(int i = 0; i < howmany; i++) {
            List<tile_loc> possible = all_moves();
            int which =  (int) (Math.random() * possible.size());
            tile_loc move = possible.get(which);
            this.move(move);
            System.out.println("moved");
        }
    }

    public void editor() {

        Scanner inrow_scan = new Scanner(System.in);

        System.out.println("Enter the number you want relative to each tile position");
        System.out.println("The tile locations go across from left to right");
        System.out.println("PLEASE NOTE: ENTER 0 IN PLACE OF SPACE");

//        for(int dy =- 1; dy < 2; dy++) {
        //          tile_loc tile_place = new tile_loc(blank.x + dx, blank.y + dy);
        //        if(legit_check(tile_place) ) {
        //            out.add(tile_place);


        List<tile_loc> edit_loc = null;

        for (int i = 0; i < 16; i++) {
            System.out.printf("Number %d \n", i);
            System.out.println("X val");
            Scanner scan_x = new Scanner(System.in);
            int user_x = scan_x.nextInt();
            System.out.println("Y val");
            Scanner scan_y = new Scanner(System.in);
            int user_y = scan_y.nextInt();
            tile_loc edit_place = new tile_loc(user_x, user_y);
            edit_loc.add(edit_place);
        }


        for(int i = 0; i < 16; i++) {
            List<tile_loc> possible = edit_loc;
            int which = (int) (i * .1);
            tile_loc move = possible.get(which);
            this.move(move);
        }

    }


    public int estimateError() {
        return this.check_wrong_tiles();
        //return 5*this.check_wrong_tiles(); // finds a non-optimal solution faster
        //return this.man_distance();
    }


    public List<FifteenPuzzle> other_puzzles() {
        ArrayList<FifteenPuzzle> out = new ArrayList<FifteenPuzzle>();
        for( tile_loc move: all_moves() ) {
            out.add( moveClone(move) );
        }
        return out;
    }



    public List<FifteenPuzzle> a_star() {
        HashMap<FifteenPuzzle,FifteenPuzzle> old_puzzle = new HashMap<FifteenPuzzle,FifteenPuzzle>();
        HashMap<FifteenPuzzle,Integer> depth = new HashMap<FifteenPuzzle,Integer>();
        final HashMap<FifteenPuzzle,Integer> score = new HashMap<FifteenPuzzle,Integer>();
        Comparator<FifteenPuzzle> comparator = new Comparator<FifteenPuzzle>() {
            @Override
            public int compare(FifteenPuzzle a, FifteenPuzzle b) {
                return score.get(a) - score.get(b);
            }
        };
        PriorityQueue<FifteenPuzzle> try_this = new PriorityQueue<FifteenPuzzle>(10000,comparator);

        old_puzzle.put(this, null);
        depth.put(this,0);
        score.put(this, this.estimateError());
        try_this.add(this);
        int count = 0;
        while( try_this.size() > 0) {
            FifteenPuzzle candidate = try_this.remove();
            count++;
            if( count % 10000 == 0) {
                System.out.printf("%,d possibilities. Queue = %,d\n", count, try_this.size());
            }
            if( candidate.check_solved() ) {
                LinkedList<FifteenPuzzle> solution = new LinkedList<FifteenPuzzle>();
                FifteenPuzzle backtrace = candidate;
                while( backtrace != null ) {
                    solution.addFirst(backtrace);
                    backtrace = old_puzzle.get(backtrace);
                }
                return solution;
            }
            for(FifteenPuzzle fiv_puz: candidate.other_puzzles()) {
                if( !old_puzzle.containsKey(fiv_puz) ) {
                    old_puzzle.put(fiv_puz,candidate);
                    depth.put(fiv_puz, depth.get(candidate) + 1);
                    int estimate = fiv_puz.estimateError();
                    score.put(fiv_puz, depth.get(candidate) + 1 + estimate);

                    try_this.add(fiv_puz);
                }
            }
        }
        return null;
    }

    private static void showSolution(List<FifteenPuzzle> solution) {
        if (solution != null ) {
            System.out.println("Complete");
            System.out.printf("%d moves required\n", solution.size());
            for( FifteenPuzzle solve_puz: solution) {
                solve_puz.board();
            }
        } else {
            System.out.println("Solution: FAILURE");
        }
    }


    public static void main(String[] args) {
        FifteenPuzzle puz = new FifteenPuzzle();
        System.out.println("Enter \"e\" to use the editor. Enter \"r\" to use a random board");

        Scanner scan_er = new Scanner(System.in);
        String user_er = scan_er.nextLine();
        if(user_er.equals("e")) {
            System.out.println("editor");
            puz.editor();
        }
        else if (user_er.equals("r")) {
            System.out.println("random");
            puz.mixer(70);
        }
        else {
            System.out.println("You broke it");
            System.exit(0);
        }

        System.out.println("mixer board:");
        puz.board();

        List<FifteenPuzzle> solution;

        System.out.println("Solving with A*");
        solution = puz.a_star();
        showSolution(solution);

    }

}
