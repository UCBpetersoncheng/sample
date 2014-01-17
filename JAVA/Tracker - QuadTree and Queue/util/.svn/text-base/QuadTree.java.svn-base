package util;

import java.util.Iterator;
import java.util.HashSet;
import java.util.NoSuchElementException;

/** A Set2D implemented with a QuadTree.  The type argument, Point,
 *  indicates the type of points contained in the set.  The rather
 *  involved type parameter structure here allows you to extend
 *  QuadPoint, and thus add additional data and methods to the points
 *  you store.
 *  @author Peterson Cheng
 */
public class QuadTree<Point> extends Set2D<Point> {

    /** An empty set of Points that uses VIEW to extract position information.
     *  The argument TRANSITIONSIZE has no externally
     *  visible effect, but may affect performance.  It is intended to specify
     *  the largest set Points that resides unsubdivided in a single node of
     *  the tree.  While space-efficient, such nodes have slower search times
     *  as their size increases.
     *  */
    public QuadTree(PointView<Point> view, int transitionSize) {
        super(view);
        _transitionSize = transitionSize;
        _root = new HashSet<WrappedPoint>(transitionSize + 1);
    }

    /* PUBLIC METHODS.  See Set2D.java for documentation */

    /** Returns my transitionSize parameter (supplied to my constructor). */
    public int getTransitionSize() {
        return _transitionSize;
    }

    @Override
    public int size() {
        if (_trans) {
            return (_quad1.size() + _quad2.size()
                    + _quad3.size() + _quad4.size());
        } else {
            return _root.size();
        }
    }

    @Override
    public void add(Point p) {
        if (contains(p)) {
            return;
        }
        if (_trans) {
            int quadrant = quadrant(x(p), y(p), _xcen, _ycen);
            quadList(quadrant).add(p);
        } else {
            if (_root.contains(new XYCompare(p))) {
                _numDups += 1;
            }
            if ((size() + 1 - _numDups > _transitionSize)) {
                _root.add(new WrappedPoint(p));
                if (!(_isBal)) {
                    balance();
                }
                transition();

            } else {
                _root.add(new WrappedPoint(p));
            }
        }
    }

    @Override
    public void remove(Point p) {
        if (!(isWithin(p))) {
            return;
        }
        if (_trans) {
            int quadrant = quadrant(x(p), y(p), _xcen, _ycen);
            quadList(quadrant).remove(p);
        } else {
            _root.remove(new WrappedPoint(p));
        }
    }

    @Override
    public boolean contains(Point p) {
        if (!(isWithin(p))) {
            return false;
        }

        if (_trans) {
            int quadrant = quadrant(x(p), y(p), _xcen, _ycen);
            return quadList(quadrant).contains(p);
        } else {
            return _root.contains(new WrappedPoint(p));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Point> iterator() {
        return (Iterator<Point>) new QuadIterator(_trans);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Point> iterator(double xl, double yl,
                                    double xu, double yu) {
        if (_trans) {
            int q1 = quadrant(xl, yl, _xcen, _ycen);
            int q2 = quadrant(xu, yl, _xcen, _ycen);
            int q3 = quadrant(xu, yu, _xcen, _ycen);
            int q4 = quadrant(xl, yu, _xcen, _ycen);
            if (q1 == 1) {
                return quadList(q1).iterator(xl, yl, xu, yu);
            } else if (q2 == 2) {
                return quadList(q2).iterator(xl, yl, xu, yu);
            } else if (q3 == 3) {
                return quadList(q3).iterator(xl, yl, xu, yu);
            } else if (q4 == 4) {
                return quadList(q4).iterator(xl, yl, xu, yu);
            } else {
                return (Iterator<Point>) new QuadIterator(xl, yl,
                                                  xu, yu);
            }
        } else {
            return (Iterator<Point>) new QuadIterator(xl, yl,
                                                  xu, yu);
        }
    }

    /* END OF PUBLIC MEMBERS */

    /** The maximum size for an unsubdivided node. */
    private int _transitionSize;

    /** The root of the tree. */
    private HashSet<WrappedPoint> _root;

    /** Holds the first quadrant.*/
    private QuadTree<Point> _quad1;

    /** Holds the second quadrant.*/
    private QuadTree<Point> _quad2;

    /** Holds the third quadrant. */
    private QuadTree<Point> _quad3;

    /** Holds the fourth quadrant. */
    private QuadTree<Point> _quad4;

    /** Holds the number of duplicates. */
    private int _numDups = 0;

    /** Holds the lower bound of the x-coordinate. */
    private double _xlow = Double.NEGATIVE_INFINITY;

    /** Holds the lower bound of the y-coordinate.*/
    private double _ylow = Double.NEGATIVE_INFINITY;

    /** Holds the upper bound of the x-coordinate. */
    private double _xupp = Double.POSITIVE_INFINITY;

    /** Holds the upper bound of the y-coordinate.*/
    private double _yupp = Double.POSITIVE_INFINITY;

    /** Holds the x coordinate of the center of this QuadTree. */
    private double _xcen = 0;

    /** Holds the y coordinate of the center of this QuadTree. */
    private double _ycen = 0;

    /** Is true if this QuadTree has children. Returns false if leaf.*/
    private boolean _trans = false;

    /** Is true if this QuadTree needs to be balanced. */
    private boolean _isBal = true;

    /** Holds the name. */
    private String _s = "root";


    /** Sets the lower bounds of THIS to A and the upper bound to B. */
    private void setBounds(Point A, Point B) {
        _xlow = x(A);
        _ylow = y(A);
        _xupp = x(B);
        _yupp = y(B);
    }

    /** Sets the lower bounds of THIS to (X0, Y0) and the upper bounds
      * to (X1, Y1) . */
    private void setBounds(double x0, double y0, double x1, double y1) {
        _xlow = x0;
        _ylow = y0;
        _xupp = x1;
        _yupp = y1;
    }

    /** Returns the different quadrant depending on the int N. */
    private QuadTree<Point> quadList(int n) {
        switch(n) {
        case 0:
        case 1:
            return _quad1;
        case 2:
            return _quad2;
        case 3:
            return _quad3;
        case 4:
            return _quad4;
        default:
            return _quad1;
        }
    }

    /** Notes that this QuadTree is unbalanced. */
    private void setBalance() {
        _isBal = false;
    }

    /** Returns true if the point P is within the bounds of this QuadTree. */
    private boolean isWithin(Point p) {
        return isWithin(x(p), y(p), _xlow, _ylow, _xupp, _yupp);
    }

    /** Initializes the new QuadTrees and sets their boundaries. Will have
     *  undefined behavior if this is called before setCenter.*/
    private void initialize() {
        _quad1 = new QuadTree<Point>(getView(), _transitionSize);
        _quad2 = new QuadTree<Point>(getView(), _transitionSize);
        _quad3 = new QuadTree<Point>(getView(), _transitionSize);
        _quad4 = new QuadTree<Point>(getView(), _transitionSize);

        _quad1.setBounds(_xcen, _ycen, _xupp, _yupp);
        _quad2.setBounds(_xlow, _ycen, _xcen, _yupp);
        _quad3.setBounds(_xlow, _ylow, _xcen, _ycen);
        _quad4.setBounds(_xcen, _ylow, _xupp, _ycen);

        _quad1.setBalance();
        _quad2.setBalance();
        _quad3.setBalance();
        _quad4.setBalance();

        _quad1.setName(_s + "_1");
        _quad2.setName(_s + "_2");
        _quad3.setName(_s + "_3");
        _quad4.setName(_s + "_4");
    }

    /**Balances the QuadTree by setting the center near the average
     *  of the point distributions. */
    private void balance() {
        double newXcen = 0;
        double newYcen = 0;
        Point p;
        for (WrappedPoint p0 : _root) {
            p = p0.getOrig();
            newXcen += x(p);
            newYcen += y(p);
        }
        _xcen = newXcen / size();
        _ycen = newYcen / size();
        _isBal = true;
    }

    /** Transitions this QuadTree to a node. */
    private void transition() {
        initialize();
        Point p;
        for (WrappedPoint p0: _root) {
            p = p0.getOrig();
            int quadrant = quadrant(x(p), y(p), _xcen, _ycen);
            quadList(quadrant).add(p);
        }
        _root = null;
        _trans = true;

    }

    /** Sets the center of this QuadTree to X, and Y. */
    private void setCenter(double x, double y) {
        _xcen = x;
        _ycen = y;
    }

    /** Labels this QuadTree S. */
    private void setName(String s) {
        _s = s;
    }

    /** Prints out this name. */
    private void printName() {
        System.out.print(_s);
    }




    /** WrappedPoint which allow HashSet to check for equality
     *  using PointView.equals() instead of Point.equals(). */
    public class WrappedPoint {

        /** Constructs a WrappedPoint P. */
        public WrappedPoint(Point p) {
            _p = p;
        }

        @Override
        public int hashCode() {
            Double d1 = new Double(x(_p));
            Double d2 = new Double(y(_p));
            return d1.hashCode() * d2.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            WrappedPoint w = (WrappedPoint) obj;
            return getView().equals(this.getOrig(), w.getOrig());
        }

        /** Returns the original point P. */
        public Point getOrig() {
            return _p;
        }

        /** Holds the original point P. */
        private Point _p;
    }

    /** Allows the HashSet to check for points that share the same
     *  (X,Y) position. */
    private class XYCompare extends WrappedPoint {

        /** Constructs a comparable WrappedPoint that has the
         *  same (X,Y) position at P.*/
        public XYCompare(Point p) {
            super(p);
        }

        @Override
        public int hashCode() {
            Point p = this.getOrig();
            Double d1 = new Double(x(p));
            Double d2 = new Double(y(p));
            return d1.hashCode() * d2.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            WrappedPoint x = (WrappedPoint) obj;
            Point a = x.getOrig();
            Point b = this.getOrig();
            return ((x(a) == x(b)) && (y(a) == y(b)));
        }
    }

    /** Returns an iterator for QuadTrees. */
    private class QuadIterator implements Iterator {

        /** Returns an iterator over a QuadTree Q of type T, bounded
         *  by (XL,YL) and (XU, YU). Behavior depends on whether or
         *  or not the tree transitioned TRANS or is a LEAF.*/
        public QuadIterator(double xl, double yl,
                            double xu, double yu) {
            _inside = !((xl > _xupp) || (yl > _yupp)
                       || (xu < _xlow) || (yu < _ylow));
            _xl = Math.max(xl, _xlow);
            _yl = Math.max(yl, _ylow);
            _xu = Math.min(xu, _xupp);
            _yu = Math.min(yu, _yupp);
            if (_trans) {
                _q1 = _quad1.iterator(_xl, _yl, _xu, _yu);
                _q2 = _quad2.iterator(_xl, _yl, _xu, _yu);
                _q3 = _quad3.iterator(_xl, _yl, _xu, _yu);
                _q4 = _quad4.iterator(_xl, _yl, _xu, _yu);
            } else {
                _r = _root.iterator();
            }
            internalHasNext();
        }

        /** Returns an iterator over a QuadTree Q of type T,
         *  behavior will differ depending on if Q has
         *  transitioned TRANS - whether or not Q is a LEAF. */
        public QuadIterator(boolean trans) {
            this(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                 Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        /** Used so calls to 'hasNext()' will not unnecessarily advance
         *  the internal iterators of the HashSet. */
        public void internalHasNext() {
            if (_trans) {
                if (_q1.hasNext()) {
                    _hasNext = true;
                } else if (_q2.hasNext()) {
                    _hasNext = true;
                } else if (_q3.hasNext()) {
                    _hasNext = true;
                } else if (_q4.hasNext()) {
                    _hasNext = true;
                } else {
                    _hasNext = false;
                }
            } else {
                while (_r.hasNext() && _inside) {
                    _item = _r.next().getOrig();
                    if ((isWithin(x(_item), y(_item),
                                 _xl, _yl,
                                 _xu, _yu))) {
                        _hasNext = true;
                        return;
                    }
                }
                _hasNext = false;
                _item = null;
                return;
            }
        }

        @Override
        public boolean hasNext() {
            return _hasNext;
        }

        @Override
        public Point next() {
            Point result;
            if (_trans) {
                if (_q1.hasNext()) {
                    result = _q1.next();
                } else if (_q2.hasNext()) {
                    result = _q2.next();
                } else if (_q3.hasNext()) {
                    result = _q3.next();
                } else if (_q4.hasNext()) {
                    result = _q4.next();
                } else {
                    throw new NoSuchElementException();
                }
            } else {
                result = _item;
                internalHasNext();
                return result;
            }
            internalHasNext();
            return result;
        }

        @Override
        public void remove() {
        }

        /** Holds the QuadTree. */
        private QuadTree<Point> _q;

        /** Holds the iterator of quad 1. */
        private Iterator<Point> _q1;

        /** Holds the iterator of quad 2. */
        private Iterator<Point> _q2;

        /** Holds the iteratr of quad 3. */
        private Iterator<Point> _q3;

        /** Holds the iterator of quad 4. */
        private Iterator<Point> _q4;

        /** Holds the iterator of root. */
        private Iterator<WrappedPoint> _r;

        /** Is true if the bounds of the arguments are in this QuadTree. */
        private boolean _inside;

        /** Returns true if this Iterator has a next item. */
        private boolean _hasNext = true;


        /** Holds the lower bound of the X coordinate. */
        private double _xl;

        /** Holds the lower bound of the Y coordinate. */
        private double _yl;

        /** Holds the upper bound of the X coordinate. */
        private double _xu;

        /** Holds the upper bound of the Y coordinate. */
        private double _yu;

        /** Is the item this iterator is currently pointing to. */
        private Point _item;

        /** Is true if it is a leaf. */
        private boolean _leaf;
    }
}

