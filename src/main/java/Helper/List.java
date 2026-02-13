package Helper;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Doubly linked list with first, last and current pointer.
 * @param <T> content type
 */
public class List<T> implements Iterable<T> {
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
        current = null;
    }
    public List(List<T> list) {
        first = null;
        last = null;
        size = 0;

        for (T t : list) {
            this.append(t);
        }
        current = null;
    }

    /**
     * Appends an element to the list.
     *
     * @param value the element to append
     */
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

    /**
     * Inserts an element before the current position.
     *
     * @param value the element to insert
     */
    public void insert(T value) {
        Node<T> newNode = new Node<>(value);

        if (first == null){
            first = newNode;
            last = newNode;
        } else if (current == null) {
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

    /**
     * Concatenates another list onto this list.
     *
     * @param list the list to append
     */
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

    /**
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Deletes the list.
     */
    public void clear() {
        first = null;
        last = null;
        current = null;
        size = 0;
    }


    //Current Methods

    /**
     * Returns true if the current pointer is not null.
     */
    public boolean hasAccess() {
        return current != null;
    }

    /**
     * Removes the element the current pointer is pointing to.
     *
     * @return the removed element
     */
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
    /**
     * Sets the current pointer to the first element in the list.
     */
    public List<T> toFirst() {
        if (first == null || current == first) return this;
        current = first;
        return this;
    }
    /**
     * Sets the current pointer to the last element in the list.
     */
    public List<T> toLast() {
        if (first == null || current == last) return this;
        current = last;
        return this;
    }
    /**
     * Sets the current pointer to the next element in the list.
     */
    public List<T> next() {
        if (first == null || current == null) return this;
        current = current.next;
        return this;
    }
    /**
     * Sets current to the previous element.
     */
    public List<T> prev() {
        if (first == null || current == null) return this;
        current = current.prev;
        return this;
    }
    /**
     * Sets the content of the element the current pointer is pointing to.
     */
    public void setContent(T value){
        if (first == null || current == null) return;
        current.data = value;
    }

    /**
     * @return the content of the current element
     */
    public T getContent() {
        if(first == null || current == null) return null;
        return current.data;
    }

    /**
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * @return whether current is the last element in the list
     */
    public boolean isLast() {
        if (isEmpty()) return false;
        return current == last;
    }
    /**
     * @return whether current is the first element in the list
     */
    public boolean isFirst() {
        if (isEmpty()) return false;
        return current == first;
    }

    public List<T> clone() {
        return new List<T>(this);
    }

    /**
     * Converts the list into an ArrayList.
     *
     * @return the list as an ArrayList
     */
    public ArrayList<T> toArrayList() {
        ArrayList<T> arrayList = new ArrayList<>();
        Node<T> temp = first;
        while (temp != null){
            arrayList.add(temp.data);
            temp = temp.next;
        }
        return arrayList;
    }

    /**
     * Converts an ArrayList into a List.
     *
     * @param list the list to convert
     * @return the converted list
     */
    public static <T> List<T> fromArrayList(java.util.List<T> list){
        List<T> customList = new List<>();
        for (T obj : list){
            customList.append(obj);
        }
        return customList;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cursor = first;

            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public T next() {
                T value = cursor.data;
                cursor = cursor.next;
                return value;
            }
        };
    }
}