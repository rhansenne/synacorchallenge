import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Interprets and executes the binary, running through the text adventure using the instructions from the file 'instructions.txt' or from manual input (if the file is not found).
 * The teleporter check is overridden after the self test completes and the teleporter location is set to the destination calculated using TelePorterDestinationSolver. 
 */
public class VM {

	static final String BINARY_FILE = "synacor-challenge\\challenge.bin";
	static final String INSTRUCTIONS_FILE = "src\\instructions.txt";
	static int[] registers = new int[8];
	static Deque<Integer> stack = new ArrayDeque<Integer>();
	
	static boolean selfTestCompleted = false;
	public static void main(String[] args) throws Exception {
		// read instructions from file if available; else read manual input
		Reader reader = new File(INSTRUCTIONS_FILE).exists() ? new BufferedReader(new FileReader(INSTRUCTIONS_FILE)) : new InputStreamReader(System.in);		
		// read binary
		byte[] littleEndian = Files.readAllBytes(Paths.get(BINARY_FILE));
		// convert to Big Endian
		int[] memory = new int[littleEndian.length/2]; 
		for (int i=0; i<littleEndian.length; i+=2)  memory[i/2] = (littleEndian[i] & 0xff) + (littleEndian[i+1] & 0xff) * 256;
		// execute
		for (int i=0;i<memory.length;i++) {
			// set new teleporter location after selftest completion
			if (memory[i+1]==32775) {
				if (!selfTestCompleted) selfTestCompleted=true;
					else {
						registers[7]=25734; // calculated using TeleporterDestinationSolver						
						registers[0]=6; // skip calculation; jump to and override teleporter check
						i=5491;
					}
			}
			switch (getVal(memory[i])) {			
				case 0 -> { // halt
					reader.close();
					System.exit(0);					
				}
				case 1 -> { // set
					registers[memory[i+1]-32768]=getVal(memory[i+2]);
					i+=2;
				}
				case 2 -> { // push
					stack.push(registers[memory[i+1]-32768]);
					i++;
				}				
				case 3 -> { // pop
					registers[memory[i+1]-32768] =stack.pop();
					i++;
				}				
				case 4 -> { // eq
					registers[memory[i+1]-32768] = getVal(memory[i+2]) == getVal(memory[i+3]) ? 1 : 0;
					i+=3;
				}	
				case 5 -> { // gt
					registers[memory[i+1]-32768] = getVal(memory[i+2]) > getVal(memory[i+3]) ? 1 : 0;
					i+=3;
				}				
				case 6 -> { // jmp
					i = getVal(memory[i+1])-1;
				}
				case 7 -> { // jt
					i = getVal(memory[i+1])!=0 ? getVal(memory[i+2])-1 : i+2;
				}
				case 8 -> { // jf
					i = getVal(memory[i+1])==0 ? getVal(memory[i+2])-1 : i+2;
				}
				case 9 -> { // add
					registers[memory[i+1]-32768] = (getVal(memory[i+2]) + getVal(memory[i+3])) % 32768;
					i+=3;
				}				
				case 10 -> { // mul
					registers[memory[i+1]-32768] = (getVal(memory[i+2]) * getVal(memory[i+3])) % 32768;
					i+=3;
				}	
				case 11 -> { // mod
					registers[memory[i+1]-32768] = (getVal(memory[i+2]) % getVal(memory[i+3])) % 32768;
					i+=3;
				}					
				case 12 -> { // and
					registers[memory[i+1]-32768] = (getVal(memory[i+2]) & getVal(memory[i+3])) % 32768;
					i+=3;
				}					
				case 13 -> { // or
					registers[memory[i+1]-32768] = (getVal(memory[i+2]) | getVal(memory[i+3])) % 32768;
					i+=3;
				}	
				case 14 -> { // not
					registers[memory[i+1]-32768] = (~getVal(memory[i+2]) & 0x7fff) % 32768;
					i+=2;
				}	
				case 15 -> { // rmem
					registers[memory[i+1]-32768] = getVal(memory[getVal(memory[i+2])]);
					i+=2;
				}				
				case 16 -> { // wmem
					memory[getVal(memory[i+1])] = getVal(memory[i+2]);
					i+=2;
				}				
				case 17 -> { // call
					stack.push(i+2);
					i = getVal(memory[i+1])-1;
				}		
				case 18 -> { // ret
					i = stack.pop() - 1;
				}								
				case 19 -> { // out
					System.out.print((char)getVal(memory[++i]));
				}
				case 20 -> { // in
					int c = reader.read(); // read from instructions file
					if (c==-1) break; // EOF
					if (c==13) c = reader.read(); // skip Carriage Return
					System.out.print((char)c);					
					registers[memory[i+1]-32768] = c;
					i++;
				}				
				default -> { /* noop */ }
			}
		}
	}
	
	static int getVal(int v) {
		return v>=32768? getVal(registers[v-32768]) : v;
	}	
}
