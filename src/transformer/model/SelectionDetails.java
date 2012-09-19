package transformer.model;

import java.awt.image.BufferedImage;
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
public class SelectionDetails {
	private BufferedImage image;
	private final List<Polygon> polygons = new ArrayList<Polygon>();

	public List<Polygon> getPolygons() {
		return polygons;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(final BufferedImage image) {
		this.image = image;
	}
}
