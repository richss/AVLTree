import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implementation of an AVL Binary Search Tree
 * @param <K> - data type for the key of a tree node
 * @param <T> - data type for the info stored in a tree node
 *
 * References extensively Eric Robert's textbook Programming Abstractions in Java
 */
public class AVLTree<K extends Comparable<? super K>, T> {

    //Define root of tree
    private AVLNode<K,T> root;

    /**
     * Default constructor
     */
    public AVLTree() {
        root = null;
    }


    /**
     * Calls a recursive method to insert a new node into the tree given:
     * @param key - key to be inserted
     * @param info - info associated with key to be inserted
     */
    public void insert(K key, T info) {
        root = insert(root, key, info);
    }

    /**
     * Private recursive method
     * @param node - current node
     * @param key - key to be inserted
     * @param info - info associated with key to be inserted
     * @return root of subtree
     */
    private AVLNode<K, T> insert(AVLNode<K,T> node, K key, T info) {

        //base case
        if (node == null) {
            node = new AVLNode<>(key, info);
            node.height = 0;
        }
        //Progress Case
        else {

            //Insert the node
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
               node.left = insert(node.left, key, info);
            } else if (cmp > 0){
                node.right = insert(node.right, key, info);
            }
            fixHeight(node); //Correct the height after the recursive insert is complete.

            //Check balance and fix if necessary (Roberts, 2017).
            int bf = getHeight(node.right) - getHeight(node.left);
            if (bf < -1)
                node = fixLeftImbalance(node);
            else if (bf > 1)
                node = fixRightImbalance(node);
        }
        return node;
    }


    /**
     * Updates the height for the node following an update. (Roberts, 2017)
     * @param node - node to fix
     */
    public void fixHeight(AVLNode<K,T> node) {
        if (node != null) {
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        }
    }


    /**
     * Fixes a left imbalance.  If a double rotation is necessary, it will handle it.
     *
     * @param node - node to rotate
     * @return root of the rotated subtree.
     */
    private AVLNode<K,T> fixLeftImbalance(AVLNode<K,T> node) {
        AVLNode<K,T> child = node.left;

        //If a double rotation is needed, rotate left
        if (getHeight(child.right) > getHeight(child.left)) {
            node.left = rotateLeft(child);
        }
        //Rotate right to fix the left imbalance.
        return rotateRight(node);
    }

    /**
     * Fixes a right imbalance.  If a double rotation is necessary, it will handle it.
     *
     * @param node - node to rotate
     * @return root of the rotated subtree.
     */
    private AVLNode<K,T> fixRightImbalance(AVLNode<K,T> node) {
        AVLNode<K,T> child = node.left;

        //If a double rotation is necessary, rotates left
        if (getHeight(child.left) > getHeight(child.right)) {
            node.right = rotateRight(child);
        }
        return rotateLeft(node);
    }


    /**
     * Implements a left rotation about the imbalanced node.
     * @param node - imbalanced node
     * @return root of rotated subtree.
     */
    private AVLNode<K,T> rotateLeft(AVLNode<K,T> node) {
        AVLNode<K,T> child = node.right;
        node.right = child.left;
        child.left = node;
        fixHeight(node);
        fixHeight(child);
        return child;
    }

    /**
     * Implements a right rotation about the imbalanced node.
     * @param node - imbalanced node
     * @return root of rotated subtree.
     */
    private AVLNode<K,T> rotateRight(AVLNode<K,T> node) {
        AVLNode<K,T> child = node.left;
        node.left = child.right;
        child.right = node;
        fixHeight(node);
        fixHeight(child);
        return child;
    }


    /**
     *
     * @return height of the tree
     */
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * Returns height of subtree
     * @param node - root of subtree
     * @return height of subtree
     */
    private int getHeight(AVLNode<K, T> node) {
        if (node == null) return -1;
        else return node.height;
    }

    /**
     * Deletes a tree node with matching key
     * @param key - key for node to delete.
     */
    public void delete(K key) {
        root = deleteByCopy(root, key);
    }

    /**
     *
     * @param node - root of current subtree for deletion recursive task
     * @param key - key of the node to delete.
     * @return root of the subtree after deletion call is complete.
     */
    private AVLNode<K,T> deleteByCopy(AVLNode<K,T> node, K key) {

        if (node == null) return null;

        if (key.compareTo(node.key) < 0) {
            node.left = deleteByCopy(node.left, key);
            return node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = deleteByCopy(node.right, key);
            return node;
        } else {

            //Node with no left child.
            if (node.left == null) node = node.right;

                //Node with no right child
            else if (node.right == null) node = node.left;

                //Node with two child nodes
            else {
                AVLNode<K, T> successor = findMin(node.right);
                node.key = successor.key;
                node.info = successor.info;
                node.right = deleteByCopy(node.right, successor.key);
            }

            //Fix the balance
            if (node != null) {

                fixHeight(node); //Fix the height of the node

                //Check balance and fix if necessary (Roberts, 2017).
                int bf = getHeight(node.right) - getHeight(node.left);
                if (bf < -1)
                    node = fixLeftImbalance(node);
                else if (bf > 1)
                    node = fixRightImbalance(node);
            }
            return node;

        }
    }


    /**
     * Calls a recursive method for preorder traversal of tree that prints each element
     */
    public void printPreOrder() {
        preOrderTraverse(root);
    }

    /**
     * Recursive method for preorder traversal of the tree.
     * @param cur - current node
     */
    private void preOrderTraverse(AVLNode<K,T> cur) {
        if (cur==null) return; //base case

        //Progress case
        System.out.println(cur.key + " => " + cur.info);
        preOrderTraverse(cur.left);
        preOrderTraverse(cur.right);
    }

    /**
     * Calls a recursive method for inorder traversal of tree that prints each element
     */
    public void printInOrder() {
        inOrderTraverse(root);
    }

    /**
     * Recursive method for inorder traversal of the tree.
     * @param cur - current node
     */
    private void inOrderTraverse(AVLNode<K,T> cur) {
        if (cur==null) return;
        inOrderTraverse(cur.left);
        System.out.println(cur.key + " => " + cur.info);
        inOrderTraverse(cur.right);
    }

    /**
     * Calls a recursive method for post traversal of tree that prints each element
     */
    public void printPostOrder() {
        postOrderTraverse(root);
    }

    /**
     * Recursive method for postorder traversal of the tree.
     * @param cur - current node
     */
    private void postOrderTraverse(AVLNode<K,T> cur) {
        if (cur==null) return;
        postOrderTraverse(cur.left);
        postOrderTraverse(cur.right);
        System.out.println(cur.key + " => " + cur.info);
    }


    /**
     * Impelementation of breadth first search
     */
    public void printBreadthFirst() {

        Deque<AVLNode<K,T>> q = new ArrayDeque<AVLNode<K,T>>();
        q.add(root);

        while (!q.isEmpty()) {
            AVLNode<K,T> cur = q.remove();
            if (cur.left != null) q.add(cur.left);
            if (cur.right != null) q.add(cur.right);
            System.out.println(cur.key + " => " + cur.info);
        }
    }

    /**
     * Retrieves a tree node's info give its key.
      * @param key - key for desired node
     * @return info of matching node
     */
    public T search(K key) {
        return search(root, key);
    }

    /**
     * Recursive method to implement search.
     * @param cur - current node
     * @param key - key to locate
     * @return info of key'd node or null if not found
     */
    private T search(AVLNode<K,T> cur, K key) {
        if (cur == null) return null;
        if (key.equals(cur.key)) return cur.info;

        if (key.compareTo(cur.key) < 0) {
            return search(cur.left, key);
        }
        return search(cur.right, key);
    }


    /**
     * @return tree node with minimum value
     */
    public AVLNode<K, T> findMin() {
        if (root == null) return null;
        return findMin(root);
    }


    /**
     * Recursive call to find the minimum tree node for a given subtree
     * @param cur - root of subtree
     * @return tree node with minimum value within subtree.
     */
    private AVLNode<K, T> findMin(AVLNode<K,T> cur) {
        if (cur.left == null)
            return cur;

        return findMin(cur.left);
    }


    /**
     * @return tree node with maximum value in the tree
     */
    public AVLNode<K, T> findMax() {
        if (root == null) return null;
        return findMax(root);
    }

    /**
     * Returns the maximum tree node for a subtree.
     * @param cur - root of subtree
     * @return maximum value of subtree
     */
    private AVLNode<K, T> findMax(AVLNode<K,T> cur) {
        if (cur.right == null)
            return cur;

        return findMax(cur.right);
    }




    /**
     *
     * @return true if tree is balanced, false otherwise
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * Determines if a subtree is balanced
     * @param node - root of subtree
     * @return true if balanced, false otherwise.
     */
    private boolean isBalanced(AVLNode<K, T> node) {
        if (node == null) return true;

        int heightLeft = getHeight(node.left);
        int heightRight = getHeight(node.right);

        if (Math.abs(heightLeft - heightRight) > 1)
            return false;
        else {
            return isBalanced(node.left) && isBalanced(node.right);
        }
    }


    public static void main(String[] args) {
        System.out.println("Hello World!");

        AVLTree<Integer,Integer> tree = new AVLTree<>();

        int [] arr = {6, 2, 8, 1, 4, 3};
        for (int i=0; i < arr.length; i++) {
            tree.insert(arr[i], arr[i]);
        }

        System.out.println("\nIn-Order");
        tree.printInOrder();

        System.out.println("\nPre-Order");
        tree.printPreOrder();

        System.out.println("\nPost-Order");
        tree.printPostOrder();


        System.out.println("\nBreadth-Fist:");
        tree.printBreadthFirst();

        System.out.print("\nHeight:" + tree.getHeight() + "\n\n");

        System.out.print("\nBalanced:" + tree.isBalanced() + "\n\n");

        tree.insert(0,0);
        System.out.println(tree.search(0));
        tree.delete(0);
        System.out.println(tree.search(0));
        tree.insert(0,0);
        System.out.println(tree.search(0));


        System.out.println(6);
        tree.delete(6);
        System.out.println(tree.search(6));
        System.out.println("\nIn-Order");
        tree.printInOrder();

    }
}
