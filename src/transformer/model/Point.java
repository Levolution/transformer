package transformer.model;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */
public class Point {
	private final int x;
	private final int y;

	public Point(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(x=" + x + ", y=" + y + ")";
	}
}
