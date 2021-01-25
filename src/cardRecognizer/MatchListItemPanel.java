package cardRecognizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import core.ImageHelper;
import core.JPanelPaintable;
import processing.core.PImage;

public class MatchListItemPanel extends JPanelPaintable {
	
	JPanelPaintable cardPanel;
	JPanelPaintable textPanel;
		
	JLabel cardText;
	JLabel cardImage;
	
	String cardNameText;
	String cardEditionText;
	String cardQtyText;
	String cardId;
	String storageLocation;
	String storageLocationId;
	
	int cardQty;

	public MatchListItemPanel(Paint painter) {
		super(painter);
		init();		
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(360, 70));
		//setMaximumSize(new Dimension(360, 70));
		
		cardPanel = new JPanelPaintable();
		cardPanel.setOpaque(false);
		cardPanel.setPreferredSize(new Dimension(80, 60));
		//cardPanel.setMaximumSize(new Dimension(80, 60));
		cardPanel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		cardImage = new JLabel();
		
		cardPanel.add(cardImage);
		
		textPanel = new JPanelPaintable();
		textPanel.setOpaque(false);
		textPanel.setPreferredSize(new Dimension(260, 60));
		//textPanel.setMaximumSize(new Dimension(260, 60));
		//textPanel.setLayout(new GridLayout(1, 1));
		
		textPanel.setBorder(new EmptyBorder(6, 12, 0, 0));
		
		//cardNamePanel = new JPanelPaintable();		
		//cardEditionPanel = new JPanelPaintable();		
		//cardQtyPanel = new JPanelPaintable();
		
		cardText = new JLabel("Card name goes here");
		//cardEdition = new JLabel("Card edition goes here");
		//cardQty = new JLabel("Card qty goes here");
		
		cardText.setVerticalAlignment(SwingConstants.TOP);
		//cardQty.setVerticalAlignment(SwingConstants.TOP);
		
		//cardNamePanel.add(cardName);
		//cardEditionPanel.add(cardEdition);
		//cardQtyPanel.add(cardQty);
	
		textPanel.add(cardText);
		//textPanel.add(cardEditionPanel);
		//textPanel.add(cardQtyPanel);
		
		this.add(cardPanel, BorderLayout.LINE_START);
		this.add(textPanel, BorderLayout.CENTER);
	}
	
	public void setCardImage(PImage p) {
		cardImage.setIcon(new ImageIcon(ImageHelper.pImageToBufferedImage(p)));
	}
	
	public void setCardName(String s) {
		cardNameText = s;
		updateText();
	}
	
	public void setCardEdition(String s) {
		cardEditionText = s;
		updateText();
	}
	
	public String getCardEdition() {
		return cardEditionText;
	}
	
	public void setCardQty(String s) {
		cardQty = Integer.parseInt(s);
		cardQtyText = s;
		updateText();
	}
	
	public void setCardQty(int i) {
		cardQty = i;
		cardQtyText = Integer.toString(cardQty);
		updateText();
	}
	
	public void decrementCardQty() {
		decrementCardQty(1);
	}
	
	public void decrementCardQty(int qty) {
		cardQty -= qty;
		cardQtyText = Integer.toString(cardQty);
		updateText();
	}
	
	public void incrementCardQty() {
		incrementCardQty(1);
	}
	
	public void incrementCardQty(int qty) {
		cardQty += qty;
		cardQtyText = Integer.toString(cardQty);
		updateText();
	}
	
	public void setCardId(String s) {
		cardId = s;
	}
	
	public String getCardId() {
		return cardId;
	}
	
	public void setStorageLocation(String s) {
		storageLocation = s;
		updateText();
	}
	
	public String getStorageLocation() {
		return storageLocation;
	}
	
	public void updateText() {
		cardText.setText(
			"<html>" +
			cardNameText + "<br/>" +
			cardEditionText + "<br/>" +
			"Qty: " + cardQtyText + " (" + storageLocation + ")" +
			"</html>"
		);
	}
}
