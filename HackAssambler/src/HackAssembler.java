import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.function.Consumer;

public class HackAssembler {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String fileName = "Pong.asm";//args[0];
    File file = new File(fileName);
    FileReader fileReader = null;
    ArrayList<Command> commands = new ArrayList();
    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    BufferedReader reader = new BufferedReader(fileReader);
    PrintWriter printWriter = null;
    try {
      
      FileWriter filewriter = new FileWriter(fileName + ".hack");
      printWriter = new PrintWriter(filewriter);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    String line;
    try {
      while ((line = reader.readLine()) != null) {
        Command command = parse(line);
        if (command == null)
          continue;
        commands.add(command);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // FirstPass :adding lables to SYmbolTable
    int ln = 0;// line number
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands.get(i);
      if (command.type() == Command.LABEL_TYPE) {
        SymbolTable.add(command.label(), ln);
        continue;
      }
      ln++;
    }
    // Second Pass
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands.get(i);
      if (command.type() == Command.A_COMMAND_TYPE) {
        String a = command.a();
        if (!isNumber(a)) {
          if (!SymbolTable.contains(a)) {
            SymbolTable.addVar(a);
          }
          command.setA(SymbolTable.get(command.a()).toString());
        }
      }
    }
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands.get(i);
      if (command.type() == Command.A_COMMAND_TYPE)
        printWriter.println(Code.generateBinaryAInstruction(command));
      if (command.type() == Command.C_COMMAND_TYPE)
        printWriter.println(Code.generateBinaryCInstruction(command));
    }
    printWriter.flush();

  }

  static boolean isNumber(String s) {
    boolean res = false;
    try {
      int n = Integer.parseInt(s);
      res=true;
    } catch (Exception e) {
      // TODO: handle exception
      res = false;
    }
    return res;
  }

  static Command parse(String line) {
    Command command = new Command();

    line = line.split("//")[0];// extracting command
    line = line.trim();
    if (line.length() == 0)
      return null;

    char firstChar = line.charAt(0);
    if (firstChar == '@') {
      String a = line.substring(1, line.length());
      command.setA(a);

    } else if (firstChar == '(') {// is the line a label symbol?
      int closeIndex = line.lastIndexOf(")");
      String label = line.substring(1, closeIndex);
      command.setLabel(label);

    } else {
      String d = "", c = "", j = "";
      if (line.contains("=")) {
        d = line.split("=")[0];
        line = line.split("=")[1];
      }
      if (line.contains(";")) {
        j = line.split(";")[1];
        line = line.split(";")[0];
      }
      c = line;
      command.setC(d, c, j);
    }

    return command;
  }

  static class Command {
    static int A_COMMAND_TYPE = 0;
    static int C_COMMAND_TYPE = 1;
    static int LABEL_TYPE = 2;

    private int lineNumber = -1;
    private int type = -1;
    private String[] fields = new String[3];

    public void setType(int type) {
      this.type = type;
    }

    public void setA(String a) {
      fields[0] = a;
      setType(A_COMMAND_TYPE);
    }

    public void setC(String dest, String comp, String jump) {
      fields[0] = dest != null ? dest : "";
      fields[1] = comp != null ? comp : "";
      fields[2] = jump != null ? jump : "";
      setType(C_COMMAND_TYPE);
    }

    public void setLabel(String label) {
      fields[0] = label;
      setType(LABEL_TYPE);
    }

    public void setLineNumber(int ln) {
      this.lineNumber = ln;
    }

    public int type() {
      return type;
    }

    public String a() {
      return fields[0];
    }

    public String dest() {
      return fields[0];
    }

    public String comp() {
      return fields[1];
    }

    public String jump() {
      return fields[2];
    }

    public String label() {
      return fields[0];
    }

  }

  static class SymbolTable {
    private static Hashtable<String, Integer> table;
    private static int varIndex = 16;
    static {
      table = new Hashtable();
      for (int i = 0; i < 16; i++) {
        table.put("R" + i, i);
      }
      table.put("SCREEN", 16384);
      table.put("KBD", 24576);
      table.put("SP", 0);
      table.put("LCL", 1);
      table.put("ARG", 2);
      table.put("THIS", 3);
      table.put("THAT", 4);
    }

    static String getBinary(String symbol) {
      Integer res = table.get(symbol);
      if (res == null)
        return null;
      return Integer.toBinaryString(res);

    }

    static Integer get(String symbol) {
      Integer res = table.get(symbol);
      return res;
    }

    static void add(String s, int v) {
      table.put(s, v);
    }

    static boolean contains(String s) {

      return table.containsKey(s);
    }

    static void addVar(String s) {
      add(s, varIndex);
      varIndex++;
    }

  }

  static class Code {
    static Hashtable<String, String> compTable = new Hashtable();
    static Hashtable<String, String> destTable = new Hashtable();
    static Hashtable<String, String> jumpTable = new Hashtable();

    static {
      // a=0
      compTable.put("0", "0101010");
      compTable.put("1", "0111111");
      compTable.put("-1", "0111010");
      compTable.put("D", "0001100");
      compTable.put("A", "0110000");
      compTable.put("!D", "0001101");
      compTable.put("!A", "0110001");
      compTable.put("-D", "0001111");
      compTable.put("-A", "0110011");
      compTable.put("D+1", "0011111");
      compTable.put("A+1", "0110111");
      compTable.put("D-1", "0001110");
      compTable.put("A-1", "0110010");
      compTable.put("D+A", "0000010");
      compTable.put("D-A", "0010011");
      compTable.put("A-D", "0000111");
      compTable.put("D&A", "0000000");
      compTable.put("D|A", "0010101");
      // a=1
      compTable.put("M", "1110000");
      compTable.put("!M", "1110001");
      compTable.put("-M", "1110011");
      compTable.put("M+1", "1110111");
      compTable.put("M-1", "1110010");
      compTable.put("D+M", "1000010");
      compTable.put("D-M", "1010011");
      compTable.put("M-D", "1000111");
      compTable.put("D&M", "1000000");
      compTable.put("D|M", "1010101");

      destTable.put("", "000");
      destTable.put("M", "001");
      destTable.put("D", "010");
      destTable.put("MD", "011");
      destTable.put("A", "100");
      destTable.put("AM", "101");
      destTable.put("AD", "110");
      destTable.put("AMD", "111");

      jumpTable.put("", "000");
      jumpTable.put("JGT", "001");
      jumpTable.put("JEQ", "010");
      jumpTable.put("JGE", "011");
      jumpTable.put("JLT", "100");
      jumpTable.put("JNE", "101");
      jumpTable.put("JLE", "110");
      jumpTable.put("JMP", "111");

    }

    static String generateBinaryCInstruction(Command c) {
      String destB = destTable.get(c.dest());
      String compB = compTable.get(c.comp());
      String jumpB = jumpTable.get(c.jump());
      return "111" + compB + destB + jumpB;
    }

    static String generateBinaryAInstruction(Command c) {
      String anumber = c.a();
      String aB = Integer.toBinaryString(Integer.parseInt(anumber));
      String raw = "0000000000000000" + aB;
      return raw.substring(raw.length() - 16, raw.length());

    }
  }

}
