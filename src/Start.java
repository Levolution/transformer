import java.awt.EventQueue;

import transformer.gui.MainFrame;

/**
 * This software has been released under the LGPL 3 license.
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * @author Levent Ak
 * 
 */

public class Start {
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
