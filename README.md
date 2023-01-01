# Synacor Challenge Solution in Java

My (quick & dirty) solution in Java for the Synacor Challenge: https://challenge.synacor.com/

**SPOILERS ahead** - don't read on if you plan to attempt the challenge yourself!
                                                                          
- `VM` 
    interprets and executes the "challenge.bin" binary, running through the text adventure using the instructions from the file 'instructions.txt' or from manual input (if the file is not found). The teleporter check is overridden after the self test completes and the teleporter location is set to the destination calculated using TelePorterDestinationSolver.
- `CoinEquationSolver` 
    determines the correct order the coins should be inputted to unlock the door.
- `TeleporterDestinationSolver` 
    calculates the correct destination value for the teleporter (to be set as 8th register value). Takes a few seconds to search the complete solution space rather than the billions of years the original function in the binary would take:). 
    I reverse engineered the logic of the expensive function, converted the recursion to iteration and compressed the stack usage by storing repetitions on the stack as a single values with their number of repetitions.
    After analysis of these repetitions, it became clear these grow following an arithmetic sequence. This predictability allows skipping over large amounts of iterations. 
    It could certainly be optimized further, but it's good and fast enough for the purpose of solving the puzzle :).  
- `OrbPathSolver` 
    determines the correct path from the orb room to the vault room. 
