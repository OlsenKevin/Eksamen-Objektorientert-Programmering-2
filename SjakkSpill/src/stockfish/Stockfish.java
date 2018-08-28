package stockfish;
 
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.OutputStreamWriter; 
 
// Methods in this class is borrowed. Author is Rahul R A. Source is found in the documentation.

public class Stockfish { 
 
 private Process engineProcess; 
 private BufferedReader processReader; 
 private OutputStreamWriter processWriter;
 
 String PATH = "C:/eksamen/stockfish-9-win/Windows/stockfish_9_x32.exe";
 

// Starts Stockfish engine as a process and initializes it 

 public boolean startEngine() { 
  try { 
   engineProcess = Runtime.getRuntime().exec(PATH); 
   processReader = new BufferedReader(new InputStreamReader( 
     engineProcess.getInputStream())); 
   processWriter = new OutputStreamWriter( 
     engineProcess.getOutputStream()); 
  } catch (Exception e) { 
   return false; 
  } 
  return true; 
 } 
 
// Takes in any valid UCI command and executes it 

 public void sendCommand(String command) { 
  try { 
   processWriter.write(command + "\n"); 
   processWriter.flush(); 
  } catch (IOException e) { 
   e.printStackTrace(); 
  } 
 } 
 
// This reads and gets the latest raw output from Stockfish
 public String getOutput(int waitTime) { 
  StringBuffer buffer = new StringBuffer(); 
  try { 
   Thread.sleep(waitTime); 
   sendCommand("isready"); 
   while (true) { 
    String text = processReader.readLine(); 
    if (text.equals("readyok")) 
     break; 
    else 
     buffer.append(text + "\n"); 
   } 
  } catch (Exception e) { 
   e.printStackTrace(); 
  } 
  return buffer.toString(); 
 } 
  
 
// Draws the current state of the board through FEN code
 public void drawBoard(String fen) { 
  sendCommand("position fen " + fen); 
  sendCommand("d"); 
 
  String[] rows = getOutput(0).split("\n"); 
 
  for (int i = 1; i < 18; i++) { 
   System.out.println(rows[i]); 
  } 
 }  
}