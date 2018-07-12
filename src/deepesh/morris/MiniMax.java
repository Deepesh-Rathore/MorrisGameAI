

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MiniMax{
	
	class node{
		char[] boardPositions = new char[21];
		int depth;
		int value;
		node parent=null;
		node(char[] b,int val) {
			this.boardPositions = b;
			this.value = val;
			this.depth = 0;
		}
		node(char[] b,int val, int d) {
			this.boardPositions = b;
			this.value = val;
			this.depth = d;
		}
		node(char[] b,node parent,int val) {
			this.boardPositions = b;
			this.value = val;
			this.depth = 0;
			this.parent=parent;
		}
	}
	
	public void MiniMaxOpening(String inputFile, String outputFile, int depth) throws IOException {
		
//		char[] bpositions = new char[21];
		File file = new File(inputFile);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		
		if ((st = br.readLine()) == null)
		{
		    System.out.println("Empty input file");
		    br.close();
		}
		else
		{
			char[] bpositions=st.toCharArray();
			br.close();
			
			node board = new node(bpositions,0);
			
			node result = MaxMin(board,depth,true);
			
			System.out.println(result.boardPositions);
			
			File fout = new File(outputFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fout));
			bw.write(String.valueOf(result.boardPositions));
			bw.newLine();
			bw.close();
			System.out.println("Done! check output file");
		}
		
		
		
		
		
	}
	
	public node MaxMin(node board,int depth, boolean ismaxPlayer) {
		
		if (depth == 0) // or board = final position
	        {
				System.out.println("dept 0: "+staticEvalOpening(board));
				return new node(board.boardPositions,staticEvalOpening(board));
	        }
		else
			{
				int v = Integer.MIN_VALUE;
				char[] tempPositions= {};
				ArrayList<node> l = new ArrayList<>();
				
				if(ismaxPlayer)
				{
					l=generateAddWhite(board);
				}
				else
				{
					l=generateAddBlack(board);
				}
				
				
				for( node n : l)
				{
					node tempNode = MinMax(n,depth-1,!ismaxPlayer);
					tempNode.parent = board;
					if(tempNode.value>v) // max
					{
						v=tempNode.value;
//						tempPositions= tempNode.boardPositions;
					}
				}
				board.value=v;
				return board;
//				return new node(tempPositions,v);
			}

	}
	
	public node MinMax(node board,int depth,boolean ismaxPlayer) {
		
		if (depth == 0) // or board = final position
	        {
				return new node(board.boardPositions,staticEvalOpening(board));
	        }
		else
			{
				int v = Integer.MAX_VALUE;
				char[] tempPositions= {};
				ArrayList<node> l = new ArrayList<>();
				
				if(ismaxPlayer)
				{
					l=generateAddWhite(board);
				}
				else
				{
					l=generateAddBlack(board);
				}
				
				for( node n : l)
				{
					node tempNode = MaxMin(n,depth-1,!ismaxPlayer);
					if(tempNode.value<v) // min
					{
						v=tempNode.value;
						tempPositions= tempNode.boardPositions;
					}
					
				}
				return new node(tempPositions,v);
			}

	}
	
	public ArrayList<node> generateAddWhite(node n) {
		
		ArrayList<node> l = new ArrayList<>();
		
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='X')
			{
				String st = String.valueOf(n.boardPositions);
				node temp = new node(st.toCharArray(),n,0);
				temp.boardPositions[i] = 'W';
				if(closeMill(i,temp))
				{
					generateRemove(temp,l);
				}
				else {
					l.add(temp);
				}
				
			}
		}
		
		return l;
	}
	
	public ArrayList<node> generateAddBlack(node n) {
		
		ArrayList<node> l = new ArrayList<>();
		
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='X')
			{
				String st = String.valueOf(n.boardPositions);
				node temp = new node(st.toCharArray(),n,0);
				temp.boardPositions[i] = 'B';
				if(closeMill(i,temp))
				{
					generateRemove(temp,l);
				}
				else {
					l.add(temp);
				}
				
			}
		}
		
		return l;
	}
	
	public void generateRemove(node n,ArrayList<node> l) {
		
		boolean addedFlag = false;
		
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i] == 'B')
			{
				if(! closeMill(i, n))
				{
					String st = String.valueOf(n.boardPositions);
					node temp = new node(st.toCharArray(),n.parent,0);
					temp.boardPositions[i] = 'X';
					l.add(temp);
					addedFlag = true;
				}
			}
		}
		
		if(addedFlag == false)
		{
			l.add(n);
		}
	}
	
	public boolean closeMill(int i, node n) {
		
		char c = n.boardPositions[i];
		switch(i) {
			case 0: if((n.boardPositions[2] == c && n.boardPositions[4] == c) || (n.boardPositions[6] == c && n.boardPositions[18] == c)) 
						{return true;}
					else
						{return false;}
			case 1: if(n.boardPositions[11] == c && n.boardPositions[20] == c)
						{return true;}
					else
						{return false;} 
			case 2: if((n.boardPositions[0] == c && n.boardPositions[4] == c) || (n.boardPositions[7] == c && n.boardPositions[15] == c)) 
						{return true;}
					else
						{return false;}
			case 3: if(n.boardPositions[10] == c && n.boardPositions[17] == c) 
						{return true;}
					else
						{return false;}
			case 4: if((n.boardPositions[2] == c && n.boardPositions[0] == c) || (n.boardPositions[8] == c && n.boardPositions[12] == c)) 
						{return true;}
					else
						{return false;}
			case 5: if(n.boardPositions[9] == c && n.boardPositions[14] == c) 
						{return true;}
					else
						{return false;} 
			case 6: if((n.boardPositions[0] == c && n.boardPositions[18] == c) || (n.boardPositions[7] == c && n.boardPositions[8] == c)) 
						{return true;}
					else
						{return false;}
			case 7: if((n.boardPositions[2] == c && n.boardPositions[15] == c) || (n.boardPositions[6] == c && n.boardPositions[8] == c)) 
						{return true;}
					else
						{return false;}
			case 8: if((n.boardPositions[4] == c && n.boardPositions[12] == c) || (n.boardPositions[6] == c && n.boardPositions[7] == c)) 
						{return true;}
					else
						{return false;}
			case 9: if((n.boardPositions[10] == c && n.boardPositions[11] == c) || (n.boardPositions[5] == c && n.boardPositions[14] == c)) 
						{return true;}
					else
						{return false;} 
			case 10: if((n.boardPositions[3] == c && n.boardPositions[17] == c) || (n.boardPositions[9] == c && n.boardPositions[11] == c)) 
						{return true;}
					else
						{return false;}
			case 11: if((n.boardPositions[9] == c && n.boardPositions[10] == c) || (n.boardPositions[1] == c && n.boardPositions[20] == c)) 
						{return true;}
					else
						{return false;}
			case 12: if((n.boardPositions[4] == c && n.boardPositions[8] == c) || (n.boardPositions[13] == c && n.boardPositions[14] == c)) 
						{return true;}
					else
						{return false;}
			case 13: if((n.boardPositions[16] == c && n.boardPositions[17] == c) || (n.boardPositions[12] == c && n.boardPositions[14] == c)) 
						{return true;}
					else
						{return false;} 
			case 14: if((n.boardPositions[12] == c && n.boardPositions[13] == c) || (n.boardPositions[5] == c && n.boardPositions[9] == c)) 
						{return true;}
					else
						{return false;}
			case 15: if((n.boardPositions[16] == c && n.boardPositions[17] == c) || (n.boardPositions[2] == c && n.boardPositions[7] == c)) 
						{return true;}
					else
						{return false;}
			case 16: if((n.boardPositions[13] == c && n.boardPositions[19] == c) || (n.boardPositions[15] == c && n.boardPositions[17] == c)) 
						{return true;}
					else
						{return false;}
			case 17: if((n.boardPositions[15] == c && n.boardPositions[16] == c) || (n.boardPositions[3] == c && n.boardPositions[10] == c)) 
						{return true;}
					else
						{return false;} 
			case 18: if((n.boardPositions[0] == c && n.boardPositions[6] == c) || (n.boardPositions[19] == c && n.boardPositions[20] == c)) 
						{return true;}
					else
						{return false;}
			case 19: if((n.boardPositions[20] == c && n.boardPositions[18] == c) || (n.boardPositions[16] == c && n.boardPositions[13] == c)) 
						{return true;}
					else
						{return false;}
			case 20: if((n.boardPositions[1] == c && n.boardPositions[11] == c) || (n.boardPositions[18] == c && n.boardPositions[19] == c)) 
						{return true;}
					else
						{return false;}
		}
		
		return false;
		
	}
	
//	static int minimax(int depth, int nodeIndex, boolean  isMax,
//            int scores[], int h)
//	{
//	    // Terminating condition. i.e leaf node is reached
//	    if (depth == h)
//	        return scores[nodeIndex];
//	 
//	    // If current move is maximizer, find the maximum attainable
//	    // value
//	    if (isMax)
//	    return Math.max(minimax(depth+1, nodeIndex*2, false, scores, h),
//	            minimax(depth+1, nodeIndex*2 + 1, false, scores, h));
//	 
//	    // Else (If current move is Minimizer), find the minimum
//	    // attainable value
//	    else
//	        return Math.min(minimax(depth+1, nodeIndex*2, true, scores, h),
//	            minimax(depth+1, nodeIndex*2 + 1, true, scores, h));
//	}
	
	
//	public static int alphaBeta(StateNode node, int depth, int a, int b, boolean player) {
//		if (depth == 0 || node.state.endGame()) {
//			return node.state.evaluate();
//		}
//
//		Enumeration<StateNode> children = node.children();
//		StateNode child;
//		int alpha = a, beta = b;
//
//		if (player == MAX_PLAYER) {
//			while (children.hasMoreElements()) {
//				child = children.nextElement();
//				alpha = max(alpha, alphaBeta(child, depth - 1, alpha, beta, !player), node, child);
//				if (beta <= alpha)
//					break; // beta cut-off
//			}
//			return alpha;
//		} else {
//			while (children.hasMoreElements()) {
//				child = children.nextElement();
//				beta = min(beta, alphaBeta(child, depth - 1, alpha, beta, !player), node, child);
//				if (beta <= alpha)
//					break; // alpha cut-off
//			}
//			return beta;
//		}
//	}
	
	public int staticEvalOpening(node board)
	{
		int whitePieces=0;
		int blackPieces=0;
		
		for(int i=0; i<board.boardPositions.length; i++)
		{
			if(board.boardPositions[i] == 'W')
			{
				whitePieces++;
			}
			else if(board.boardPositions[i] == 'B')
			{
				blackPieces++;
			}
		}
		
		return whitePieces-blackPieces;
	}
	
//	public int staticEvalMidEnd(int w, int b, int bmoves)
//	{
//		if (b <= 2) 
//		{
//			return 10000;
//		}
//		else if (w <= 2) 
//		{
//			return -10000;
//		}
//		else if (bmoves==0)
//			{
//			return 10000;
//			}
//		else 
//			return  1000*(w - b) - bmoves;
//	}

	public static void main(String[] args) {
		
		MiniMax mm = new MiniMax();
//		String s = "WXXXXXXXXXXXXXXXBXXXX";
//		node n1 = mm.new node(s.toCharArray(),0);
//		String t = String.valueOf(n1.boardPositions);
//		node n2 = mm.new node(t.toCharArray(),0);
//		n1.boardPositions[1]='B';
		
		
//		System.out.println(n1.boardPositions[1]+" "+n2.boardPositions[1]);
//		System.out.println(mm.closeMill(5, n1));
		
//		ArrayList<node> l = mm.generateAddWhite(n1);
//		System.out.println(l.size());
//		for(node element : l)
//		{
//			System.out.println(element.boardPositions);
//		}
//		
		String inputfile = "D:\\mywork\\git\\MorrisGameAI\\src\\deepesh\\morris\\input.txt";
		String outputfile = "D:\\mywork\\git\\MorrisGameAI\\src\\deepesh\\morris\\output.txt";
		
		try {
			mm.MiniMaxOpening(inputfile, outputfile, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
