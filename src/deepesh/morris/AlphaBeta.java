package deepesh.morris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class AlphaBeta{
	static int count=0;
	
	class node{
		char[] boardPositions = new char[21];
		int value;
		node parent=null;
		
		node(int val) {
			this.boardPositions = null;
			this.value = val;
		}
		node(char[] b,int val) {
			this.boardPositions = b;
			this.value = val;
		}
		
		node(char[] b,node parent,int val) {
			this.boardPositions = b;
			this.value = val;
			this.parent=parent;
		}
	}
	
	public void ABOpening(String inputFile, String outputFile, int depth) throws IOException {
		
		
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
			
			
			node result = MaxMin(board,depth,true,'o',Integer.MIN_VALUE,Integer.MAX_VALUE); //openingOrMidgame = 'o' for opening, 'm' for mid-end game
			
			System.out.println("Input position:  "+st);
			System.out.println("Output position: "+String.valueOf(result.boardPositions));
			System.out.println("Positions evaluated by static estimation: "+count);
			System.out.println("MINIMAX estimate: "+result.value);
			File fout = new File(outputFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fout));
			bw.write(String.valueOf(result.boardPositions));
			bw.newLine();
			bw.close();
		}
		
	}
		public void ABGame(String inputFile, String outputFile, int depth) throws IOException {
			

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
				
				node result = MaxMin(board,depth,true,'m',Integer.MIN_VALUE,Integer.MAX_VALUE); // openingOrMidgame = o for opening, m for mid-end game
				
				System.out.println("Input position:  "+st);
				System.out.println("Output position: "+String.valueOf(result.boardPositions));
				System.out.println("Positions evaluated by static estimation: "+count);
				System.out.println("MINIMAX estimate: "+result.value);
				File fout = new File(outputFile);
				BufferedWriter bw = new BufferedWriter(new FileWriter(fout));
				bw.write(String.valueOf(result.boardPositions));
				bw.newLine();
				bw.close();
				
			}
		
		
	}
		
	
	
	public node MaxMin(node board,int depth, boolean ismaxPlayer,char openingOrMidgame,int alpha,int beta) {
		// openingOrMidgame = o for opening, m for mid-end game
		node newNode = new node(Integer.MIN_VALUE);
		
		if (depth == 0) // or board = final position
	        {
	
					if (openingOrMidgame == 'm') 
					{
						staticEvalMidEnd(board);
						return board;
					}
					else 
					{
						staticEvalOpening(board);
						return board;
					}
					
				
	        }
		else
			{
				ArrayList<node> l = new ArrayList<>();
				
				if(openingOrMidgame == 'o') {
					l = generateMovesOpening(board,ismaxPlayer);
				}
				else if (openingOrMidgame == 'm') {
					l = generateMovesMidgameEndgame(board,ismaxPlayer);
				}
				

				
				for( node n : l)
				{
					node tempNode = MinMax(n,depth-1,!ismaxPlayer,openingOrMidgame, alpha, beta);
					if(tempNode.value>newNode.value)
					{
						newNode.value=tempNode.value;
						newNode.boardPositions=n.boardPositions;
						board.value=tempNode.value;
						if(newNode.value>=beta)
						{
							return newNode;
						}
						else
						{
							alpha = Math.max(alpha, tempNode.value);
						}
					}
					
				}
				
				return newNode;
			}
	}
	
	public node MinMax(node board,int depth,boolean ismaxPlayer,char openingOrMidgame,int alpha,int beta) {
		
		node newNode = new node(Integer.MAX_VALUE);
		
		if (depth == 0) // or board = final position
	        {
	
					if (openingOrMidgame == 'm') 
					{
						staticEvalMidEnd(board);
						return board;
					}
					else 
					{
						staticEvalOpening(board);
						return board;
					}
					
	        }
		else
			{
				ArrayList<node> l = new ArrayList<>();
				
				if(openingOrMidgame == 'o') {
					l = generateMovesOpening(board,ismaxPlayer);
				}
				else if (openingOrMidgame == 'm') {
					l = generateMovesMidgameEndgame(board,ismaxPlayer);
				}
				
				for( node n : l)
				{
					node tempNode = MaxMin(n,depth-1,!ismaxPlayer,openingOrMidgame, alpha, beta);

					if(tempNode.value<newNode.value)
					{
						newNode.value=tempNode.value;
						newNode.boardPositions=n.boardPositions;
						board.value=tempNode.value;
						if(newNode.value<=alpha)
						{
							return newNode;
						}
						else
						{
							beta = Math.max(beta, tempNode.value);
						}
					}
					
				}
				
				return newNode;
			}

	}
	
	public ArrayList<node> generateMovesOpening(node board,boolean ismaxPlayer) {
		ArrayList<node> l = new ArrayList<>();

		if(ismaxPlayer)
		{
			l=generateAddWhite(board);
		}
		else
		{
			l=generateAddBlack(board);
		}

		return l;
	}
	
	public ArrayList<node> generateMovesMidgameEndgame(node board,boolean ismaxPlayer) {
		ArrayList<node> l = new ArrayList<>();
		
		int whitePieces=0;
		
		for(int i=0; i<board.boardPositions.length; i++)
		{
			if(board.boardPositions[i] == 'W')
			{
				whitePieces++;
			}
			
		}
		 
			if(whitePieces == 3) 
		{
			
			if(ismaxPlayer)
			{
				l=generateHoppingWhite(board);
			}
			else
			{
				l=generateHoppingBlack(board);
			}
		}
		else
		{
			if(ismaxPlayer)
			{
				l=generateMoveWhite(board);
			}
			else
			{
				l=generateMoveBlack(board);
			}
		}

		return l;
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
	
	public ArrayList<node> generateMoveWhite(node n) {
		ArrayList<node> l = new ArrayList<>();
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='W')
			{
				ArrayList<Integer> listNeighbors = neighors(i);
				for(int j : listNeighbors)
				{
					if(n.boardPositions[j]=='X')
					{
						String st = String.valueOf(n.boardPositions);
						node temp = new node(st.toCharArray(),n,0);
						temp.boardPositions[i] = 'X';
						temp.boardPositions[j] = 'W';
						if(closeMill(j,temp))
						{
							generateRemove(temp,l);
						}
						else {
							l.add(temp);
						}
					}
				}
			}
		}
		return l;
	}
	
	public ArrayList<node> generateMoveBlack(node n) {
		ArrayList<node> l = new ArrayList<>();
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='B')
			{
				ArrayList<Integer> listNeighbors = neighors(i);
				for(int j : listNeighbors)
				{
					if(n.boardPositions[j]=='X')
					{
						String st = String.valueOf(n.boardPositions);
						node temp = new node(st.toCharArray(),n,0);
						temp.boardPositions[i] = 'X';
						temp.boardPositions[j] = 'B';
						if(closeMill(j,temp))
						{
							generateRemove(temp,l);
						}
						else {
							l.add(temp);
						}
					}
				}
			}
		}
		return l;
	}
	
	public ArrayList<node> generateHoppingWhite(node n) {
		
		ArrayList<node> l = new ArrayList<>();
		
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='W')
			{
				for(int j=0; j<n.boardPositions.length; j++)
				{
					if(n.boardPositions[j]=='X')
					{
						String st = String.valueOf(n.boardPositions);
						node temp = new node(st.toCharArray(),n,0);
						temp.boardPositions[i]='X';
						temp.boardPositions[j]='W';
						
						if(closeMill(j,temp))
						{
							generateRemove(temp,l);
						}
						else {
							l.add(temp);
						}
					}
				}
			}
		}
		
		return l;
	}
	
public ArrayList<node> generateHoppingBlack(node n) {
		
		ArrayList<node> l = new ArrayList<>();
		
		for(int i=0; i<n.boardPositions.length; i++)
		{
			if(n.boardPositions[i]=='B')
			{
				for(int j=0; j<n.boardPositions.length; j++)
				{
					if(n.boardPositions[j]=='X')
					{
						String st = String.valueOf(n.boardPositions);
						node temp = new node(st.toCharArray(),n,0);
						temp.boardPositions[i]='X';
						temp.boardPositions[j]='B';
						
						if(closeMill(j,temp))
						{
							generateRemove(temp,l);
						}
						else {
							l.add(temp);
						}
					}
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
	
	public ArrayList<Integer> neighors(int boardPos){
		ArrayList<Integer> nb = new ArrayList<>();
		
		switch(boardPos) {
			case 0: nb.add(1);
					nb.add(2);
					nb.add(6);
					break;
			case 1: nb.add(0);
					nb.add(11);
					break;

			case 2: nb.add(0);
					nb.add(4);
					nb.add(7);
					nb.add(3);
					break;
			case 3: nb.add(10);
					nb.add(2);
					break;

			case 4: nb.add(8);
					nb.add(2);
					nb.add(5);
					break;
			case 5: nb.add(4);
					nb.add(9);
					break;

			case 6: nb.add(7);
					nb.add(18);
					nb.add(0);
					break;
			case 7: nb.add(6);
					nb.add(8);
					nb.add(2);
					nb.add(15);
					break;

			case 8: nb.add(4);
					nb.add(7);
					nb.add(12);
					break;
			case 9: nb.add(5);
					nb.add(10);
					nb.add(14);
					break;

			case 10: nb.add(3);
					nb.add(9);
					nb.add(11);
					nb.add(17);
					break;
			case 11: nb.add(1);
					nb.add(10);
					nb.add(20);
					break;

			case 12: nb.add(8);
					nb.add(13);
					break;
			case 13: nb.add(14);
					nb.add(12);
					nb.add(16);
					break;

			case 14: nb.add(9);
					nb.add(13);
					break;
			case 15: nb.add(7);
					nb.add(16);
					break;

			case 16: nb.add(19);
					nb.add(13);
					nb.add(15);
					nb.add(17);
					break;
			case 17: nb.add(10);
					nb.add(16);
					break;

			case 18: nb.add(19);
					nb.add(6);
					break;
			case 19: nb.add(20);
					nb.add(18);
					nb.add(16);
					break;
			case 20: nb.add(19);
					nb.add(11);
					break;
		}
		
		return nb;
	}
	
	
	
	public void staticEvalOpening(node board)
	{
		int whitePieces=0;
		int blackPieces=0;
		count++;
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
		
		board.value = whitePieces-blackPieces;
	}
	
	public void staticEvalMidEnd(node board)
	{
		int whitePieces=0;
		int blackPieces=0;
		count++;
		
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
		
		
		if (blackPieces <= 2) 
		{
			board.value = 10000;
		}
		else if (whitePieces <= 2) 
		{
			board.value = -10000;
		}
		
		ArrayList<node> l = generateMovesMidgameEndgame(board, false);
		int numBlackMoves = l.size();
		
		if (numBlackMoves==0)
		{
			board.value = -10000;
		}
		else 
			{board.value = 1000*(whitePieces - blackPieces) - numBlackMoves;}
	}

	public static void main(String[] args) {
		
		AlphaBeta ab = new AlphaBeta();

//		String inputfile = "C:\\Users\\Deepesh\\eclipse-workspace\\AI_morris_game\\src\\deepesh\\morris\\input.txt";
//		String outputfile = "C:\\Users\\Deepesh\\eclipse-workspace\\AI_morris_game\\src\\deepesh\\morris\\output.txt";
		String inputfile=args[0];
		String outputfile=args[1];
		int depth = Integer.parseInt(args[2]);
		try {
			ab.ABOpening(inputfile, outputfile, depth);
			ab.ABGame(inputfile, outputfile, depth);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
