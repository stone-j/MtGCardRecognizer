package cardRecognizer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import core.helper.AudioHelper;
import core.logging.ConsoleHelper;

public class UpdateQuantityMouseListener implements MouseListener {
	
	private int qty;
	
	ConsoleHelper consoleHelper = new ConsoleHelper();
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;
	
	UpdateQuantityMouseListener(int myQty, CardRecognizer myCardRecognizer) {
		qty = myQty;
		cardRecognizer = myCardRecognizer;
		//pagingHelper = myPagingHelper;
		pagingHelper = myCardRecognizer.pagingHelper;
	}
	
	public void UpdateQuantity() {
		consoleHelper.PrintMessage("I have registered a click on qty " + qty + " button");
		consoleHelper.PrintMessage("pagingHelper.getFocusItem()[0] = " + pagingHelper.getFocusItem()[0]);
		consoleHelper.PrintMessage("pagingHelper.getCurrentPageIndex() = " + pagingHelper.getCurrentPageIndex());

		if (pagingHelper.getCardId() != 0) {
			//ConsoleHelper.PrintMessage("pagingHelper.getCardId() = " + pagingHelper.getCardId());
			if (pagingHelper.getFocusItem()[0] == pagingHelper.getCurrentPageIndex()) {
				cardRecognizer.matchListMasterBorderPanel.decrementCardQty(pagingHelper.getFocusItem()[1], pagingHelper.getQtyOfLastUpdate());
				cardRecognizer.matchListMasterBorderPanel.incrementCardQty(pagingHelper.getFocusItem()[1], qty);
			}
			
			pagingHelper.setQtyOfLastUpdate(qty);
			
			Thread t = new Thread(new Runnable() {
			    public void run() {
			    	cardRecognizer.webHelper.incrementQtyAndUndoLastUpdate(Integer.toString(pagingHelper.getCardId()), cardRecognizer.foilCheckboxState, qty);
			    }
			});
			t.start();
			
			AudioHelper.PlayAudioFile(cardRecognizer.updateQtySoundFile);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		UpdateQuantity();
	} 
	public void mouseEntered(MouseEvent e) { } 
	public void mouseExited(MouseEvent e) { } 
	public void mousePressed(MouseEvent e) { } 
	public void mouseReleased(MouseEvent e) { } 
}
