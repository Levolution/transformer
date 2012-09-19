package transformer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;
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
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 937694705300160334L;

	private final SelectionDetails loadedObject = new SelectionDetails();

	private JList<Polygon> polygonList;
	private JList<Point> pointList;

	private DefaultListModel<Polygon> polygonModel;
	private DefaultListModel<Point> pointModel;

	private JButton removePolygonButton;
	private JButton addPointButton;
	private JButton removePointButton;

	private Painter2DComponent painterPanel;

	private JTextField imageFileField;

	public MainFrame() {
		setTitle("Image Manipulator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setContentPane(create2DPanel());

		setSize(800, 600);
		setLocationByPlatform(true);
	}

	private Container create2DPanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));

		panel.add(createImagePanel(), "growx, spanx 2, wrap");
		panel.add(createPainter2DPanel(), "grow, push, spany 3");
		panel.add(createPolygonPanel(), "grow, width 250px, height 50%, wrap");
		panel.add(createPointPanel(), "grow, width 250px, height 50%, wrap");
		panel.add(createPainter3DPanel(), "grow");

		return panel;
	}

	private Component createPainter3DPanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder("Contrast View"));

		panel.add(new JButton(new Show3DViewAction()), "span, split, tag ok");

		return panel;
	}

	private Component createImagePanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder("Background image"));

		imageFileField = new JTextField();
		imageFileField.setEnabled(false);
		panel.add(imageFileField, "grow, push");

		panel.add(new JButton(new SelectImageAction()), "grow");

		return panel;
	}

	private class SelectImageAction extends AbstractAction {
		private static final long serialVersionUID = 362536956189691421L;

		private static final String IMAGE_FOLDER = "./img";

		public SelectImageAction() {
			super("Select image...");
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final JFileChooser imageChooser = new JFileChooser(IMAGE_FOLDER);
			imageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			imageChooser.setFileFilter(new ImageFileFilter());

			final int answer = imageChooser.showOpenDialog(MainFrame.this);
			if (answer != JFileChooser.APPROVE_OPTION) {
				return;
			}

			final File selectedFile = imageChooser.getSelectedFile();
			BufferedImage image;
			try {
				image = ImageIO.read(selectedFile);
			} catch (final IOException e) {
				JOptionPane.showMessageDialog(MainFrame.this,
						"Could not load image. Please select a valid image.");
				return;
			}

			imageFileField.setText(selectedFile.getAbsolutePath());
			loadedObject.setImage(image);

			painterPanel.repaint();

		}
	}

	private static class ImageFileFilter extends FileFilter {
		private static final String[] ACCEPTED_ENDINGS = { "jpg", "jpeg",
				"png", "gif" };

		@Override
		public String getDescription() {
			final StringBuilder builder = new StringBuilder("Image file (*.")
					.append(ACCEPTED_ENDINGS[0]);

			for (int i = 1; i < ACCEPTED_ENDINGS.length; i++) {
				builder.append(", *.").append(ACCEPTED_ENDINGS[i]);
			}
			builder.append(")");

			return builder.toString();
		}

		@Override
		public boolean accept(final File f) {
			if (f == null || !f.isFile()) {
				return false;
			}

			final String name = f.getName().toLowerCase();

			for (final String ending : ACCEPTED_ENDINGS) {
				if (name.endsWith("." + ending)) {
					return true;
				}
			}

			return false;
		}
	}

	private Component createPainter2DPanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder("Area Selection View"));

		panel.add(painterPanel = new Painter2DComponent(), "grow, push");

		return panel;
	}

	private class Show3DViewAction extends AbstractAction {
		private static final long serialVersionUID = 2422012323441768430L;

		public Show3DViewAction() {
			super("Show contrast image...");
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			ContrastDialog.showDialog(MainFrame.this, loadedObject);
		}
	}

	private class Painter2DComponent extends JComponent {
		private static final long serialVersionUID = -9176608239867078015L;

		public Painter2DComponent() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent event) {
					final int x = event.getX();
					final int y = event.getY();

					final Point point = new Point(x, y);

					addPoint(point);
				}
			});
		}

		@Override
		protected void paintComponent(final Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());

			if (loadedObject.getImage() != null) {
				g.drawImage(loadedObject.getImage(), 0, 0, null);
			}

			final int selectedIndex = polygonList.getSelectedIndex();
			Polygon selectedPolygon = null;
			if (selectedIndex != -1
					&& polygonList.getSelectedIndices().length == 1) {
				selectedPolygon = polygonModel.getElementAt(selectedIndex);
			}

			for (final Polygon polygon : loadedObject.getPolygons()) {
				if (selectedPolygon != null && polygon == selectedPolygon) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(Color.WHITE);
				}

				final List<Point> points = polygon.getPoints();
				for (int i = 0; i < points.size(); i++) {
					final Point from = points.get(i);
					final Point to = points.get((i + 1) % points.size());

					g.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
				}
			}

		}
	}

	private Component createPolygonPanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder("Polygons"));

		polygonModel = new DefaultListModel<Polygon>();
		polygonList = new JList<Polygon>(polygonModel);
		polygonList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent event) {
				pointModel.removeAllElements();

				final int[] selectedIndices = polygonList.getSelectedIndices();

				if (selectedIndices.length == 0) {
					removePolygonButton.setEnabled(false);
					addPointButton.setEnabled(false);
				} else if (selectedIndices.length == 1) {
					removePolygonButton.setEnabled(true);

					final int selectedIndex = selectedIndices[0];
					final Polygon polygon = polygonModel.get(selectedIndex);
					for (final Point point : polygon.getPoints()) {
						pointModel.addElement(point);
					}

					addPointButton.setEnabled(true);
				} else {
					removePolygonButton.setEnabled(true);
					addPointButton.setEnabled(false);
				}

				painterPanel.repaint();
			}
		});
		panel.add(new JScrollPane(polygonList), "grow, push, wrap");
		panel.add(new JButton(new NewPolygonAction()), "span, split 2, tag ok");
		panel.add(removePolygonButton = new JButton(new RemovePolygonAction()),
				"tag cancel");

		return panel;
	}

	private class NewPolygonAction extends AbstractAction {
		private static final long serialVersionUID = 6759308443402206439L;

		public NewPolygonAction() {
			super("New...");
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final String name = JOptionPane.showInputDialog(MainFrame.this,
					"Name?", "polygon" + System.currentTimeMillis());

			if (name == null) {
				return;
			}

			if (name.trim().isEmpty()) {
				JOptionPane.showMessageDialog(MainFrame.this,
						"Polygon name must not be empty.");
				return;
			}

			final Polygon polygon = new Polygon(name);

			loadedObject.getPolygons().add(polygon);
			polygonModel.addElement(polygon);
			polygonList.setSelectedIndex(polygonModel.getSize() - 1);

			painterPanel.repaint();
		}
	}

	private class RemovePolygonAction extends AbstractAction {
		private static final long serialVersionUID = -6760201735837465568L;

		public RemovePolygonAction() {
			super("Remove");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final int[] selectedIndices = polygonList.getSelectedIndices();

			final int reply = JOptionPane.showConfirmDialog(MainFrame.this,
					"Do you want to remove " + selectedIndices.length
							+ " Polygon(s)?");

			if (reply != JOptionPane.YES_OPTION) {
				return;
			}

			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				final int selectedIndex = selectedIndices[i];
				polygonModel.remove(selectedIndex);
				loadedObject.getPolygons().remove(selectedIndex);
			}

			painterPanel.repaint();
		}
	}

	private Component createPointPanel() {
		final JPanel panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder("Points"));

		pointModel = new DefaultListModel<Point>();
		pointList = new JList<Point>(pointModel);
		pointList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent event) {
				final int[] selectedIndices = pointList.getSelectedIndices();
				removePointButton.setEnabled(selectedIndices.length > 0);
			}
		});
		panel.add(new JScrollPane(pointList), "grow, push, wrap");
		panel.add(addPointButton = new JButton(new AddPointAction()),
				"span, split 2, tag ok");
		panel.add(removePointButton = new JButton(new RemovePointAction()),
				"tag cancel");

		return panel;
	}

	private void addPoint(final Point point) {
		final int selectedIndex = polygonList.getSelectedIndex();

		if (selectedIndex == -1) {
			return;
		}

		final Polygon polygon = polygonModel.getElementAt(selectedIndex);
		polygon.add(point);
		if (polygon.hasIntersectingLines()) {
			polygon.remove(point);
			return;
		}

		pointModel.addElement(point);
		painterPanel.repaint();
	}

	private class AddPointAction extends AbstractAction {
		private static final long serialVersionUID = 5094635708495867841L;

		public AddPointAction() {
			super("Add...");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final Point point = PointDialog.createPoint(MainFrame.this);

			if (point == null) {
				return;
			}

			addPoint(point);
		}
	}

	private class RemovePointAction extends AbstractAction {
		private static final long serialVersionUID = -4404647665957440318L;

		public RemovePointAction() {
			super("Remove");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final int[] selectedIndices = pointList.getSelectedIndices();

			final int reply = JOptionPane.showConfirmDialog(MainFrame.this,
					"Do you want to remove " + selectedIndices.length
							+ " Point(s)?");

			if (reply != JOptionPane.YES_OPTION) {
				return;
			}

			final int selectedPolygonIndex = polygonList.getSelectedIndex();
			final Polygon polygon = polygonModel
					.getElementAt(selectedPolygonIndex);

			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				final int selectedIndex = selectedIndices[i];
				pointModel.remove(selectedIndex);
				polygon.getPoints().remove(selectedIndex);
			}

			painterPanel.repaint();
		}
	}
}
