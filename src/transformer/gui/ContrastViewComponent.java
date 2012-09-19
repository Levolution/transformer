package transformer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;

import javax.swing.JComponent;

import transformer.model.Point;
import transformer.model.Polygon;
import transformer.model.SelectionDetails;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */
public class ContrastViewComponent extends JComponent {
	private static final long serialVersionUID = -4794693476823864998L;

	private int contrast;
	private final SelectionDetails selection;

	public ContrastViewComponent(SelectionDetails selection) {
		this.selection = selection;
	}

	@Override
	protected void paintComponent(Graphics g) {
		BufferedImage copy = new BufferedImage(selection.getImage().getWidth(),
				selection.getImage().getHeight(), selection.getImage()
						.getType());

		BufferedImage polygonImage = createPolygonImage();
		BufferedImage contrastImage = createContrastImage();

		for (int y = 0; y < polygonImage.getHeight(); y++) {
			for (int x = 0; x < polygonImage.getWidth(); x++) {
				int rgb;
				if (polygonImage.getRGB(x, y) == Color.GREEN.getRGB()) {
					rgb = contrastImage.getRGB(x, y);
				} else {
					rgb = selection.getImage().getRGB(x, y);
				}

				copy.setRGB(x, y, rgb);
			}
		}

		g.drawImage(copy, 0, 0, null);
	}

	private BufferedImage createPolygonImage() {
		BufferedImage image = new BufferedImage(
				selection.getImage().getWidth(), selection.getImage()
						.getHeight(), selection.getImage().getType());
		Graphics g = image.getGraphics();

		g.setColor(Color.GREEN);
		for (Polygon polygon : selection.getPolygons()) {
			List<Point> points = polygon.getPoints();

			if (points.isEmpty()) {
				continue;
			}

			int[] xPoints = new int[points.size() + 1];
			int[] yPoints = new int[points.size() + 1];

			for (int i = 0; i < points.size() + 1; i++) {
				Point point = points.get(i % points.size());

				xPoints[i] = point.getX();
				yPoints[i] = point.getY();
			}

			g.fillPolygon(xPoints, yPoints, points.size() + 1);
		}

		return image;
	}

	private BufferedImage createContrastImage() {
		BufferedImage output = new BufferedImage(selection.getImage()
				.getWidth(), selection.getImage().getHeight(), selection
				.getImage().getType());

		RescaleOp op = new RescaleOp(1, contrast, null);
		op.filter(selection.getImage(), output);

		return output;
	}

	public void updateContrast(int contrast) {
		this.contrast = contrast;
		repaint();
	}
}
