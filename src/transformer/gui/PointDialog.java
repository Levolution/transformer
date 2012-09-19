package transformer.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import transformer.model.Point;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */
public class PointDialog extends JDialog {
	private static final long serialVersionUID = -2697397623677311159L;

	private Point point;

	private JTextField xTextField;

	private JTextField yTextField;

	private PointDialog(final Component parent) {
		setTitle("Add point");
		setModal(true);

		setLayout(new MigLayout("fill", "[][]"));

		add(new JLabel("X"));
		add(xTextField = new JTextField(), "wrap, grow");

		add(new JLabel("Y"));
		add(yTextField = new JTextField(), "wrap, grow");

		add(new JButton(new SaveAction()), "tag, span, split 2, tag ok");
		add(new JButton(new CancelAction()), "tag cancel");

		pack();
		setLocationRelativeTo(parent);
	}

	private class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 3634637344166279964L;

		public SaveAction() {
			super("Save");
		}

		@Override
		public void actionPerformed(final ActionEvent event) {

			int x;
			try {
				x = Integer.parseInt(xTextField.getText());
			} catch (final NumberFormatException e) {
				JOptionPane.showMessageDialog(PointDialog.this,
						"X must be an integer number.");
				return;
			}

			if (x < 0) {
				JOptionPane.showMessageDialog(PointDialog.this,
						"X must not be negative.");
				return;
			}

			int y;
			try {
				y = Integer.parseInt(yTextField.getText());
			} catch (final NumberFormatException e) {
				JOptionPane.showMessageDialog(PointDialog.this,
						"Y must be an integer number.");
				return;
			}

			if (y < 0) {
				JOptionPane.showMessageDialog(PointDialog.this,
						"Y must not be negative.");
				return;
			}

			point = new Point(x, y);

			setVisible(false);
		}
	}

	private class CancelAction extends AbstractAction {
		private static final long serialVersionUID = -3025370525206235706L;

		public CancelAction() {
			super("Cancel");
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			setVisible(false);
		}
	}

	public static Point createPoint(final Component parent) {
		final PointDialog dialog = new PointDialog(parent);
		dialog.setVisible(true);
		return dialog.point;
	}
}
