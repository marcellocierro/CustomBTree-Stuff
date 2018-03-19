package sample;

import java.io.RandomAccessFile;
import java.util.ArrayList;

//BTree Derived from algorithms book,

//Takes in generic key and values.
public class BTree<Key extends Comparable<Key>, Value> {

    // max children per B-tree node = m-1
    private static final int M = 4;

    //root of the b tree
    private Node root;
    // height of the b tree, used for debugging
    private int height;
    // number of key-value pairs in the B-tree
    private int N;
    private ArrayList<String> keys;
    private static final String keyFile = /* Put file path here */"keys.txt"; // save in respective directory
    private static final String valueFile = /* Put file path here */ "values.ser"; // save in respective directory


    // helper B-tree node data type
    private static final class Node {
        // number of children
        private int childCount;
        // children array.
        private Entry[] children = new Entry[M];

        // create a node with k children
        private Node(int k) {
            childCount = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private Object val;
        private Node next;

        Entry(Comparable key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    //initializes a brand spankin new b tree
    public BTree() {
        root = new Node(0);
    }

    //Checks if table is empty, hopefully not.
    public boolean isEmpty() {
        return size() == 0;
    }

    //Returns size of table based on # of kv pairs
    public int size() {
        return N;
    }

    //Tracks height
    public int height() {
        return height;
    }

    //Array list of keys
    public ArrayList<String> getKeys() {
        return keys;
    }


    //Returns value of any non-null key
    public Value get(Key key) {
        if (key == null) throw new NullPointerException("key must not be null");
        return search(root, key, height);
    }


    //Recursive function to search for a key
    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.childCount; j++) {
                if (eq(key, children[j].key)) {
                    //  System.out.println("Hit at height " + ht);
                    return (Value) children[j].val;
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.childCount; j++) {
                if (j + 1 == x.childCount || less(key, children[j + 1].key))
                    return search(children[j].next, key, ht - 1);
            }
        }
        return null;
    }


    //Inserts the new key-value pair into the table.
    //This overwrites the old value with the new value if they key is already present.
    public void put(Key key, Value val) {
        if (key == null) throw new NullPointerException("key must not be null");
        Node u = insert(root, key, val, height);
        N++;
        if (u == null) return;
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    //Inserts a new key value pair
    //This traverses until a spot is open, at node given at parameter
    private Node insert(Node h, Key key, Value val, int ht) {
        int j; // The child array index to place the new entry into.
        Entry t = new Entry(key, val, null); // Create an entry with key value pairs and no next value.

        //if there is no more height to be traversed we are then at a leaf.
        //we then begin to traverse the child array to find a position in the node to place the new entry
        if (ht == 0) {
            for (j = 0; j < h.childCount; j++) {
                if (less(key, h.children[j].key)) {
                    break;
                }
            }
        }

        //however if height is not done being traversed, we keep traversing through the tree :(
        else {
            //cycles through and finds the next child node to jump into. afterwards traversal continues.
            for (j = 0; j < h.childCount; j++) {
                if ((j + 1 == h.childCount) || less(key, h.children[j + 1].key)) { //True if loop is at last child's index (j), or the key for the current entry fits into the current slot (j).
                    Node u = insert(h.children[j++].next, key, val, ht - 1); // Recursively call insert with the h(drop into node) being an intermediate key'd valued child or the last child if key is larger than all.
                    //J is incremented here because the if statement is hit if the key is larger than all child links or a link larger than the key is found. If a node is promoted it
                    //may be greater than or equal to the node that was slotted into here but not greater than node J + 1. So node j + 1 will be replaced with the promoted node and it will
                    //be shifted to the right because it is guaranteed to be greater than the promoted node.
                    if (u == null) return null; // If the insertion does not result in a split then return null
                    t.key = u.children[0].key; // If the node is split set the T node's key to the key of the new RIGHT NODE.
                    //This updates the key copy links in this node for the future.
                    t.next = u; //The next value of T is set as the new RIGHT NODE
                    break;
                }
            }
        }

        //iterates through the # of children the current node has, if i is > j(the position of the new entry) then iteration stops
        //Elements in place "t" in place "h" and where i > t.[i] are all shifted to the right.
        for (int i = h.childCount; i > j; i--)
            h.children[i] = h.children[i - 1];

        //Creates new entry node into the targets location.
        h.children[j] = t;
        h.childCount++;
        //Checker to see if the number of children is not at the count of the degree
        //if so return null.
        return (h.childCount < M) ? null : split(h);
    }

    //Splits node in half returning the right node, this is due to the right side containing the largest value.
    private Node split(Node h) {
        Node t = new Node(M / 2);
        h.childCount = M / 2;
        for (int j = 0; j < M / 2; j++)
            t.children[j] = h.children[M / 2 + j];
        return t;
    }

    //helper function to determine whether k1 is less than k2
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    //helper function to determine whether k1 is equal to k2
    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    //serializes our b tree into a random access file.
    public static void serialize(BTree bt, String kf, String vf) {
        try {
            RandomAccessFile kFile = new RandomAccessFile(kf, "rw");
            RandomAccessFile vFile = new RandomAccessFile(vf, "rw");
            vFile.seek(0);
            kFile.seek(0);
            ArrayList ky = bt.getKeys();
            vFile.writeDouble(bt.N);
            for (int x = 0; x < bt.N; x++) {
                kFile.writeUTF((String) ky.get(x));
                vFile.writeLong((Long) bt.get((String) ky.get(x)));
            }
            kFile.close();
            vFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //calls serialize method
    public static void serialize(BTree bt) {
        BTree.serialize(bt, keyFile, valueFile);
    }

    //deserializer for our b tree returning key and value pairs.
    public static BTree deserialize(String kf, String vf) throws Exception {
        BTree bt = new BTree();
        int size;
        RandomAccessFile kFile = new RandomAccessFile(kf, "r");
        RandomAccessFile vFile = new RandomAccessFile(vf, "r");

        kFile.seek(0);
        vFile.seek(0);

        size = vFile.readInt();

        for (int q = 0; q < size; q++) {
            bt.put(kFile.readUTF(), vFile.readLong());
        }
        kFile.close();
        vFile.close();
        return bt;
    }

    public static BTree deserialize() throws Exception {
        return BTree.deserialize(keyFile, valueFile);
    }

}