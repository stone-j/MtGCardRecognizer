package cardRecognizer;

import core.ArrayHelper;
import core.ProcessingHelper;

public class WebHelper {
	String getCardInfoURL;
	String updateQtyURL;
	String clearLastUpdateURL;

	//constructor
	WebHelper(XMLWorker xmlWorker) {
		getCardInfoURL = xmlWorker.GetURLFromXml("getCardInfoURL");
		updateQtyURL = xmlWorker.GetURLFromXml("updateQtyURL");
		clearLastUpdateURL = xmlWorker.GetURLFromXml("clearLastUpdateURL");
	}
	
	public String[] getMatchResults (int index, int cardId) {

		byte sendWebRequest4[] = ProcessingHelper.loadBytes	(
				getCardInfoURL +
				"cardId=" + Integer.toString(cardId)
				);

		String matchResultsRaw = ArrayHelper.ByteArrayToString(sendWebRequest4, 2);

		//println("matchResultsRaw = " + matchResultsRaw);
		return matchResultsRaw.split("\\@");
	}
	
	public String[] incrementQty(String cardId, boolean isFoil) {
		return incrementQty(cardId, isFoil, 1, false);
	}
	
	public String[] incrementQty(String cardId, boolean isFoil, int qty) {
		return incrementQty(cardId, isFoil, qty, false);
	}
	
	//this is used when the foil checkbox is toggled,
	//or an alternate item in the match list is clicked
	public String[] incrementQtyAndUndoLastUpdate(String cardId, boolean isFoil, int qty) {
		return incrementQty(cardId, isFoil, qty, true);
	}

	public String[] incrementQtyAndUndoLastUpdate(String cardId, boolean isFoil) {		
		return incrementQty(cardId, isFoil, 1, true);
	}
	
	public String[] incrementQty(String cardId, boolean isFoil, int qty, boolean undoLastUpdate) {
		
		byte sendWebRequest[] = ProcessingHelper.loadBytes	(
				updateQtyURL +
				"cardId=" + cardId +
				"&isFoil=" + (isFoil ? "1" : "0") + 
				"&undoLastUpdate=" + (undoLastUpdate ? "1" : "0") +
				"&qty=" + qty);

		String matchResultsRaw = ArrayHelper.ByteArrayToString(sendWebRequest, 2);

		return matchResultsRaw.split("\\@");
	}
	
	public void undoLastUpdate() {
		
		ProcessingHelper.loadBytes (
			updateQtyURL +
			"cardId=-1" +
			"&isFoil=0" +
			"&undoLastUpdate=1" +
			"&qty=0" +
			"&storageLocationId=0"
		);	
	}
	
	public void clearLastUpdate() {
		ProcessingHelper.loadBytes(clearLastUpdateURL);			
	}
}
