package practice8;

import practice7.INode;
import practice7.ITree;
import practice7.Quadruple;
import practice7.Tree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Practice8 {

    /**
     * Створити множину значень
     * @param count кількість
     * @param max максимальне значення
     * @return множину
     */
    public static Set<Integer> buildSet(Integer count, Integer max) {
        Set<Integer> unique = new HashSet<>();
        ThreadLocalRandom rrr = ThreadLocalRandom.current();
        while ( unique.size()<count) {
            unique.add(rrr.nextInt(1, max));
        }
        return unique;
    }

    /**
     * Заповнити дерево значеннями из унікальної множини
     * @param tree дерево
     * @param unique унікальна множина
     * @return дерево зі значеннями
     */
    public static ITree populateTree(ITree tree, Set<Integer> unique) {
        for (Integer x: unique) {
            tree.insert(x);
        }
        return tree;
    }

    /**
     * Завдання:
     *      1. Побудувати бінарне, AVL та RedBlack дерева для унікальної множини $caves
     *      2. Здійснити пошук елемента $item в цих деревах
     * @param showTree чи показувати на екран бінарне дерево
     * @param caves унікальна множина значень
     * @param item елемент для пошуку
     */
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
            Quadruple<INode, Integer, Boolean, Long> found = tree.findOrInsertAndFind(item);
            if (showTree) tree.printTree();
            System.out.println("Пошук в бінарному дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки), зайняло "+found.d+" ms":""));
        }

        if (showTree) avlTree.printTree();
        {
            Quadruple<INode, Integer, Boolean, Long> found = avlTree.findOrInsertAndFind(item);
            if (showTree) avlTree.printTree();
            System.out.println("Пошук в AVL (збалансованому) дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)":"")+", зайняло "+found.d+" ms");
        }

        if (showTree) redBlackTree.printTree();
        {
            Quadruple<INode, Integer, Boolean, Long> found = redBlackTree.findOrInsertAndFind(item);
            if (showTree) redBlackTree.printTree();
            System.out.println("Пошук в RB (Червого-Чорному) дереві. Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)":"")+", зайняло "+found.d+" ms");
        }
    }

    /**
     * Запуск програми
     * @param args не використовується
     */
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Введіть кількість печер: ");
        int caveAmount = input.nextInt();

        System.out.println("Введіть кількість злитків для пошуку: ");
        int item = input.nextInt();

        execute(false, buildSet(caveAmount, caveAmount*2), item);

        System.out.println("Порівняння різних дерев");
        List<Integer> counts = new ArrayList<>();
        counts.add(100); counts.add(1000); counts.add(10_000); counts.add(100_000); counts.add(200_000); counts.add(500_000);
        for (int c: counts) {
            System.out.println("Кількість: "+c+". **********");
            execute(false, buildSet(c,c*3), item);
        }
    }
}
