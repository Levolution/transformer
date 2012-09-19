package transformer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */
public class Polygon {
	private final String name;
	private final List<Point> points;

	public Polygon(final String name) {
		this.name = name;
		this.points = new ArrayList<Point>();
	}

	public void add(final Point p) {
		points.add(p);
	}

	public void remove(final Point p) {
		points.remove(p);
	}

	public List<Point> getPoints() {
		return points;
	}

	public boolean hasIntersectingLines() {
		for (int i = 0; i < points.size(); i++) {
			final Point fromLine1 = points.get(i);
			final Point toLine1 = points.get((i + 1) % points.size());

			for (int j = 0; j < points.size(); j++) {
				final Point fromLine2 = points.get(j);
				final Point toLine2 = points.get((j + 1) % points.size());

				if (fromLine1 == fromLine2 && toLine1 == toLine2) {
					continue;
				}

				if (linesIntersect(fromLine1, toLine1, fromLine2, toLine2)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean linesIntersect(final Point fromLine1, final Point toLine1,
			final Point fromLine2, final Point toLine2) {
		// TODO Implement.
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

}
