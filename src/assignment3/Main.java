/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2018
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> badWords;
	static HashMap<String, String> pathDFS;

	public static void main(String[] args) throws Exception {

		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
        ArrayList<String> res = Main.getWordLadderBFS("svart", "money");
        printLadder(res);
		// TODO methods to read in words, output ladder
	}

    public static void initialize() {
        // initialize your static variables or constants here.
        // We will call this method before running our JUNIT tests.  So call it
        // only once at the start of main.
        badWords = new ArrayList<String>();
        pathDFS = new HashMap<String, String>();
    }

	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word.
	 * If command is /quit, return empty ArrayList. 
	 */
    public static ArrayList<String> parse(Scanner keyboard) {
    	// TO DO
    	ArrayList<String> inputList;
    	String[] input;
    	inputList = new ArrayList<String>();
    	input = keyboard.next().toLowerCase().split(" ");
    	if(input.length!=1 && !input[0].equals("/quit")) {
    		inputList.add(input[0]);
    		inputList.add(input[1]);
    	}
    	return inputList;
    }
	  
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		ArrayList<String> res = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		pathDFS.put(null, start);
		if(!dict.contains(start.toUpperCase()) || !dict.contains(end.toUpperCase())) {
			res.add(start);
			res.add(end);
		}
		if(helperDFS(start, end, dict)) {
			for(String word : pathDFS.values()) {
				res.add(word);
			}
		}
		else {
			res.add(start);
			res.add(end);
		}
		return res; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	int index=0;
    	ArrayList<String> res = new ArrayList();
		Set<String> dict = makeDictionary();
    	Queue<String> q = new LinkedList();
    	HashMap<String, String> map = new HashMap(); //created a hashmap instead of set to keep track of visited words and path between start and end
    	q.add(start);
		String prev_word="";
        if(!dict.contains(start.toUpperCase())|| !dict.contains(end.toUpperCase())){
            res.add(start);
            res.add(end);
            return res; // replace this line later with real return
        }
    	else{
    	    while(!q.isEmpty()){
                String word = ((LinkedList<String>) q).remove(0);
                if(word==start){
                    map.put(null, word);
                }

                if(word.equals(end.toUpperCase())){
                    res.add(start);
                    res.add(end);
                    return res;
                }
                if(word!=end){

                }
                char[] arr = word.toCharArray();
                for(int i =0; i< arr.length; i++){
                    for(char c='a'; c<'z';c++){
                        char t = arr[i];
                        if(t!=c)
                            arr[i]=c;
                        String new_word = new String(arr);
                        if(dict.contains(new_word.toUpperCase()) &&!map.containsKey(new_word) && !map.containsValue(new_word) && !q.contains(new_word)){
                            q.add(new_word);
                            map.put(new_word, word);
                            if(new_word.equals(end)){
                                ArrayList<String> arev = new ArrayList<>();
                                arev.add(end);
                                int n_index = index-1;
                                String w = end;
                                while(w!=null){
                                    w=map.get(w);
                                    arev.add(0,w);
                                }
                                arev.remove(0);
                                return arev;
                            }
                        }
                        arr[i]=t;
                    }
                }

            }
        }
		res.add(start);
    	res.add(end);
		return res; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
        if(ladder.size()==2)
            System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1));
        else{
            System.out.println("a " + (ladder.size()-2) + "-rung world ladder exists between "+ ladder.get(0) + " and " + ladder.get(ladder.size()-1));
            for( String s : ladder){
                System.out.println(s);
            }
        }
	}
	// TODO
	// Other private static methods here
	private static boolean helperDFS(String start, String end, Set<String> dict) {
		char[] startChar;
		String new_start;
		char hold;
		if(start.equals(end)) {
			return true;
		}
		startChar = start.toCharArray();
		for(int i =0; i<startChar.length; i++) {
			for(char j='a'; j<='z'; j++) {
				hold = startChar[i];
				if(startChar[i]!=j) {
					startChar[i]=j;
				}
				new_start = new String(startChar);
				if(dict.contains(new_start.toUpperCase()) && !badWords.contains(new_start) && !pathDFS.containsKey(new_start) && !pathDFS.containsValue(new_start)) {
					pathDFS.put(start, new_start);
					if(helperDFS(new_start, end, dict)) {
						return true;
					}
					else {
						pathDFS.remove(start);
					}
				}
				startChar[i] = hold;
			}
		}
		badWords.add(start);
		return false;
	}
	
	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
