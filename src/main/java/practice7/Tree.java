package practice7;

import java.util.Stack;

public class Tree implements ITree {

    public class Node implements INode {
        public Node left;
        public Node right;
        public Integer data;
        public Integer getData() {return data; }
        public Node(Integer data) {
            this.data = data;
        }
    }

    public class Triple<A,B,C> {
        public A a; public B b; public C c;
        public Triple(A a, B b, C c) {this.a = a; this.b = b; this.c = c;}
    }

    public static class Quadruple<A,B,C,D> {
        public A a; public B b; public C c; public D d;
        public Quadruple(A a, B b, C c, D d) {this.a = a; this.b = b; this.c = c; this.d = d;}
    }

    public Node rootNode; // кореневий вузол

    public Tree() { // Пусте дерево
        rootNode = null;
    }

    @Override public void insert(Integer value) { // метод вставки нового елементу
        Node newNode = new Node(value); // створення нового вузла
        if (rootNode == null) { // якщо кореневий вузол не існує
            rootNode = newNode;// то новий елемент і є кореневий вузол
        } else { // кореневий вузол існує
            Node currentNode = rootNode; // починаємо з кореневого вузла
            Node parentNode;
            while (true) // виконувати нескінченно, тому що маємо внутрішній вихід из циклу
            {
                parentNode = currentNode;
                if (value == currentNode.data) {   // якщо такий елемент у дереві уже існує, то не зберігаємо його
                    return;    // і просто виходимо із методу
                } else if (value < currentNode.data) {   // рухаємося ліворуч
                    currentNode = currentNode.left;
                    if (currentNode == null) { // якщо дійшли до кінця ланцюжка
                        parentNode.left = newNode; //  то вставити ліворуч і вийти з методу
                        return;
                    }
                } else { // рухаємося праворуч (якщо не ліворуч)
                    currentNode = currentNode.right;
                    if (currentNode == null) { // якщо дійшли до кінця ланцюжка
                        parentNode.right = newNode; //то вставити праворуч
                        return; // і вийти
                    }
                }
            }
        }
    }

    public void printTree() {
        TreePrinter.print(rootNode);
        /*
        // метод для виведення дерева в консоль
        Stack globalStack = new Stack(); // загальний стек для значень дерева
        globalStack.push(rootNode);
        int gaps = 100; // початкове значення відстані між елементами
        boolean isRowEmpty = false;

        while (isRowEmpty == false) {
            Stack localStack = new Stack(); // локальний стек для завдання нащадків елемента
            isRowEmpty = true;

            for (int j = 0; j < gaps; j++)
                System.out.print(' ');
            while (globalStack.isEmpty() == false) { // поки в загальному стеку є елементи
                Node temp = (Node) globalStack.pop(); // беремо наступний, при цьому видаляючи його зі стека
                if (temp != null) {
                    System.out.print(temp.data); // виводимо його значення в консолі
                    localStack.push(temp.left); // зберігаємо в локальний стек, спадкоємці поточного елемента
                    localStack.push(temp.right);
                    if (temp.left != null || temp.right != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("__");// - якщо елемент порожній
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < gaps * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            gaps /= 2;// при переході на наступний рівень відстань між елементами щоразу зменшується
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop()); // переміщуємо всі елементи з локального стеку до глобального
        }*/
    }

    public Quadruple<INode, Integer, Boolean, Long> find(int value) { // пошук вузла по значенню
        long startedMs = System.currentTimeMillis();
        int comparisonCount = 1;    // для підрахунку кількості порівнянь
        Node currentNode = rootNode; // починаємо пошук з кореневого вузла
        while (currentNode.data != value) { // пошук доки не знайдемо елемент або доки не переберемо усі
            comparisonCount++;
            if (value < currentNode.data) { // рухаємося ліворуч
                currentNode = currentNode.left;
            } else { // або рухаємося праворуч
                currentNode = currentNode.right;
            }
            if (currentNode == null) { // якщо дит немає
                return new Quadruple<>(null, comparisonCount, false, System.currentTimeMillis() - startedMs); // повертаємо null
            }
        }
        return new Quadruple<>(currentNode, comparisonCount, false, System.currentTimeMillis() - startedMs); // повертаємо знайдений елемент
    }

    public Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer key) {
        long startedMs = System.currentTimeMillis();
        Quadruple<INode, Integer, Boolean, Long> found = this.find(key);
        if (found==null || found.a==null) {
            insert(key);
            found = this.find(key);
            found.c = true;
        }
        found.d = System.currentTimeMillis() - startedMs;
        return found;
    }

}
