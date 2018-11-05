import java.util.*;

public class FibonacciHeap {
	
	private HashMap<String, Node> map = new HashMap<String, Node>();
	
	Node head;
	Node maxNode;
	
	public void insertOrUpdate(String str, int frequency) {
		if (map.containsKey(str)) {
			increaseKey(str, frequency);
		} else {
			Node node = insert(frequency);
			map.put(str, node);
		}
	}
	
	public void getMostUsed(int num) {
		System.out.println("num123:" + num);
		return;
	}
	
	public void displayTopCircularList() {
		Node curr = this.head;
		do {
			System.out.print(curr.val + "-->");
			curr = curr.next;
		} while (curr != this.head);
		System.out.println("back to head");
	}
	
	Node insert(int frequency) {
		if (head == null) {
			head = new Node();
			head.val = frequency;
			head.next = head;
			head.prev = head;
			maxNode = head;
			return head;
		}
		Node node = new Node();
		Node lastNode = head.prev;
		node.prev = lastNode;
		node.next = head;
		head.prev = node;
		lastNode.next = node;
		node.val = frequency;
		if (node.val > maxNode.val) maxNode = node;
		return node;
	}
	
	void increaseKey(String str, int frequency) {
		return;
	}

}

class Node {
	Node next;
	Node prev;
	Node[] children;
	Node parent;
	int val;
}
