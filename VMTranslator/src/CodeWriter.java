import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.SizeLimitExceededException;

public class CodeWriter {
  public final static String LOCAL_SEG = "local";
  public final static String ARG_SEG = "argument";
  public final static String THIS_SEG = "this";
  public final static String THAT_SEG = "that";
  public final static String CONS_SEG = "constant";
  public final static String STATIC_SEG = "static";
  public final static String POINTER_SEG = "pointer";
  public static final String TEMP_SEG = "temp";

  static List<String> similar_segs;
  static Map<String, String> segs_map = new HashMap();
  public static List<String> Arth_Commands;
  
  static {
    similar_segs = new ArrayList<String>();
    similar_segs.add(LOCAL_SEG);
    similar_segs.add(ARG_SEG);
    similar_segs.add(THIS_SEG);
    similar_segs.add(THAT_SEG);

    segs_map.put(LOCAL_SEG, "LCL");
    segs_map.put(ARG_SEG, "ARG");
    segs_map.put(THIS_SEG, "THIS");
    segs_map.put(THAT_SEG, "THAT");

    String[] arths = { "add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not" };
    Arth_Commands = Arrays.asList(arths);
  }

  private PrintWriter out;
  private String asp = "@SP";
  private String fname;
  private int lbsIndex = 1;
  private String currentFunction=null;
  private String tempComment="";
  
  
  
  public CodeWriter(Writer output) {
    out = (PrintWriter) output;
  }
  
  private  void putComment(String c) {
    tempComment="    //"+c;
  }
  private void clc() {
    tempComment="";
  }
  public void setFileName(String name) {
    this.fname = name;

  }
  private String lbs() {
    return "Label" + lbsIndex++;
  }

  void writeInit() {
    putComment("Start");
    jp("@256",
        "D=A",
        asp,
        "M=D",
        "@Sys.init",
        "0;JMP"
        );
    clc();
  }
  
  void writeLabel(String label) {
    putComment("LABEL " + label);

    String sl=labelFormatter(label);
    
    jp("("+sl+")");
  }
  
  void writeGoto(String label) {
    putComment("GOTO "+ label);
    
    jp("@"+labelFormatter(label),
        "0;JMP");
  }
  
  void writeIf(String label) {
    putComment("IF-GOTO " + label);
    jp(asp,
        "M=M-1",//if doesnt work remove this line that removes the last element from the stack
        "A=M",
        "D=M",
        "@"+labelFormatter(label),
        "D;JNE");// if doesn't work replace JNE with JGT
  }
  
  private String labelFormatter(String label) {
    if(currentFunction==null)
      return label;
    String fl=String.format("%s$%s",currentFunction,label);
    return fl;
  }
  
  void writeFunction(String func,int numOfLocals) {
    putComment("FUNCTION " + func + " " + numOfLocals);

    currentFunction=func;
    jp("("+func+")");//writes label
    
    clc();
    for(int i=0;i<numOfLocals;i++) {
      jp("@" + 0, "D=A", asp, "A=M", "M=D", asp, "M=M+1");
    }
  }
  
  
  void writeReturn() {
    putComment("RETURN");

    String endframe="@endframe";
    String retadd="@returnAddress";
    
    jp("@LCL","D=M",endframe,"M=D");//stores LCL into a temp variable "endframe"
    
    clc();
    
    jp("@5","D=A",endframe,"A=M","A=A-D","D=M",retadd,"M=D");//stores returnAddress stored in the stack into a temp var "returnAddress"
    
    jp(asp,"M=M-1","A=M","D=M","@ARG","A=M","M=D");//repositions the return value for the caller
    
    jp("@ARG","D=M+1",asp,"M=D");//repositions sp of the caller
          
    jp(endframe,"A=M-1","D=M","@THAT","M=D");//repositions THAT
    
    jp("@2","D=A",endframe,"A=M-D","D=M","@THIS","M=D");//repositions THIS
       
    jp("@3","D=A",endframe,"A=M-D","D=M","@ARG","M=D");//repositions ARG
    
    jp("@4","D=A",endframe,"A=M-D","D=M","@LCL","M=D");//repositions LCL
    
    jp(retadd,"A=M","0;JMP");//return    
  }
  
  
  
  void writeCall(String func,int numOfArgs) {
    putComment("CALL " + func + " " + numOfArgs);

    String retAddress=GenerateRetAddr(func);
    
    jp("@"+retAddress,"D=A",asp,"A=M","M=D",asp,"M=M+1",//push return address
    
    "@LCL","D=M",asp,"A=M","M=D",asp,"M=M+1",//PUSH LOCAL BASE ADDRESS IN THE STACK
    
    "@ARG","D=M",asp,"A=M","M=D",asp,"M=M+1",//PUSH ARG BASE ADDRESS IN THE STACK
    
    "@THIS","D=M",asp,"A=M","M=D",asp,"M=M+1",//PUSH THIS BASE ADDRESS IN THE STACK
    
    "@THAT","D=M",asp,"A=M","M=D",asp,"M=M+1",//PUSH THAT BASE ADDRESS IN THE STACK
    
    "@"+numOfArgs,"D=A",asp,"D=M-D","@5","D=D-A","@ARG","M=D",//reposition ARG
    
    asp,"D=M","@LCL","M=D");//repositions LCL
    
    putComment("GOTO (CALL)");
    jp("@"+func,"0;JMP");   
    putComment("RETURN ADDRESS (CALL)");
    jp("("+retAddress+")");//writes label
    
  }
  
  private int retadd=1;
  private String  GenerateRetAddr(String func) {
    return func+"$ret."+retadd++;
  }
  
  

  //////////////////////////////////////////////////////////Arithmatic
  /////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  
  
  
  void writeArithmatic(String comm) {
    putComment(comm);
    switch (comm) {
    case "add":
      add();
      break;
    case "sub":
      sub();
      break;
    case "neg":
      neg();
      break;
    case "eq":
      eq();
      break;
    case "gt":
      gt();
      break;
    case "lt":
      lt();
      break;
    case "and":
      and();
      break;
    case "or":
      or();
      break;
    case "not":
      not();
      break;
    default:
      throw new IllegalArgumentException();
    }
  }

  private void add() {
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "M=D+M");
  }

  private void sub() {
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "M=M-D");
  }

  private void neg() {
    jp(asp, "A=M", "A=A-1", "M=-M");
  }

  // 0 as false
  // -1 as true
  private void eq() {
    String lbs1 = lbs();
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "D=M-D", "M=-1", "@" + lbs1, "D;JEQ", asp, "A=M", "A=A-1", "M=0", 
        "(" + lbs1 + ")");
  }

  private void lt() {
    String lbs1 = lbs();
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "D=M-D", "M=-1", "@" + lbs1, "D;JLT", asp, "A=M", "A=A-1", "M=0",
        "(" + lbs1 + ")");
  }

  private void gt() {
    String lbs1 = lbs();
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "D=M-D", "M=-1", "@" + lbs1, "D;JGT", asp, "A=M", "A=A-1", "M=0",
        "(" + lbs1 + ")");
  }

  private void and() {
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "M=D&M");
  }

  private void or() {
    jp(asp, "M=M-1", "A=M", "D=M", "A=A-1", "M=D|M");
  }

  private void not() {
    jp(asp, "A=M", "A=A-1", "M=!M");
  }

  
  //////////////////////////////////////////////////////////PUSH POP
  /////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  
  void writePushPop(String command, String segment, int index) {
    putComment("POP " + segment + " " + index);
    
    
    if (command.equals( Parser.PUSH_CONS)) {
      if (similar_segs.contains(segment)) {
        segment = segs_map.get(segment);
        pushSimilarSegs(segment, index);
      }
      if (segment.equals(STATIC_SEG))
        pushStatic(index);
      if (segment.equals( TEMP_SEG))
        pushTemp(index);
      if (segment.equals(POINTER_SEG))
        pushPointer(index);
      if (segment.equals(CONS_SEG))
        pushConstant(index);

    }
    if (command .equals( Parser.POP_CONS)) {
      if (similar_segs.contains(segment)) {
        segment = segs_map.get(segment);
        popSimilarSegs(segment, index);
      }
      if (segment.equals(STATIC_SEG))
        popStatic(index);
      if (segment.equals(TEMP_SEG))
        popTemp(index);
      if (segment.equals(POINTER_SEG))
        popPointer(index);
    }

  }

 
  
  private void pushConstant(int index) {
    // TODO Auto-generated method stub
    jp("@" + index, "D=A", asp, "A=M", "M=D", "@SP", "M=M+1");

  }

  private void popPointer(int index) {
    // TODO Auto-generated method stub
    int i = 3 + index;

    jp(asp, "M=M-1", "A=M", "D=M", "@" + i, "M=D");

  }

  private void popTemp(int index) {
    // TODO Auto-generated method stub
    int i = 5 + index;
    jp(asp, "M=M-1", "A=M", "D=M", "@" + i, "M=D");

  }

  private void pushPointer(int index) {
    // TODO Auto-generated method stub
    int i = 3 + index;
    jp("@" + i, "D=M", asp, "A=M", "M=D", asp, "M=M+1");

  }

  private void pushTemp(int index) {
    // TODO Auto-generated method stub
    int i = 5 + index;
    jp("@" + i, "D=M", asp, "A=M", "M=D", asp, "M=M+1");

  }

  private void pushStatic(int index) {
    jp("@" + fname + '.' + index, "D=M", asp, "A=M", "M=D", asp, "M=M+1");

  }

  private void popStatic(int index) {
    // TODO Auto-generated method stub
    jp(asp, "M=M-1", "A=M", "D=M", "@" + fname + '.' + index, "M=D");

  }

  private void pushSimilarSegs(String seg, int index) {
    jp("@" + index, "D=A", "@" + seg, "A=M+D", "D=M", asp, "A=M", "M=D", asp, "M=M+1");
  }

  private void popSimilarSegs(String seg, int index) {
    jp("@" + index, "D=A", "@" + seg, "A=M+D", "D=A", asp, "A=M", "M=D", "A=A-1", "D=M", "A=A+1", "A=M", "M=D", asp,
        "M=M-1");

  }
  
  /////////Print 

  private String joiner(String... sss) {
    String lines = String.join("\n", sss);
    return lines;
  }

  
  int pcounter=0;
  private void jp(String... sss) {
    sss[0]=sss[0]+tempComment;
   for(int i=0;i<sss.length;i++) {
     if(sss[i].startsWith("(")) continue;
     sss[i]=sss[i]+"             //"+pcounter++;
   }
    out.println(joiner(sss));
  }

  
  
  void close() {
    out.flush();
    out.close();

  }
}
