package practice8;

import practice7.INode;
import practice7.ITree;
import practice7.Quadruple;

import java.util.Stack;

public class RedBlackTree implements ITree {
    public class Node implements INode{
        Integer data;
        Node left;
        Node right;
        Node parent;
        boolean color;
        public Node(Integer data, Boolean color) {
            this.data = data;
        }
        @Override public Integer getData() {
            return data;
        }

        @Override
        public String toString() {
            return ""+data+" ("+(color==RED ? "RED" : "BLACK")+")";
        }
    }

    static final boolean RED = false;
    static final boolean BLACK = true;

    private Node root;

    @Override public void insert(Integer data) {
        Node node = root;
        Node parent = null;

        // Проходимо дерево вліво або вправо залежно від data
        while (node != null) {
            parent = node;
            if (data < node.data) {
                node = node.left;
            } else if (data > node.data) {
                node = node.right;
            } else {
                throw new IllegalArgumentException("Дублікат " + data);
            }
        }

        // Вставляємо нову вершину
        Node newNode = new Node(data, RED);
        if (parent == null) {
            root = newNode;
        } else if (data < parent.data) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;

        // Випадок 1: Батько є null, ми дойшли до корня, кінець рекурсії
        if (parent == null) {
            // Розкоментуйте наступний рядок, якщо ви хочете застосувати чорні корені (правило 2):
            // node.color = BLACK;
            return;
        }

        // Батька - чорний --> нічого не робимо
        if (parent.color == BLACK) {
            return;
        }

        // Батька є сервоний, тоді ...
        Node grandparent = parent.parent;

        // Випадок 2:
        // Відсутність бабусі й дідуся означає, що батько є коренем. Якщо ми домагаємося чорних коренів
        // (правило 2), дідусь і бабуся ніколи не будуть нульовими, а наступний блок if-then може бути видалено.
        if (grandparent == null) {
            // Оскільки цей метод викликається лише для червоних вузлів (або для нещодавно вставлених - або -рекурсивно для червоних бабусь і дідусів),
            // все, що нам потрібно зробити, це перефарбувати корінь у чорний колір.
            parent.color = BLACK;
            return;
        }

        // Отримати дядька (може бути нуль, у цьому випадку його колір ЧОРНИЙ)
        Node uncle = getUncle(parent);

        // Випадок 3: дядько червоний -> перефарбувати батьків, дідуся та бабусю та дядька
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grandparent.color = RED;
            uncle.color = BLACK;

            // Рекурсивний виклик для бабусі й дідуся, який тепер червоний. Це може бути root або мати червоного батька,
            // і в такому випадку нам потрібно виправити більше...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Примітка щодо продуктивності:
        // Було б швидше виконати перевірку кольору дядька в наступному коді.
        // Таким чином ми б уникнули двічі перевіряти напрямок бабусь і батьків (один раз у getUncle()
        // і один раз у наступному else-if).
        // Але для кращого розуміння коду я залишив перевірку кольору дядька як окремий крок.

        // Батько залишився дитиною бабусі та дідуся
        else if (parent == grandparent.left) {
            // Випадок 4a: дядько чорний, а вузол лівий->правий «внутрішня дитина» його бабусі чи дідуся
            if (node == parent.right) {
                rotateLeft(parent);

                // Нехай "батько" вказує на новий кореневий вузол повернутого піддерева.
                // Він буде перефарбований на наступному кроці, до якого ми збираємося перейти.
                parent = node;
            }

            // Випадок 5a: дядько чорний, а вузол ліворуч->лівий «зовнішній дочірній» від бабусі та дідуся
            rotateRight(grandparent);

            // Перефарбуйте оригінальних батьків і дідусів
            parent.color = BLACK;
            grandparent.color = RED;
        }

        // Батько - права дитина бабусі й дідуся
        else {
            // Випадок 4b: дядько чорний, а вузол праворуч->лівий «внутрішня дитина» його бабусі та дідуся
            if (node == parent.left) {
                rotateRight(parent);

                // Нехай "батько" вказує на новий кореневий вузол повернутого піддерева.
                // Він буде перефарбований на наступному кроці, до якого ми збираємося перейти.
                parent = node;
            }

            // Випадок 5b: дядько чорний, а вузол праворуч->правий «зовнішня дитина» його бабусі чи дідуся
            rotateLeft(grandparent);

            // Перефарбуйте оригінальних батьків і дідусів
            parent.color = BLACK;
            grandparent.color = RED;
        }
    }

    private Node getUncle(Node parent) {
        Node grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("Батько не є дитиною своїх дідусів і бабусь");
        }
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Вузол не є дочірнім до свого батька");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    public Quadruple<INode,Integer,Boolean, Long> find(Integer key) {
        Node node = root;
        Integer comparisonCount = 1;
        while (node != null) {
            if (key.equals(node.data)) {
                return new Quadruple<>(node,comparisonCount,false,0L);
            } else if (key < node.data) {
                node = node.left;
            } else {
                node = node.right;
            }
            comparisonCount++;
        }
        return new Quadruple(null,0,false,0L);
    }

    @Override public Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer key) {
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

    public void printTree() { // метод для виведення дерева в консоль
        Stack globalStack = new Stack(); // загальний стек для значень дерева
        globalStack.push(root);
        int gaps = 32; // початкове значення відстані між елементами
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
        }
    }
}
