import java.util.*;

/**
 * Determines the correct path from the orb room to the vault room.
 */
public class OrbPathSolver {

	static final char[][] MAZE = new char[][] {
		{ '*' ,  8  , '-' ,  1  },
		{ 4   , '*' ,  11 , '*' },
		{ '+' ,  4  , '-' ,  18 },
		{ 22  , '-' ,  9  , '*' }
	};
	static final int ROW_START = 3;
	static final int COL_START = 0;
	static final int ROW_END = 0;
	static final int COL_END = 3;
	
	static class Path {
		int lastRow, lastCol, weight;
		String path="";
		char operator='+';
		Path(int r, int c, int w, char o, String p) {this.lastRow=r; this.lastCol=c; this.weight=w; this.operator=o; this.path=p;}
	}
	
	public static void main(String[] args) {
		List<Path> paths = new ArrayList<Path>();
		Path startPath = new Path(ROW_START,COL_START,1,'*',"");
		paths.add(startPath);
		List<Path> nextPaths = new ArrayList<Path>();
		outer: while(true) {
			for (Path path:paths) {
				char roomValue = MAZE[path.lastRow][path.lastCol];
				if (roomValue>22) path.operator=roomValue;
				else switch (path.operator) { // update orb weight
					case '+': path.weight += roomValue; break;
					case '-': path.weight -= roomValue; break;
					case '*': path.weight *= roomValue; break;
				}
				if (path.weight<=0) continue; // orb shatters
				if (path.lastRow==ROW_START && path.lastCol==COL_START && paths.size()>1) continue; // orb evaporates
				if (path.lastRow==ROW_END && path.lastCol==COL_END) {
					if (path.weight==30) { // path found
						System.out.println(path.path);
						break outer;
					} else continue; // orb evaporates
				} else {
					if (path.lastRow>0) { // go north
						Path nextPath = new Path(path.lastRow-1,path.lastCol,path.weight,path.operator,path.path+System.lineSeparator()+"north");
						nextPaths.add(nextPath);
					}
					if (path.lastRow<MAZE.length-1) { // go south
						Path nextPath = new Path(path.lastRow+1,path.lastCol,path.weight,path.operator,path.path+System.lineSeparator()+"south");
						nextPaths.add(nextPath);
					}
					if (path.lastCol>0) { // go west
						Path nextPath = new Path(path.lastRow,path.lastCol-1,path.weight,path.operator,path.path+System.lineSeparator()+"west");
						nextPaths.add(nextPath);
					}
					if (path.lastCol<MAZE[0].length-1) { // go east
						Path nextPath = new Path(path.lastRow,path.lastCol+1,path.weight,path.operator,path.path+System.lineSeparator()+"east");
						nextPaths.add(nextPath);
					}
				}
			}
			paths = nextPaths;
			nextPaths = new ArrayList<Path>();
		}
	}
}