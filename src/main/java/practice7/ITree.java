package practice7;

public interface ITree {
    void insert(Integer value);
    Quadruple<INode,Integer,Boolean, Long> findOrInsertAndFind(Integer key);
}
