/**
 * Implementation of a Binary Tree Node
 * @param <K> - type for node key
 * @param <T> - type for node info
 */

public class AVLNode<K, T> {

    //Node's key, info pair
    protected K key;
    protected T info;
    protected int height;

    //References to node's left and right child.
    protected AVLNode<K,T> left;
    protected AVLNode<K,T> right;

    /**
     * Node constructor
     * @param key - key of new node
     * @param info - info of new node
     */
    public AVLNode(K key, T info) {
        this.key = key;
        this.info = info;
        left = right = null;
        height = 0;
    }
}
