package cardRecognizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import core.swingComponent.BorderMaker;

public class FoilCheckboxPanel extends JPanel {
	
	public CardRecognizer cardRecognizer;
	JCheckBox foilCheckbox;
	ActionListener foilCheckboxActionListener;
	
	FoilCheckboxPanel(String label, CardRecognizer myCardRecognizer) {
		super();
		cardRecognizer = myCardRecognizer;
		init(label);
	}

	public void init(String label) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(false);
		setPreferredSize(new Dimension(100, 40));
		setMaximumSize(new Dimension(360, 40));
		
		foilCheckbox = new JCheckBox("Foil?");
		foilCheckbox.setForeground(new Color(220,220,220));
		foilCheckbox.setBorder(new BorderMaker(BorderMaker.NONE, 0, 0));
		foilCheckbox.setOpaque(false);
		
		foilCheckboxActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				cardRecognizer.toggleFoilCheckbox();
			}
		};
		
		foilCheckbox.addActionListener(foilCheckboxActionListener);
		
		add(foilCheckbox);
	}
}
