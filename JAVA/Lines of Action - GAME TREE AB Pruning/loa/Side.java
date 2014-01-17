// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.

package loa;

/** Indicates a piece or player color. */
enum Side {
    /** The names of the two sides. */
    BLACK, WHITE;

    /** Return the opposing color. */
    Side opponent() {
        return this == BLACK ? WHITE : BLACK;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /** The initial representing this player's side. */
    public String initial() {
        return this == BLACK ? "B" : "W";
    } 

    /** Returns capitalized string representing this player's side. */
    public String capital() {
        return this == BLACK ? "Black" : "White";
    }

}
