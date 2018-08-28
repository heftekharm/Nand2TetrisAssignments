import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
  public final static String ARTH_CONS = "C_ARTITHMETIC";
  public final static String PUSH_CONS = "C_PUSH";
  public final static String POP_CONS = "C_POP";
  public final static String LABEL_CONS = "C_LABEL";
  public final static String GO_CONS = "C_GOTO";
  public final static String FUNC_CONS = "C_FUNCTION";
  public final static String IF_CONS = "C_IF";
  public final static String RETURN_CONS = "C_RETURN";
  public final static String CALL_CONS = "C_CALL";

  private String currentCommand;
  private String currentArg1;
  private int currentArg2;

  private String currentRawLine;
  private LineNumberReader lineReader;

  public Parser(Reader reader) {
    lineReader = new LineNumberReader(reader);
  }

  boolean hasMoreCommands() {
    String line;
    boolean has = false;
    while (true) {
      try {
        line = lineReader.readLine();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        has = false;
        break;
      }
      if (line == null) {
        has = false;
        break;
      }
      line = line.split("/")[0];// removing comments
      line = line.trim();// removing white spaces;
      if (line.length() > 0) {
        has = true;
        currentRawLine = line;
        break;
      }

    }
    return has;

  }

  void advance() {
    List<String> cparts = new ArrayList();
    for (String el : currentRawLine.split(" ")) {
      el = el.trim();
      if (el.length() > 0)
        cparts.add(el);
    }
    if (cparts.size() == 3) { // So It is a push pop command
      if (cparts.get(0).equalsIgnoreCase("push")) {
        currentCommand = PUSH_CONS;
      }
      if (cparts.get(0).equalsIgnoreCase("pop")) {
        currentCommand = POP_CONS;
      }
      if (cparts.get(0).equalsIgnoreCase("function")) {
        currentCommand = FUNC_CONS;
      }
      if (cparts.get(0).equalsIgnoreCase("call")) {
        currentCommand = CALL_CONS;
      }
      currentArg1 = cparts.get(1);
      currentArg2 = Integer.parseInt(cparts.get(2));

    } else if (cparts.size() == 2) {
      String p0=cparts.get(0);
      if (p0.startsWith("if")) {
        currentCommand = IF_CONS;
      } else if(p0.startsWith("go")) {
        currentCommand = GO_CONS;
      }else {
        currentCommand = LABEL_CONS;//else starts with label
      }
      currentArg1 = cparts.get(1);     
      
    } else if (cparts.size() == 1) { // Its a Arithmetic command
      if (cparts.get(0).equalsIgnoreCase("return")) {// its a return
        currentCommand = RETURN_CONS;
      } else if (cparts.get(0).startsWith("(")) {// its a label
        currentCommand = LABEL_CONS;
      } else {
        currentCommand = ARTH_CONS;
        currentArg1 = cparts.get(0);
      }
    }
  }

  String commandType() {
    return currentCommand;
  }

  String arg1() {
    return currentArg1;
  }

  int arg2() {
    return currentArg2;
  }
}
