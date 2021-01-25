package cardRecognizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import core.JPanelPaintable;

public class UpdateQuantityPanel extends JPanelPaintable {
	
	static final int UP = 0;
	static final int DOWN = 1;
	
	JPanelPaintable qty1Panel;
	JPanelPaintable qty2Panel;
	JPanelPaintable qty3Panel;
	JPanelPaintable qty4Panel;
		
	JLabel qty1Label;
	JLabel qty2Label;
	JLabel qty3Label;
	JLabel qty4Label;
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;

	public UpdateQuantityPanel(CardRecognizer myCardRecognizer) {
		super();
		cardRecognizer = myCardRecognizer;
		//pagingHelper = myPagingHelper;
		init();		
	}
	
	public void init() {
		this.setLayout(new GridLayout(1, 4));
		setOpaque(false);
		setPreferredSize(new Dimension(260, 30));
		setMaximumSize(new Dimension(260, 30));
		
		qty1Panel = new JPanelPaintable(new Color(155,155,155));
		qty1Panel.setOpaque(false);
		qty1Panel.setSize(new Dimension(40, 30));
		qty1Panel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		qty2Panel = new JPanelPaintable(new Color(122,122,122));
		qty2Panel.setOpaque(false);
		qty2Panel.setSize(new Dimension(40, 30));
		qty2Panel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		qty3Panel = new JPanelPaintable(new Color(155,155,155));
		qty3Panel.setOpaque(false);
		qty3Panel.setSize(new Dimension(40, 30));
		qty3Panel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		qty4Panel = new JPanelPaintable(new Color(122,122,122));
		qty4Panel.setOpaque(false);
		qty4Panel.setSize(new Dimension(40, 30));
		qty4Panel.setBorder(new EmptyBorder(0, 6, 0, 0));
		
		qty1Panel.addMouseListener(new UpdateQuantityMouseListener(1, cardRecognizer));
		qty2Panel.addMouseListener(new UpdateQuantityMouseListener(2, cardRecognizer));
		qty3Panel.addMouseListener(new UpdateQuantityMouseListener(3, cardRecognizer));
		qty4Panel.addMouseListener(new UpdateQuantityMouseListener(4, cardRecognizer));
		
		qty1Label = new JLabel("1", SwingConstants.CENTER);
		qty2Label = new JLabel("2", SwingConstants.CENTER);
		qty3Label = new JLabel("3", SwingConstants.CENTER);
		qty4Label = new JLabel("4", SwingConstants.CENTER);
		
		qty1Panel.add(qty1Label);
		qty2Panel.add(qty2Label);
		qty3Panel.add(qty3Label);
		qty4Panel.add(qty4Label);
		
		this.add(qty1Panel);
		this.add(qty2Panel);
		this.add(qty3Panel);
		this.add(qty4Panel);
	}
}
