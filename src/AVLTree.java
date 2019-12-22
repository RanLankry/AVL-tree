// shani cohen shanic1 315374827
// ran lankry ranlankry 312176548

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */


public class AVLTree {
	
	private IAVLNode root;  //pointer to the root node
	private IAVLNode minimum; //pointer to node with max key in the tree
	private IAVLNode maximum; //pointer to node with min key in the tree
	
 /** 
  * public AVLTree()
  * constructs a new empty Tree 
 */	
	//complexity--> O(1)
	public AVLTree() {
		root=new AVLNode();
		minimum=root;
		maximum=root;	
	}
	
	 /** 
	  * public AVLTree(IAVLNode root)
	  * constructs a new Tree from given root
	 */	
	//complexity--> O(logn)
	public AVLTree(IAVLNode root) {
		this(); //if root is null
		if (root!=null) {
			this.root=root;
			root.setParent(null);
		
			IAVLNode min=root;
			while (min.getSubtreeSize()!=1) {
				min=min.getLeft();
			}
			minimum=min;
		
			IAVLNode max=root;
			while (max.getSubtreeSize()!=1) {
				max=max.getRight();
			}
			maximum=max;
		}
	}
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
	//complexity--> O(1)
  public boolean empty() {
	  if (root.getKey()==-1) {
		  return true;
	  }
    return false;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  //complexity--> O(logn)
  public String search(int k)
  {
	  IAVLNode searchPointer=root;
	  while(searchPointer.getKey()!= k && searchPointer.getKey()!=-1) {
		  if (searchPointer.getKey()> k)
		  {
			  searchPointer=searchPointer.getLeft();
		  }
		  else
		  {
			  searchPointer=searchPointer.getRight();
		  }
	  }
	  return searchPointer.getValue();
  }

  /**
   * private boolean searchIf(int k)
   *
   * returns true--> if key exist. else--> return false.
   * 
   */
  //complexity--> O(logn)
  private boolean searchIf(int k)
  {
	  IAVLNode searchPointer=root;
	  while(searchPointer.getKey()!= k && searchPointer.getKey()!=-1) {
		  if (searchPointer.getKey()> k)
		  {
			  searchPointer=searchPointer.getLeft();
		  }
		  else
		  {
			  searchPointer=searchPointer.getRight();
		  }
	  }
	  return (searchPointer.getKey() != -1);
  }
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
  //complexity--> O(logn)
   public int insert(int k, String i) {
	  //complexity--> O(1)
	  if(empty())  //insert node to an empty tree. number of rotations-->0
	  {
		  root=new AVLNode(k,i);
		  minimum=root;
		  maximum=root;
		  return 0;
	  }
	  
	  //complexity--> O(logn)
	  if(searchIf(k)) { //check if key exists in the tree. number of rotation considered-->-1
		  return -1;
	  }
	  //complexity--> O(logn)
	 IAVLNode insertionPlace= searchLocationForInsert(k,true,true); //get insertion place AND increasing the size and sum vals on search path
	
	 // insert according to keys order --> O(1)
	 if (k<insertionPlace.getKey()) {  
		 insertionPlace.setLeft(new AVLNode(k,i));
		 insertionPlace.getLeft().setParent(insertionPlace);
		 if (k<minimum.getKey()) { //new node can be minimum
			 minimum=insertionPlace.getLeft();
		 }
	 }
	 else {
		 insertionPlace.setRight(new AVLNode(k,i));
		 insertionPlace.getRight().setParent(insertionPlace);
		 if (k>maximum.getKey()) { //new node can be maximum
			 maximum=insertionPlace.getRight();
		 }
	 }
	 
	 AVLNode bottomUp=(AVLNode)insertionPlace; // pointer for going up from insertion place
	 //complexity--> O(logn)
	 //check if we got to the root OR the height did not change
	 while(bottomUp!= null && bottomUp.getHeight()!= (1+ Math.max(bottomUp.getLeft().getHeight(), bottomUp.getRight().getHeight()))) { 
		 if (bottomUp.getBalanceFactor()==2) { //this node is a criminal
			 AVLNode leftNode= (AVLNode)(bottomUp.getLeft());
			 if(leftNode.getBalanceFactor()==-1) {
				 LR(bottomUp); //O(1)
				 return 2;
			 }
			 else{
				 LL(bottomUp); //O(1)
				 return 1;
			 }
		 }
		 else {
			 if (bottomUp.getBalanceFactor()==-2) { //this node is a criminal
				 AVLNode rightNode= (AVLNode)(bottomUp.getRight());
				 if(rightNode.getBalanceFactor()==-1) {
					 RR(bottomUp); //O(1)
					 return 1;
				 }
				 else{
					 RL(bottomUp); //O(1)
					 return 2;
				 }
			 }
			//here BF is < |2|
			// if we get here the node is not a criminal AND its height changed --> we need to continue the search for criminals
			 bottomUp.setHeight(1+ Math.max(bottomUp.getLeft().getHeight(), bottomUp.getRight().getHeight())); // update height
			 bottomUp=(AVLNode)bottomUp.getParent();
		 }
		 
	 }
	 // here we out of the while loop--> we got to root parent(null) and updated the height field for everyone needed 
	 //OR the height does not changed so no more updates needed
	 return 0;
	  
   }
   
   /**
    * private IAVLNode searchLocationForInsert(int key,boolean toIncreaseSize,boolean toIncreaseSum)
    *  
    * search insertion place in a tree
    * returns pointer to the node that will be the parent of the new node
    * precondition--> the key does not exist in the tree and the tree is not empty
    * optional--> while searching, the function can increased the values of the fields: size and sum , during the search path
    */
   //complexity--> O(logn)
   private IAVLNode searchLocationForInsert(int key,boolean toIncreaseSize,boolean toIncreaseSum) {
	   AVLNode searcher=(AVLNode) root;
	   int addSize=0;
	   int addSum=0;
	   if(toIncreaseSize) {
		   addSize=1;
	   }
	   if(toIncreaseSum) {
		   addSum=key;
	   }
	   while(searcher.getSubtreeSize()!=0) { // when size is 0 , it is virtual node
		   searcher.setSubtreeSize(searcher.getSubtreeSize()+addSize);
		   searcher.setSum(searcher.getSum()+addSum);
		   if (key<searcher.getKey()) {
			   searcher=(AVLNode)searcher.getLeft();
		   }
		   else {
			   searcher=(AVLNode)searcher.getRight();
		   }
	   }
	   return searcher.getParent();
   }
   
   
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
*/
   //complexity--> O(logn)
   public int delete(int k)
   {
	 //complexity--> O(logn)
	   if(!searchIf(k)) { //item with key k was not found in the tree
		   return -1;
	   }
	   
	   int numRebalance=0;
	   int valueForUpdateSumField= k; // will be used if the node to be deleted has 2 sons
	   int successorKey=-1; // will be used if the node to be deleted has 2 sons
	   IAVLNode toDelete=searchLocationOfDelete(k); //complexity--> O(logn)
	   if(toDelete.getLeft().getKey()!= -1 && toDelete.getRight().getKey()!=-1) { // toDelete has 2 sons
		   IAVLNode successor= successor(toDelete); //complexity--> O(logn)
		   successorKey= successor.getKey(); //will help identify the location of the switch, while updating fields bottom up
		   valueForUpdateSumField=successorKey; 
		   replace(toDelete,successor); // switch toDelete node and its successor --> O(1)
		   toDelete=successor;   // this will be the node that physically will be deleted

	   }
	   //here toDelete node has at most 1 son
	   //update minimum and maximum fields if needed
	   // for node which has 2 sons, this is irrelevant because it won't be minimum or maximum in the tree
	   if(k==minimum.getKey()) {
		   minimum= successor(toDelete); //complexity--> O(logn)
	   		}
	   if (k==maximum.getKey()) {
		   maximum= predecessor(toDelete); //complexity--> O(logn)
	   }
	   
	   if(toDelete.getLeft().getKey()!=-1) { // toDelete has a left son (can't be minimum) (I won't get here if before I searched for successor)
			   if(toDelete.getParent()!=null) { //toDelete is not the root
				   if(toDelete.getKey()<toDelete.getParent().getKey()) { // toDelete is left son
					   //delete -->O(1)
					   toDelete.getParent().setLeft(toDelete.getLeft()); 
					   toDelete.getLeft().setParent(toDelete.getParent());
				   }
				   else { //toDelete is right son 
					 //delete -->O(1)
					   toDelete.getParent().setRight(toDelete.getLeft()); 
					   toDelete.getLeft().setParent(toDelete.getParent());
					   
				   }
			   }
			   else { // toDelete is a root
				 //delete -->O(1)
				   toDelete.getLeft().setParent(toDelete.getParent());
				   root=toDelete.getLeft();
			   }
		   }
		   else { // toDelete has a right son OR it is a leaf 
			   if(toDelete.getParent()!=null) { //toDelete is not the root
				   if(toDelete.getKey()<toDelete.getParent().getKey()) { // toDelete is left son
					 //delete -->O(1)
					   toDelete.getParent().setLeft(toDelete.getRight()); 
					   toDelete.getRight().setParent(toDelete.getParent());
			   }
				   else { // toDelete is right son
					   //delete -->O(1)
					   toDelete.getParent().setRight(toDelete.getRight()); 
					   toDelete.getRight().setParent(toDelete.getParent());	   
				   }
			   }
			   else { // toDelete is a root
				 //delete -->O(1)
				   toDelete.getRight().setParent(toDelete.getParent());
				   root=toDelete.getRight();
			   }
				   
		   	}
	   
	   //after physical deletion, we are going to update the search path bottomUp
	   IAVLNode updatePointer = toDelete.getParent();
	   boolean changedHeight=true;
	   while(updatePointer!=null) { // going all the way up --> O(logn) iterations
		   //update size field --> O(1)
		   updatePointer.setSubtreeSize(updatePointer.getSubtreeSize()-1); 
		   if(updatePointer.getKey()==successorKey) { // successorKey!= -1 if the deleted node had 2 sons
			   valueForUpdateSumField=k; // from now the new value of sum field is sum<--sum-k
			   successorKey=-1;
		   }
		   //update sum field --> O(1)
		   updatePointer.setSum(updatePointer.getSum()-valueForUpdateSumField);// valueForUpdateSumField --> k OR successor's key
		   if (changedHeight) {
			   //here we need to update height
			   int oldHeight= updatePointer.getHeight();
			   updatePointer.setHeight(1+ Math.max(updatePointer.getLeft().getHeight(), updatePointer.getRight().getHeight()));
			   int BF=updatePointer.getBalanceFactor();
			   if(BF==0 || BF==1 || BF==-1) {
				   //if the height didn't change
				   if (updatePointer.getHeight()== oldHeight){
					   changedHeight=false; // no more update height field AND no more rotations
					   updatePointer=updatePointer.getParent();
					   continue;
				   } 
			   }

			   if(BF==2) {
				   if(updatePointer.getLeft().getBalanceFactor()==1 || updatePointer.getLeft().getBalanceFactor()==0) {
					   LL(updatePointer);
					   numRebalance+=1;
					   updatePointer=updatePointer.getParent().getParent();
					   continue;
				   }
				   if(updatePointer.getLeft().getBalanceFactor()==-1) {
					   LR(updatePointer);
					   numRebalance+=2;
					   updatePointer=updatePointer.getParent().getParent();
					   continue;
				   }
				   
			   }
			   if(BF==-2) {
				   if(updatePointer.getRight().getBalanceFactor()==-1 || updatePointer.getRight().getBalanceFactor()==0) {
					   RR(updatePointer);
					   numRebalance+=1;
					   updatePointer=updatePointer.getParent().getParent();
					   continue;
				   }
				   if(updatePointer.getRight().getBalanceFactor()==1) {
					   RL(updatePointer);
					   numRebalance+=2;
					   updatePointer=updatePointer.getParent().getParent();
					   continue;
				   }
			   }
		   }
		   updatePointer=updatePointer.getParent();
		   
	   }
	   if((minimum.getKey()==maximum.getKey())&&(minimum.getKey()==-1)){
		   minimum=root;
		   maximum=root;
	   }
	   return numRebalance;
 }
   
   
   /**
    * private IAVLNode searchLocationOfDelete(int key)
    * 
    *precondition--> key is in the current tree
    *returns pointer for node that needs to be delete 
    * 
    */
   //complexity--> O(logn)
   private IAVLNode searchLocationOfDelete(int key) {
	   IAVLNode returnPointer=root;
	   while(key!=returnPointer.getKey()) {
		   if(key<returnPointer.getKey()) {
			   returnPointer=returnPointer.getLeft();
		   }
		   else {
			   returnPointer=returnPointer.getRight();
		   }
	   }
	   return returnPointer;
   }
   
   /**
    *private IAVLNode successor(IAVLNode node)
    *
    *returns the successor of the node in the tree
    * or virtual node if it is the maximum
    */
   //complexity--> O(logn)
   private IAVLNode successor(IAVLNode node) {
	   
	   IAVLNode successor=node;
	   if(node.getRight().getKey()!=-1) {
		   successor=node.getRight();
		   while(successor.getLeft().getKey()!=-1) {
			   successor=successor.getLeft();
		   }
		   return successor;
	   }
	   while(successor.getParent()!=null && successor.getKey()>successor.getParent().getKey()  ) {
		   successor=successor.getParent();
	   }
	   if (successor.getParent()==null) {
		   return new AVLNode();
	   }
	   return successor.getParent();
   }
   
   /**
   *private IAVLNode predecessor(IAVLNode node)
   *
   *returns the predecessor of the node in the tree
   * or virtual node if it is the minimum
   */
   //complexity--> O(logn)
   private IAVLNode predecessor(IAVLNode node) {
	   IAVLNode predecessor=node;
	   if(node.getLeft().getKey()!=-1) {
		   predecessor=node.getLeft();
		   while(predecessor.getRight().getKey()!=-1) {
			   predecessor=predecessor.getRight();
		   }
		   return predecessor;
	   }
	   while(predecessor.getParent()!=null && predecessor.getKey()<predecessor.getParent().getKey()  ) {
		   predecessor=predecessor.getParent();
	   }
	   if (predecessor.getParent()==null) {
		   return new AVLNode();
	   }
	   return predecessor.getParent();
   }
   
   /**
   *private void replace(IAVLNode toBeReplaced, IAVLNode replacer )
   *
   *replace the 'toBeReplaced' node with the 'replacer' fields
   * 
   */
   //complexity--> O(1)
   private void replace(IAVLNode toBeReplaced, IAVLNode replacer ) {
	   IAVLNode newNode= new AVLNode(replacer.getKey(),replacer.getValue());
	   newNode.setParent(toBeReplaced.getParent());
	   newNode.setLeft(toBeReplaced.getLeft());
	   newNode.setRight(toBeReplaced.getRight());
	   newNode.setHeight(toBeReplaced.getHeight());
	   newNode.setSubtreeSize(toBeReplaced.getSubtreeSize());
	   newNode.setSum(toBeReplaced.getSum());
	   if(toBeReplaced.getParent()!=null) { // it is not a root
		   if(toBeReplaced.getParent().getLeft().getKey()==toBeReplaced.getKey()) { //the 'toBeReplaced' node is left son 
			   toBeReplaced.getParent().setLeft(newNode);
		   }
		   else { //the 'toBeReplaced' node is right son 
			   toBeReplaced.getParent().setRight(newNode);
		   }
	   }
	   else { // we replace the root
		   root=newNode;
	   }
	   toBeReplaced.getLeft().setParent(newNode);
	   toBeReplaced.getRight().setParent(newNode);
	   //disconnect the node
	   toBeReplaced.setParent(null);
	   toBeReplaced.setLeft(null);
	   toBeReplaced.setRight(null);
   }
   
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   //complexity--> O(1)
   public String min()
   {
	   return this.minimum.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   //complexity--> O(1)
   public String max()
   {
	   return this.maximum.getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   //complexity--> O(n)
   public int[] keysToArray()
   {
       
 	  int[] arr=new int[this.getRoot().getSubtreeSize()];  // creating an array in the size of the tree
 	  int[] index= {0}; // global counter
 	  inOrderKeys(this.getRoot(),arr, index); // calling the in-order recursion method
 	 
       return arr;  
   }
   /**
    * private void inOrderKeys(IAVLNode node,int[] arr, int[] index)
    * gets an array and a global "counter"
    * fills the array with in-order tree walk from minimum to maximum
    * or an empty array if the tree is empty.
    * precondition: index=={0} && arr.lenght=node.size()
    */
   //complexity--> O(n)
   private void inOrderKeys(IAVLNode node,int[] arr, int[] index) {
		  if (node.getKey()!=-1) { // we are standing on a real node
			  
			  //"in-order" tree walk
			  inOrderKeys(node.getLeft(),arr,index);
			  arr[index[0]]=node.getKey();
			  index[0]=index[0]+1; // updating the global counter
			  inOrderKeys(node.getRight(),arr,index);
			  
		  }
	  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   //complexity--> O(n)
   public String[] infoToArray()
   {
 	  String[] arr=new String[this.getRoot().getSubtreeSize()];  // creating an array in the size of the tree
 	  int[] index= {0}; // creating a "global counter"
 	  inOrderInfo(this.getRoot(),arr, index);  // calling the in-order recursion method
 	 
       return arr;                   
   }
   
   /**
    * private void inOrderInfo(IAVLNode node,String[] arr, int[] index)
    * gets an array and a global "counter"
    * fills the array with in-order tree walk from minimum to maximum
    * or an empty array if the tree is empty.
    * precondition: index=={0} && arr.lenght=node.size()
    */
   //complexity--> O(n)
   private void inOrderInfo(IAVLNode node,String[] arr, int[] index) {
		  if (node.getKey()!=-1) {  // we are standing on a real node
			  
			  //"in-order" tree walk
			  inOrderInfo(node.getLeft(),arr,index);
			  arr[index[0]]=node.getValue();
			  index[0]=index[0]+1; // updating the global counter
			  inOrderInfo(node.getRight(),arr,index);
			  
		  }
	  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   //complexity--> O(1)
   public int size()
   {
	   return this.root.getSubtreeSize(); 
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   //complexity--> O(1)
   public IAVLNode getRoot()
   {
	   return this.root;
   }
     /**
    * public string select(int i)
    *
    * Returns the value of the i'th smallest key (return null if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    */   
   //complexity--> O(logk)
   public String select(int i)
   {
	   if (this.getRoot().getKey()==-1) { // the tree is empty
		   return null;
	   }
	   // finding the root with subtree that contains at least i nodes - O(logk)
	   IAVLNode node=this.minimum;
	   while (node.getSubtreeSize()<i) {
		   node=node.getParent();
	   }
	   //calling the general function "treeSelect" on subtree "node" - - O(logk)
	   return treeSelect(node,i).getValue();
   }

   /**
    * private IAVLNode treeSelect(IAVLNode node, int i)
    *
    * Returns the value of the i'th smallest key (return null if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    */
   
   //complexity--> O(logn)
   private IAVLNode treeSelect(IAVLNode node, int i) {
	   int sumOfNodes=node.getLeft().getSubtreeSize()+1; // number of nodes in the left subtree of the root, including the root
	   if (i==sumOfNodes) { // we are on the i'th node
		   return node;
	   }
	   else {
		   if (i<sumOfNodes) { // the i'th node is in the left subtree
			   return treeSelect(node.getLeft(),i);
		   }
			   else { // the i'th node is in the right subtree
				   return treeSelect(node.getRight(),i-sumOfNodes);
			   }
	   }
   }
   /**
    * public int less(int i)
    *
    * Returns the sum of all keys which are less or equal to i
    * i is not neccessarily a key in the tree 	
    *
	* precondition: none
    * postcondition: none
    */   
   public int less(int i)
   {
	   if (this.getRoot().getKey()==-1 || i<this.minimum.getKey()){
		   return 0; // the tree is empty or i is smaller than the minimum
	   }
	   
	   
	   // O(logn)
	   IAVLNode curr=this.searchNode(i); // looking for the key in the tree
	   
	   if (curr==null) { // tree is not in the tree
		   
		   // looking for the predecessor - O(logn)
		   IAVLNode temp=this.searchLocationForInsert(i, false,false);
		   if (i>temp.getKey()) {
			   curr=temp;
		   }
		   else {
			   while (i<temp.getKey() && temp!=this.getRoot()) {
				   temp=temp.getParent();   
			   }
			   curr=temp; 
		   }
	   }
	   
	   // the key is in the tree - O(logn)
	   int sum=curr.getKey()+curr.getLeft().getSum();
	   while (curr!=this.getRoot()) {
		   if (curr==curr.getParent().getRight()) {
			   sum=sum+curr.getParent().getKey()+curr.getParent().getLeft().getSum();
		   }
		   curr=curr.getParent();
	   }
	   
	   return sum; 
   }
   
   /**
    * private void RR (IAVLNode node)
    *
    * execute RR rotation
    * precondition-->this is the right rotation that needs to be performed , node is a criminal
    * 
    */
   //complexity--> O(1)
   private void RR (IAVLNode node) {
	   IAVLNode A= node.getRight();
	   if (node.getParent()!=null) { //node is not a root
		   if (node.getKey()<node.getParent().getKey()) { //the criminal is left child
			   node.getParent().setLeft(A);
		   }
		   else { //the criminal is right child
			   node.getParent().setRight(A);
		   }
	   }
	   else {
		   root=A;
	   }
	   A.setParent(node.getParent());
	   node.setParent(A);
	   node.setRight(A.getLeft());
	   A.getLeft().setParent(node);
	   A.setLeft(node);
	    
	   //update fields
	   node.setHeight(1+ Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
	   A.setHeight(node.getHeight()+1);// After rotation the height of the 2 subTrees of the new "root" will be the some(the root BF is 0)
	   A.setSubtreeSize(node.getSubtreeSize()); //A.size <-- B.size
	   node.setSubtreeSize(node.getLeft().getSubtreeSize()+node.getRight().getSubtreeSize()+1);//B.size<--B.left.size+B.right.size+1
	   A.setSum(node.getSum()); //A.sum <-- B.sum
	   node.setSum(node.getLeft().getSum()+(node.getRight()).getSum()+node.getKey()); //B.sum<--B.left.sum+B.right.sum+B.key
   }
   
   /**
    * private void LL (IAVLNode node)
    *
    * execute LL rotation
    * precondition-->this is the right rotation that needs to be performed , node is a criminal
    * 
    */
   //complexity--> O(1)
   private void LL (IAVLNode node) {
	   IAVLNode A= node.getLeft();
	   if (node.getParent()!=null) { //node is not a root
		   if (node.getKey()<node.getParent().getKey()) { //the criminal is left child
			   node.getParent().setLeft(A);
		   }
		   else { //the criminal is right child
			   node.getParent().setRight(A);
		   }
	   }
	   else {
		   root=A;
	   }
	   A.setParent(node.getParent());
	   node.setParent(A);
	   node.setLeft(A.getRight());
	   A.getRight().setParent(node);
	   A.setRight(node);
	   //update fields
	   node.setHeight(1+ Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
	   A.setHeight(node.getHeight()+1);// After rotation the height of the 2 subTrees of the new "root" will be the some(the root BF is 0)
	   A.setSubtreeSize(node.getSubtreeSize()); //A.size <-- B.size
	   node.setSubtreeSize(node.getLeft().getSubtreeSize()+node.getRight().getSubtreeSize()+1);//B.size<--B.left.size+B.right.size+1
	   A.setSum(node.getSum()); //A.sum <-- B.sum
	   node.setSum(node.getLeft().getSum()+node.getRight().getSum()+node.getKey()); //B.sum<--B.left.sum+B.right.sum+B.key
   }
   
   /**
    * private void RL (IAVLNode node)
    *
    * execute RR rotation
    * precondition-->this is the right rotation that needs to be performed , node is a criminal
    * 
    */
   //complexity--> O(1)
   private void RL (IAVLNode node) {
	   IAVLNode A=node.getRight();
	   IAVLNode B=A.getLeft();
	   A.setLeft(B.getRight());
	   B.getRight().setParent(A);
	   if (node.getParent()!=null) { //node is not a root
		   if (node.getKey()<node.getParent().getKey()) { //the criminal is left child
			   node.getParent().setLeft(B);
		   }
		   else { //the criminal is right child
			   node.getParent().setRight(B);
		   }
	   }
	   else {
		   root=B;
	   }
	   B.setParent(node.getParent());
	   node.setParent(B);
	   node.setRight(B.getLeft());
	   B.getLeft().setParent(node);
	   B.setRight(A);
	   B.setLeft(node);
	   A.setParent(B);

	   //update fields
	   node.setHeight(1+ Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
	   A.setHeight(1+ Math.max(A.getLeft().getHeight(), A.getRight().getHeight()));
	   B.setHeight(node.getHeight()+1);// After rotation the height of the 2 subTrees of the new "root" will be the some(the root BF is 0)
	   B.setSubtreeSize(node.getSubtreeSize()); //B.size <-- C.size
	   node.setSubtreeSize(node.getLeft().getSubtreeSize()+node.getRight().getSubtreeSize()+1);//C.size<--C.left.size+C.right.size+1
	   A.setSubtreeSize(A.getLeft().getSubtreeSize()+A.getRight().getSubtreeSize()+1);//A.size<--A.left.size+A.right.size+1
	   B.setSum(node.getSum()); //B.sum <-- C.sum
	   node.setSum(node.getLeft().getSum()+node.getRight().getSum()+node.getKey()); //C.sum<--C.left.sum+C.right.sum+C.key
	   A.setSum(A.getLeft().getSum()+A.getRight().getSum()+A.getKey()); //A.sum<--A.left.sum+A.right.sum+A.key
	   
   }
   
   
   /**
    * private void LR (IAVLNode node)
    *
    * execute RR rotation
    * precondition-->this is the right rotation that needs to be performed , node is a criminal
    * 
    */
   //complexity--> O(1)
   private void LR (IAVLNode node) {
	   IAVLNode A=node.getLeft();
	   IAVLNode B=A.getRight();
	   A.setRight(B.getLeft());
	   B.getLeft().setParent(A);
	   if (node.getParent()!=null) { //node is not a root
		   if (node.getKey()<node.getParent().getKey()) { //the criminal is left child
			   node.getParent().setLeft(B);
		   }
		   else { //the criminal is right child
			   node.getParent().setRight(B);
		   }
	   }
	   else {
		   root=B;
	   }
	   B.setParent(node.getParent());
	   node.setParent(B);
	   node.setLeft(B.getRight());
	   B.getRight().setParent(node);
	   B.setRight(node);
	   B.setLeft(A);
	   A.setParent(B);
	   //update fields
	   node.setHeight(1+ Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
	   A.setHeight(1+ Math.max(A.getLeft().getHeight(), A.getRight().getHeight()));
	   B.setHeight(node.getHeight()+1);// After rotation the height of the 2 subTrees of the new "root" will be the some(the root BF is 0)
	   B.setSubtreeSize(node.getSubtreeSize()); //B.size <-- C.size
	   node.setSubtreeSize(node.getLeft().getSubtreeSize()+node.getRight().getSubtreeSize()+1);//C.size<--C.left.size+C.right.size+1
	   A.setSubtreeSize(A.getLeft().getSubtreeSize()+A.getRight().getSubtreeSize()+1);//A.size<--A.left.size+A.right.size+1
	   B.setSum(node.getSum()); //B.sum <-- C.sum
	   node.setSum(node.getLeft().getSum()+node.getRight().getSum()+node.getKey()); //C.sum<--C.left.sum+C.right.sum+C.key
	   A.setSum(A.getLeft().getSum()+A.getRight().getSum()+A.getKey()); //A.sum<--A.left.sum+A.right.sum+A.key
	   
   }   
   
   //complexity--> O(logn)
   private IAVLNode searchNode(int k)
   {
 	  IAVLNode searchPointer=root;
 	  while(searchPointer.getKey()!= k && searchPointer.getKey()!=-1) {
 		  if (searchPointer.getKey()> k)
 		  {
 			  searchPointer=searchPointer.getLeft();
 		  }
 		  else
 		  {
 			  searchPointer=searchPointer.getRight();
 		  }
 	  }
 	  if (searchPointer.getKey()== -1) // the key isn't in the tree
 	  {
 		  return null;
 	  }
 	  return searchPointer;
   }
   


   
 
	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setSubtreeSize(int size); // sets the number of real nodes in this node's subtree
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		public void setSum(int sum); // sets the sum of the node
		public int getSum();  // Returns the sum of the node (0 for virtual nodes)
		public int getBalanceFactor(); // Returns the BF of the node 
		
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  	private int key;
	  	private String value;
	  	private IAVLNode parent;
	  	private IAVLNode left;
	  	private IAVLNode right;
	  	private int sum;
	  	private int size;
	  	private int height;
	  	
	  	public AVLNode() {  // initializing a virtual node - O(1)
	  		key=-1;
	  		value=null;
	  		parent=null;
	  		left=null;
	  		right=null;
	  		sum=0;
	  		size=0;
	  		height=-1;
	  	}
	  	public AVLNode(int key, String value) { // initializing a real node - O(1)
	  		this.key=key;
	  		this.value=value;
	  		parent=null;
	  		this.left=new AVLNode();
	  		this.left.setParent(this);
	  		this.right=new AVLNode();
	  		this.right.setParent(this);
	  		sum=key;
	  		size=1;
	  		height=0;
	  	}
	  
	  	//complexity--> O(1)
	  	public int getKey()
		{
			return key;
		}
	  	
	  	//complexity--> O(1)
		public String getValue()
		{
			return value;
		}
		
		//complexity--> O(1)
		public void setLeft(IAVLNode node)
		{
			 this.left=node;
		}
		
		//complexity--> O(1)
		public IAVLNode getLeft()
		{
			return this.left;
		}
		
		//complexity--> O(1)
		public void setRight(IAVLNode node)
		{
			 this.right=node;
		}
		
		//complexity--> O(1)
		public IAVLNode getRight()
		{
			return this.right;
		}
		
		//complexity--> O(1)
		public void setParent(IAVLNode node)
		{
			 this.parent=node;
		}
		
		//complexity--> O(1)
		public IAVLNode getParent()
		{
			return this.parent;
		}

		// Returns True if this is a non-virtual AVL node
		//complexity--> O(1)
		public boolean isRealNode()
		{
			if (this.key==-1) {
				return false;	
			}
			return true;
		}
		
		//complexity--> O(1)
		public void setSubtreeSize(int size)
		{
			this.size=size;
		}
		
		//complexity--> O(1)
		public int getSubtreeSize()
		{
			return this.size;
		}
		
		//complexity--> O(1)
		public void setHeight(int height)
		{
			this.height=height;
		}
		
		//complexity--> O(1)
		public int getHeight()
		{
			return this.height;
		}
		
		//complexity--> O(1)
		public void setSum(int sum) 
		{
			this.sum=sum;
		}
		
		//complexity--> O(1)
		public int getSum() {
			return this.sum;
		}
		/**
		 * returns BF of a node
		 */
		
		//complexity--> O(1)
		public int getBalanceFactor() {
			   return this.left.getHeight()-this.right.getHeight();
		}
  }

}
