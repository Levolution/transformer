package transformer.gui;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import transformer.model.SelectionDetails;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */
public class ContrastDialog extends JDialog {
	private static final long serialVersionUID = -6033376370490330211L;

	private final JSlider contrastSlider;
	private final ContrastViewComponent contrastView;

	public ContrastDialog(Component parent, SelectionDetails selection) {
		setTitle("Contrast View");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new MigLayout());

		contrastView = new ContrastViewComponent(selection);
		add(contrastView, "grow, push, wrap");

		contrastSlider = new JSlider(-100, 100, 0);
		contrastSlider.setMajorTickSpacing(10);
		contrastSlider.setMinorTickSpacing(1);
		contrastSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				int contrast = contrastSlider.getValue();
				contrastView.updateContrast(contrast);
			}
		});
		add(contrastSlider, "growx");

		setSize(500, 500);
		setLocationRelativeTo(parent);

	}

	public static void showDialog(final Component parent,
			final SelectionDetails object23D) {
		new ContrastDialog(parent, object23D).setVisible(true);
	}
}
