package puzzle;
import java.util.HashSet;

/** A puzzle-solving engine.
    @author Peterson Cheng */
class Solver {

    /** Asserts that E0 and E1 are associated (e.g., Tom is the
     *  carpenter). */
    HashSet<Triple> associate(NamedEntity e0, NamedEntity e1) {
        Association a = assocFn(e0, e1);
        HashSet<Triple> result = Triple.keep(a);
        if (e0.isImpossible() || e1.isImpossible()) {
            setImpossible();
        }
        e0.pickKnown();
        e1.pickKnown();
        return result;
    }

    /** Asserts that E0 and E1 are not associated (e.g., Tom is not the
     *  carpenter). */
    HashSet<Triple> disassociate(NamedEntity e0, NamedEntity e1) {
        Association b = assocFn(e0, e1, true);
        HashSet<Triple> result = Triple.keep(b);
        if (e0.isImpossible() || e1.isImpossible()) {
            setImpossible();
        }
        e0.pickKnown();
        e1.pickKnown();
        return result;
    }

    /** Asserts that E exists (e.g., Tom lives around here). */
    void exists(NamedEntity e) {
        Triple.add(e);
    }

    /** Return true iff the current set of facts is impossible. */
    public boolean impossible() {
        return _isImpossible;
    }

    /** Sets impossible flag to true. */
    public void setImpossible() {
        _isImpossible = true;
    }

    /** Returns the association function class from N0, N1, and B. */
    public static Association assocFn(NamedEntity n0,
                                      NamedEntity n1, boolean b) {
        return new Association(n0, n1, b);
    }

    /** Returns the association function class from N0, N1. */
    public static Association assocFn(NamedEntity n0, NamedEntity n1) {
        return new Association(n0, n1, false);
    }

    /** Returns true if it is impossible. */
    private boolean _isImpossible = false;

    /** Used to apply a filter only Triples that contain
     *  the two associated NamedEntities. */
    private static class Association implements Func {

         /** Initializes a class such that f can call N0 and
            N1. If B is true, returns the disassociation
            function instead.*/
        Association(NamedEntity n0, NamedEntity n1, boolean b) {
            _n0 = n0;
            _n1 = n1;
            _b = b;
        }

        /** Returns true if the triple T is consistent with
         *  the association of N0 and N1. If the disassocation
         *  flag B is set, keeps items if it does not contain
         *  both N0 and N1. (Dissasociation, NAND). Otherwise
         *  keeps items that do not contain N0 or N1 at all,
         *  or both at the same time. (Association, XNOR). */
        public boolean f(Triple t) {
            boolean x = t.contains(_n0);
            boolean y = t.contains(_n1);
            if (_b) {
                return !(x && y);
            } else {
                return !(x ^ y);
            }
        }

        /** Is the first entity N0. */
        private NamedEntity _n0;

        /** Is the second entity N1. */
        private NamedEntity _n1;

        /** Boolean if it returns association or
            disassociation. */
        private boolean _b;
    }

    
    /** Used to filter only Triples that contain
     *  are not possible.*/
    private static class DblAssociation implements Func {

         /** Initializes a class such that f can call N0 and
            N1. If B is true, returns the disassociation
            function instead.*/
        DblAssociation(Triple t1, Triple t2) {
            _t1 = t1;
            _t2 = t2;
        }

        public boolean f(Triple t) {
            return false;
        }

        /** Is the first entity N0. */
        private Triple _t1;

        /** Is the second entity N1. */
        private Triple _t2;
    }
}
