package cardRecognizer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import core.GraphicalBorderPanel;
import core.ImageHelper;
import core.JPanelPaintable;
import processing.core.PImage;

public class IdentifiedCardMasterBorderPanel extends GraphicalBorderPanel {
	
	JPanelPaintable rawIdentifiedCardPanel;
	JPanelPaintable identifiedCardPanel;
	JPanelPaintable identifiedCardMasterPanel;
	
	int identifiedCardWidth;
	int identifiedCardHeight;
	
	ImageIcon rawIdentifiedCardIcon;
	ImageIcon identifiedCardIcon;
	
	JLabel rawIdentifiedCardLabel;
	JLabel identifiedCardLabel;
		
	UpdateQuantityPanel updateQuantityPanel;
	FoilCheckboxPanel foilCheckboxPanel;	
	UndoMatchButtonPanel undoMatchButtonPanel;
	
	public CardRecognizer cardRecognizer;

	public IdentifiedCardMasterBorderPanel(String borderFolder, Color bgColor, CardRecognizer cardRecognizer) {
		super(borderFolder);

		rawIdentifiedCardPanel = new JPanelPaintable(bgColor, false);
		identifiedCardPanel = new JPanelPaintable(bgColor, false);
//		identifiedCardPanel.setSize(new Dimension(
//			identifiedCardWidth,
//			identifiedCardHeight
//		));
		
		rawIdentifiedCardIcon = new ImageIcon(ImageHelper.getBufferedImageWithAlphaChannelFromURL(CardRecognizer.cardBackImagePath));		
		rawIdentifiedCardLabel = new JLabel();
		rawIdentifiedCardLabel.setIcon(rawIdentifiedCardIcon);
		rawIdentifiedCardPanel.add(rawIdentifiedCardLabel);
		rawIdentifiedCardLabel.setVisible(true);
		rawIdentifiedCardPanel.setBackground(bgColor);
		
		identifiedCardIcon = new ImageIcon(ImageHelper.getBufferedImageWithAlphaChannelFromURL(CardRecognizer.cardBackImagePath));		
		identifiedCardLabel = new JLabel();
		identifiedCardLabel.setIcon(identifiedCardIcon);
		identifiedCardPanel.add(identifiedCardLabel);
		identifiedCardLabel.setVisible(true);
		identifiedCardPanel.setBackground(bgColor);
		
		updateQuantityPanel = new UpdateQuantityPanel(cardRecognizer);
		
		foilCheckboxPanel = new FoilCheckboxPanel("Foil?", cardRecognizer);
		
		undoMatchButtonPanel = new UndoMatchButtonPanel("No Match Present/Undo Last Match", cardRecognizer);
		
		identifiedCardMasterPanel = new JPanelPaintable(bgColor, false);
		
		//https://stackoverflow.com/questions/20114174/what-layout-accepts-percentages-instead-of-values-in-swing
		identifiedCardMasterPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets=new Insets(0,0,0,0);
        //gbc.weighty = 55;		
        identifiedCardMasterPanel.add(rawIdentifiedCardPanel, gbc);
		gbc.gridy = 1;
		identifiedCardMasterPanel.add(identifiedCardPanel, gbc);
		gbc.gridy = 2;
		//gbc.weighty = 15;
		identifiedCardMasterPanel.add(updateQuantityPanel, gbc);
		gbc.gridy = 3;
		//gbc.weighty = 15;
		identifiedCardMasterPanel.add(foilCheckboxPanel, gbc);
		gbc.gridy = 4;
		//gbc.weighty = 15;
		identifiedCardMasterPanel.add(undoMatchButtonPanel, gbc);
		
		buildPanel(identifiedCardMasterPanel);
		
//		add(foilCheckboxPanel);
//		add(undoMatchButtonPanel);
		
		//buildPanel(identifiedCardMasterPanel);
		
	}
	
	
	public void setRawImage(PImage image) {
		rawIdentifiedCardIcon = new ImageIcon(ImageHelper.pImageToBufferedImage(image));	
		rawIdentifiedCardLabel.setIcon(rawIdentifiedCardIcon);
		repaint();
	}
	
	public void setImage(String imagePath) {
		identifiedCardIcon = new ImageIcon(ImageHelper.getBufferedImageWithAlphaChannelFromURL(imagePath));			
		identifiedCardLabel.setIcon(identifiedCardIcon);
		repaint();
	}

}
