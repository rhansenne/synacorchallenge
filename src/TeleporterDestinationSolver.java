/**
 * Calculates the correct destination value for the teleporter (to be set as 8th register value).
 * Takes a few seconds to complete.
 */
public class TeleporterDestinationSolver {

	static int[] stack = new int[4];
	static int[] stackrepetitions = new int[5]; // compress repetitions on the stack
	static int stackSize = 0;
		
	public static void main(String[] args) {
		for (int i=0;i<32768;i++)
			if (calculateR0(new int[] {4,1,i})==6) {
				System.out.println(i);
				break;
			}
	}
	
	private static int calculateR0(int[] regs) {
		while (true) {
			if (regs[0]!=0) {
				//6035
				if (regs[1]!=0) {
					//6048 - refactored to skip ahead based on Arithmetic Progression
					if (stackSize>0 && stack[stackSize-1]==regs[0])
						stackrepetitions[stackSize]+=regs[1];
					else {
						stack[stackSize++]=regs[0];
						stackrepetitions[stackSize]=regs[1]-1;
					}
					regs[1]=0;
				} else {
					regs[0]--;
					regs[1] = regs[2];
				}
			} else {
				//6030 - refactored to skip ahead based on Arithmetic Progression
				if (stackSize>1) {
					if (stack[stackSize-1]==1) {
						regs[1] = (int)(regs[1]+stackrepetitions[stackSize])%32768;
						stackrepetitions[stackSize] = 0;
					} else if (stack[stackSize-1]==2) {
						regs[1] = (int)(regs[1]+stackrepetitions[stackSize]*(regs[2]+1))%32768;
						stackrepetitions[stackSize] = 0;
					}
				}
				regs[0] = (regs[1]+1)% 32768;
				if (stackSize==1) {
					int multiplier = regs[2]+1;
					if (stack[stackSize-1]==3) {
						int addition = 2*regs[2]+1;
						for (int i=0;i<stackrepetitions[1];i++)
							regs[0] = (regs[0]*multiplier+addition)%32768;
					} else if (stack[stackSize-1]==2) {
						regs[0] += (stackrepetitions[1]*multiplier)%32768;
					} else if (stack[stackSize-1]==1) {
						regs[0] = (int)(regs[0]+stackrepetitions[1])%32768;
					}
					regs[1] = regs[0]-1;
					stackrepetitions[1] = 0;
				} 
				if(stackSize==0) return regs[0];		
				regs[1] = regs[0];
				if (stackrepetitions[stackSize]==0) regs[0] = stack[--stackSize];
				else { 
					regs[0] = stack[stackSize-1]; 
					stackrepetitions[stackSize]--;
				}
				regs[0]--;
			}			
		}
	}	
}
