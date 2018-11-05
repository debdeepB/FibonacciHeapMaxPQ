import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	private static Scanner sc;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("Please pass file url as a command line argument");
			return;
		}
		File file = new File(args[0]);
		FibonacciHeap fh = new FibonacciHeap();
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("stop")) return;
				if (line.startsWith("$")) {
					String[] words = line.split(" ");
					fh.insertOrUpdate(words[0].substring(1), Integer.parseInt(words[1]));
				} else {
					fh.getMostUsed(Integer.parseInt(line));
				}
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter a valid file url");
			e.printStackTrace();
		}
	}

}
