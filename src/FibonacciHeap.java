import java.util.*;

public class FibonacciHeap {
	
	private HashMap<String, Node> map = new HashMap<String, Node>();
	
	Node head;
	Node maxNode;
	
	public void insertOrUpdate(String str, int frequency) {
		if (map.containsKey(str)) {
			increaseKey(str, frequency);
		} else {
			Node node = insert(str, frequency);
			map.put(str, node);
		}
	}
	
	public ArrayList<String> getMostUsed(int num) {
		// do deleteMax num times and then reinsert it back into
		// the fibHeap
		ArrayList<Node> deletedNodes = new ArrayList<Node>();
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			Node deleted = deleteMax();
			deletedNodes.add(deleted);
			result.add(deleted.name);
		}
		// insert it back into the fibHeap
		return result;
	}

	Node deleteMax() {
		// take the children of maxNode and insert it to the top level circular list
		for (Node child : maxNode.children)
			insert(child);
		// detach maxNode from the toplevel circular list
		maxNode.prev.next = maxNode.next;
		maxNode.next.prev = maxNode.prev;
		if (head == maxNode) head = maxNode.next;
		displayTopCircularList();
		return maxNode;
	}
	
	public void displayTopCircularList() {
		Node curr = this.head;
		do {
			System.out.print(curr.val + "-->");
			curr = curr.next;
		} while (curr != this.head);
		System.out.println("back to head");
	}
	
	Node insert(String str, int frequency) {
		if (head == null) {
			head = new Node();
			head.val = frequency;
			head.next = head;
			head.prev = head;
			head.name = str;
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
		node.name = str;
		if (node.val > maxNode.val) maxNode = node;
		return node;
	}
	
	Node insert(Node node) {
		if (node == null) return null;
		if (head == null) {
			head = node;
			head.val = node.val;
			head.next = head;
			head.prev = head;
			head.name = node.name;
			maxNode = head;
			return head;
		}
		Node lastNode = head.prev;
		node.prev = lastNode;
		node.next = head;
		head.prev = node;
		lastNode.next = node;
		return node;
	}
	
	void increaseKey(String str, int frequency) {
		return;
	}

}

class Node {
	Node next;
	Node prev;
	ArrayList<Node> children = new ArrayList<Node>();
	Node parent;
	int val;
	String name;
}
