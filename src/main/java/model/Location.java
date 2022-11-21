package model;

public class
Location {
    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int hashCode() {
        return (Integer.toString(row) + Integer.toString(col)).hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (null == o || !(o instanceof Location)) { return false; }
        if (o == this) { return true; }

        Location other = (Location) o; 
        return (this.row == other.row) && (this.col == other.col); 
    }

    

}
