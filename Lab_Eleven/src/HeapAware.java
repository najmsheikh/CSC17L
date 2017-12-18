public interface HeapAware<T> extends Comparable<T>
{
    int getIndex();
    void setIndex(int i);
}
