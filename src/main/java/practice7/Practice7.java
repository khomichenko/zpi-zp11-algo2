package practice7;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Practice7 {

    public static void populateTree(Tree tree, int nodesAmount, int max) {
        Set<Integer> unique = new HashSet<>();
        while ( unique.size()<nodesAmount) {
            unique.add(ThreadLocalRandom.current().nextInt(1, max));
        }
        for (Integer x: unique) {
            tree.insert(x);
        }
    }


    public static void main(String[] args) {
        // Створюємо об'єкт Scanner для зчитування чисел, введених користувачем
        Scanner input = new Scanner(System.in);
        System.out.println("Введіть кількість печер: ");
        int caveAmount = input.nextInt();

        Boolean showTree = true;

        // Створюємо бінарне дерево та заповнюємо його випадковими елементами
        Tree tree = new Tree();
        populateTree(tree, caveAmount, caveAmount*3);
        if (showTree) tree.printTree();

        // Зчитуємо з клавіатури кількість злитків для пошуку
        System.out.println("Введіть кількість злитків для пошуку: ");
        int item = input.nextInt();

        Tree.Quadruple<INode, Integer, Boolean, Long> found = tree.findOrInsertAndFind(item);
        if (showTree) tree.printTree();
        System.out.println("Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)" :"")+", зайняло "+found.d+" ms");

    }
}
