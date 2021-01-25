package cardRecognizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import core.JPanelPaintable;
import core.PagingHelper;

public class MatchListPagingPanel extends JPanelPaintable {
	
	static final int UP = 0;
	static final int DOWN = 1;
	
	JPanelPaintable pageUpPanel;
	JPanelPaintable pageDownPanel;	
		
	JLabel pageUpLabel;
	JLabel pageDownLabel;
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;

	public MatchListPagingPanel(CardRecognizer myCardRecognizer, PagingHelper myPagingHelper) {
		super();
		cardRecognizer = myCardRecognizer;
		pagingHelper = myPagingHelper;
		init();
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		setOpaque(false);
		//setPreferredSize(new Dimension(360, 70));
		//setMaximumSize(new Dimension(360, 70));
		
		pageUpPanel = new JPanelPaintable(new Color(102,106,153));
		pageUpPanel.setOpaque(false);
		pageUpPanel.setPreferredSize(new Dimension(120, 40));
		//pageUpPanel.setMaximumSize(new Dimension(80, 60));
		pageUpPanel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		pageDownPanel = new JPanelPaintable(new Color(102,106,153));
		pageDownPanel.setOpaque(false);
		pageDownPanel.setPreferredSize(new Dimension(120, 40));
		//pageDownPanel.setMaximumSize(new Dimension(80, 60));
		pageDownPanel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		pageUpLabel 	= new JLabel("Previous Page", SwingConstants.CENTER);
		pageDownLabel 	= new JLabel("Next Page", SwingConstants.CENTER);
		
		pageUpPanel.add(pageUpLabel);
		pageDownPanel.add(pageDownLabel);
		
		pageUpPanel.addMouseListener(new PagingButtonMouseListener(UP, cardRecognizer, pagingHelper));
		pageDownPanel.addMouseListener(new PagingButtonMouseListener(DOWN, cardRecognizer, pagingHelper));
		
//		cardImage = new JLabel();
//		
//		cardPanel.add(cardImage);
//		
//		textPanel = new JPanelPaintable();
//		textPanel.setOpaque(false);
//		textPanel.setPreferredSize(new Dimension(260, 60));
//		textPanel.setMaximumSize(new Dimension(260, 60));
//		//textPanel.setLayout(new GridLayout(1, 1));
//		
//		textPanel.setBorder(new EmptyBorder(6, 12, 0, 0));
//		
//		//cardNamePanel = new JPanelPaintable();		
//		//cardEditionPanel = new JPanelPaintable();		
//		//cardQtyPanel = new JPanelPaintable();
//		
//		cardText = new JLabel("Card name goes here");
//		//cardEdition = new JLabel("Card edition goes here");
//		//cardQty = new JLabel("Card qty goes here");
//		
//		cardText.setVerticalAlignment(SwingConstants.TOP);
//		//cardQty.setVerticalAlignment(SwingConstants.TOP);
//		
//		//cardNamePanel.add(cardName);
//		//cardEditionPanel.add(cardEdition);
//		//cardQtyPanel.add(cardQty);
//	
//		textPanel.add(cardText);
//		//textPanel.add(cardEditionPanel);
//		//textPanel.add(cardQtyPanel);
		
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = gbc.gridy = 0;
//        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(pageUpPanel, BorderLayout.LINE_START);
		
//        gbc.anchor = GridBagConstraints.NORTHEAST;
//        gbc.gridx = 4;
//        gbc.gridwidth = 1;
		this.add(pageDownPanel, BorderLayout.LINE_END);
		
//		this.add(pageUpPanel, BorderLayout.LINE_START);
//		this.add(pageDownPanel, BorderLayout.LINE_END);
	}
}
