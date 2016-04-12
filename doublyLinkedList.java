package editor;

import javafx.scene.text.Text;

/**
 * Created by haoranyu on 3/4/16.
 */
public class doublyLinkedList {
    public class Node {
        public Text item;
        public Node next;
        public Node prev;

        protected Node(Text i, Node p, Node n) {
            item = i;
            next = n;
            prev = p;
        }
    }

    protected Node sentinel;
    protected int size;
    public Node currentNode;
    private int currentPos;
    public Node iter = sentinel;


    public doublyLinkedList(){
        size = 0;
        sentinel = new Node(null, null, null);
        currentNode = sentinel;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    doublyLinkedList(Text x){
        size = 1;
        sentinel = new Node(null, null, null);
        Node newNode = new Node(x, null, null);
        sentinel.next = newNode;
        newNode.next = sentinel;

        sentinel.prev = newNode;
        newNode.prev = sentinel.next;
    }


    public void addLast(Text Item){
        size += 1;
        Node lastNode = new Node(Item, null, null);
        sentinel.prev.next = lastNode;
        lastNode.next = sentinel;

        lastNode.prev = sentinel.prev;
        sentinel.prev = lastNode;
    }

    public void addFirst(Text Item){
        size += 1;
        Node oldFront = sentinel.next;
        Node newFront = new Node(Item, null, null);
        newFront.next  = oldFront;
        sentinel.next = newFront;

        newFront.prev = sentinel;
        oldFront.prev = newFront;
    }

    public boolean isEmpty(){
        if(size == 0)
            return true;
        else
            return false;
    }

    public int size(){
        return size;
    }

    public Text removeLast(){
        size -= 1;
        //if(sentinel.next == null)
        //return null;
        Node lastNode = sentinel.prev;
        lastNode.prev.next = sentinel;
        sentinel.prev = lastNode.prev;
        return lastNode.item;
    }


    public Text removeFirst(){
        size -= 1;
        //if(sentinel.next == null)
        //return null;

        Node frontNode = sentinel.next;
        sentinel.next = frontNode.next;
        frontNode.next.prev = sentinel;

        return frontNode.item;
    }


    public Node deleteChar() {
        size -= 1;
        Node temp = currentNode;
        currentNode.prev.next = currentNode.next;
        currentNode.next.prev = currentNode.prev;
        currentNode = currentNode.prev;
        return temp;
    }

    public Text get(int index){
        Node frontNode = sentinel.next;
        // if(frontNode == null)
        // return null;
        for(int i = 0; i < index; i++)
            frontNode = frontNode.next;

        return frontNode.item;
    }

    public Text getLast(){

        return sentinel.prev.item;
    }


    private Text getItem(Node n, int index){
        if(index == 0)
            return n.item;
        else{
            return getItem(n.next, index - 1);
        }
    }

    public void addChar(Text x) {
        size += 1;
        Node newNode = new Node(x, currentNode, currentNode.next);
        currentNode.next.prev = newNode;
        currentNode.next = newNode;
        currentNode = currentNode.next;

    }

    public Text getRecursive(int index){
        Node frontNode = sentinel.next;
        return getItem(frontNode, index);
    }


    public int currentPos() {

        return currentPos;
    }
}
