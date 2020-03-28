package amp190005;
/** Starter code for Red-Black Tree
 */

import java.util.Scanner;

/**
 * 
 * @author vedant, vishal
 *
 * @param <T> Generic Type - can refer any Object
 */
public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	static class Entry<T> extends BinarySearchTree.Entry<T> {
		boolean color;
		
		// default color always inserted as RED
		Entry(T x, Entry<T> left, Entry<T> right) {
			super(x, left, right);
			color = RED;

		}

		Entry(T x) {
			super(x);
			color = BLACK;
		}

		boolean isRed() {
			return color == RED;
		}
		boolean isBlack() {
			return color == BLACK;
		}
	}

	Entry<T> NIL;
	RedBlackTree() {
		NIL = new Entry<>(null,null,null);
		NIL.color = BLACK;
		root = NIL;
	}

	/**
	 * Add x to tree. If tree contains a node with same key, replace element by x.
	 * Node is added using super function and the color is fixed here
	 * @return true if x is a new element added to tree and false otherwise
	 */
	public boolean add(T x) {
		boolean add = super.add(x);
		if (add) {
			
			// initializing root node
			if (size == 1) {
				root = new Entry<T>(x, null, null);
				((Entry<T>) root).color = BLACK;
				((Entry<T>) root).left = NIL;
				((Entry<T>) root).right = NIL;
			} else {
				Entry<T> t = bstNodeToRBNode((Entry<T>) root);
				if (x.compareTo((T) t.element) > 0) {
					t.right = new Entry<T>(x, NIL, NIL);
					z.add(t);
					insertFixViolation((Entry<T>) t.right);
				} else {
					t.left = new Entry<T>(x, NIL, NIL);
					z.add(t);
					insertFixViolation((Entry<T>) t.left);
				}
				
				// root is always black
				((Entry<T>) root).color = BLACK;
			}
		}
		return add;
	}

	/**
	 * setting up the color of new node
	 * A Stack z is used to trace the path of the new node. All parents will be present.
	 * @param x new node
	 */
	private void insertFixViolation(Entry<T> x) {
		Entry<T> parent = (Entry<T>) z.pop();
		if (parent == null)
			return;
		Entry<T> grandparent = (Entry<T>) z.pop();
		Entry<T> uncle = null;

		// violation of property no two adjacent red nodes
		if (x != root && x.color == RED && parent.color == RED && grandparent != null) {
			
			// finding uncle
			if (grandparent.left == parent)
				uncle = (Entry<T>) grandparent.right;
			else
				uncle = (Entry<T>) grandparent.left;
			
			// requires only re-coloring and propagating to grandparent
			if (uncle != null && uncle.color == RED) {
				parent.color = BLACK;
				uncle.color = BLACK;
				grandparent.color = RED;
				insertFixViolation(grandparent);
			}
			
			// requires rotation and re-coloring
			else {
				if (grandparent.left == parent && parent.left == x) { // left left case
					rotateRight(grandparent);
				} else if (grandparent.left == parent && parent.right == x) { // left right case
					z.push(grandparent);
					rotateLeft(parent);
					z.pop();
					parent = (Entry<T>) grandparent.left;
					rotateRight(grandparent);
				} else if (grandparent.right == parent && parent.left == x) { // right left case
					z.push(grandparent);
					rotateRight(parent);
					z.pop();
					parent = (Entry<T>) grandparent.right;
					rotateLeft(grandparent);
				} else if (grandparent.right == parent && parent.right == x) { // right right case
					rotateLeft(grandparent);
				}
				
				// swap parent color and grand parent color
				boolean temp = grandparent.color;
				grandparent.color = parent.color;
				parent.color = temp;
				insertFixViolation(parent);
			}
		}
	}

	/**
	 * rotation of tree to the left around parentNode
	 * 		p				x
	 * 		 \		=> 	   /
	 * 		  \    		  /
	 * 		   x		 p
	 * @param parentNode - p as in above fig 
	 */
	private void rotateLeft(Entry<T> parentNode) {
		Entry<T> temp = (Entry<T>) parentNode.right; // temp = p address
		parentNode.right = temp.left;
		temp.left = parentNode;
		if (z.peek() != null && z.peek().left == parentNode) {
			z.peek().left = temp;
		} else if (z.peek() != null && z.peek().right == parentNode) {
			z.peek().right = temp;
		} else {
			root = temp;
		}
	}

	/**
	 * rotation of tree to the right around parentNode
	 * 		p				x
	 * 	   /		=>		 \
	 * 	  /					  \
	 *   x					   p
	 * @param parentNode - p as in above fig
	 */
	private void rotateRight(Entry<T> parentNode) {
		Entry<T> temp = (Entry<T>) parentNode.left; // = node // temp = p address
		parentNode.left = temp.right;
		temp.right = parentNode;
		if (z.peek() != null && z.peek().left == parentNode) {
			z.peek().left = temp;
		} else if (z.peek() != null && z.peek().right == parentNode) {
			z.peek().right = temp;
		} else {
			root = temp;
		}
	}

	/**
	 * Helper function used for insertion
	 * Searches where new node is inserted in the tree.
	 * @param node - root node of RBT Entry type
	 * @return	- new node inserted as BST Entry Type
	 */
	private Entry<T> bstNodeToRBNode(Entry<T> node) {
		if (node == NIL) {
			return NIL;
		} else if (node.left != NIL && !(node.left instanceof RedBlackTree.Entry)) {
			return node;
		} else if (node.right != NIL && !(node.right instanceof RedBlackTree.Entry)) {
			return node;
		}
		Entry<T> t;
		t = bstNodeToRBNode((Entry<T>) node.left);
		if (t != NIL)
			return t;
		t = bstNodeToRBNode((Entry<T>) node.right);
		return t;
	}

	/**
	 * Uses BSTRemove function to clear the value
	 * This function re-balances the color of the tree.
	 * @param x - node to be removed
	 */
	public T remove(T x) {
		T remove = super.remove(x);

		// when actual deleted node after BST is leaf and its color is red
		if (isLeafNode && deletedNodeColor == RED) {
			return remove;
		}

		// when actual deleted node after BST is leaf and its color is black
		else if (isLeafNode && deletedNodeColor == BLACK) {
			deleteFixViolation();
		}
		
		// non leaf node i.e. replaced by a child which is non leaf
		else if(!isLeafNode && deletedNodeColor==BLACK){
			if(direction.equals("right")) {
				((Entry<T>)(z.peek().right)).color = BLACK;
			}
			else if(direction.equals("left")) {
				((Entry<T>)(z.peek().left)).color = BLACK;
			}
		}
		return null;
	}

	/**
	 * This method recursively fixes the violation of RBTree properties 
	 * after a node is deleted
	 */
	private void deleteFixViolation() {
		Entry<T> sibling = null;
		Entry<T> parent = (Entry<T>) z.pop();
		
		// find sibling
		if (direction.equals("right"))
			sibling = (Entry<T>) parent.left;
		else if (direction.equals("left"))
			sibling = (Entry<T>) parent.right;

		// sibling exists & it's color is black
		if (sibling != null && sibling.color == BLACK) {
			
			// sibling's both child are null 
			if (sibling.left == NIL && sibling.right == NIL) {
				sibling.color = RED;
				if (parent.color == RED) {
					parent.color = BLACK;
					return;
				}
				if (z.peek()!=null && z.peek().left.element != null &&z.peek().left.element.equals(parent.element)) {
					direction = "left";
					deleteFixViolation();
				} else if (z.peek()!=null && z.peek().right.element!=null && z.peek().right.element.equals(parent.element)) {
					direction = "right";
					deleteFixViolation();
				}
			} 
			
			// sibling's both child are RED
			else if (sibling.right != null && ((Entry<T>) (sibling.right)).color == RED && sibling.left != null
					&& ((Entry<T>) (sibling.left)).color == RED) {
				if (direction.equals("right")) {
					rotateRight(parent);
					boolean temp = parent.color;
					parent.color = sibling.color;
					sibling.color = temp;
					((Entry<T>) (sibling.left)).color = BLACK;
					return;
				} else if (direction.equals("left")) {
					rotateLeft(parent);
					boolean temp = parent.color;
					parent.color = sibling.color;
					sibling.color = temp;
					((Entry<T>) (sibling.right)).color = BLACK;
					return;
				}
			} 
			
			// sibling's right child is RED
			else if (sibling.right != null && ((Entry<T>) (sibling.right)).color == RED) {
				if (direction.equals("right")) { // left right case
					z.push(parent);
					rotateLeft(sibling);
					sibling = (Entry<T>) parent.left;
					boolean temp = sibling.color;
					sibling.color = ((Entry<T>) (sibling.left)).color;
					((Entry<T>) (sibling.left)).color = temp;
					if (z.peek()!=null && z.peek().left.element!= null &&z.peek().left.element.equals(parent.element)) {
						direction = "left";
						deleteFixViolation();
					} else if (z.peek()!=null && z.peek().right.element != null && z.peek().right.element.equals(parent.element)) {
						direction = "right";
						deleteFixViolation();
					}
				} else if (direction.equals("left")) { // right right case
					rotateLeft(parent);
					boolean temp = parent.color;
					parent.color = sibling.color;
					sibling.color = temp;
					((Entry<T>) (sibling.right)).color = BLACK;
					return;
				}
			} 
			
			// sibling's left child is RED
			else if (sibling.left != null && ((Entry<T>) (sibling.left)).color == RED) {
				if (direction.equals("right")) {
					rotateRight(parent);
					boolean temp = parent.color;
					parent.color = sibling.color;
					sibling.color = temp;
					((Entry<T>) (sibling.left)).color = BLACK;
					return;
				} else if (direction.equals("left")) {
					z.push(parent);
					rotateRight(sibling);
					sibling = (Entry<T>) parent.right;
					boolean temp = sibling.color;
					sibling.color = ((Entry<T>) (sibling.right)).color;
					((Entry<T>) (sibling.right)).color = temp;

					if (z.peek()!=null && z.peek().left.element!= null && z.peek().left.element.equals(parent.element)) {
						direction = "left";
						deleteFixViolation();
					} else if (z.peek()!=null && z.peek().right.element!=null &&z.peek().right.element.equals(parent.element)) {
						direction = "right";
						deleteFixViolation();
					}
				}
			} 
			
			// all remaining cases mostly when sibling and both its child are BLACK
			else {
				sibling.color = RED;
				if (parent.color == RED) {
					parent.color = BLACK;
					return;
				}
				if (z.peek()!=null && z.peek().left.element!= null &&z.peek().left.element.equals(parent.element)) {
					direction = "left";
					deleteFixViolation();
				} else if (z.peek()!=null && z.peek().right.element != null && z.peek().right.element.equals(parent.element)) {
					direction = "right";
					deleteFixViolation();
				}
			}
		} 
		
		// sibling is RED in color, color of child doesn't matter
		else if (sibling != null && sibling.color == RED) {
			
			if (direction.equals("right")) {
				rotateRight(parent);
			} else if (direction.equals("left")) {
				rotateLeft(parent);
			}
			
			// swap parent color and sibling color
			boolean temp = parent.color;
			parent.color = sibling.color;
			sibling.color = temp;
			z.push(sibling);
			z.push(parent);
			deleteFixViolation();
		}

	}

	/**
	 * overrides BST printTree method
	 */
	public void printTree() {
		System.out.print("[" + size + "]");
		this.printTree((Entry<T>) root);
		System.out.println();
	}

	// In order traversal of RBTree
	public void printTree(Entry<T> node) {
		if (node.element != null) {
			printTree((Entry<T>) node.left);
			String s = node.color == BLACK ? (String) "B" : "R";
			System.out.print(" " + node.element + "" + s);
			printTree((Entry<T>) node.right);
		}
	}

	public boolean isRootBlack(){
		if(root == NIL)
			return true;
		if(((Entry<T>)root).color == RED)
			return false;
		else
			return true;
	}

	public boolean validateNodes(Entry<T> node){

		Entry<T> leftNode= (Entry<T>) node.left;
		Entry<T> rightNode = (Entry<T>) node.right;

		if (node.isLeaf() && node.color == RED) {
			// Leafs should not be red
			System.out.println("Leaf node is not red");
			return false;
		}

		if (node.color == RED) {
			// You should not have two red nodes in a row

			if (leftNode.color == RED) {
				System.out.println("Adjacent red left nodes: "+ node.element + " and " + leftNode.element);
				return false;
			}
			if (rightNode.color == RED) {
				System.out.println("Adjacent red right nodes: "+ node.element + " and " + rightNode.element);
				return false;
			}
		}

		if (leftNode != NIL && !leftNode.isLeaf()) {
			// Check BST property
			boolean leftCheck = leftNode.element.compareTo(node.element) <= 0;
			if (!leftCheck)
				return false;
			// Check red-black property
			leftCheck = this.validateNodes(leftNode);
			if (!leftCheck)
				return false;
		}

		if (rightNode != NIL  && !rightNode.isLeaf()) {
			// Check BST property
			boolean rightCheck = rightNode.element.compareTo(node.element) > 0;
			if (!rightCheck)
				return false;
			// Check red-black property
			rightCheck = this.validateNodes(rightNode);
			if (!rightCheck)
				return false;
		}
		return true;
	}


	public int computeHeight(Entry<T> node){
		if(node == NIL){
			return 0;
		}

		int leftHeight = computeHeight((Entry<T>) node.left);
		int rightHeight = computeHeight((Entry<T>) node.right);
		int add = node.color == BLACK ? 1 : 0;
		// The current subtree is not a red black tree if and only if
		// one or more of current node's children is a root of an invalid tree
		// or they contain different number of black nodes on a path to a null node.
		if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight)
			return -1;
		else
			return leftHeight + add;
	}

	public boolean isBlackHeightValid(Entry<T> root){
		return computeHeight(root) != -1;
	}

	public boolean verifyRBT(){
		if(!isRootBlack()) {
			System.out.println("Root is not black");
			return false;
		}
		if(!validateNodes((Entry<T>) root)) {
			System.out.println("Not a valid RBT");
			return false;
		}
		if(!isBlackHeightValid((Entry<T>) root)){
			System.out.println("Invalid height");
			return false;
		}
		System.out.println("Valid RBT");
		return true;
	}

	/**
	 * Sample Input 10 85 15 70 20 60 30 50 65 80 90 40 5 55 -60 -15 -85 -70 -90 -20
	 * 
	 	10
		Add 10 : [1] 10B
		85
		Add 85 : [2] 10B 85R
		15
		Add 15 : [3] 10R 15B 85R
		70
		Add 70 : [4] 10B 15B 70R 85B
		20
		Add 20 : [5] 10B 15B 20R 70B 85R
		60
		Add 60 : [6] 10B 15B 20B 60R 70R 85B
		30
		Add 30 : [7] 10B 15B 20R 30B 60R 70R 85B
		50
		Add 50 : [8] 10B 15R 20B 30B 50R 60B 70R 85B
		65
		Add 65 : [9] 10B 15R 20B 30B 50R 60B 65R 70R 85B
		80
		Add 80 : [10] 10B 15R 20B 30B 50R 60B 65R 70R 80R 85B
		90
		Add 90 : [11] 10B 15R 20B 30B 50R 60B 65R 70R 80R 85B 90R
		40
		Add 40 : [12] 10B 15B 20B 30B 40R 50B 60R 65B 70B 80R 85B 90R
		5
		Add 5 : [13] 5R 10B 15B 20B 30B 40R 50B 60R 65B 70B 80R 85B 90R
		55
		Add 55 : [14] 5R 10B 15B 20B 30B 40R 50B 55R 60R 65B 70B 80R 85B 90R 
		-60
		Remove -60 : [13] 5R 10B 15B 20B 30B 40B 50R 55R 65B 70B 80R 85B 90R
		-15
		Remove -15 : [12] 5B 10B 20B 30B 40B 50R 55R 65B 70B 80R 85B 90R
		-85
		Remove -85 : [11] 5B 10B 20B 30B 40B 50R 55R 65B 70B 80R 90B
		-70
		Remove -70 : [10] 5B 10B 20B 30B 40B 50R 55R 65B 80B 90B
		-90
		Remove -90 : [9] 5B 10B 20B 30B 40B 50B 55B 65R 80B
		-20
		Remove -20 : [8] 5R 10B 30B 40B 50B 55B 65B 80B
	 * @param args
	 */
	public static void main(String[] args) {

		RedBlackTree<Integer> t = new RedBlackTree<>();
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int x = in.nextInt();
			if (x > 0) {
				System.out.print("Add " + x + " : ");
				t.add(x);
				t.printTree();
			} else if (x < 0) {
				System.out.print("Remove " + x + " : ");
				t.remove(-x);
				t.printTree();
			} else {
				Comparable[] arr = t.toArray();
				System.out.print("Final: ");
				for (int i = 0; i < t.size; i++) {
					System.out.print(arr[i] + " ");
				}
				System.out.println();
				return;
			}
		}

	}
}
