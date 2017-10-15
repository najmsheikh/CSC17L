import java.util.Arrays;

public class MyHeap<T extends Comparable<T>> extends Heap<T> {
    /*
    *   Name:       MyHeap()
    *   Input:      Maximum capacity of the MyHeap object.
    *   Output:     N/A
    *   Purpose:    Construct an empty heap with the specified capacity.
    */
    public MyHeap(int max) {
        super(max);
    }

    /*
    *   Name:       MyHeap()
    *   Input:      An array to build the heap of; the maximum capacity.
    *   Output:     N/A
    *   Purpose:    Construct a heap using the given array and the specified capacity.
    */
    public MyHeap(T[] A, int size) {
        super(A, size);
        makeHeap();
    }

    /*
    *   Name:       drawHeap()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Visually draw the heap.
    */
    public void drawHeap() {
        heapdisplay W = new heapdisplay(1920, 1080);
        W.drawtree(H, size);
    }

    /*
    *   Name:       heapSort()
    *   Input:      An array that needs to be sorted.
    *   Output:     N/A
    *   Purpose:    Sort the array in ascending order using heap sort.
    */
    public static <S extends Comparable<S>> void heapSort(S[] A) {
//        Create an empty heap of the same size as the array
        int heapSize = A.length;
        Heap<S> tempA = new Heap<S>(heapSize);

//        Insert each element into the array, thereby making sure they are
//        at least max-heap sorted
        for (S element : A)
            tempA.insert(element);

//        Working backwards, delete the root nodes one at a time
        for (int i = heapSize - 1; i >= 0; i--)
            A[i] = tempA.deleteTop();
    }

    /*
    *   Name:       makeHeap()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Sort the array so that it satisfies max-heap properties.
    */
    public void makeHeap() {
//        Heapify every non-leaf node
        for (int i = (size / 2); i >= 0; i--)
            swapdown(i);
    }

    /*
    *   Name:       swap()
    *   Input:      Indices of the elements that need to be swapped.
    *   Output:     N/A
    *   Purpose:    Swap the element in the first specified index with the next
    *               specified index.
    */
    public void swap(int i, int j) {
        T tmp = H[i];
        H[i] = H[j];
        H[j] = tmp;
    }

    /*
    *   Name:       heapSort()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Sort the heap array in ascending order using heap sort. Do so
    *               without actually deleting elements.
    */
    public void heapSort() {
        int heapSize = size;    // maintain size for the iterator
//        Working backwards, swap the root and last node, delete the last node, and
//        finally make sure the result still satisfies max-heap properties
        for (int i = heapSize - 1; i >= 0; i--) {
            swap(0, size - 1);
            size--;
            makeHeap();
        }
    }

    /*
    *   Name:       search()
    *   Input:      The value to be searched; the starting index.
    *   Output:     Whether the value exists or not.
    *   Purpose:    Recursively search for a value in a heap -- taking advantage of
    *               its tree structure.
    */
    public boolean search(T x, int index) {
//        Check if children exist, then check if either are the target value, otherwise
//        keep recursing.
        return left(index) < size && right(index) < size && (cmt.compare(x, H[left(index)]) == 0 || cmt.compare(x, H[right(index)]) == 0 || search(x, ++index));
    }


    public static void main(String[] args) {
//        Creating a test array of 10k random integers
        int n = 10000;
        Integer[] A = new Integer[n];
        for (int i = 0; i < A.length; i++)
            A[i] = (int) (Math.random() * n);
        System.out.println("Random array: " + Arrays.toString(A));

//        Sort the array using static heap sort
        heapSort(A);
        System.out.println("Sorted array: " + Arrays.toString(A));

//        Create a MyHeap using the previous array
        MyHeap<Integer> testHeap = new MyHeap<Integer>(A, n);
//        testHeap.drawHeap();

//        Check if the number 6979 exists in the MyHeap
        System.out.println("Result using linear search: " + testHeap.search(6979));
        System.out.println("Result using heap search: " + testHeap.search(6979, 0));

//        Sort the array using non-static heap sort
        System.out.println("Heap array: " + Arrays.toString(A));
        testHeap.heapSort();
        System.out.println("Sorted array: " + Arrays.toString(A));
    }
}
