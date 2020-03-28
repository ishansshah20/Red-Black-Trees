package amp190005;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for red black tree implementation.

public class RedBlackTreeDriver {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc;
		/*if (args.length > 0) {
			File file = new File(args[0]);
			sc = new Scanner(file);
		} else {
			sc = new Scanner(System.in);
		}*/
		File file = new File("C:\\Users\\Ishan\\IdeaProjects\\Implementation of DS and Algo\\RBT\\src\\amp190005\\sk-t01.txt");
		sc = new Scanner(file);

		String operation = "";
		long operand = 0;
		int modValue = 999983;
		long result = 0;
		RedBlackTree<Long> redBlackTree = new RedBlackTree<>();
		// Initialize the timer
		Timer timer = new Timer();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
				case "Add": {
					operand = sc.nextLong();
					if(redBlackTree.add(operand)) {
						result = (result + 1) % modValue;
					}
					System.out.println("Element added:"+operand);
					redBlackTree.printTree();
					if(!redBlackTree.verifyRBT())
						break;
					break;
				}
				case "Remove": {
					operand = sc.nextLong();
					System.out.println("Remove element:"+operand);
					if (redBlackTree.remove(operand) != null) {
						result = (result + 1) % modValue;
					}
					System.out.println("Element removed:"+operand);
					redBlackTree.printTree();
					if(!redBlackTree.verifyRBT())
						break;
					break;
				}
				case "Contains":{
					operand = sc.nextLong();
					if (redBlackTree.contains(operand)) {
						result = (result + 1) % modValue;
					}
					System.out.println("Tree contains:"+result);
					redBlackTree.printTree();
					break;
				}

				case "Validate":{
					boolean valid = redBlackTree.verifyRBT();
					if(!valid)
						System.out.println("Invalid RBT");
					else{
						System.out.println("Valid RBT");
					}
					break;
				}
			}
		}

		// End Time
		timer.end();

		System.out.println(result);
		System.out.println(timer);
	}
}
