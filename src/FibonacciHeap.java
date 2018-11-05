import java.util.*;

public class FibonacciHeap {
	
	private HashMap<String, Node> map = new HashMap<String, Node>(); 
	
	public void insertOrUpdate(String str, int frequency) {
		if (map.containsKey(str)) {
			increaseKey(str, frequency);
		} else {
			insert(str, frequency);
		}
	}
	
	public void getMostUsed(int num) {
		System.out.println("num:" + num);
		return;
	}
	
	void insert(String str, int frequency) {
		return;
	}
	
	void increaseKey(String str, int frequency) {
		return;
	}
}

class Node {
	
}
