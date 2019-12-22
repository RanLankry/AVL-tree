
public class AVLtester {

	public static void main(String[] args) {
		
		AVLTree tree = new AVLTree();

		if (tree.empty()==false) {
			System.out.println("error in line 9");
		}
		tree.insert(20, "ran");
		tree.insert(10, "shani");
		tree.insert(15, "david");
		tree.insert(25, "gal");

		if (tree.empty()==true) {
			System.out.println("error in line 17");
		}
		if (tree.getRoot().getKey()!=15) {
			System.out.println("error in line 20");
		}
		if (tree.getRoot().getHeight()!=2) {
			System.out.println("error in line 23");
		}
		if (tree.getRoot().getSubtreeSize()!=4) {
			System.out.println("error in line 26");
		}
		if (tree.less(25)!=70 || tree.less(20)!=45 || tree.less(15)!=25) {
			System.out.println("error!!");
		}

		tree.insert(30, "yonatan");
		tree.delete(10);
		
		
		if (tree.getRoot().getKey()!=25 || tree.getRoot().getHeight()!=2 ||
				tree.getRoot().getLeft().getRight().getKey()!=20 || tree.search(10)!=null) {
			System.out.println("error in line 34");
		}
		if (tree.getRoot().getSubtreeSize()!=4) {
			System.out.println("error in line 37");
		}
		System.out.println("well done!");
	}

}
