package cardRecognizer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core.AudioHelper;
import core.ConsoleHelper;
import core.ProcessingHelper;
import processing.core.PImage;

//https://coderanch.com/t/341446/java/detect-click-panel
public class MatchListItemMouseListener implements MouseListener {
	
	ConsoleHelper consoleHelper = new ConsoleHelper();
	
	CardRecognizer cardRecognizer;
	PagingHelper pagingHelper;
	int index;
	
	MatchListItemMouseListener(CardRecognizer myCardRecognizer, PagingHelper myPagingHelper, int myIndex) {
		cardRecognizer = myCardRecognizer;
		pagingHelper = myPagingHelper;
		index = myIndex;
	}
	
	public void mouseClicked(MouseEvent e) {
		consoleHelper.PrintMessage("I have registered a click in card slot " + index + " !!!");
		
		cardRecognizer.deleteMatchedImage();
		
		cardRecognizer.editionCode 			= cardRecognizer.matchResultsDetails[index][0];
		cardRecognizer.cardSeq 				= cardRecognizer.matchResultsDetails[index][1];
		cardRecognizer.cardName				= cardRecognizer.matchResultsDetails[index][2];
		//cardRecognizer.storageLocationId	= cardRecognizer.matchResultsDetails[index][6];
		
		cardRecognizer.saveMatchedImage();
		
		//cardRecognizer.selectedMatchIndex = index;
		
		cardRecognizer.matchListMasterBorderPanel.decrementCardQty(pagingHelper.getFocusItem()[1], pagingHelper.getQtyOfLastUpdate());
		pagingHelper.setFocusItem(index);
		cardRecognizer.matchListMasterBorderPanel.incrementCardQty(index);
		
		Object source = e.getSource();
		
		if (source instanceof MatchListItemPanel) {
			pagingHelper.setCardId(Integer.parseInt(((MatchListItemPanel)source).getCardId()));
		}
		pagingHelper.setQtyOfLastUpdate(1);		
		
		cardRecognizer.setOfLastCard = cardRecognizer.matchListMasterBorderPanel.getCardEdition(index);
		
		cardRecognizer.matchListMasterBorderPanel.recolorItemPanels();
		
		AudioHelper.PlayAudioFile(cardRecognizer.updateQtySoundFile);
		
		Thread t = new Thread(new Runnable() {
		    public void run() {
		    	cardRecognizer.identifiedCardMasterBorderPanel.setImage(cardRecognizer.cardsImagePath + cardRecognizer.editionCode.replace("con", "cfx").replace("CON", "cfx") + "/" + cardRecognizer.cardSeq + ".jpg");
		    	cardRecognizer.webHelper.incrementQtyAndUndoLastUpdate(cardRecognizer.matchListMasterBorderPanel.getCardId(index), cardRecognizer.foilCheckboxState);
		    }
		});
		t.start();
		
		//cardRecognizer.identifiedCardMasterBorderPanel.setImage(CardRecognizer.cardsImagePath + cardRecognizer.editionCode + "/" + cardRecognizer.cardSeq + ".jpg");
	} 
	public void mouseEntered(MouseEvent e) { } 
	public void mouseExited(MouseEvent e) { } 
	public void mousePressed(MouseEvent e) { } 
	public void mouseReleased(MouseEvent e) { } 
}
