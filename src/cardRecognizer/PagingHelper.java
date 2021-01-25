package cardRecognizer;

public class PagingHelper extends core.PagingHelper {	

	private int cardId;
	private int qtyOfLastUpdate;

	public PagingHelper(int myItemsPerPage) {
		super(myItemsPerPage);
	}
	
	public void setCardId(int myCardId) {
		cardId = myCardId;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public void setQtyOfLastUpdate(int qty) {
		qtyOfLastUpdate = qty;
	}
	
	public int getQtyOfLastUpdate() {
		return qtyOfLastUpdate;
	}

}
