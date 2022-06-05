package practice7;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Practice7 {

    /**
     * Заповнює дерево випадковими елементами від 1 до $max
     * @param tree дерево
     * @param nodesAmount кількість вершин
     * @param max максимальне значення вершини (1..max)
     */
    public static void populateTree(Tree tree, int nodesAmount, int max) {
        Set<Integer> unique = new HashSet<>(); // тільки унікальні
        ThreadLocalRandom rrr = ThreadLocalRandom.current(); // райдомайзер
        while ( unique.size()<nodesAmount) {
            unique.add(rrr.nextInt(1, max));
        }
        // перебираємо всі унікальні елементи і вставляемо в дерево
        for (Integer x: unique) {
            tree.insert(x);
        }
    }

    /**
     * Запуск програми
     * @param args не використовується
     */
    public static void main(String[] args) {
        // Створюємо об'єкт Scanner для зчитування чисел, введених користувачем
        Scanner input = new Scanner(System.in);
        System.out.println("Введіть кількість печер: ");
        int caveAmount = input.nextInt();

        Boolean showTree = true; //  виводити дерево на екран ?

        // Створюємо бінарне дерево та заповнюємо його випадковими елементами
        Tree tree = new Tree();
        long startedMs = System.currentTimeMillis();
        populateTree(tree, caveAmount, caveAmount*3);
        System.out.println("Дерево (1.."+(caveAmount*3)+") було створено за "+(System.currentTimeMillis() - startedMs)+" ms: ");
        if (showTree) tree.printTree();

        // Зчитуємо з клавіатури кількість злитків для пошуку
        System.out.println("Введіть кількість злитків для пошуку: ");
        int item = input.nextInt();

        Quadruple<INode, Integer, Boolean, Long> found = tree.findOrInsertAndFind(item);
        if (showTree) tree.printTree();
        System.out.println("Значення "+found.a.getData()+" було знайдено після "+found.b+" порівнянь "+(found.c?" (і після вставки)" :"")+", зайняло "+found.d+" ms");

    }
}
