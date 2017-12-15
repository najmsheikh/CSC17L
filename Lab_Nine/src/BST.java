/* Binary Search Trees Fall 2017 version, intermediate version with height */

public interface BST<T extends Comparable<T>> {
    int size();             // number of vertices in tree; O(n)

    int depth();            // length of longest branch, Nil tree has depth 0; O(n)

    boolean isEmpty();      // Nil or Vertex; O(1)

    boolean search(T x);    // determine if x is in tree; O(log n)

    BST<T> insert(T x);     // insert new node; O(log n)

    BST<T> delete(T x);     // delete node T; O(log n)

    BST<T> delMax(Vertex<T> n); // delete max value, replace n.item with max

    T item();               // return value stored at non-empty node

    BST<T> left();          // left subtree of non-empty node

    BST<T> right();         // right subtree of non-empty node

    int height();           // O(1) access to height

    BST<T> clone();         // clone the current tree
}

class Nil<T extends Comparable<T>> implements BST<T> {
    public int size() {
        return 0;
    }

    public int depth() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean search(T x) {
        return false;
    }

    public BST<T> insert(T x) {
        return new Vertex<T>(x, this, this);
    }

    public BST<T> delete(T x) {
        return this;
    }

    public int height() {
        return 0;
    }

    public BST<T> clone() {
        return new Nil<>();
    }

    /*  The following methods are implemented only to fulfill the interface
        requirements, thus allowing us to use the previous methods everywhere
        without having to type-cast.
     */
    public T item() {
        throw new RuntimeException("Nil has no item");
    }

    public BST<T> left() {
        throw new RuntimeException("Nil has no left");
    }

    public BST<T> right() {
        throw new RuntimeException("Nil has no right");
    }

    public BST<T> delMax(Vertex<T> n) {
        throw new RuntimeException("delMax() should never be called on Nil node");
    }

    public String toString() {
        return "";
    }
}

class Vertex<T extends Comparable<T>> implements BST<T> {
    protected T item;           // value stored at node
    protected BST<T> left;      // left subtree
    protected BST<T> right;     // right subtree

    protected int height;

    public Vertex(T i, BST<T> l, BST<T> r) {
        item = i;
        left = l;
        right = r;
        setHeight();
    }

    // Recursively checks for the size of the tree
    public int size() {
        return 1 + left.size() + right.size();
    }

    // recursive depth, kept to verify height() function
    public int depth() {
        int ld = left.depth(), rd = right.depth();
        if (ld > rd) return ld + 1;
        else return rd + 1;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean search(T x) {
        int c = x.compareTo(item);
        return (c == 0 || (c < 0 && left.search(x)) || (c > 0 && right.search(x)));
        // if (c==0) return true;
        // else if (c<0) return left.search(x); else return right.search(x);
    }

    // insert non-duplicate new node
    public BST<T> insert(T x) {
        int c = x.compareTo(item);
        // If item is smaller than current node item, insert left
        if (c < 0)
            left = left.insert(x);
            // If item is larger than current node item, insert right
        else if (c > 0)
            right = right.insert(x);
        setHeight();
        balance();
        return this;
    }

    // setHeight() ADDED AFTER RECURSIVE CALLS
    public BST<T> delete(T x) {
        // first find x:
        int c = x.compareTo(item);
        if (c < 0) left = left.delete(x);
        else if (c > 0) right = right.delete(x);
        else  // found it - now delete it
        {
            if (left.isEmpty()) return right; // lucky case
            else  // delete max vertex on left, set this.item to max on left
                left = left.delMax(this);
        }
        setHeight();  // ADDED FOR AVL TREES
        balance();
        return this;  // this pointer doesn't change
    }

    // delMax take a point to node that will have its item replaced
    public BST<T> delMax(Vertex<T> modnode) {
        if (right.isEmpty())  // largest node found
        {
            modnode.item = this.item;
            return left; // left-tree may still be non-empty!
        }
        right = right.delMax(modnode);
        setHeight(); // SETHEIGHT HERE TOO (this is where tree actually change)
        balance();
        return this;
    }

    public T item() {
        return item;
    }

    public BST<T> left() {
        return left;
    }

    public BST<T> right() {
        return right;
    }

    public int height() {
        return height;
    }

    protected void setHeight() {
        int hl = left.height(), hr = right.height();
        height = Math.max(hl, hr) + 1;
    }

    public BST<T> clone() {
        BST<T> lCopy = null;
        BST<T> rCopy = null;
        if (this.left != null) {
            lCopy = this.left.clone();
        }
        if (this.right != null) {
            rCopy = this.right.clone();
        }
        return new Vertex<T>(item, lCopy, rCopy);
    }

    public void LL() {
        // If the current Vertex has an empty subtree, nothing happens
        if (!left.isEmpty()) {
            Vertex<T> tmp = new Vertex<>(item, left, right);

            tmp.left = this.left.right();
            this.item = left.item();
            this.left = this.left.left();
            this.right = tmp;

            setHeight();
        }
    }

    public void RR() {
        if (!right.isEmpty()) {
            Vertex<T> tmp = new Vertex<>(item, left, right);

            tmp.right = this.right.left();
            this.item = right.item();
            this.right = this.right.right();
            this.left = tmp;

            setHeight();
        }
    }

    public void balance() {
        int d = right.depth() - left.depth();

        if (d > 1) {
            if (right.right().depth() - right.left().depth() > 0) {
                RR();
            } else {
                ((Vertex<T>) right).LL();
                RR();
            }
        } else if (d < -1) {
            if (left.right().depth() - left.left().depth() < 0) {
                LL();
            } else {
                ((Vertex<T>) left).RR();
                LL();
            }
        }
    }

    // inorder traversal of tree
    public String toString() {
        return left + " " + item + " " + right;
    }
}

