package cardRecognizer;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class WindowController extends JFrame {

	public WindowController(String frameTitle, Dimension size) {
		super(frameTitle);
		//this.setPreferredSize (new Dimension(900, 600));
		//this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//this.setLayout(new BorderLayout());
		this.setPreferredSize(size);
		this.pack();
		this.setVisible(true);
	}
}

