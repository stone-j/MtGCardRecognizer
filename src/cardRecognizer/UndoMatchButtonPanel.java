package cardRecognizer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UndoMatchButtonPanel extends JPanel {
	
	public CardRecognizer cardRecognizer;
	
	UndoMatchButtonPanel(String label, CardRecognizer myCardRecognizer) {
		super();
		cardRecognizer = myCardRecognizer;
		init(label);
	}

	public void init(String label) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(false);
		setPreferredSize(new Dimension(100, 40));
		setMaximumSize(new Dimension(360, 40));
		
		final JButton undoMatchButton = new JButton(label);
		
		ActionListener undoMatchActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				cardRecognizer.undoMatch();
			}
		};
		
		undoMatchButton.addActionListener(undoMatchActionListener);
		
		add(undoMatchButton);
	}
}
