package practice8;

import practice7.INode;
import practice7.ITree;
import practice7.Tree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Practice8 {

    /*public static int[] createCaves(int numberOfCaves) {
        Set<Integer> unique = new HashSet<>();
        // Заповнюємо кожну печеру випадковою кількістю злитків золота
        // Можлива кількість злитків від 1 до numberOfCaves
        while (unique.size() != numberOfCaves) {
            int randInt = ThreadLocalRandom.current().nextInt(1, 10000);
            unique.add(randInt);
        }
        return unique.stream() .mapToInt(Integer::intValue) .toArray();
    }

    // бінарний пошук
    public static boolean binarySearch(int[] caves, int first, int last, int item, Map<Integer, Integer> cavesMap) {
        int position;
        int comparisonCount = 1;    // для підрахунку кількості порівнянь

        // для початку знайдемо індекс середнього елементу масиву
        position = (first + last) / 2;
        System.out.println("Починаємо пошук з печери: " + cavesMap.get(caves[position]));

        while ((caves[position] != item) && (first <= last)) {
            comparisonCount++;
            if (caves[position] > item) {  // якщо число більше заданого для пошуку
                last = position - 1; // зменшуємо позицію на 1.
            } else {
                first = position + 1;    // інакше збільшуємо на 1
            }
            position = (first + last) / 2;
            System.out.println("Наступний крок печера: " + cavesMap.get(caves[position]));
        }
        if (first <= last) {
            System.out.println(item + " злитків золота знаходяться у " + cavesMap.get(caves[position]) + " печері");
            System.out.println("Методом бінарного пошуку було знайдено золото після " + comparisonCount + " порівнянь");
            return true;
        } else {
            System.out.println("Золото не знайдено після " + comparisonCount + " порівнянь. Створюємо нову печеру із " +
                    "заданою кількістю злитків золота");
            return false;
        }
    }
    */

    public static Set<Integer> buildSet(int nodesAmount, Integer max) {
        Set<Integer> unique = new HashSet<>();
        ThreadLocalRandom rrr = ThreadLocalRandom.current();
        while ( unique.size()<nodesAmount) {
            unique.add(rrr.nextInt(1, max));
        }
        return unique;
    }

    public static ITree populateTree(ITree tree, Set<Integer> unique) {
        for (Integer x: unique) {
            tree.insert(x);
        }
        return tree;
    }

    private static void execute(Boolean showTree, Set<Integer> caves, Integer item) {
        long startedMs = System.currentTimeMillis();
        Tree         tree    = (Tree) populateTree(new Tree(), caves);
        System.out.println("Побудова бінарного дерева зайняло "+(System.currentTimeMillis()-startedMs)+" ms");
        startedMs = System.currentTimeMillis();
        AVLTree      avlTree = (AVLTree) populateTree(new AVLTree(), caves);
        System.out.println("Побудова AVL дерева зайняло "+(System.currentTimeMillis()-startedMs)+" ms");
        startedMs = System.currentTimeMillis();
        RedBlackTree redBlackTree = (RedBlackTree) populateTree(new RedBlackTree(), caves);
        System.out.println("Побудова RB дерева зайняло "+(System.currentTimeMillis()-startedMs)+" ms");

        if (showTree) tree.printTree();
        {
            Tree.Quadruple<INode, Integer, Boolean, Long> found = tree.findOrInsertAndFind(item);
            if (showTree) tree.printTree();
            System.out.println("Пошук в бінарному дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки), зайняло "+found.d+" ms":""));
        }

        if (showTree) avlTree.printTree();
        {
            Tree.Quadruple<INode, Integer, Boolean, Long> found = avlTree.findOrInsertAndFind(item);
            if (showTree) avlTree.printTree();
            System.out.println("Пошук в AVL (збалансованому) дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)":"")+", зайняло "+found.d+" ms");
        }

        if (showTree) redBlackTree.printTree();
        {
            Tree.Quadruple<INode, Integer, Boolean, Long> found = redBlackTree.findOrInsertAndFind(item);
            if (showTree) redBlackTree.printTree();
            System.out.println("Пошук в RB (Червого-Чорному) дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)":"")+", зайняло "+found.d+" ms");
        }
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Введіть кількість печер: ");
        int caveAmount = input.nextInt();

        System.out.println("Введіть кількість злитків для пошуку: ");
        int item = input.nextInt();

        execute(false, buildSet(caveAmount, caveAmount*2), item);


        System.out.println("Порівняння різних дерев");
        List<Integer> counts = new ArrayList<>();
        counts.add(100); counts.add(1000); counts.add(10_000); counts.add(100_000); counts.add(1_000_000);
        for (int c: counts) {
            System.out.println("Кількість: "+c+". **********");
            execute(false, buildSet(c,c*2), item);
        }







    }


}
