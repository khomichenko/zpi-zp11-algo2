package practice8;

import practice7.INode;
import practice7.ITree;
import practice7.Quadruple;

import java.util.Stack;

public class AVLTree implements ITree {

    public class Node implements INode {
        Integer data;
        Integer height;
        Node left;
        Node right;
        Node(Integer data) {
            this.data = data;
        }
        @Override public Integer getData() {
            return data;
        }
        @Override
        public String toString() {
            return ""+ data +" (height:"+(height==null?0:height)+")";
        }
    }

    private Node root;

    /**
     * Пошук вузла дерева по значенню
     * @param  value значення
     * @return ноду, кількість порівнянь, чи була вставка (завжди false), кількість мілісекунд
     */
    public Quadruple<INode,Integer,Boolean, Long> find(Integer value) {
        long startedMs = System.currentTimeMillis();
        Node current = root;
        int comparisonCount = 1; // для підрахунку кількості порівнянь
        while (current != null) {
            if (current.data.equals(value)) {
                break;
            }
            comparisonCount++;
            current = current.data < value ? current.right : current.left;
        }
        return new Quadruple<>(current, comparisonCount,false,System.currentTimeMillis() - startedMs) ;
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

    @Override public void insert(Integer key) {
        root = insert(root, key);
    }

    public void delete(Integer key) {
        root = delete(root, key);
    }

    public Node getRoot() {
        return root;
    }

    public Integer height() {
        return root == null ? -1 : root.height;
    }

    /**
     * Вставити елемент в дерево. Після вставки дерево потрібно збалансувати
     * @param key значення
     */
    private Node insert(Node node, Integer key) {
        if (node == null) {
            return new Node(key);
        } else if (node.data > key) { // вставляємо в ліве піддерево (рекурсивно)
            node.left = insert(node.left, key);
        } else if (node.data < key) { // вставляємо в праве піддерево (рекурсивно)
            node.right = insert(node.right, key);
        } else {
            // throw new RuntimeException("duplicate Key!");
            System.out.println("duplicate key");
        }
        return rebalance(node); // балансуємо і вертаємо результат
    }

    private Node delete(Node node, Integer key) {
        if (node == null) {
            return node;
        } else if (node.data > key) {
            node.left = delete(node.left, key);
        } else if (node.data < key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node mostLeftChild = mostLeftChild(node.right);
                node.data = mostLeftChild.data;
                node.right = delete(node.right, node.data);
            }
        }
        if (node != null) {
            node = rebalance(node);
        }
        return node;
    }

    private Node mostLeftChild(Node node) {
        Node current = node;
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Балансировка дерева
     * @param z вершина
     * @return збалансоване дерево
     */
    private Node rebalance(Node z) {
        updateHeight(z); // визначення висоти дерева
        Integer balance = getBalance(z); // баланс - різниця між висотами лівого і правого піддерева
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
            } else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(Node n) {
        Integer lh = height(n.left);
        Integer rh = height(n.right);
        n.height = 1 + Math.max(lh!=null ? lh: 0, rh!=null? rh: 0);
    }

    private Integer height(Node n) {
        return n == null ? -1 : (n.height==null ? 0 : n.height);
    }

    public Integer getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
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
