class Logger {
  
  enum LoggerStatus {
    OFF, ON, VERBOSE
  }
    
  static LoggerStatus loggerStatus = LoggerStatus.OFF;
  static Class[] loggerScope;
  
  // Methods for actually logging
  
  public static void log(Class c, String string) {
    Logger.log(c, string, false);
  }
  
  public static void log(Class c, String string, boolean isVerbose) {
    if (Logger.loggerStatus == LoggerStatus.OFF) {
      // If the message is verbose, no printing. If it isn't then print it if the c is in the scope.
      if (!isVerbose && Logger.classIsInScope(c)) formatAndPrint(c, string);
    } else if (loggerStatus == LoggerStatus.ON) {
      // If the message is not verbose, print it
      if (!isVerbose) formatAndPrint(c, string);
      // If the c is in the scope, also print
      else if (Logger.classIsInScope(c)) formatAndPrint(c, string);
    } else if (loggerStatus == LoggerStatus.VERBOSE) {
      // Print everything
      formatAndPrint(c, string);
    }
  }
  
  // Methods for dealing with the status
  
  public static void setStatus(LoggerStatus status) {
    loggerStatus = status;
  }
  
  public static void setStatus(String status) throws IllegalArgumentException {
    if ("ON".equalsIgnoreCase(status)) {
      Logger.loggerStatus = LoggerStatus.ON;
    } else if ("OFF".equalsIgnoreCase(status)) {
      Logger.loggerStatus = LoggerStatus.OFF;
    } else if ("VERBOSE".equalsIgnoreCase(status)) {
      Logger.loggerStatus = LoggerStatus.VERBOSE;
    } else {
      throw new IllegalArgumentException("Could not find a LoggerStatus state that matches " + status);
    }
  }
  
  public static void turnOn() {
    Logger.loggerStatus = LoggerStatus.ON;
  }
  
  public static void turnOff() {
    Logger.loggerStatus = LoggerStatus.OFF;
  }
  
  // Methods for dealing with the scope
  
  public static void addClassToScope(Class c) {
    // Special cases
    if (loggerScope == null) {
      // If the scope is undefined, define a scope with just this c
      Logger.loggerScope = new Class[]{c};
      return;
    } else if (classIsInScope(c)) {
      // If the c is already there print a warning to System.err
      System.err.println("WARNING: Attempted to add " + c.getSimpleName() + " to the Logger scope but it was already there. This request was ignored.");
      return;
    }
      
    // Create a new array with room for the new c and move all the old classes to it.
    Class[] tempLoggerScope = new Class[Logger.loggerScope.length + 1];
    for (int i = 0; i < Logger.loggerScope.length; i++) tempLoggerScope[i] = Logger.loggerScope[i];
    
    // Add the new c to the end of the array and set it as the logger scope
    tempLoggerScope[tempLoggerScope.length - 1] = c;
    Logger.loggerScope = tempLoggerScope;
  }
  
  public static void removeClassFromScope(Class c) {
    if (!classIsInScope(c)) {
      // If the c is not there print a warning to System.err
      System.err.println("WARNING: Attempted to remove " + c.getSimpleName() + " from the Logger scope but it was not there. This request was ignored.");
      return;
    }
    
    // Create a new array with one less element for the c being removed
    Class[] tempLoggerScope = new Class[Logger.loggerScope.length - 1];
    
    // Move all the old classes to the new array except for the one being removed
    int index;
    for (index = 0; !Logger.loggerScope[index].getName().equals(c.getName()); index++)
      tempLoggerScope[index] = Logger.loggerScope[index];
    for (index++; index < Logger.loggerScope.length; index++)
      tempLoggerScope[index - 1] = Logger.loggerScope[index];
    
    // Make tempLoggerScope the new scope
    Logger.loggerScope = tempLoggerScope;
  }
  
  
  public static boolean classIsInScope(Class c) {
    // Not in scope if there is no scope
    if (loggerScope == null) return false;
    
    // Linear search since I don't feel like bothering with sorting and the scope should always be pretty small
    for (int i = 0; i < loggerScope.length; i++) {
      // If the c is in the scope return true, else false
      // We're comparing strings here since Class does not implements an equals() method
      if (loggerScope[i].getName().equals(c.getName())) return true;
    }
    
    // If we can't find it then it isn't there. Return false.
    return false;
  }
  
  public static String loggerToString() {
    // First part represtents the status. On, Off, or Verbose.
    String line1 = "Status: ";
    if (Logger.loggerStatus == LoggerStatus.ON) line1 += "ON";
    else if (Logger.loggerStatus == LoggerStatus.OFF) line1 += "OFF";
    else if (Logger.loggerStatus == LoggerStatus.VERBOSE) line1 += "VERBOSE";
    
    // Second part is a list of the classes in the scope.
    String line2 = "Scope: ";
    if (Logger.loggerScope != null && loggerScope.length > 0) {
      for (int i = 0; i < Logger.loggerScope.length; i++) {
        if (i < Logger.loggerScope.length - 1) line2 += Logger.loggerScope[i].getSimpleName() + ", ";
        else line2 += Logger.loggerScope[i].getSimpleName();
      }
    } else {
      line2 += "EMPTY";
    }
    
    // Return it with some brackets and a pipe to make it readable
    return "[" +  line1 + " | " + line2 + "]";
  }
  
  // This private method just sets up the printing format. Override or modify this to change the way printing is formated.
  private static void formatAndPrint(Class c, String string) {
    System.out.println(c.getSimpleName() + ": " + string);
  }
  
}