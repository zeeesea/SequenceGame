package Helper;

public class List<T> {
    //Variables
    private Node<T> first;
    private Node<T> last;
    private Node<T> current;
    private int size;

    private class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;
        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public List() {
        first = null;
        last = null;
        size = 0;
    }
    public void append(T value){
        Node<T> newNode = new Node<>(value);

        if (first == null){
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        last = newNode;
        size++;
    }
    public void insert(T value) {
        Node<T> newNode = new Node<>(value);

        if (first == null){
            first = newNode;
            last = newNode;
        } else if (current == null) {
            // Wenn current noch nicht definiert, dann value hinten anhängen
            append(value);
            return;
        } else if (current.prev == null){
            current.prev = newNode;
            newNode.next = current;
            first = newNode;
        } else {
            current.prev.next = newNode;
            newNode.next = current;
            newNode.prev = current.prev;
            current.prev = newNode;
        }
        size++;
    }
    public void concat(List<T> list) {
        if (list == null || list.first == null) return;

        if (first == null) {
            first = list.first;
            last = list.last;
            size = list.size;
        } else {
            last.next = list.first;
            list.first.prev = last;
            last = list.last;
            size += list.size;
        }
    }

    //Helper Methods
    public boolean isEmpty() {
        return size == 0;
    }
    public void clear() {
        first = null;
        last = null;
        current = null;
        size = 0;
    }


    //Current Methods
    public boolean hasAccess() {
        return current != null;
    }
    public T remove(){
        Node<T> temp = current;
        if(first == null || current == null) return null;

        if (first == last) {
            first = null;
            last = null;
            current = null;
        } else if (current == first) {
            current.next.prev = null;
            first = current.next;
            current = current.next;
        } else if (current == last){
            current.prev.next = null;
            last = current.prev;
            current = current.prev;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current = current.prev;
        }
        size--;
        return temp.data;
    }
    public void toFirst() {
        if (first == null || current == first) return;
        current = first;
    }
    public void toLast() {
        if (first == null || current == last) return;
        current =  last;
    }
    public void next() {
        if (first == null || current == null || current == last) return;
        current = current.next;
    }
    public void prev() {
        if (first == null || current == null || current == first) return;
        current = current.prev;
    }
    public void setContent(T value){
        if (first == null || current == null) return;
        current.data = value;
    }
    public T getContent() {
        if(first == null || current == null) return null;
        return current.data;
    }

    public int size() {
        return size;
    }
}