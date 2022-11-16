import java.util.Scanner;

public class Library {
  // ***** Scanners ***********************************//
  public static Scanner numscan = new Scanner(System.in);
  public static Scanner wordscan = new Scanner(System.in);

  // ***** Text Colors for the Console *****************//
  public static final String RESET = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String TEAL = "\u001B[36m";
  public static final String PINK = "\u001B[95m";
  public static final String CYAN = "\u001B[96m";

  public static final String BLACK_BACKGROUND = "\u001B[40m";
  public static final String RED_BACKGROUND = "\u001B[41m";
  public static final String GREEN_BACKGROUND = "\u001B[42m";
  public static final String YELLOW_BACKGROUND = "\u001B[43m";
  public static final String BLUE_BACKGROUND = "\u001B[44m";
  public static final String PURPLE_BACKGROUND = "\u001B[45m";
  public static final String CYAN_BACKGROUND = "\u001B[46m";
  public static final String WHITE_BACKGROUND = "\u001B[47m";

  // *****************************************************
  /**
  * gives random int between max and min parameters
  * getRandom(min,max)
  */
  public static int getRandom(int min, int max){
    return (int) (Math.random() * (max - min + 1) + min);
    
  } // end getRandom

} // end Class