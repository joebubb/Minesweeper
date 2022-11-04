public class Location {
    private int row; 
    private int column; 

    public Location(int row, int column) {
        this.row = row; 
        this.column = column; 
    }

    public int getRow() {
        return row; 
    }

    public int getColumn() {
        return column; 
    }

    /*
     * This method will return the hashCode of the string resulting from the 
     * concatenation of the row and the column. 
     * 
     * This is because if the row and column are the same, we want the same hashCode, 
     * and by default the memory address is hashed. This isn't the behavior we want. By default, 
     * locations will ALWAYS be considered different because they are stored in differnt spots in memory
     * unless they share a reference. 
     * 
     * Example: 
     * Location l1 = new Location(0, 1); // stored at 0x00
     * Location l2 = new Location(0, 1); // stored at 0x01
     *          ^
     *          |
     * These identical locaitons will have two different hashes since the reference Java stores is
     * different.
     * 
     * With this implementation of hashCode, two different Locations won't get the same hashCode since the string 
     * will surely by different, granted that Java's hashing algorithm is robust enough to 
     * avoid collisions. 
     */
    public int hashCode() {
        return (Integer.toString(row) + Integer.toString(column)).hashCode();
    }

    // returns true if both objects are Locations and they represent the same spot on the board 
    @Override
    public boolean equals(Object o) {
        if (null == o || !(o instanceof Location)) { return false; }
        if (o == this) { return true; }

        Location other = (Location) o; 
        return (this.row == other.row) && (this.column == other.column); 
    }
}
