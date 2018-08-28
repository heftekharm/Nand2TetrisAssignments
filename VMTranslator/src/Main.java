import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    // TODO Auto-generated method stub
    String inputFileName = "FibonacciElement";// args[0];
    File inputFile = new File(inputFileName);
    String outName;
    String[] vmFilesList;

   // if (!inputFile.exists())
     // throw new FileNotFoundException("Input File Not Found!");

    if (inputFile.isDirectory()) {
      outName = inputFileName;

      vmFilesList = inputFile.list(new FilenameFilter() {

        @Override
        public boolean accept(File dir, String fname) {
          // TODO Auto-generated method stub
          if (fname.endsWith(".vm"))
            return true;
          return false;
        }
      });
      
      int sysIndex=0;
      while (sysIndex<vmFilesList.length && !vmFilesList[sysIndex].equalsIgnoreCase("Sys.vm")) {//to find index of Sys.vm file within the array
        sysIndex++;
      }
      
      if(sysIndex!=vmFilesList.length) {
        //the array includes Sys.vm with index of sysIndex. 
       // String ztemp=vmFilesList[0];//moving sys.vm  to be inserted as first vm file that is converted to assembly
       // vmFilesList[0]=vmFilesList[sysIndex];
       // vmFilesList[sysIndex]=ztemp;
      }

    } else {
      vmFilesList = new String[1];
      vmFilesList[0] = inputFileName;
      int dotLastIndex = inputFileName.lastIndexOf('.');
      outName = dotLastIndex > 0 ? inputFileName.substring(0, dotLastIndex) : inputFileName; // input file Name withput
                                                                                             // extention
    }

    String outExtension = ".asm";
    String outputFileName = outName + outExtension;
    FileWriter writer = null;
    try {
      writer = new FileWriter(outputFileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    PrintWriter pwriter = new PrintWriter(writer);
    CodeWriter cw = new CodeWriter(pwriter);
    cw.writeInit();
    FileReader reader = null;
    for (String fname : vmFilesList) {
      
      reader = new FileReader(inputFileName+File.separator+fname);
      
      Parser parser = new Parser(reader);
      
      int dotLastIndex = fname.lastIndexOf('.');
      cw.setFileName(fname.substring(0, dotLastIndex));
      
      while (parser.hasMoreCommands()) {
        parser.advance();
        String type = parser.commandType();
        String arg1 = parser.arg1();
        int arg2 = parser.arg2();
        switch (type) {
        
        case Parser.ARTH_CONS:
          //pwriter.println("//" + arg1);
          cw.writeArithmatic(arg1);
          break;
        
        case Parser.POP_CONS:
          //pwriter.println("//POP " + arg1 + " " + arg2);
          cw.writePushPop(type, arg1, arg2);
          break;
          
        case Parser.PUSH_CONS:
          //pwriter.println("//PUSH " + arg1 + " " + arg2);
          cw.writePushPop(type, arg1, arg2);
          break;
        
        case Parser.LABEL_CONS:
          //pwriter.println("//LABEL "+ arg1);
          cw.writeLabel(arg1);//in this case arg1=>label
          break;
        
        case Parser.GO_CONS:
          //pwriter.println("//GOTO " + arg1);
          
          cw.writeGoto(arg1);
          break;
        
        case Parser.IF_CONS:
          //pwriter.println("//IF-GOTO " + arg1);
          
          cw.writeIf(arg1);
          break;
        
        case Parser.CALL_CONS:
          //pwriter.println("//CALL " + arg1 + " " + arg2);
          cw.writeCall(arg1, arg2);
          break;
        
        case Parser.FUNC_CONS:
          //pwriter.println("//FUNCTION " + arg1 + " " + arg2);

          cw.writeFunction(arg1, arg2);
          break;
        
        case Parser.RETURN_CONS:
          //pwriter.println("//RETURN");
          cw.writeReturn();
          break;
        }

      }
    }

    cw.close();

  }

}
