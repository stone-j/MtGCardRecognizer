package cardRecognizer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core.ConsoleHelper;
import core.PagingHelper;

public class PagingButtonMouseListener implements MouseListener {
	
	static final int UP = 0;
	static final int DOWN = 1;
	
	private int pagingDirection;
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;
	
	PagingButtonMouseListener(int myPagingDirection, CardRecognizer myCardRecognizer, PagingHelper myPagingHelper) {
		pagingDirection = myPagingDirection;
		cardRecognizer = myCardRecognizer;
		pagingHelper = myPagingHelper;
	}
	
	public void mouseClicked(MouseEvent e) {
		ConsoleHelper.PrintMessage("I have registered a click on the page " + (pagingDirection == 0 ? "Up" : "Down") + " button");

		if (pagingDirection == UP) {
			//bail out of the page doesn't change
			//("previous page" button is clicked when on the first page)
			if (!pagingHelper.previousPage()) {
				return;
			};
		} else if (pagingDirection == DOWN) {
			pagingHelper.nextPage();
		}
		
		cardRecognizer.populateMatchListItems();
	} 
	public void mouseEntered(MouseEvent e) { } 
	public void mouseExited(MouseEvent e) { } 
	public void mousePressed(MouseEvent e) { } 
	public void mouseReleased(MouseEvent e) { } 
}
