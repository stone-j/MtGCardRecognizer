package cardRecognizer;

import java.awt.event.KeyEvent;

import core.helper.AudioHelper;

public class KeyEventManager extends core.event.KeyEventManager {
	
	CardRecognizer cardRecognizer;
	
	public KeyEventManager(CardRecognizer myCardRecognizer) {
		super();
		this.cardRecognizer = myCardRecognizer;
	}
	
	@Override
	//This fires whenever a key is pressed
	public void KeyCodeAction(int keyCode) {
		int qty = 0;
		//ConsoleHelper.PrintMessage(keyCode);
		switch (keyCode) {
			case KeyEvent.VK_1:
			case KeyEvent.VK_NUMPAD1:
				qty = 1;
				break;
			case KeyEvent.VK_2:
			case KeyEvent.VK_NUMPAD2:
				qty = 2;
				break;
			case KeyEvent.VK_3:
			case KeyEvent.VK_NUMPAD3:
				qty = 3;
				break;
			case KeyEvent.VK_4:
			case KeyEvent.VK_NUMPAD4:
				qty = 4;
				break;	
		}
		
		if (qty > 0) {		
			UpdateQuantityMouseListener updateQuantityMouseListener = new UpdateQuantityMouseListener(qty, cardRecognizer);
			updateQuantityMouseListener.UpdateQuantity();
		}
	}

}
