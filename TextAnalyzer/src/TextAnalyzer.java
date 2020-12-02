import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;


/**
 * HashMap key represents word that has occurred and value that matches key is the stored occurrences of that word.
 * There can not be duplicates of a key stored in HashMap
 * @author adam
 *
 */
public class TextAnalyzer extends Application {
	private static ListView<String> listView = new ListView<String>();
	private static String word;
	private static String cnt;
	private static HashSet set = new HashSet();
	private static LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
	private static HashMap<String, Integer> temp = new HashMap<String, Integer>();
	public String getWord() {
		return word;
	}
	
	/**
	 * Constructs and initializes a word
	 * @param word word
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
	/**
	 * Returns word count
	 * @return
	 */
	public String getCount() {
		return cnt;
	}
	
	/**
	 * Constructs and initializes a count for number of occurrences of a word
	 * @param cnt count
	 */
	public void setCount(Integer cnt) {
		this.cnt = cnt.toString();
	}
	/**
	 * calls init
	 */
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}
	
	
	
	
	
	/**
	 * Initiates the JavaFX listview.
	 * Populates listview with values from sorted HashMap.
	 * @param primaryStage.
	 */
	
	public void init(Stage primaryStage) {
		primaryStage.setTitle("Word Occurences");
		ObservableList<String> names = FXCollections.observableArrayList(
				 );
	    listView.setItems(names); 
	    for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
			setWord(entry.getKey());
			setCount(entry.getValue());
			String newStr = getWord() + " " + getCount();
			listView.getItems().add(newStr);
		}
		listView.getItems().add(getWord());
		listView.setPrefHeight(names.size()*20 + 2);
		listView.setMaxSize(400, 960);
	    VBox layout = new VBox(15);
	    layout.setPadding(new Insets(5, 5, 5, 5));
	    layout.getChildren().addAll(listView);
	    layout.setStyle("-fx-background-color: BEIGE");
	    //Setting the stage
	    Scene scene = new Scene(layout, 800, 1000);
		
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	
	
	/**
	 * Compares words and checks for duplicates.
	 * @param word if word is duplicate HashSet will not allow duplicate word to be added.
	 * @return
	 */
	public static boolean checkDuplicates(String word) {
		//if word is duplicate HashSet add() will return false;
		boolean add = set.add(word);
		if (!add) {	
			return true;
		}
		return false;
	}
	/**
	 * Creates HashMap that counts the occurrences of words and stores values with keys.
	 * @param arrStr the array that contains all words from Macbeth play.
	 * @return
	 */
	public static HashMap<String, Integer> countOccur(String[] arrStr) {
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for (String word : arrStr) {
			if (checkDuplicates(word) == true) {
				int n = temp.get(word);
				temp.replace(word, n+1);
			} else {
				temp.put(word, 1);
			};
		}
		return sortByOccur(temp);	
	}
	/**
	 * Sorts words by most often occurrence to least occurred.
	 * @param temp temporary HashMap that needs to be sorted.
	 * @return
	 */
	public static HashMap<String, Integer> sortByOccur(HashMap<String, Integer> temp) {
		//sorts hashmap into a LinkedHashMap with descending order by value
		temp.entrySet()
		.stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));	
        return printToFile(sortedMap); 
	}
	/**
	 * Results of word occurrence count are stored on an output file.
	 * @param temp HashMap that has the newly sorted words and corresponding count of occurrence.
	 * @return
	 */
	public static HashMap<String, Integer> printToFile(HashMap<String, Integer> temp) {
		FileWriter output_file;
		try 
		{
			output_file = new FileWriter("output.txt");
			BufferedWriter output = new BufferedWriter(output_file);
			output.write("word, frequency of occurrence");
			output.newLine();
			for (Map.Entry<String, Integer> entry : temp.entrySet()) {
				output.write(entry.getKey() + ", " + entry.getValue());
				output.newLine();
			}
			output.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return temp;
	}
	/**
	 * Reads input file to a string.
	 * The string is filtered to remove special characters.
	 * An array is made from string with string split into separate strings by spaces in String.
	 * Calls countOccur function with arrStr as parameter passed.
	 * @return
	 * @throws FileNotFoundException
	 */
	public static void readFile() throws FileNotFoundException {
		StringBuilder macbethContent;
		String str;
		String[] arrStr = null;
		File infile = new File("input.txt");
		macbethContent = new StringBuilder((int)infile.length());
		try	(Scanner scanner = new Scanner(infile)) {
			while(scanner.hasNextLine()) {
				macbethContent.append(scanner.nextLine());
				macbethContent.append(" ");
			}
			str = macbethContent.toString();
			String temp1 = str.replaceAll("[-,.;:!?]+", " ");
			String temp2 = temp1.replaceAll("[\\t\\n\\r]+", " ");
			arrStr = temp2.toLowerCase().split("[\\s]+");
			
			countOccur(arrStr);	
			
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	/**
	 * Calls readFile and launches javaFX listView
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		readFile();
		//String mysqlStr = "";
		//Class.forName("com.mysql.cj.jdbc.Driver");
		//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wordoccurences", "root", "password1");
		//Statement state = con.createStatement();
		//try {
		//ResultSet res = state.executeQuery("select * from word");
		//while (res.next()) {
		//mysqlStr = mysqlStr + res.getString("word") + " ";
		//}
		//res.close();
		//con.close();
		//} catch (SQLException e) {
		//	e.printStackTrace();
		//}
		
		//String[] arrStr = null;
		//arrStr = mysqlStr.toLowerCase().split("[\\s]+");
		//countOccur(arrStr);
		Application.launch(args);	
	}
}
