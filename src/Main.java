import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	private static Scanner sc;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please a command line argument");
			return;
		}
		try {
			File file = new File(args[0]);
			FibonacciHeap fh = new FibonacciHeap();
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("stop")) break;
				if (line.startsWith("$")) {
					String[] words = line.split(" ");
					fh.insertOrUpdate(words[0].substring(1), Integer.parseInt(words[1]));
				} else {
					fh.getMostUsed(Integer.parseInt(line));
				}
			}
			sc.close();
			fh.displayTopCircularList();
		} catch (FileNotFoundException e) {
			System.out.println("Please enter a valid file url");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknown error occured");
			e.printStackTrace();
		}
	}

}
