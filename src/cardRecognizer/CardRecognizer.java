package cardRecognizer;

//----------------------------------------------
//https://github.com/sarxos/webcam-capture
//----------------------------------------------
import com.github.sarxos.webcam.Webcam;
//----------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
//import com.asprise.util.ocr.OCR;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PImage;
import processing.core.PVector;
import processing.core.PConstants;
import processing.data.Table;
import processing.data.TableRow;

import core.ImageHelper;
import core.JPanelPaintable;
import core.LookAndFeelHelper;
import core.MathHelper;
import core.ProcessingHelper;
import core.WebcamHelper;
import core.ColorHelper;
import core.Comparator_IntegerArray;
import core.ConsoleHelper;
import core.ExceptionLogger;
import core.FileHelper;
import core.FontHelper;
import core.GraphicalBorderPanel;
import core.AudioHelper;
import core.ArrayHelper;
import core.openCV.Contour;
import core.openCV.OpenCV;
import core.openCV.OpenCVHelper;
import core.openCV.Threshold;
import core.openCV.WebcamLabel;

/* FIXED: (root cause was calling garbage collection every loop.
See system.gc() in the run() method for details.)
CPU usage is way too high;
this program uses the same webcam parameters as the LegoRecognizer,
and runs at 1/10th the framerate. WHY?!?
Need to debug exactly where the CPU bottleneck is
It doesnt seem to be in the paint methods, but that could be a culprit.

response time when a paging button is clicked is too slow-- either implement predictive caching, or some sort of hourglass/wait symbology
*/

public class CardRecognizer {
	
	KeyEventManager keyEventManager;
	
	Color globalBGColor;
	Color menubarColor;
	
	String webcamName;
	int webcamCaptureWidth;
	int webcamCaptureHeight;
	int webcamDisplayWidth;
	int webcamDisplayHeight;
	BufferedImage webcamImage;
	
	int applicationWidth;
	int applicationHeight;
	
	XMLWorker xmlWorker = new XMLWorker(false, "");
	ConsoleHelper consoleHelper = new ConsoleHelper();
	LookAndFeelHelper lookAndFeelHelper = new LookAndFeelHelper();
	WebcamHelper webcamHelper = new WebcamHelper();
	ProcessingHelper processingHelper = new ProcessingHelper();
	ImageHelper imageHelper = new ImageHelper();
	FontHelper fontHelper = new FontHelper(false, "");
	
	int webcamXScaler;
	int webcamYScaler;
	int minOpenCVThreshold = 0;
	int maxOpenCVThreshold = 255;
	int defaultOpenCVThreshold = 128;
	int maximumThresholdMutation;
	int openCVMinRegionSizePercentage;
	int openCVMaxRegionSizePercentage;
	
	Font customFont;
	
	JPanel webcamPanel;
	ImageIcon webcamIcon;
	WebcamLabel webcamLabel;
	
	GraphicalBorderPanel webcamBorderPanel;
	
	int identifiedCardWidth;
	int identifiedCardHeight;
	
	WindowController windowController;
	
	JPanelPaintable matchListMasterPanel;
	
	//JLabel poop1;
	
	public String editionCode;
	public String cardSeq;
	public String cardName;
	//public String storageLocationId;
	public String updateTimestamp;
	
	WebHelper webHelper;
	
	String applicationName;
	
	private boolean foundCard = false;
	
	Comparator_IntegerArray comparator_IntegerArray;
	
	//CardUpdateQuantityTracker cardUpdateQuantityTracker;
	
	//------------------------------------------

	static final int cardWidth = 250;
	static final int cardHeight = 350;

	static final int stockCardWidth = 312;
	static final int stockCardHeight = 445;

	static final int subImageSegmentsX = 4;
	static final int subImageSegmentsY = 3;

	static final int programWidth = 1900;
	static final int programHeight = 720;

	static final int webcamWidth = 1280;
	static final int webcamHeight = 720;

	static final int cardArtX = 20;
	static final int cardArtY = 39;
	static final int cardArtWidth = 213;
	static final int cardArtHeight = 158;

	static final int cardNameX = 0;
	static final int cardNameY = 0;
	static final int cardNameWidth = 180;
	static final int cardNameHeight = 40;

	static final int objectMargin = 10;

	static final int totalHashes = 132;
	static final int uniqueHashTypes = 11;

	int debounceLoops;
	int cardRemovedDebounceLoops;
	int updateQuantities;

	String cardsImagePath;
	String cardBackImagePath;

	static final String soundFileFilename = System.getProperty("user.dir") + File.separator + "audio" + File.separator + "beep6.wav";
	static final String updateQtySoundFileFilename = System.getProperty("user.dir") + File.separator + "audio" + File.separator + "updateQty.wav";
	
	Integer[][] allCardHashes;
	Integer[][] compareResults;
	int[] cardHashesToCompare;

	int totalCards;

	Threshold threshold;
	int minThreshold = 0;
	int maxThreshold = 255;
	int defaultThreshold = 128;
	
	int cardIsPresentCounter = 0;
	int cardIsAbsentCounter = 0;
	boolean lockingOntoCard = false;

	//int selectedMatchIndex = 0;
	//int currentMatchPageIndex = 0;

	int[][] segmentDimensionValue;

	int[] currentRGBLValue;

	int[] greenOutlineColor = new int[] {11, 181, 19};
	int[] redOutlineColor = new int[] {166, 13, 20};

	boolean freezeSliderChangeEvents = true;
	boolean cardIsPresent = false;
	boolean cardIsPresentFlagStateChange = false;

	boolean checkForUpsideDownImages = false; //set this to TRUE if you want the system to check both orientations of the card (upside-down & rightside-up)

	boolean foilCheckboxState = false;

	String cardColorWhereClause;
	String setOfLastCard = "";
	String addCardImageHashToMatchFullURL;

	String[][] matchResultsDetails;
	PagingHelper pagingHelper;
	static final int matchListItemsPerPage = 10;

	PImage src;
	PImage card;
	PImage cardFlipped;
	PImage card2;
	PImage[] cardArtSubImage = new PImage[2];
	PImage CardNameSubImage;
	PImage stockCard;

	Contour contour;
	//Contour contour2;

	ArrayList<Contour> contours = new ArrayList<Contour>();
	ArrayList<PVector> pVector = new ArrayList<PVector>();

	Table cardHashTable;

	//Capture video;
	OpenCV opencv;

	File soundFile;
	File updateQtySoundFile;

	BufferedImage ocrBufImg;
	
	Webcam webcam;
	
	IdentifiedCardMasterBorderPanel identifiedCardMasterBorderPanel;
	MatchListMasterBorderPanel matchListMasterBorderPanel;
	
	ExceptionLogger exceptionLogger;


	public static void main (String[] args) {
		new CardRecognizer();
	}

	public CardRecognizer() {		
		init();
    	run();
	}	
	
	
	public void buildUIComponents() {
		
		//poop1 = new JLabel();
		
		//add JPanel to hold the webcam image and Contour outline
		webcamPanel = new JPanelPaintable(globalBGColor, false);
		webcamIcon = new ImageIcon((BufferedImage)ImageHelper.getScaledImage(webcamImage, webcamDisplayWidth, webcamDisplayHeight));
		webcamLabel = new WebcamLabel(
			webcamDisplayWidth,
			webcamDisplayHeight,
			webcamXScaler,
			webcamYScaler
		);
		webcamLabel.setIcon(webcamIcon);
		webcamPanel.setLayout(new BorderLayout());
		webcamPanel.add(webcamLabel, BorderLayout.LINE_START);
		webcamPanel.setBackground(globalBGColor);
		
		//webcamPanel.add(poop1, BorderLayout.LINE_END);
		
		webcamBorderPanel = new GraphicalBorderPanel(webcamPanel, GraphicalBorderPanel.PLAIN_PURPLE, false, "");
		
		identifiedCardMasterBorderPanel = new IdentifiedCardMasterBorderPanel(GraphicalBorderPanel.FRAME06, globalBGColor, this);

		matchListMasterBorderPanel = new MatchListMasterBorderPanel(GraphicalBorderPanel.FRAME06, globalBGColor, this, pagingHelper);
		
		windowController = new WindowController(
			applicationName,
			new Dimension(applicationWidth, applicationHeight)
		);
		
		//-------------------------------------------------------------------------
		//https://stackoverflow.com/questions/20114174/what-layout-accepts-percentages-instead-of-values-in-swing
		//-------------------------------------------------------------------------
		windowController.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = gbc.weighty = 70;
        windowController.add(webcamBorderPanel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 15;
		windowController.add(identifiedCardMasterBorderPanel, gbc);
		gbc.gridx = 2;
        //gbc.gridy = 0;
        //gbc.gridwidth = 1;
        //gbc.gridheight = 2;
        gbc.weightx = /*gbc.weighty = */ 15;
		windowController.add(matchListMasterBorderPanel, gbc);
		//-------------------------------------------------------------------------
		//-------------------------------------------------------------------------
		
//		//windowController.add(webcamPanel, BorderLayout.WEST);		
//		windowController.add(webcamBorderPanel, BorderLayout.LINE_START);
//		windowController.add(identifiedCardMasterBorderPanel, BorderLayout.CENTER);
//		windowController.add(matchListMasterBorderPanel, BorderLayout.LINE_END);
//		//windowController.add(infoMasterBorderPanel, BorderLayout.CENTER);
	}

	private void init() {
		//size(1900, 740);
		exceptionLogger = new ExceptionLogger(false, "");
		lookAndFeelHelper.setLookAndFeel("Metal");
		
		keyEventManager = new KeyEventManager(this);
		
		loadXMLValues();
		initializeVariables();
		
		FileHelper.DeleteAllFilesInDirectory(System.getProperty("user.dir") + File.separator + "match_audits");
		
		webcam = webcamHelper.getWebcam(webcamName, webcamCaptureWidth, webcamCaptureHeight, exceptionLogger);
		webcam.open(true); //set async = true so webcam does not block
		webcamImage = webcam.getImage();
		src = ImageHelper.bufferedImagetoPImage(webcamImage);
		opencv = new OpenCV(src);
	
		//WindowController windowController = new WindowController("Test Frame");
		buildUIComponents();

		//cardArtSubImage[] = new PImage[2];

		//https://processing.org/reference/loadTable_.html
		//this resolves to F:\My Documents\Dropbox\Workspace\MtGCardRecognizer\data\imageHashes.csv
		cardHashTable = processingHelper.loadTable(System.getProperty("user.dir") + "\\data\\imageHashes.csv", "csv");

		totalCards = cardHashTable.getRowCount();

		consoleHelper.PrintMessage("total cards = " + Integer.toString(totalCards));

		allCardHashes = new Integer[totalCards][totalHashes + 1]; //index 0 is cardid, index 1-84 are the hash values
		cardHashesToCompare = new int[totalHashes];
		compareResults = new Integer[totalCards * (checkForUpsideDownImages ? 2 : 1)][2]; //CardId, matchValue (totalCards * 2 is so that we can compare both rightside-up images, and upside-down images)

		//populate card hash array using the table data
		int rowIndex = 0;

		//populate for rightside-up images
		for (TableRow row : cardHashTable.rows()) {
			for (int i = 0; i < cardHashTable.getColumnCount(); i ++) {
				allCardHashes[rowIndex][i] = row.getInt(i);
			}
			rowIndex++;
		}
		
		//remove the last updated record from the db, in case the user
		//tries to "undo last match" before the first card is registered.
		webHelper.clearLastUpdate();
		
		//threshold = new Threshold(minThreshold, maxThreshold, defaultThreshold, 5);

		soundFile = new File(soundFileFilename);
		updateQtySoundFile = new File(updateQtySoundFileFilename);

		card = ProcessingHelper.createImage(cardWidth, cardHeight, PConstants.ARGB);
		cardFlipped = ProcessingHelper.createImage(cardWidth, cardHeight, PConstants.ARGB);
		card2 = ProcessingHelper.createImage(cardWidth, cardHeight, PConstants.ARGB);

		stockCard = processingHelper.loadImage(cardBackImagePath);
	}
	
	
	public void initializeVariables() {

		threshold = new Threshold(minOpenCVThreshold, maxOpenCVThreshold, defaultOpenCVThreshold, maximumThresholdMutation);
		
		webHelper = new WebHelper(xmlWorker);
		
		comparator_IntegerArray = new Comparator_IntegerArray(1); //the value here is the column you want to sort by
		
		pagingHelper = new PagingHelper(matchListItemsPerPage);

	}
	
	
	private boolean cardLockOn() {
		
		boolean hasLockOn = foundCard;
		
		cardIsPresentFlagStateChange = false;
		
		if (
			opencv.findNSidedPolygon(
				4,
				webcamCaptureWidth * webcamCaptureHeight * openCVMinRegionSizePercentage / 100, 
				webcamCaptureWidth * webcamCaptureHeight * openCVMaxRegionSizePercentage / 100
			)
		) {
			cardIsPresentCounter++;
			lockingOntoCard = true;
			//threshold.addViableValue();
		} else {
			cardIsAbsentCounter++;
			lockingOntoCard = false;
			//threshold.addNonViableValue();
			//if (--waitLoops < 0) waitLoops = 0;
		}
		
		if (cardIsPresentCounter > debounceLoops && !cardIsPresent) {
			hasLockOn = true;
			cardIsPresent = true;
			cardIsPresentFlagStateChange = true;
			cardIsPresentCounter = cardIsAbsentCounter = 0;
			//webcamLabel.showContour(true);
			webcamLabel.foundContour(true);
			threshold.addViableValue();
			//waitLoops = initialWaitLoops;
			//webcamLabel.showContour(false);
		}		

		//added the "* 2" to avoid unintentional unlocks
		if (cardIsAbsentCounter > cardRemovedDebounceLoops && cardIsPresent) {
			hasLockOn = false;
			cardIsPresent = false;
			cardIsPresentFlagStateChange = true;
			cardIsPresentCounter = cardIsAbsentCounter = 0;
			//webcamLabel.showContour(false);
			webcamLabel.foundContour(false);
			threshold.addNonViableValue();
		} 
		
		return hasLockOn;
	}
//			card = OpenCVHelper.getWarpPerspectivePImage(
//				opencv,
//				card,
//				ProcessingHelper.RotationCorrection(opencv.identifiedPolygon),
//				cardWidth,
//				cardHeight
//			);
	
	public void saveMatchedImage() {
		//save the composite image here, creating a pImage from the file path in the line above, and using the PImage variable "card".
		PImage matchedImg = ImageHelper.bufferedImagetoPImage(imageHelper.getBufferedImageWithAlphaChannelFromURL(cardsImagePath + editionCode.replace("con", "cfx").replace("CON", "cfx") + "/" + cardSeq + ".jpg"));
		
		matchedImg.resize(0, card.height);
		PImage compositeMatches = new PImage(card.width + matchedImg.width, MathHelper.getMaximumValue(card.height, matchedImg.height), PConstants.ARGB);
		compositeMatches.set(0, 0, card);
		compositeMatches.set(card.width, 0, matchedImg);
		compositeMatches.save(System.getProperty("user.dir") + File.separator + "match_audits" + File.separator + updateTimestamp + ".png");
	}
	
	public void deleteMatchedImage() {
		File fileToDelete = new File(System.getProperty("user.dir") + File.separator + "match_audits" + File.separator + updateTimestamp + ".png");
		fileToDelete.delete();
	}

	private void run() {while (true) {
				
		//this puts the image on screen-- need to find a pure java alternative
//		this.image(src, 0, 0);
//
//		image(
//				stockCard,
//				webcamWidth + objectMargin,
//				objectMargin
//				);
		
		webcamImage = webcam.getImage();
		webcamIcon.setImage((BufferedImage)ImageHelper.getScaledImage(
			webcamImage,
			webcamDisplayWidth,
			webcamDisplayHeight
		));
		
		//windowController.repaint(); //TODO: Can we change this to only repaint the webcam window?
		webcamPanel.repaint();
		//identifiedCardMasterBorderPanel.repaint();
		
		src = ImageHelper.bufferedImagetoPImage(webcamImage);

		opencv.loadImage(src);
		opencv.invert();
		//if (!lockingOntoCard) {
			opencv.threshold(threshold.getBestPotentialThresholdValue(foundCard));
		//}
		
		foundCard = cardLockOn();
		
		if(foundCard && cardIsPresentFlagStateChange) {
			
			cardIsPresentFlagStateChange = false;
			
			//System.out.println("cardIsPresentCounter = " + Integer.toString(cardIsPresentCounter));
			
			card = OpenCVHelper.getWarpPerspectivePImage(
				opencv,
				card,
				ProcessingHelper.RotationCorrection(opencv.identifiedPolygon),
				cardWidth,
				cardHeight
			);
			
//			cardFlipped = ImageHelper.RotateImage180(card.get());
//			card2 = ImageHelper.MakeTransparentRegion(card, cardArtX, cardArtY, cardArtWidth, cardArtHeight);
			
			cardArtSubImage[0] = card.get(
				cardArtX,
				cardArtY,
				cardArtWidth,
				cardArtHeight
			);
			
			analyzeImage();
			
			identifiedCardMasterBorderPanel.setRawImage(card);
			identifiedCardMasterBorderPanel.setImage(cardsImagePath + editionCode.replace("con", "cfx").replace("CON", "cfx") + "/" + cardSeq + ".jpg");
			
			if (updateQuantities != 0) {
				saveMatchedImage();
			}
						
			pagingHelper.setFocusItem(0);
			DrawMatchListContents();
			AudioHelper.PlayAudioFile(soundFile);


//	
//			cardArtSubImage[1] = cardFlipped.get(
//					cardArtX,
//					cardArtY,
//					cardArtWidth,
//					cardArtHeight
//					);
//	
//			CardNameSubImage = card.get(
//					cardNameX,
//					cardNameY,
//					cardNameWidth,
//					cardNameHeight
//					);
		} else if(!foundCard && cardIsPresentFlagStateChange) {
			
			cardIsPresentFlagStateChange = false;
			identifiedCardMasterBorderPanel.setImage(cardBackImagePath);
			//stockCard = ProcessingHelper.loadImage(cardBackImagePath);
		}     

		//added this to attempt to avoid a random null pointer exception upon startup
		if(opencv.contour != null) {
			webcamLabel.setPoints(opencv.contour.getPoints());
		}
		
		//Calling garbage collection every loop cost each loop ~500ms and an additional 50% cpu
		//System.gc();
		
	}}
	
	private void analyzeImage() {
		for (int p = 0; p < (checkForUpsideDownImages ? 2 : 1); p++) { //loops once for rightside-up image, and again for upside-down image

			segmentDimensionValue = new int[subImageSegmentsY * subImageSegmentsX][8]; // Second dimension has 4 buckets: R, G, B, L

			PImage subImage;

			/*
			break the image into sub images, sequenced like this:
			1  4  7  10
			2  5  8  11
			3  6  9  12
			 */ 

			int subImageWidth = cardArtSubImage[p].width / subImageSegmentsX + 1;
			int subImageHeight = cardArtSubImage[p].height / subImageSegmentsY + 1;

			//get the average color values from each art segment
			for (int x = 0; x < subImageSegmentsX; x++) {
				for (int y = 0; y < subImageSegmentsY; y++) {

					int startXCoord = cardArtSubImage[p].width / subImageSegmentsX * x + 1;
					int startYCoord = cardArtSubImage[p].height / subImageSegmentsY * y + 1;

					subImage = cardArtSubImage[p].get(startXCoord, startYCoord, subImageWidth, subImageHeight);

					subImage.loadPixels();

					currentRGBLValue = ColorHelper.GetAverageAndStdevRGBLValues(subImage);

					ArrayHelper.arrayCopy(currentRGBLValue, 0, segmentDimensionValue[subImageSegmentsY * x + y], 0, currentRGBLValue.length);
				}
			}

			//-------------------------------------------------------------------------------
			// DONE: CALCULATE THE "HASHES TO MATCH" LOCALLY RATHER THAN SENDING TO WEBSERVER
			//-------------------------------------------------------------------------------

			//0=red, 1=green, 2=blue, 3=lum, 4=redstdev, 5=greenstdev, 6=bluestdev, 7=lumstdev
			int[] sums = new int[8];
			double[] avgs = new double[8];
			double[] stdevs = new double[8];

			//sum up values for the avg calculation
			for (int i = 0; i < segmentDimensionValue.length; i++) {
				for (int j = 0; j < segmentDimensionValue[i].length; j++) {
					sums[j] += segmentDimensionValue[i][j];
				}
			}

			//calculate avg
			for (int i = 0; i < sums.length; i++) {
				avgs[i] = (double)sums[i] / segmentDimensionValue.length;
			}

			//sum up values for the stdev calculation
			for (int i = 0; i < segmentDimensionValue.length; i++) {
				for (int j = 0; j < segmentDimensionValue[i].length; j++) {
					stdevs[j] += Math.pow((segmentDimensionValue[i][j] - avgs[j]), 2);
				}
			}

			//calculate stdev
			for (int i = 0; i < stdevs.length; i++) {
				//stdevs[i] = (int)Math.sqrt(stdevs[i] / segmentDimensionValue.length);
				stdevs[i] = (int)Math.sqrt(stdevs[i] / segmentDimensionValue.length);
			}

			//zero out cardHashesToCompare array
			for (int i = 0; i < totalHashes; i++) {
				cardHashesToCompare[i] = 0;
			}

			//build final cardHashesToCompare array
			for (int i = 0; i < uniqueHashTypes; i++) {
				for (int j = 0; j < segmentDimensionValue.length; j++) {
					for (int k = 0; k < segmentDimensionValue.length; k++) {

						if (j != k) {
							switch (i) {
							case 0: //red
								if (segmentDimensionValue[k][0] > segmentDimensionValue[j][0]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][0] - segmentDimensionValue[j][0]) > stdevs[0]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 1: //green
								if (segmentDimensionValue[k][1] > segmentDimensionValue[j][1]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] +=(int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][1] - segmentDimensionValue[j][1]) > stdevs[1]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 2: //blue
								if (segmentDimensionValue[k][2] > segmentDimensionValue[j][2]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][2] - segmentDimensionValue[j][2]) > stdevs[2]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 3: //lum
								if (segmentDimensionValue[k][3] > segmentDimensionValue[j][3]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][3] - segmentDimensionValue[j][3]) > stdevs[3]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 4: //red/green
								if (segmentDimensionValue[k][0] > segmentDimensionValue[j][1]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][0] - segmentDimensionValue[j][1]) > (stdevs[0] + stdevs[1]) / 2) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 5: //red/blue
								if (segmentDimensionValue[k][0] > segmentDimensionValue[j][2]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][0] - segmentDimensionValue[j][2]) > (stdevs[0] + stdevs[2]) / 2) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 6: //green/blue
								if (segmentDimensionValue[k][1] > segmentDimensionValue[j][2]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][1] - segmentDimensionValue[j][2]) > (stdevs[1] + stdevs[2]) / 2) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 7: //red stdev
								if (segmentDimensionValue[k][4] > segmentDimensionValue[j][4]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][4] - segmentDimensionValue[j][4]) > stdevs[4]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 8: //green stdev
								if (segmentDimensionValue[k][5] > segmentDimensionValue[j][5]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][5] - segmentDimensionValue[j][5]) > stdevs[5]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 9: //blue stdev
								if (segmentDimensionValue[k][6] > segmentDimensionValue[j][6]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][6] - segmentDimensionValue[j][6]) > stdevs[6]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;
							case 10: //lum stdev
								if (segmentDimensionValue[k][7] > segmentDimensionValue[j][7]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, (k > j ? k - 1 : k) * 2);
								}
								if (Math.abs(segmentDimensionValue[k][7] - segmentDimensionValue[j][7]) > stdevs[7]) {
									cardHashesToCompare[(i * segmentDimensionValue.length) + j] += (int)Math.pow(2, ((k > j ? k - 1 : k) * 2) + 1);
								}
								break;								
							}
						}
					}
				}
			}

			consoleHelper.PrintMessage("cardHashesToCompare.length = " + Integer.toString(cardHashesToCompare.length));

			for (int i = 0 + p * totalCards; i < (p + 1) * totalCards; i++) {		
				compareResults[i][0] = allCardHashes[i % totalCards][0]; //set the CardId
				compareResults[i][1] = 0;

				for (int j = 0; j < cardHashesToCompare.length; j++) { //start at index 1, since index 0 is the cardid
					compareResults[i][1] += (Integer.bitCount(allCardHashes[i % totalCards][j + 1] ^ cardHashesToCompare[j]));
				}			
			}
		}


		//sort the matches array by lowest match value
		Arrays.sort(compareResults, comparator_IntegerArray); //the value here is the column you want to sort by

		//---------------------------------------------------------------------
		// this block of code is a candidate for a function
		//---------------------------------------------------------------------

		matchResultsDetails = new String[matchListItemsPerPage][8]; //10 containers for [Set, Seq, Name, Qty]

		pagingHelper.setCurrentPageIndex(0);

		//fetch card info for the top 10 matches
		for (int i = 0; i < matchListItemsPerPage; i++) {
			
			int cardId = compareResults[i + (matchListItemsPerPage * pagingHelper.getCurrentPageIndex())][0];
			matchResultsDetails[i] = webHelper.getMatchResults(i, cardId);

			//uncomment this line to see the match scores
			//matchResultsDetails[i][3] = matchResultsDetails[i][3] + " (" + Integer.toString(compareResults[i][1]) + ")";
		}

		editionCode 		= matchResultsDetails[0][0];
		cardSeq 			= matchResultsDetails[0][1];
		cardName			= matchResultsDetails[0][2];
		//storageLocationId 	= matchResultsDetails[0][6];

		//try to find cards with the same name as the #1 match, that match the "last set" used
		//i.e. if we're cataloging all 4th edition cards, then assume that the next card is also from 4th edition.
		for (int i = 1; i < matchListItemsPerPage; i++) { //i=1 because we're skipping the first match

			if (cardName.equals(matchResultsDetails[i][2]) && setOfLastCard.equals(matchResultsDetails[i][0])) {
				//set the card's match score to 1 less than the top match
				compareResults[i + (matchListItemsPerPage * pagingHelper.getCurrentPageIndex())][1] = compareResults[0 + (matchListItemsPerPage * pagingHelper.getCurrentPageIndex())][1] - 1;
				//then re-sort the match list
				Arrays.sort(compareResults, comparator_IntegerArray);
			}
		}

//		//update the quantity of the matched card automatically
//		byte updateCardQtyRequest[] = ProcessingHelper.loadBytes	(
//				updateQtyURL +
//				"cardId=" + Integer.toString(compareResults[0][0]) +
//				"&isFoil=0" +
//				"&undoLastUpdate=0"
//				);
		
		int cardId = compareResults[0][0];
		pagingHelper.setCardId(cardId);
		pagingHelper.setQtyOfLastUpdate(1);
		pagingHelper.setFocusItem(0);
		consoleHelper.PrintMessage("pagingHelper.getFocusItem()[0] = " + pagingHelper.getFocusItem()[0]);
		consoleHelper.PrintMessage("pagingHelper.getCurrentPageIndex() = " + pagingHelper.getCurrentPageIndex());

		if (updateQuantities != 0) {
			updateTimestamp = webHelper.incrementQty(Integer.toString(cardId), false)[0];
		}

		//----------------------------------------------------------------------------------
		//then, repeat the process to retrieve card info for the reordered result set
		//----------------------------------------------------------------------------------
		for (int i = 0; i < matchListItemsPerPage; i++) {

//			//println(getCardInfoURL + "cardId=" + Integer.toString(compareResults[i + (10 * currentMatchPageIndex)][0]));
//
//			byte sendWebRequestSecondPass[] = ProcessingHelper.loadBytes	(
//					getCardInfoURL +
//					"cardId=" + Integer.toString(compareResults[i + (10 * currentMatchPageIndex)][0])
//					);
//
//			String matchResultsRawSecondPass = ArrayHelper.ByteArrayToString(sendWebRequestSecondPass, 2);
//
//			//println("matchResultsRaw = " + matchResultsRaw);
//			matchResultsDetails[i] = matchResultsRawSecondPass.split("\\@");
//
//			//uncomment this line to see the match scores
//			//matchResultsDetails[i][3] = matchResultsDetails[i][3] + " (" + Integer.toString(compareResults[i][1]) + ")";
			
			cardId = compareResults[i + (matchListItemsPerPage * pagingHelper.getCurrentPageIndex())][0];
			matchResultsDetails[i] = webHelper.getMatchResults(i, cardId);
		}

		editionCode 		= matchResultsDetails[0][0];
		cardSeq 			= matchResultsDetails[0][1];
		cardName			= matchResultsDetails[0][2];
		//storageLocationId 	= matchResultsDetails[0][6];

		setOfLastCard = editionCode;
		consoleHelper.PrintMessage("setOfLastCard = " + setOfLastCard);
		//----------------------------------------------------------------------------------
		//----------------------------------------------------------------------------------

	}


//	//TURNED THESE OFF SO PROGRAM WOULD COMPILE -JS
//	public void Threshold(float theValue) {
//		if (!freezeSliderChangeEvents) {
//			threshold = (int)(theValue);
//		}
//	}
//
//
//	public void DrawThresholdControl(int xPos, int yPos, String label) {
//
//		cp5 = new ControlP5(this);
//		PFont fontBig = createFont("Verdana", 14, true);
//		PFont fontSmall = createFont("Verdana", 12, true); 
//		cp5.setFont(fontSmall);
//
//		text(
//				label,
//				xPos,
//				yPos + 20
//				);
//
//		sliderThreshold = cp5.addSlider(
//				label,
//				0,
//				255,
//				threshold,
//				xPos + 79,
//				yPos,
//				200,
//				30
//				)
//				.setCaptionLabel("")
//				.setDecimalPrecision(0)
//				.setColorBackground(color(120))
//				.setColorForeground(color(90))
//				.setColorActive(color(60,80,80));
//
//		freezeSliderChangeEvents = false;
//	}


	void DrawMatchListContents() {

		for (int r = 0; r < matchResultsDetails.length; r++) { 

			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][0] = " + matchResultsDetails[r][0]);
			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][1] = " + matchResultsDetails[r][1]);
			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][2] = " + matchResultsDetails[r][2]);
			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][3] = " + matchResultsDetails[r][3]);
			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][4] = " + matchResultsDetails[r][4]);
			 consoleHelper.PrintMessage("matchResultsDetails[" + Integer.toString(r) + "][7] = " + matchResultsDetails[r][7]);

			PImage myCard = processingHelper.loadImage(cardsImagePath + matchResultsDetails[r][0].replace("con", "cfx").replace("CON", "cfx") + "/" + matchResultsDetails[r][1] + ".jpg");
			PImage myCardCrop = new PImage(259, 196);
			if (myCard != null) {
				myCardCrop = myCard.get(25, 46, 259, 196);
				myCardCrop.resize(0, 60);
			}
			
			
			matchListMasterBorderPanel.setCardName(matchResultsDetails[r][2], r);
			matchListMasterBorderPanel.setCardEdition(matchResultsDetails[r][0], r);
			matchListMasterBorderPanel.setCardQty(matchResultsDetails[r][3], r);
			matchListMasterBorderPanel.setCardId(matchResultsDetails[r][4], r);
			matchListMasterBorderPanel.setStorageLocation(matchResultsDetails[r][7], r);
			matchListMasterBorderPanel.setCardImage(myCardCrop, r);
			
			matchListMasterBorderPanel.recolorItemPanels();
		}
	}
	
	
	public void undoMatch() {
		consoleHelper.PrintMessage("I have registered a match cancel button!");

		if (pagingHelper.focusItemExistsOnCurrentPage() && pagingHelper.getFocusItem()[1] != -1) {
			matchResultsDetails[0][3] = Integer.toString(Integer.parseInt(matchResultsDetails[0][3]) - 1);
		}
		
		if (updateQuantities != 0) {
			deleteMatchedImage();
		}

		pagingHelper.setFocusItem(-1);
		DrawMatchListContents();

//		stockCard = ProcessingHelper.loadImage(cardBackImagePath);
//		image(
//				stockCard,
//				webcamWidth + objectMargin,
//				objectMargin
//				);
		
		if (updateQuantities != 0) {
			webHelper.undoLastUpdate();
		}
	}
	
	
	public void toggleFoilCheckbox() {

		if (pagingHelper.getFocusItem()[1] != -1) {
			foilCheckboxState = !foilCheckboxState;
			
			webHelper.incrementQtyAndUndoLastUpdate(
				Integer.toString(pagingHelper.getCardId()),
				foilCheckboxState,
				pagingHelper.getQtyOfLastUpdate()
			);
		}
	}
	
	public void populateMatchListItems() {
		matchResultsDetails = new String[matchListItemsPerPage][8];

		//fetch card info for the top 10 matches
		for (int i = 0; i < matchListItemsPerPage; i++) {

			int cardId = compareResults[i + (matchListItemsPerPage * pagingHelper.getCurrentPageIndex())][0];
			matchResultsDetails[i] = webHelper.getMatchResults(i, cardId);
		}

		//pagingHelper.setFocusItem(-1);
		DrawMatchListContents();
	}
	
	
	public void loadXMLValues() {
		//---------------------------------------------------
		//----LOAD XML VALUES INTO VARIABLES-----------------
		//---------------------------------------------------
		
		cardsImagePath = xmlWorker.GetURLFromXml("cardsImagePath");
		cardBackImagePath = xmlWorker.GetURLFromXml("cardBackImagePath");
		
		applicationName = xmlWorker.GetDataFromXml("applicationWindow.width");;
		
		applicationWidth = xmlWorker.GetIntFromXml("applicationWindow.width");
		applicationHeight = xmlWorker.GetIntFromXml("applicationWindow.height");
		
		globalBGColor = new Color(
			xmlWorker.GetIntFromXml("globalBGColor.red"),
			xmlWorker.GetIntFromXml("globalBGColor.green"),
			xmlWorker.GetIntFromXml("globalBGColor.blue")
		);
		menubarColor = new Color(
			xmlWorker.GetIntFromXml("menubarColor.red"),
			xmlWorker.GetIntFromXml("menubarColor.green"),
			xmlWorker.GetIntFromXml("menubarColor.blue")
		);
		
		webcamName = xmlWorker.GetDataFromXml("webcam.name");
		webcamCaptureWidth = xmlWorker.GetIntFromXml("webcam.captureWidth");
		webcamCaptureHeight = xmlWorker.GetIntFromXml("webcam.captureHeight");
		webcamDisplayWidth = xmlWorker.GetIntFromXml("webcam.displayWidth");
		webcamDisplayHeight = xmlWorker.GetIntFromXml("webcam.displayHeight");
		webcamXScaler = webcamDisplayWidth / webcamCaptureWidth;
		webcamYScaler = webcamDisplayHeight / webcamCaptureHeight;
		openCVMinRegionSizePercentage = xmlWorker.GetIntFromXml("webcam.opencv.identifiedRegion.minRegionSizePercentage");
		openCVMaxRegionSizePercentage = xmlWorker.GetIntFromXml("webcam.opencv.identifiedRegion.maxRegionSizePercentage");
		maximumThresholdMutation = xmlWorker.GetIntFromXml("webcam.opencv.threshold.maximumMutation");
		
		identifiedCardWidth = xmlWorker.GetIntFromXml("identifiedCard.width");
		identifiedCardHeight = xmlWorker.GetIntFromXml("identifiedCard.height");
		
		debounceLoops = xmlWorker.GetIntFromXml("debounceLoops");
		cardRemovedDebounceLoops = xmlWorker.GetIntFromXml("cardRemovedDebounceLoops");
		
		customFont = fontHelper.loadFont(
			xmlWorker.GetDataFromXml("customFont.fileName"),
			xmlWorker.GetIntFromXml("customFont.size"),
			exceptionLogger,
			false,
			""
		);
		
		updateQuantities = xmlWorker.GetIntFromXml("updateQuantities");
		
		xmlWorker = null;
	}
}
