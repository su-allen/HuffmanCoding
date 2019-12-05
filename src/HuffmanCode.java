// Allen Su
// CSE 143 D with Hunter Schafer and CSE 143 DL with I-Miao Chien
// Homework 8
// HuffmanCode implements the Huffman coding algorithm that compresses data to create
// a file that occupies a smaller memory space than before. HuffmanCode compresses a 
// file of english characters and write the Huffman code to a .code file. The class can
// use this .code file to decompress the file back into it's original format.

import java.io.*;
import java.util.*;


public class HuffmanCode {
	
	
	private HuffmanNode overallRoot; // stores huffman coding algorithem
	
	// post: creates a new HuffmanCode object from the given frequencies of each character
	// to be used when compressing the given file of english characters.
	public HuffmanCode(int[] frequencies) {
		Queue <HuffmanNode >huffmanQueue = new PriorityQueue <HuffmanNode>() ;
		for (int i = 0; i < frequencies.length; i ++) {
			if(frequencies[i] > 0) {
				huffmanQueue.add(new HuffmanNode((char) i, frequencies[i]));
			}
		}
		while (huffmanQueue.size() > 1) {
			HuffmanNode left = huffmanQueue.remove();
			HuffmanNode right = huffmanQueue.remove();
			HuffmanNode top = new HuffmanNode((char) 0, left.frequency + right.frequency);
			top.left = left;
			top.right = right;
			huffmanQueue.add(top);
			
		}
		overallRoot = huffmanQueue.remove();
	}
	
	// post: creates a new HuffmanCode object by reading a huffman code from a .code file
	// for decompressing the given file of english characters.
	public HuffmanCode(Scanner input) {
		overallRoot = new HuffmanNode((char) 0, 0);
		while (input.hasNext()) {
			int asciiValue = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			overallRoot = helperHuffmanCode(asciiValue, code, overallRoot);
		}
		
	}
	// Helper Method
	// post: creates a new HuffmanCode object by reading a huffman code from a .code file
	private HuffmanNode helperHuffmanCode(int asciiValue, String code, HuffmanNode root) {	
		if(code.length() == 0) {
			return new HuffmanNode((char) asciiValue, 0);
		} else if (root == null) {
			root = new HuffmanNode((char) 0, 0);
		}
		if(code.substring(0,1).equals("0")) {
			root.left = helperHuffmanCode(asciiValue, code.substring(1), root.left);
		} else {
			root.right = helperHuffmanCode(asciiValue, code.substring(1), root.right);
		}
		return root;
	}
	
	// post: stores the current huffman code to the given output in the format of 
	// ascii number corresponding to a english character followed by its huffman code, each
	// on its own line. This repeats for each character and its code.
	public void save(PrintStream output) {
		saveHelper(overallRoot, output, "");
	}
	
	// Helper method
	// post: stores the current huffman code to the given output stream in the format of 
	// ascii number corresponding to a english character followed by its huffman code, each
	// on its own line. This repeats for each character and its code.
	private void saveHelper(HuffmanNode root, PrintStream output, String s) {
		if (root != null) {	
			if (root.character != 0) {
				output.println((int) root.character);
				output.println(s);
			}
			saveHelper(root.left, output, s + 0);
			saveHelper(root.right, output, s + 1);
		}
	}
	
	// post: decompresses a file by reading from a given huffman code and writing the 
	// corresponding characters of the code to the given output.
	public void translate(BitInputStream input, PrintStream output) {
		while(input.hasNextBit()) {
			translateHelper(input, output, overallRoot);
		}
	}
	
	// Helper method
	// post: reads from a given huffman code and writes the corresponding characters of 
	// the code to the given output.
	private void translateHelper(BitInputStream input, PrintStream output, HuffmanNode root) {
		if (root.left == null && root.right == null) {	
			output.write((int) root.character);
		} else if (input.hasNextBit()) {
			int bit = input.nextBit();
			if (bit == 0) {
				translateHelper(input,output,root.left);
			} else {
				translateHelper(input,output,root.right);
			}
		}	
		
	}
	
	//HuffmanNode class represents a single node in a tree used by the HuffmanCode class.
	private static class HuffmanNode implements Comparable{
    	public char character; // stores a character 
    	public int frequency; // stores frequency of character
        public HuffmanNode left; // reference to left subtree
        public HuffmanNode right; // reference to right subtree

        // post: constructs a node with the given type and data.
        public HuffmanNode(char character, int frequency) {
        	this.character = character;
        	this.frequency = frequency;
        }
        
        // post: compares the frequency of a character between two huffman nodes 
		public int compareTo(Object other) {
			HuffmanNode otherNode = (HuffmanNode) other;
			return this.frequency - otherNode.frequency;
		}



    }
}
