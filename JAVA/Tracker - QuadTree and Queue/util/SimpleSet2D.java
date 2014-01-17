/** A simple Set2D implementation.  This is too slow for large sets of
 *  data.  */

package util;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** A Set2D implemented as a simple list of POINTS (undivided).
 *  @author P. N. Hilfinger
 */
public class SimpleSet2D<Point> extends Set2D<Point> {

    /** An empty set of Points that are viewed with VIEW. */
    public SimpleSet2D(PointView<Point> view) {
        super(view);
    }

    /* PUBLIC METHODS.  See Set2D.java for documentation */

    @Override
    public int size() {
        return _points.size();
    }

    @Override
    public void add(Point p) {
        if (!contains(p)) {
            _points.add(p);
        }
    }

    @Override
    public void remove(Point p) {
        for (int i = 0; i < _points.size(); i += 1) {
            if (getView().equals(_points.get(i), p)) {
                _points.remove(i);
            }
        }
    }

    @Override
    public boolean contains(Point p) {
        for (Point q : _points) {
            if (getView().equals(p, q)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Point> iterator() {
        return _points.iterator();
    }

    @Override
    public Iterator<Point> iterator(double xl, double yl,
                                    double xu, double yu) {
        return new BBoxIterator(xl, yl, xu, yu);
    }

    /** Inner class supporting iterators over bounded sets of points. */
    private class BBoxIterator implements Iterator<Point> {
        /** Underlying iterator over _points. */
        private Iterator<Point> _iter = _points.iterator();

        /** A new iterator over the bounding box (XL,YL) to (XU,YU). */
        BBoxIterator(double xl, double yl, double xu, double yu) {
            _xl = xl;
            _yl = yl;
            _xu = xu;
            _yu = yu;
            _next = null;
        }

        @Override
        public boolean hasNext() {
            while (_next == null && _iter.hasNext()) {
                _next = _iter.next();
                if (!Set2D.isWithin(x(_next), y(_next), _xl, _yl, _xu, _yu)) {
                    _next = null;
                }
            }
            return _next != null;
        }

        @Override
        public Point next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Point v = _next;
            _next = null;
            return v;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Bounds of my bounding box. */
        private double _xl, _yl, _xu, _yu;
        /** Next point to deliver, or null if not yet fetched from
         *  _points. */
        private Point _next;
    }

    /** The points in this set. */
    private final ArrayList<Point> _points = new ArrayList<Point>();

}


