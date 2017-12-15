public class StudentBST {
    public static void main(String[] args) {
        BSTGraph W = new BSTGraph(1024, 768);
        BST<Integer> testBST = new Nil<>();
        for (int i = 0; i < 99; i++)
            testBST = testBST.insert((int) (Math.random() * 100));

        BST<Integer> clonedBST = testBST.clone();
        System.out.println("Cloned tree prior deletions on original tree:\n" + clonedBST);

        W.drawtree(testBST);
        for (int i = 0; i < 25; i++) {
            try {
                Thread.sleep(500);
                testBST.delete((int) (Math.random() * 100));
                W.drawtree(testBST);
            } catch (Exception e) {
            }
        }

        System.out.println("Cloned tree after deletions on original tree:\n" + clonedBST);
        System.out.println("## Both outputs should be same since we are printing the cloned tree (which we don't touch) ##");
    }
}
