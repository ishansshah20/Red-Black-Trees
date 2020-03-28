package amp190005; /**
 * @author Vishal Shah
 * @author Vedant Kumar
 * 
 **/

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T>{
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
			this.left = left;
			this.right = right;
        }

		public Entry(T x) {
			this.element = x;
		}

		protected boolean isLeaf() {
			if (left.element != null)
				return false;
			if (right.element != null)
				return false;
			return true;
		}
    }
    
    Entry<T> root;
    int size;
    public Stack<Entry<T>> z;
    int index;
    
    boolean deletedNodeColor;
    String direction;
    boolean isLeafNode;
    
    public BinarySearchTree() {
	root = null;
	size = 0;
    }


    /** 
     * Checks if x is in the tree by check_in_tree function
     * @return true if x is in the tree otherwise it will return false
     * 
     */
    public boolean contains(T x) {
    	Entry<T> v=check_in_tree(x);
    	if( v == null || v.element != x) {
    		return false;
    	}
    	else {
    		return true;
    	}
    	
	}

    private Entry<T> check_in_tree(T x) {
    	z= new Stack<Entry<T>>();
    	z.push(null);
    	return find(root,x);
    }


	private Entry<T> find(Entry<T> t, T x) {
		if (t.element == null|| t.element.compareTo(x)==0) {
			return t;
		}
		while(true)
		{
			if (t.element != null && x.compareTo(t.element)== -1) {
				if (t.left.element== null) {
					break;
				}
				else {
					z.push(t);
					t=t.left;
				}
			}
			else if (t.element != null && x.compareTo(t.element)== 0) {
				break;
				
			}
			else {
				if(t.right.element == null) {
					break;
				}
				else {
					z.push(t);
					t=t.right;
				}
			}
			
		}
		return t;
	}


	/** Checks is there an element that is equal to x in the tree.
     *  @return Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
	return check_in_tree(x)==null ||check_in_tree(x).element !=x ? null:check_in_tree(x).element;
    }

    /** Add x to tree. 
     *  If tree contains a node with same key, replace element by x.
     *  @return true if x is a new element added to tree and false otherwise
     */
    public boolean add(T x) {
    	if (size==0) {
    		root=new Entry<T>(x, null, null);
    		size=size+1;
    		return true;
    	}
    	else {
    		Entry<T> a=check_in_tree(x);
    		if(a.element.compareTo(x)== 0) {
    			a.element=x;
    			return false;
    		}
    		else if(x.compareTo(a.element)<0) {
    			a.left=new Entry<T>(x, null, null);
    			
    		}
    		else {
    			a.right=new Entry<T>(x, null, null);
    			
    		}
    		size=size+1;
    		return true;
    	}
	
    }

    /** 
     *  Remove x from tree. 
     *  @return x if found, otherwise return null
     */
    public T remove(T x) {
    	if(root.element == null)
    		return null;
    	Entry<T> t = check_in_tree(x);
    	if(t.element.compareTo(x) != 0)
    	{
    		return null;
    	}
    	T result=t.element;
    	if(t.left.element == null && t.right.element == null)
    		isLeafNode = true;
    	if(t.left.element == null || t.right.element == null) {
    		deletedNodeColor = ((RedBlackTree.Entry) t).color;
    		bypass(t);
    	}
    	else
    	{
    		z.push(t);
    		Entry<T> minRight = find(t.right,x);
    		if(minRight.left.element == null && minRight.right.element==null) {
    			isLeafNode = true;
    		}
    		t.element = minRight.element;
    		deletedNodeColor = ((RedBlackTree.Entry) minRight).color;
    		bypass(minRight);
    	}
		size=size-1;
    	return result;
    }
    	
    /**
     * Helper function used in remove().
     * Precondition: t has at most one child
     *              stack s has path from root to parent of t
     */	
	
    private void bypass(Entry<T> t) {
		Entry<T> p = z.peek();
    	Entry<T> c = t.left.element == null ? t.right : t.left;
    	if(p.element == null)
    		root = c;
    	else
    	{
    		if(p.left == t) {
    			p.left = c;
    			direction = "left";
    		}
    		else {
    			p.right = c;
    			direction = "right";
    		}
    	}
    }


	/**
     * Finds the minimum element of tree
     * @return minimum element, null if tree is empty
     */

    public T min() {
    	if(size==0) {
    		return null;
    		
    	}
    	Entry<T> t=root;
    	while(t.left != null) {
    		t=t.left;
    	}
	return t.element;
    }
    /**
     * Finds the maximum element of tree
     * @return maximum element, null if tree is empty
     */
    public T max() {
    	if(size==0) {
		return null;
	}
	Entry<T> t=root;
	while(t.right != null) {
		t=t.right;
		}
	return t.element;
    }

    /**
     * Creates an array with the elements using in-order traversal of tree
     * @return array with elements of tree
     */
    public Comparable[] toArray() {
	Comparable[] arr = new Comparable[size];
	index=0;
	
	/* write code to place elements in array here */
	intoarr(arr,this.root);
	return arr;
	
    }
    /**
     *  Helper method to create an array with
     * the in-order traversal of tree
     */

private void intoarr(Comparable[] arr, Entry<T> node) {
		if(node != null) {
			intoarr(arr,node.left);
			arr[index++]=(Comparable)node.element;
			intoarr(arr, node.right);
		}
		
	}



    public static void main(String[] args) {
	BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }           
        }
    }


    public void printTree() {
	System.out.print("[" + size + "]");
	printTree(root);
	System.out.println();
    }

    // Inorder traversal of tree
    public void printTree(Entry<T> node) {
	if(node != null) {
	    printTree(node.left);
	    System.out.print(" " + node.element);
	    printTree(node.right);
	}
    }


	@Override
	public Iterator<T> iterator() {
		return null;
	}

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
