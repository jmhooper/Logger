# Logger

This class is used to log output because I don't like dealing with System.out.prinln() everywhere.
The logger prints using the following methods:

This will print in the On mode
`Logger.log(Class class, String string);`

This allows you tell a message to print in just verbose mode by passing true for isVerbose
`Logger.log(Class class, String string, boolean isVerbose);`

## Logger Status

The logger has three modes.
- Off:     The logger will not print anything.
           This is good for actual deployment when you don't want to see any log statements.
- On:      The logger will print standard alerts. 
           Alerts that are printed when the logger is "On" will reflect the general status of classes, but wil not print "nitty-gritty"
           details such as the value of instance of local variables or the state of a method.
- Verbose: The logger will print everything it can.

Off is suggested for when you are deploying the software and On is recommended for use during development. Verbose is recommended for debugging.

The loggers status can be set with the following:

Accepted string values are (case insensitive) "On", "Off", and "Verbose". Will throw an IllegalArgumentException if you get it wrong.
`Logger.setStatus(String status);`

Turns the logger on.
`Logger.turnOn();`

Turns the logger off.
`Logger.turnOff();`

  
## Logger Scope

The logger's scope is the set of classes that the logger will focus on printing.

If the logger is Off, then the logger will print messages from a class in the scope like it is On.
If the Logger is On then it will print messages from a class in the scope like it is Verbose.

To add or remove a class from the scope use the following methods

-`Logger.addClassToScope(Class c);`

-`Logger.removeClassFromrScope(Class c);`

#### Notes on classes already/not in the scope

- If you try to add a class that is already in the scope the logger will log a warning to System.err and ignore the request.
- If you try to remove a class that isn't in the scope the logger will log a warning to System.err and ignore the request.
- You can use `Logger.classIsInScope(Class c)` to test to see if a class is in the scope of the logger.