import java.util.*;

public class FibonacciHeap {
	
	private HashMap<String, Node> map = new HashMap<String, Node>();
	
	Node head;
	public Node maxNode;
	
	public void insertOrUpdate(String str, int frequency) {
		if (map.containsKey(str)) {
			increaseKey(map.get(str), frequency);
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
			displayTopCircularList();
			deleteMax();
			System.out.println("After deleting");
			displayTopCircularList();
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

	void deleteMax() {
		// take the children of maxNode and insert it to the top level circular list
		// System.out.println("maxNode");
		maxNode.display();
		Node child = maxNode.child;
		for (int i = 0; i < maxNode.degree; i++) {
			// System.out.println("Inserting" + child.val);
			Node sibling = child.next;
			System.out.println("sibling:"+sibling.name);
			// detach it from the linked list
			child.prev.next = child.next;
			child.next.prev = child.prev;
			
			// attach to root list
			child.prev = head;
			child.next = head.next;
			head.next = child;
			child.next.prev = child;
			
			child.parent = null;

			System.out.println("child "+i+":");
			child.display();
			
			child = sibling;
		}
		
		// detach maxNode from the toplevel circular list
		remove(maxNode);
		if (head == null) return;
//		System.out.println("After deleting maxNode");
//		displayTopCircularList();
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
				// degreeMap.get(degree).display();
//				curr.display();
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
//		System.out.println("((((((((((((((((((((((((()))))))))))))))))))))))))");
		do {
			System.out.print(curr.name + "("+ curr.val + ")" + "(degree:" +curr.degree + ")-->");
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			curr.display();
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
