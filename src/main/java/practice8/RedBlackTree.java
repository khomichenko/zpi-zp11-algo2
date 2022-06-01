package practice8;

import practice7.INode;
import practice7.ITree;
import practice7.Tree;

import java.util.Stack;

public class RedBlackTree implements ITree {
    public class Node implements INode{
        Integer data;
        Node left;
        Node right;
        Node parent;
        boolean color;
        public Node(Integer data) {
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

    @Override public void insert(Integer key) {
        Node node = root;
        Node parent = null;

        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (key < node.data) {
                node = node.left;
            } else if (key > node.data) {
                node = node.right;
            } else {
                throw new IllegalArgumentException("BST already contains a node with key " + key);
            }
        }

        // Insert new node
        Node newNode = new Node(key);
        newNode.color = RED;
        if (parent == null) {
            root = newNode;
        } else if (key < parent.data) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        // Parent is black --> nothing to do
        if (parent.color == BLACK) {
            return;
        }

        // From here on, parent is red
        Node grandparent = parent.parent;

        // Case 2:
        // Not having a grandparent means that parent is the root. If we enforce black roots
        // (rule 2), grandparent will never be null, and the following if-then block can be
        // removed.
        if (grandparent == null) {
            // As this method is only called on red nodes (either on newly inserted ones - or -
            // recursively on red grandparents), all we have to do is to recolor the root black.
            parent.color = BLACK;
            return;
        }

        // Get the uncle (may be null/nil, in which case its color is BLACK)
        Node uncle = getUncle(parent);

        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grandparent.color = RED;
            uncle.color = BLACK;

            // Call recursively for grandparent, which is now red.
            // It might be root or have a red parent, in which case we need to fix more...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Note on performance:
        // It would be faster to do the uncle color check within the following code. This way
        // we would avoid checking the grandparent-parent direction twice (once in getUncle()
        // and once in the following else-if). But for better understanding of the code,
        // I left the uncle color check as a separate step.

        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 4a: Uncle is black and node is left->right "inner child" of its grandparent
            if (node == parent.right) {
                rotateLeft(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5a: Uncle is black and node is left->left "outer child" of its grandparent
            rotateRight(grandparent);

            // Recolor original parent and grandparent
            parent.color = BLACK;
            grandparent.color = RED;
        }

        // Parent is right child of grandparent
        else {
            // Case 4b: Uncle is black and node is right->left "inner child" of its grandparent
            if (node == parent.left) {
                rotateRight(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5b: Uncle is black and node is right->right "outer child" of its grandparent
            rotateLeft(grandparent);

            // Recolor original parent and grandparent
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
            throw new IllegalStateException("Parent is not a child of its grandparent");
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
            throw new IllegalStateException("Node is not a child of its parent");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    public Tree.Quadruple<INode,Integer,Boolean, Long> find(int key) {
        Node node = root;
        Integer comparisonCount = 1;
        while (node != null) {
            if (key == node.data) {
                return new Tree.Quadruple<>(node,comparisonCount,false,0L);
            } else if (key < node.data) {
                node = node.left;
            } else {
                node = node.right;
            }
            comparisonCount++;
        }
        return new Tree.Quadruple(null,0,false,0L);
    }

    @Override public Tree.Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer key) {
        long startedMs = System.currentTimeMillis();
        Tree.Quadruple<INode, Integer, Boolean, Long> found = this.find(key);
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
