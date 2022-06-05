package practice7;

public class Tree implements ITree {

    public Node rootNode = null; // кореневий вузол

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
    }

    /**
     * Пошук вузла дерева по значенню
     * @param  value значення
     * @return ноду, кількість порівнянь, чи була вставка (завжди false), кількість мілісекунд
     */
    public Quadruple<INode, Integer, Boolean, Long> find(Integer value) { // пошук вузла по значенню
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
            if (currentNode == null) { // якщо дитини немає, то повертаємо null
                return new Quadruple<>(null, comparisonCount, false, System.currentTimeMillis() - startedMs);
            }
        }
        // повертаємо знайдений елемент
        return new Quadruple<>(currentNode, comparisonCount, false, System.currentTimeMillis() - startedMs);
    }

    /**
     * Пошук вузла дерева по значенню, але якщо не знайдено - вставити, і знову пошук
     * @param value значення
     * @return ноду, кількість порівнянь, чи була вставка (false або true), кількість мілісекунд
     */
    public Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer value) {
        long startedMs = System.currentTimeMillis();
        Quadruple<INode, Integer, Boolean, Long> found = this.find(value);
        if (found==null || found.a==null) { // якщо не знайшли
            insert(value); // вставляэмо елемент в деревно
            found = this.find(value); // знову шукаємо
            found.c = true;
        }
        found.d = System.currentTimeMillis() - startedMs;
        return found;
    }

}
