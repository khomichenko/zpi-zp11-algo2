package practice7;

public interface ITree {
    void insert(Integer value);
    Tree.Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer key);
}
