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
			deletedNodes.add(maxNode);
			result.add(maxNode.name);
			System.out.println("deleting "+maxNode.name);
			System.out.println("********************************");
			deleteMax();
			displayTopCircularList();
		}
		// insert it back into the fibHeap
		return result;
	}

	void deleteMax() {
		// take the children of maxNode and insert it to the top level circular list
		System.out.println("maxNode");
		maxNode.display();
		Node child = maxNode.child;
		for (int i = 0; i < maxNode.degree; i++) {
			System.out.println("Inserting" + child.val);
			Node sibling = child.next;
			// detach it from the linked list
			child.prev.next = child.next;
			child.next.prev = child.prev;
			insert(child);
			child.display();
			
			child = sibling;
		}
		
		// detach maxNode from the toplevel circular list
		remove(maxNode);
		if (head == null) return;
		System.out.println("After deleting maxNode");
		displayTopCircularList();
		maxNode = head;
		// count the num of nodes in top circular list
		Node curr = this.head;
		int size = 0;
		do {
			size++;
			curr = curr.next;
		} while (curr != this.head);
		// start melding
		HashMap<Integer, Node> degreeMap = new HashMap<Integer, Node>();
		degreeMap.put(head.degree, head);
		curr = head.next;
		int count = 1;
		while (count < size) {
			int degree = curr.degree;
			Node next = curr.next;
			while(degreeMap.containsKey(degree)) {
				System.out.println("meld("+degreeMap.get(degree).val+","+curr.val+")");
				degreeMap.get(degree).display();
				curr.display();
				Node meldedNode = meld(degreeMap.get(degree), curr);
				System.out.println("melded");
				meldedNode.display();
				degreeMap.remove(degree);
				degree = meldedNode.degree;
				curr = meldedNode;
			}
			degreeMap.put(degree, curr);
			if (maxNode.val < curr.val) {
				maxNode = curr;
			}
			curr = next;
			count++;
		}
	}
	
	void displayDegreeMap(HashMap<Integer, Node> map) {
		for (int key: map.keySet()) {
			System.out.println(key+":"+map.get(key).val);
		}
	}
	
	Node meld(Node node1, Node node2) {
		remove(node1);
		remove(node2);
		if (node1.val > node2.val) {
			node1.addChild(node2);
			insert(node1);
			return node1;
		} else {
			node2.addChild(node1);
			insert(node2);
			return node2;
		}
	}
	
	void remove(Node node) {
		if (node.next == node) {
			head = null;
			return;
		}
		node.prev.next = node.next;
		node.next.prev = node.prev;
		if (head == node) head = node.next;
	}
	
	public void displayTopCircularList() {
		if (head == null) {
			System.out.println("empty");
			return;
		}
		Node curr = this.head;
		do {
			System.out.print(curr.val + "(children:" +curr.degree + ")-->");
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
	Node child;
	Node parent;
	int val;
	int degree = 0;
	String name;
	
	public void addChild(Node node) {
		degree++;
		if (this.child == null) {
			this.child = node;
			node.next = node;
			node.prev = node;
		} else {
			Node lastNode = this.child.prev;
			node.prev = lastNode;
			node.next = this.child;
			lastNode.next = node;
		}
	}
	
	void display() {
		System.out.println("name:" + name + " val:" + val + " degree:" + degree);
		Node curr = this.child;
		for (int i = 0; i < degree; i++) {
			System.out.print(curr.val + "[children:" +curr.degree + "]--");
			curr = curr.next;
		}
		System.out.println();
	}
}
