package enigma;

/** Class that represents a reflector in the enigma.
 *  @author Peterson Cheng
 */
class Reflector extends Rotor {

    /** Constructs a Reflector with ID (B, C, etc). */
    public Reflector(String id) {
        super(id);
    }

    /** Returns a useless value; should never be called. */
    @Override
    int convertBackward(int unused) {
        throw new UnsupportedOperationException();
    }

    /** Reflectors do not advance. */
    @Override
    void advance() {
    }

    /** Reflectors are never at a notch position
     *  (always returns false).*/
    @Override
    boolean atNotch() {
        return false;
    }
}
