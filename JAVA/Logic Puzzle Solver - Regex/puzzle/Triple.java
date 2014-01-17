package puzzle;
import static puzzle.EntityType.*;
import java.util.HashSet;

/** A data structure which holds a person, occupation, and color.
    @author Peterson Cheng */

class Triple {

    /** A Triple with person PERSON, occupation OCCUPATION,
     *  color COLOR. */
    Triple(NamedEntity person, NamedEntity occupation, NamedEntity color) {
        if (!(person.getType() == PERSON
              && occupation.getType() == OCCUPATION
              && color.getType() == COLOR)) {
            String errmsg = "Argument of triple not in form: "
                + "Triple(PERSON, OCCUPATION, COLOR)";
            throw new IllegalArgumentException(errmsg);
        }
        _p = person;
        _o = occupation;
        _c = color;
    }

    /** Resets the Triple static variables in between runs. */
    static void reset() {
        _pList.clear();
        _oList.clear();
        _cList.clear();
        _tList.clear();
    }

    /** Adds NamedEntity N to the TripleList. */
    static void add(NamedEntity n) {
        switch (n.getType()) {
        case PERSON:
            _pList.add(n);
            break;
        case OCCUPATION:
            _oList.add(n);
            break;
        case COLOR:
            _cList.add(n);
            break;
        default:
            System.out.println("Default switch case Triple.Java Line 27");
            break;
        }
    }

    /** Fills the internal lists with anonymous values. */
    static void normalize() {
        int m = Math.max(_pList.size(), _oList.size());
        m = Math.max(m , _cList.size());
        while (_pList.size() < m) {
            String s = NamedEntity.makeName("p");
            _pList.add(NamedEntity.create(s, PERSON));
        }
        while (_oList.size() < m) {
            String s = NamedEntity.makeName("o");
            _oList.add(NamedEntity.create(s, OCCUPATION));
        }
        while (_cList.size() < m) {
            String s = NamedEntity.makeName("c");
            _cList.add(NamedEntity.create(s, COLOR));
        }
    }

    /** Populates the list with all triples .*/
    static void populate() {
        _tList.clear();
        Triple.normalize();
        for (NamedEntity p: _pList) {
            for (NamedEntity o: _oList) {
                for (NamedEntity c: _cList) {
                    _tList.add(new Triple(p, o, c));
                }
            }
        }
    }

    /** Prints a HashSet<Triple> T. */
    static void printHash(HashSet<Triple> t, String msg) {
        System.out.println(msg);
        for (Triple t0: t) {
            System.out.println(t0);
        }
        System.out.println("");
    }

    /** Prints a Triple[] T. */
    static void printHash(Triple[] t, String msg) {
        System.out.println(msg);
        for (Triple t0: t) {
            System.out.println(t0);
        }
        System.out.println("");
    }

    /** Cannot remove t0 if there is only one left. Will alow
     *  N to be removed.*/
    static boolean careRemove(NamedEntity n, Triple t0) {
        NamedEntity[] ents = { t0.getPerson(),
                               t0.getOccupation(),
                               t0.getColor()};
        int[] numLeft = new int[3];
        boolean rflag = false;
        for (int i = 0; i < 3; i++) {
            if (!(ents[i].equals(n))) {
                numLeft[i] = ents[i].hasNamedEntity().size();
                /**System.out.printf("\ncareRemove(%s)\n", ents[i].toString());
                System.out.printf("##%s##\n\n", t0.toString());
                for (Triple t: ents[i].hasNamedEntity()) {//
                System.out.println(t.toString());
                }*/
            }
        }
        for (int i: numLeft) {
            if (i == 1) {
                rflag = true;
            }
        }
        if (!(rflag)) {
            _tList.remove(t0);
        }
        return (!(rflag));
    }
    

    /** Given H, keeps all items n such that
     * h(n) is true. */
    static HashSet<Triple> keep(Func h) {
        HashSet<Triple> result = new HashSet<Triple>();
        Triple[] tArray = _tList.toArray(new Triple[0]);
        for (Triple t: tArray) {
            if (h.f(t)) {
                continue;
            } else {
                result.add(t);
                _tList.remove(t);
            }
        }
        return result;
    }

    /** Given H, returns a copy of _tList such that
     *  h(n) is true. */
    static HashSet<Triple> subset(Func h) {
        Triple[] tArray = _tList.toArray(new Triple[0]);
        HashSet<Triple> result = new HashSet<Triple>(_tList);
        for (Triple t: tArray) {
            if (h.f(t)) {
                continue;
            } else {
                result.remove(t);
            }
        }
        return result;
    }

    /** Makes this triple T the only one in solver SOLVER. */
    static HashSet<Triple> makeExclusive(Solver solver, Triple t) {
        HashSet<Triple> result = new HashSet<Triple>();
        result.addAll(solver.associate(t.getPerson(),
                                       t.getColor()));
        result.addAll(solver.associate(t.getPerson(),
                                       t.getOccupation()));
        result.addAll(solver.associate(t.getOccupation(),
                                       t.getColor()));
        return result;
    }

    /** Makes this triple the only one in SOLVER. */
    static HashSet<Triple> makeExclusive(Solver solver) {
        //Triple.printTripleList();//
        HashSet<Triple> result = new HashSet<Triple>();
        for (NamedEntity n: _pList) {
            Triple[] t = n.hasNamedEntity().toArray(new Triple[0]);
            //Triple.printHash(t, n.toString());//
            if (t.length == 1) {
                    result.add(t[0]);
            }
        }
        for (NamedEntity n: _oList) {
            Triple[] t = n.hasNamedEntity().toArray(new Triple[0]);
            //Triple.printHash(t, n.toString());//
            if (t.length == 1) {
                result.add(t[0]);
            }
        }
        for (NamedEntity n: _cList) {
            Triple[] t = n.hasNamedEntity().toArray(new Triple[0]);
            //Triple.printHash(t, n.toString());//
            if (t.length == 1) {
                    result.add(t[0]);
            }
        }
        prune(result);
        for (Triple t0: result) {
            makeExclusive(solver, t0);
        }
        return result;
    }

    /** Generates and returns string array of possible answers. */
    public String[] answerGen() {
        String p = _p.toString();
        String o = _o.toString();
        String c = _c.toString();
        String[] result = new String[5];
        result[0] = p + " is the " + o + " and lives in the "
            + c + " house.";
        result[1] = p + " is the " + o + ".";
        result[2] = p + " lives in the " + c + " house.";
        result[3] = "The " + o + " lives in the " + c + " house.";
        result[4] = "Nothing.";
        return result;
    }

    /** Returns the PERSON in this triple. */
    public NamedEntity getPerson() {
        return _p;
    }

    /** Returns the OCCUPATION in this triple.*/
    public NamedEntity getOccupation() {
        return _o;
    }

    /** Returns the COLOR in this triple. */
    public NamedEntity getColor() {
        return _c;
    }

    /** Returns true iff this Triple contains an anonymous NamedEntity. */
    public boolean isAnonymous() {
        return _p.isAnonymous() || _o.isAnonymous() || _c.isAnonymous();
    }

    /** Returns true iff NamedEntity M is in Triple. */
    public boolean contains(NamedEntity m) {
        NamedEntity[] n0 = {_p, _o, _c};
        boolean result = false;
        for (NamedEntity n : n0) {
            if (n.equals(m)) {
                return true;
            }
        }
        return result;
    }

    /** Returns true if two triples are completely different. */
    public static boolean different(Triple t1, Triple t2) {
        boolean b1 = t1.getPerson().equals(t2.getPerson());
        boolean b2 = t1.getOccupation().equals(t2.getOccupation());
        boolean b3 = t1.getColor().equals(t2.getColor());
        return (!(b1 || b2 || b3));
    }

    /** Returns true if the triple is completely unique
     *  None of the Entities in Triple T0 are in T. */
    public static boolean unique(HashSet<Triple> t, Triple t0) {
        boolean b = true;
        for (Triple s: t) {
            b &= different(s, t0);
        }
        return b;
    }

    /** Prunes a HashSet of Triples T, removing all ununique entries. */
    public static void prune(HashSet<Triple> t) {
        Triple[] t0 = t.toArray(new Triple[0]);
        Triple[] t1 = t.toArray(new Triple[0]);
        for (Triple tA : t0) {
            for (Triple tB : t1) {
                if (!(tA.equals(tB))) {
                    if(!(different(tA, tB))) {
                        t.remove(tA);
                        t.remove(tB);
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        Triple e = (Triple) other;
        boolean b1 = e.getPerson().equals(_p);
        boolean b2 = e.getOccupation().equals(_o);
        boolean b3 = e.getColor().equals(_c);
        return (b1 && b2 && b3);
    }

    /** Returns a hashcode for me, allowing Triples to be used
        in HashSets and HashMaps. */
    @Override
    public int hashCode() {
        return _p.hashCode() * _o.hashCode() * _c.hashCode();
    }

    @Override
    public String toString() {
        return (String.format("%s is a %s and lives in a %s house.",
                              _p, _o, _c));
    }

    /** The internal list of person entities. */
    private static HashSet<NamedEntity> _pList  = new HashSet<NamedEntity>();

    /** Returns _pList. */
    public static HashSet<NamedEntity> pList() {
        return _pList;
    }

    /** The internal list of occupation entities. */
    private static HashSet<NamedEntity> _oList = new HashSet<NamedEntity>();

    /** Returns _oList. */
    public static HashSet<NamedEntity> oList() {
        return _oList;
    }

    /** The internal list of color entities. */
    private static HashSet<NamedEntity> _cList = new HashSet<NamedEntity>();

    /** Returns _cList. */
    public static HashSet<NamedEntity> cList() {
        return _cList;
    }

    /** The internal list of all triple permutations. */
    private static HashSet<Triple> _tList = new HashSet<Triple>();

    /** Returns _tList. */
    public static HashSet<Triple> tList() {
        return _tList;
    }

    /** Prints out N lines of the triples. */
    public static void printTriple(int n) {
        for (Triple t: _tList) {
            if (n == 0) {
                return;
            }
            n--;
            System.out.println(t.toString());
        }
        System.out.println();
    }

    /** Prints out the triple list. */
    public static void printTripleList() {
        System.out.println("Lines: " + Triple.tList().size());
        for (Triple t: Triple.tList()) {
            System.out.println(t.toString());
        }
        System.out.println("\n\n");
    }

    /** The internal PERSON. */
    private NamedEntity _p;

    /** The interal OCCUPATION. */
    private NamedEntity _o;

    /** The internal COLOR. */
    private NamedEntity _c;
}
