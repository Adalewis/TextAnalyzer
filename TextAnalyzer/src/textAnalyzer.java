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

public class textAnalyzer extends Application {
	private static ListView<String> listView = new ListView<String>();
	private static String word;
	private static String cnt;
	private static HashSet set = new HashSet();
	private static LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
	private static HashMap<String, Integer> temp = new HashMap<String, Integer>();
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCount() {
		return cnt;
	}
	public void setCount(Integer cnt) {
		this.cnt = cnt.toString();
	}
	
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}
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
	
	
	
	
	
	
	public static boolean checkDuplicates(String word) {
		//if word is duplicate HashSet add() will return false;
		boolean add = set.add(word);
		if (!add) {	
			return true;
		}
		return false;
	}
	
	public HashMap<String, Integer> countOccur(String[] arrStr) {
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for (String word : arrStr) {
			if (checkDuplicates(word) == true) {
				int n = temp.get(word);
				temp.replace(word, n+1);
				setWord(word);
				setCount(n);
			} else {
				temp.put(word, 1);
				setWord(word);
				setCount(1);
			};
		}
		return sortByOccur(temp);	
	}
	
	public static HashMap<String, Integer> sortByOccur(HashMap<String, Integer> temp) {
		//sorts hashmap into a LinkedHashMap with descending order by value
		temp.entrySet()
		.stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));	
        return printToFile(sortedMap); 
	}
	
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
	
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		StringBuilder macbethContent;
		String str;
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
			String[] arrStr = temp2.toLowerCase().split("[\\s]+");
			textAnalyzer count = new textAnalyzer();
			count.countOccur(arrStr);	
			
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		Application.launch(args);
		
	}

	
	
	
}

