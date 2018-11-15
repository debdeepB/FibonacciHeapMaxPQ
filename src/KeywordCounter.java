import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * @author debdeepbasu
 * The driver class which uses the FibonacciHeap class to count the most used keywords.
 */
public class KeywordCounter {

	private static Scanner sc;

	/**
	 * Pass the filename as input argument
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please a command line argument");
			return;
		}
		try {
			File file = new File(args[0]);
			File outFile = new File("output_file.txt");
			FileWriter writer = new FileWriter(outFile);
			FibonacciHeap fh = new FibonacciHeap();
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("stop")) break;
				if (line.startsWith("$")) {
					String[] words = line.split(" ");
					fh.insertOrUpdate(words[0].substring(1), Integer.parseInt(words[1]));
				} else {
					ArrayList<String> mostUsed = fh.getMostUsed(Integer.parseInt(line));
					String resultString = "";
					for (String res : mostUsed) {
						resultString += res + ",";
					}
					resultString = resultString.substring(0, resultString.length() - 1);
					resultString += "\n";
					writer.write(resultString);
				}
			}
			sc.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Please enter a valid file url");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknown error occured");
			e.printStackTrace();
		}
	}

}
