import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RMI_Main extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/**Width and height of squares */
	private static final int SQUARE_DIMEN = 80;

	/**The max and min numbers to be used for the 1st question (change if not difficult enough)*/ 
	private static final int MIN_STARTING_NUM = 1; private static final int MAX_STARTING_NUM = 60;

	/**How long the game will go for*/
	private static final double GAME_LENGTH = 60.0;

	/**Maximum answer at each step of the two step equations*/
	private static final int FIRST_STEP_MAX = 200;
	private static final int SECOND_STEP_MAX = 120;

	/**Minimum currentValue should ever reach */
	private static final int MINIMUM = 2;

	/*
	 * (non-Javadoc)
	 * @see acm.program.GraphicsProgram#run()
	 * 
	 * 
	 * Declare x and y of GLabels inside box
	 * display welcome message
	 * wait for click
	 * display first question and answers
	 * wait for user click
	 * process which box was clicked
	 * determine if correct
	 * if no, assess time penalty
	 * use while loop
	 * if yes, give next question
	 * 
	 * when time is up, finish question and display end screen
	 */
	public void run()
	{
		/*
		 * 1= 2/1 2= 4/2 3= 1/4 4= 4/4 5 = 1/2 6 = 3/4 7 = 3/2 8= 3/1 9 = 4/4
		 * 
		 */
		rgen.setSeed(1);
		setupGame();
		waitForClick();
		remove(welcomeLabel);
		playGame();
	}
	/**Displays a GLabel that directs the player to begin the game*/
	private void displayWelcome()
	{
		//add a label at the center
		welcomeLabel = new GLabel("Tap to start!");
		welcomeLabel.setFont("Helvetica-20");
		double x = WIDTH/2 - (welcomeLabel.getWidth() / 2);
		double y = HEIGHT/2 + (welcomeLabel.getHeight() / 2);
		welcomeLabel.setLocation(x, y);
		welcomeLabel.setColor(Color.white);
		add(welcomeLabel);
	}
	/**Method where all game setup takes place (before game begins)
	 * Excludes setting up GLabels for numbers, which takes place
	 * in playGame()
	 */
	private void setupGame()
	{
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		setBackground(Color.DARK_GRAY);
		score = 0;
		setupSquares();
		setupLabels();
	}
	/**
	 * Sets up the GRect squares that will hold 
	 * labels and detect touch inside
	 */
	private void setupSquares()
	{	
		//add a square near the top
		double x = (WIDTH/2) - (SQUARE_DIMEN/2);
		double y = (HEIGHT/4) - (SQUARE_DIMEN/2);
		questionSquare = new GRect (x, y, SQUARE_DIMEN, SQUARE_DIMEN);
		questionSquare.setColor(Color.white);
		add(questionSquare);

		//add a square near the lower left
		x = (WIDTH/4) - (SQUARE_DIMEN/2);
		y = ((HEIGHT/4)*3) - (SQUARE_DIMEN/2);
		leftAnswerSquare = new GRect (x, y, SQUARE_DIMEN, SQUARE_DIMEN);
		leftAnswerSquare.setColor(Color.white);
		add(leftAnswerSquare);

		//add a square near the bottom center
		x = (WIDTH/2) - (SQUARE_DIMEN/2);
		y = ((HEIGHT/4)*3) - (SQUARE_DIMEN/2);
		centerAnswerSquare = new GRect (x, y, SQUARE_DIMEN, SQUARE_DIMEN);
		centerAnswerSquare.setColor(Color.white);
		add(centerAnswerSquare);

		//add a square near the lower right
		x = ((WIDTH/4)*3) - (SQUARE_DIMEN/2);
		y = ((HEIGHT/4)*3) - (SQUARE_DIMEN/2);
		rightAnswerSquare = new GRect (x, y, SQUARE_DIMEN, SQUARE_DIMEN);
		rightAnswerSquare.setColor(Color.white);
		add(rightAnswerSquare);

	}
	/**
	 * Sets up all the labels for the game pre-play
	 */
	private void setupLabels() 
	{
		displayWelcome();
		setupTimer();
		setupScore();
	}
	/**Sets up the timerlabel GLabel timerLabelLocation GPoint*/
	private void setupTimer()
	{
		timerLabel = new GLabel(Double.toString(GAME_LENGTH));
		timerLabelLocation = new GPoint((WIDTH / 20), (HEIGHT / 20));
		timerLabel.setLocation(timerLabelLocation);
		timerLabel.setColor(Color.white);
		add(timerLabel);
	}
	/**Sets up the scoreLabel GLabel and scoreLabelLocation GPoint*/
	private void setupScore()
	{
		//Init label with current score
		scoreLabel = new GLabel(Integer.toString(score));
		//Init scoreLabel's GPoint
		scoreLabelLocation = new GPoint(WIDTH - (WIDTH / 20), 
				(HEIGHT / 20));
		scoreLabel.setLocation(scoreLabelLocation);
		scoreLabel.setColor(Color.white);
		add(scoreLabel);
	}
	/**
	 * Method where all game playing takes place
	 */
	private void playGame()
	{
		/*display first question and answers
		 * wait for user click
		 * process which box was clicked
		 * determine if correct
		 * if no, assess time penalty
		 * use while loop
		 * if yes, give next question
		 * 
		 * when time is up, finish question and display end screen
		 */

		/*If label is null, init label randomize it
		 * If label is not null, take correct answer and make it new
		 * question
		 */
		displayQuestion();
		/*
		 * Displays the formula needed that to do on the question
		 * 
		 */
		createFormulas();
		/*
		 * getCorrectAnswer();
		 * getWrongAnswers(); 
		 */
//		updateGUI();
//			updateOperationLabels(); CHANGE NAME
//			updateAnswers();
	}
	/**
	 * Method that displays the GLabel in the questionSquare
	 * If no label exists in that location, display a random number 1-60 
	 * If the label does exist, take the previous correct answer and make it the new question
	 */
	private void displayQuestion()
	{
		if (questionSquareLabel == null)
		{
			//init current value 
			currentValue = rgen.nextInt(1,60);
			//init label w/current value
			questionSquareLabel = new GLabel(Integer.toString(currentValue));
			//Change properties
			//X is halfway through the box - half the label's width
			//Y is halfway through the box + half the label's width
			questionSquareLabel.setFont("Helvetica-32");
			double xLocation = questionSquare.getX() + (questionSquare.getWidth() / 2) 
					- (questionSquareLabel.getWidth() / 2);
			double yLocation = questionSquare.getY() + (questionSquare.getHeight() / 2)
					+ (questionSquareLabel.getHeight() / 2);
			questionSquareLabelLocation = new GPoint(xLocation, yLocation);
			questionSquareLabel.setLocation(questionSquareLabelLocation);
			questionSquareLabel.setColor(Color.WHITE);
		}
		else
		{
			questionSquareLabel.setLabel(Integer.toString(correctAnswer));
		}
		//Add label
		add(questionSquareLabel);
	}
	/**Displays the two lines of formulas that the player uses to determine the answer
	 * Generates two different random numbers that determine operators
	 * two numbers cannot be both "addition" or both "subtraction"
	 * for question box: max of 120 - min of 2
	 * for multiplication: Can't end up with above 200
	 * for division: % == 0
	 * Store factors in array, use rgen to get int that will represent factor used for division
	 * stored as IVARS
	 * amount of values in array > 2 (avoid prime numbers)
	 * display them (update GUI)
	 * 
	 * This is where the main logic for the program occurs
	 * The program first creates two variables that are randomized between 1 and 4
	 * These are used to determine what operators will be performed for each part of the 2-steps.
	 * 
	 */
	private void createFormulas()
	{
		createOperatorCodes();
		int firstStepNumber = createFirstStep();
//		int secondStepNumber = createSecondStep();
//		createWrongAnswers(firstStepNumber, secondStepNumber);

	}

	/**
	 * Create randomly generated codes that indicate which operations will
	 * be performed on the top number.
	 * Ex. 1 = addition, 2 = subtraction
	 * These codes are stored as class variables operatorTop and operatorBottom
	 * Also contains logic to prevent unwanted combinations
	 */
	private void createOperatorCodes()
	{
		operatorTop = rgen.nextInt(1,4);
		operatorBottom = rgen.nextInt(1, 4);

		//Logic to remove unwanted combinations
		if(operatorTop == 1 && operatorBottom == 1)
		{
			operatorBottom = rgen.nextInt(2,4);
		}
		while(operatorTop == 2 && operatorBottom == 2)
		{
			operatorBottom = rgen.nextInt(1,4);
		}
	}

	/**
	 * Creates the first step to be performed on the question number
	 * using the codes from createOperatorCodes()
	 * Contains a switch statement that calls createAdditionStep,
	 * createSubtractionStep.... ect. based on the code
	 */
	private int createFirstStep()
	{
		//Number that will be used in the given step
		int numberCreated = 0;

		//Switch statement for 1st-step based on what operator was rolled (1=addition, 2=sub, 3= mult)
		switch(operatorTop)
		{
		case 1: numberCreated = createAdditionStep(FIRST_STEP_MAX); //init numberCreated 
		break;
		case 2: numberCreated = createSubtractionStep();
		break;
		case 3: numberCreated = createMultiplicationStep(FIRST_STEP_MAX);
		break;
		case 4: numberCreated = createDivisionStep();
		break;
		default: println("Something went horribly wrong");
		}
		
		return numberCreated;
	}

	/**This method creates an addition step for one part of the two step equation
	 * @return numberToAdd number that will be added to current value
	 * */
	private int createAdditionStep(int max)
	{
		int rgenMax = max - currentValue;
		int numberToAdd; 
		//Cop-out method that prevents an error if the number generated can only be <0
		if(rgenMax <= 0)
		{
			numberToAdd = createSubtractionStep();
			println("Addition cop-out occured!");
		}
		else //If the addition step is viable
		{
			numberToAdd = rgen.nextInt(MINIMUM, rgenMax); //number that will be added
			currentValue += numberToAdd; //update current value
		
			//This updates the formula label so the user knows what to do
			updateOperationLabelTop(1, numberToAdd);
		}
		return numberToAdd;
	}
	/**This method creates an addition step for one part of the two step equation
	 * @return numberToAdd number that will be added to current value
	 * */
	private int createSubtractionStep()
	{
		int rgenMax = currentValue - MINIMUM;
		int numberToSubtract;
		
		//Cop-out method: multiplies if subtraction isn't viable
		if(rgenMax <= 0)
		{
			numberToSubtract = createMultiplicationStep(FIRST_STEP_MAX);
			println("Subtraction cop-out occured!");
		}
		else
		{
			numberToSubtract = rgen.nextInt(1, rgenMax); //number that will be subtracted
			currentValue -= numberToSubtract; //update current value

			//This updates the formula label so the user knows what to do
			updateOperationLabelTop(2, numberToSubtract);
		}
		return numberToSubtract;
	}
	/**
	 * 
	 * @param max The maximum amount for this step to reach
	 * @return numberToAdd number that will be added to current value
	 */
	private int createMultiplicationStep(int max)
	{
		/*
		 * Logic: rgenMax * currentvalue = 200 -> rgenMax = 200/currentValue
		 */
		int rgenMax = max / currentValue;
		int numberToMultiply;
		
		//If multiplication is inpossible, does division instead
		if(rgenMax >= 1)
		{
			numberToMultiply = createDivisionStep();
			println("Multiplication cop-out occured!");
		}
		else
		{
			numberToMultiply = rgen.nextInt(MINIMUM, rgenMax); 
			currentValue *= numberToMultiply;

			//This updates the formula label so the user knows what to do
			updateOperationLabelTop(3, numberToMultiply);
		}
		return numberToMultiply;
	}
	private int createDivisionStep()
	{
		/*
		 * Rules:
		 * currentValue % numberToDivide == 0
		 * numberToDivide != 1 && numberToDivide != currentValue  
		 */

		/*
		 * How this method works:
		 * 1) Creates an ArrayList
		 * 2) for loop that checks up to 1/2 of current value
		 * 		if the current number (i) % 0 of currentValue, store it in the ArrayList
		 * 3) If the array only has two values (therefore it is a prime number), do addition instead
		 * 		numberToDivide = doAdditionStep();
		 */
		ArrayList<Integer> factors = new ArrayList<Integer>();

		for(int i = 2; i <= (currentValue / 2); i++)
		{
			if(currentValue % i == 0)
			{
				factors.add(i);
			}
		}

		int numberToDivide = factors.get(rgen.nextInt(1, (factors.size() -1)));
		
		//Cop out if the number is prime or division isn't viable
		if(factors.size() == 2)
		{
			numberToDivide = createAdditionStep(FIRST_STEP_MAX);
			println("Division cop-out occured!");
		}
		else
		{
			currentValue /= numberToDivide;

			//This updates the formula label so the user knows what to do
			updateOperationLabelTop(4, numberToDivide);
		}
		return numberToDivide;
	}
	/**
	 * pre: currentValue has been updated
	 * @param operationType 
	 */
	private void updateOperationLabelTop(int operationType, int numberCreated)
	{
		String operationTypeString = "";
		switch(operationType)
		{
		case 1: operationTypeString = " + ";
		break;
		case 2: operationTypeString = " - ";
		break;
		case 3: operationTypeString = " * ";
		break;
		case 4: operationTypeString = " / ";
		break;
		}

		//Assign uperationLabelTop
		operationLabelTop = new GLabel(operationTypeString + numberCreated);
	}
	/**Instance of random generator for creating all random events*/
	RandomGenerator rgen = new RandomGenerator().getInstance();

	/**Squares that will hold labels and detect touch inside*/
	private GRect questionSquare;
	private GRect leftAnswerSquare;
	private GRect centerAnswerSquare;
	private GRect rightAnswerSquare;

	/**GLabels to be stored inside the squares*/
	private GLabel questionSquareLabel;
	private GLabel leftAnswerSquareLabel;
	private GLabel centerAnswerSquareLabel;
	private GLabel rightAnswerSquareLabel;

	/**Helper GLabels to be displayed during the game*/
	private GLabel welcomeLabel;
	private GLabel scoreLabel;
	private GLabel timerLabel;
	private GLabel operationLabelTop;
	private GLabel operationLabelBottom;

	/** GPoints where the GLabels should be placed in each square */
	private GPoint questionSquareLabelLocation;
	private GPoint leftAnswerSquareLabelLocation;
	private GPoint centerAnswerSquareLabelLocation;
	private GPoint rightAnswerSquareLabelLocation;

	/**GPoints for other labels*/
	private GPoint timerLabelLocation;
	private GPoint scoreLabelLocation;

	/**...The score*/
	private int score;
	private int currentValue;
	private int correctAnswer;	

	/**integers with value 1-4 that determine which operation is used*/
	private int operatorTop;
	private int operatorBottom;
}