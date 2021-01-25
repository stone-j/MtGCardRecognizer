package cardRecognizer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import core.GraphicalBorderPanel;
import core.JPanelPaintable;
import processing.core.PImage;

public class MatchListMasterBorderPanel extends GraphicalBorderPanel {
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;
	JPanelPaintable matchListMasterPanel;
	ArrayList<MatchListItemPanel> matchListItemPanel;
	ArrayList<MatchListItemMouseListener> matchListItemMouseListener;
	MatchListPagingPanel matchListPagingPanel;
	
	public MatchListMasterBorderPanel(String borderFolder, Color bgColor, CardRecognizer myCardRecognizer, PagingHelper myPagingHelper) {
		super(borderFolder);
		cardRecognizer = myCardRecognizer;
		pagingHelper = myPagingHelper;
		
		matchListMasterPanel = new JPanelPaintable(bgColor, false);
		//matchListMasterPanel.setLayout(new GridLayout(11, 1));
		//matchListMasterPanel.setLayout(new BoxLayout(matchListMasterPanel, BoxLayout.Y_AXIS));
		matchListMasterPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		matchListMasterPanel.setTransparentAdd(true);
		//matchListMasterPanel.setPreferredSize(new Dimension(360, 450));
		//matchListMasterPanel.setMaximumSize(new Dimension(360, 1600));
		
		matchListItemPanel = new ArrayList<MatchListItemPanel>();
		matchListItemMouseListener = new ArrayList<MatchListItemMouseListener>();
		
		for(int i = 0; i < CardRecognizer.matchListItemsPerPage; i++) {
			if (pagingHelper.isFocusItem(i)) {
				matchListItemPanel.add(new MatchListItemPanel(new Color(60,134,174)));
			} else if (i % 2 == 0) {
				matchListItemPanel.add(new MatchListItemPanel(new Color(155,155,155)));
			} else {
				matchListItemPanel.add(new MatchListItemPanel(new Color(122,122,122)));
			}
			matchListItemMouseListener.add(new MatchListItemMouseListener(cardRecognizer, pagingHelper, i));
			matchListItemPanel.get(i).addMouseListener(matchListItemMouseListener.get(i));
			
			gbc.gridy = i;
			gbc.gridheight = 1;
	        gbc.gridwidth = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.anchor = GridBagConstraints.NORTHWEST;
	        
	        matchListMasterPanel.add(matchListItemPanel.get(i), gbc);
		}
		
		matchListPagingPanel = new MatchListPagingPanel(cardRecognizer, pagingHelper);
		
		gbc.gridy = CardRecognizer.matchListItemsPerPage;
		
		matchListMasterPanel.add(matchListPagingPanel, gbc);
		
		//matchListMasterPanel.add(Box.createVerticalGlue());
		
		buildPanel(matchListMasterPanel);
	}
	
	public void recolorItemPanels() {
		for(int i = 0; i < CardRecognizer.matchListItemsPerPage; i++) {
			if (pagingHelper.isFocusItem(i)) {
				matchListItemPanel.get(i).setPaint(new Color(60,134,174));
			} else if (i % 2 == 0) {
				matchListItemPanel.get(i).setPaint(new Color(155,155,155));
			} else {
				matchListItemPanel.get(i).setPaint(new Color(122,122,122));
			}
		}
	}

	public void setCardImage(PImage p, int index) {
		matchListItemPanel.get(index).setCardImage(p);
	}
	
	public void setCardName(String s, int index) {
		matchListItemPanel.get(index).setCardName(s);
	}
	
	public void setCardEdition(String s, int index) {
		matchListItemPanel.get(index).setCardEdition(s);
	}
	
	public String getCardEdition(int index) {
		return matchListItemPanel.get(index).getCardEdition();
	}
	
	public void setCardQty(String s, int index) {
		matchListItemPanel.get(index).setCardQty(s);
	}
	
	public void setCardId(String s, int index) {
		matchListItemPanel.get(index).setCardId(s);
	}
	
	public String getCardId(int index) {
		return matchListItemPanel.get(index).getCardId();
	}
	
	public void setStorageLocation(String s, int index) {
		matchListItemPanel.get(index).setStorageLocation(s);
	}
	
	public String getStorageLocation(int index) {
		return matchListItemPanel.get(index).getStorageLocation();
	}
	
	public void decrementCardQty(int index) {
		matchListItemPanel.get(index).decrementCardQty();
	}
	
	public void decrementCardQty(int index, int qty) {
		matchListItemPanel.get(index).decrementCardQty(qty);
	}
	
	public void incrementCardQty(int index) {
		matchListItemPanel.get(index).incrementCardQty();
	}
	
	public void incrementCardQty(int index, int qty) {
		matchListItemPanel.get(index).incrementCardQty(qty);
	}
}
