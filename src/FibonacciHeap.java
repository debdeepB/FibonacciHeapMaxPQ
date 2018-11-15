import java.util.*;

/**
 * @author debdeepbasu
 * Max FibonacciHeap implementation in Java. Supports insert O(1), the deleteMax O(logn), meld O(1) and increaseKey O(1) methods in amortized time complexity. 
 *
 */
public class FibonacciHeap {
	
	private HashMap<String, Node> map = new HashMap<String, Node>();
	
	/**
	 * The head pointer in the top level circular list
	 */
	Node head;
	/**
	 * The maxNode pointer always points to the node having the maximum key
	 */
	public Node maxNode;
	
	/**
	 *
	 * This method checks if the HashMap contains the keyword. If it is already present, it calls increaseKey to increase the key of the node in the heap.
	 * Else it, creates a node and inserts it into the top level doubly linked list.
	 * @param str Keyword of the inserted entry
	 * @param frequency number of times the keyword has been entered
	 */
	public void insertOrUpdate(String str, int frequency) {
		if (map.containsKey(str)) {
			increaseKey(map.get(str), frequency);
		} else {
			Node node = insert(str, frequency);
			map.put(str, node);
		}
	}
	
	/**
	 *
	 * This method queries the heap to get the most used keywords by iteratively calling deleteMax 'num' numbers of times.
	 * After retrieving all of the most used entries, it reinserts it into the heap, because we don't want to delete them.
	 * @param num number of most used keywords to be retrieved from the heap
	 * @return A list of keywords for the query
	 */
	public ArrayList<String> getMostUsed(int num) {
		// do deleteMax num times and then reinsert it back into
		// the fibHeap
		ArrayList<Node> deletedNodes = new ArrayList<Node>();
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			deletedNodes.add(maxNode);
			result.add(maxNode.name);
			deleteMax();
		}
		
		// insert it back into the fibHeap
		for (int i =0; i < deletedNodes.size(); i++) {
			deletedNodes.get(i).child = null;
			deletedNodes.get(i).degree = 0;
			deletedNodes.get(i).parent = null;
			insert(deletedNodes.get(i));
		}
		
		return result;
	}

	/**
	 * Deletes the max from the heap and reconstructs the heap by pairwise-merging. The maxNode points to the next largest node in the heap.
	 */
	void deleteMax() {
		// take the children of maxNode and insert it to the top level circular list
		Node child = maxNode.child;
		for (int i = 0; i < maxNode.degree; i++) {
			Node sibling = child.next;

			child.prev.next = child.next;
			child.next.prev = child.prev;
			
			insert(child);
			
			child = sibling;
		}
		
		// detach maxNode from the toplevel circular list
		remove(maxNode);
		if (head == null) return;
		maxNode = head;
		// count the num of nodes in top circular list
		Node curr = this.head;
		int size = 0;
		do {
			size++;
			curr = curr.next;
		} while (curr != this.head);
		// start pairwise merging
		HashMap<Integer, Node> degreeMap = new HashMap<Integer, Node>();
		degreeMap.put(head.degree, head);
		curr = head.next;
		int count = 1;
		while (count < size) {
			int degree = curr.degree;
			Node next = curr.next;
			while(degreeMap.containsKey(degree)) {
				Node meldedNode = meld(degreeMap.get(degree), curr);
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
	
	
	/**
	 * 
	 * Detach both trees from their circular lists. Make the node with smaller key the child of the node with the larger key. Insert melded node in the top level
	 * circular list. 
	 * @param node1 Node to be melded with node2
	 * @param node2 Node to be melded with node1
	 * @return The melded node
	 */
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
	
	/**
	 * Remove a node from the top level circular list by re-adjusting the pointers.
	 * @param node Remove this node from the top-level doubly linked circular list
	 */
	void remove(Node node) {
		if (node.next == node) {
			head = null;
			return;
		}
		node.prev.next = node.next;
		node.next.prev = node.prev;
		if (head == node) head = node.next;
	}
	

	
	/**
	 * Insert a new keyword in the heap in the top level doubly linked circular list.
	 * @param str Keyword of the to be inserted entry
	 * @param frequency frequency of the to be inserted entry
	 * @return The inserted node
	 */
	Node insert(String str, int frequency) {
		if (head == null) {
			head = new Node();
			head.val = frequency;
			head.next = head;
			head.prev = head;
			head.name = str;
			head.parent = null;
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
		node.parent = null;
		if (node.val > maxNode.val) maxNode = node;
		return node;
	}
	
	/**
	 * Insert the node into the top level doubly linked list. Readjust maxNode and head pointers if necessary.
	 * @param node Insert this node in the top level doubly linked circular list
	 * @return the inserted node
	 */
	Node insert(Node node) {
		if (node == null) return null;
		if (head == null) {
			head = node;
			head.val = node.val;
			head.next = head;
			head.prev = head;
			head.name = node.name;
			head.parent = null;
			head.childCut = false;
			maxNode = head;
			return head;
		}
		Node lastNode = head.prev;
		node.prev = lastNode;
		node.next = head;
		node.parent = null;
		node.childCut = false;
		head.prev = node;
		lastNode.next = node;
		if (node.val > maxNode.val) maxNode = node;
		return node;
	}
	
	/**
	 * Increase the key of this node, cascading cut may follow if it's value is greater than that of it's parent.
	 * @param node The node whose key is to be increased.
	 * @param val The value by which the key should be increased by.
	 */
	void increaseKey(Node node, int val) {
		node.val += val;
		if (node.parent != null && node.val > node.parent.val) {
			Node parent = node.parent;
			cutFromParent(node, parent);
			cascadingCut(parent);
		} else {
			if (node.val > maxNode.val) {
				maxNode = node;
			}
		}
	}
	
	/**
	 * 
	 * Cut this node from it's parent.
	 * @param node The Node which is going to get cut from it's parent
	 * @param parent parent of the node which is about to lose a child
	 */
	void cutFromParent(Node node, Node parent) {
		// reset child pointer of parent
		if (parent.child == node) {
			parent.child = node.next;
		}
		// remove node from doubly linked list
		node.prev.next = node.next;
		node.next.prev = node.prev;
		node.parent = null;
		insert(node);
		node.childCut = false; // now that it's top level
		parent.degree--;
		if (parent.degree == 0) {
			parent.child = null;
		}
	}
	
	
	/**
	 * @param node Perform cascading cuts on this node and keep going up until you parent is null
	 */
	void cascadingCut(Node node) {
		Node parent = node.parent;
		if (parent != null) {
			if (node.childCut == false) {
				node.childCut = true;
			} else {
				cutFromParent(node, parent);
				cascadingCut(parent);
			}
		}
	}

}

class Node {
	Node next;
	Node prev;
	Node child;
	Node parent;
	boolean childCut = false;
	int val;
	int degree = 0;
	String name;
	
	/**
	 * @param node Add this node as a child of the current node.
	 */
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
			this.child.prev = node;
		}
		node.parent = this;
	}
	
	
	/**
	 * Display this node in the console. 
	 */
	void display() {
		System.out.println("name: " + name + " val: " + val + " degree: " + degree);
		System.out.println("prev: " + prev.name + " prev.val: " + prev.val + "prev.degree: " + prev.degree);
		System.out.println("next: " + next.name + " next.val: " + next.val + " degree: " + next.degree);
		if (parent != null) {
			System.out.println("parent:"+parent.name + "val:" + parent.val);
		}
		Node curr = this.child;
		for (int i = 0; i < degree; i++) {
			System.out.println(curr.name + "("+curr.val+")" + "[children:" +curr.degree + "]--");
			curr.display();
			curr = curr.next;
		}
		System.out.println();
	}
}
