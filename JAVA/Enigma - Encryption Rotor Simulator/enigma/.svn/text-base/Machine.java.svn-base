package enigma;

/** Class that represents a complete enigma machine.
 *  @author Peterson Cheng
 */
class Machine {

    /** The reflector. */
    private Reflector _reflect;

    /** The left Rotor. */
    private Rotor _left;

    /** The middle Rotor. */
    private Rotor _mid;

    /** The right Rotor. */
    private Rotor _right;

    /** Set my rotors to (from left to right), REFLECTOR, LEFT,
     *  MIDDLE, and RIGHT.  Initially, their positions are all 'A'. */
    void setRotors(Reflector reflector,
                   Rotor left, Rotor middle, Rotor right) {
        _reflect = reflector;
        _left = left;
        _mid = middle;
        _right = right;
    }

    /** Set the positions of my rotors according to SETTING, which
     *  must be a string of 4 upper-case letters. The first letter
     *  refers to the reflector position, and the rest to the rotor
     *  positions, left to right. */
    void setPositions(String setting) {
        _reflect.setPosition(Rotor.toIndex(setting.charAt(0)));
        _left.setPosition(Rotor.toIndex(setting.charAt(1)));
        _mid.setPosition(Rotor.toIndex(setting.charAt(2)));
        _right.setPosition(Rotor.toIndex(setting.charAt(3)));
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {

        char[] result = new char[msg.length()];
        Rotor[] rotors = {_right, _mid, _left};

        for (int k = 0; k < msg.length(); k++) {
            boolean[] rotate = {true,
                                _mid.atNotch() || _right.atNotch(),
                                _mid.atNotch() };
            for (int i = 0; i < rotate.length; i++) {
                if (rotate[i]) {
                    rotors[i].advance();
                }
            }

            int p = Rotor.toIndex(msg.charAt(k));
            for (int i = 0; i < rotors.length; i++) {
                p = rotors[i].convertForward(p);
            }
            p = _reflect.convertForward(p);
            for (int i = 2; i >= 0; i--) {
                p = rotors[i].convertBackward(p);
            }
            result[k] = Rotor.toLetter(p);
        }

        return new String(result);

    }
}
