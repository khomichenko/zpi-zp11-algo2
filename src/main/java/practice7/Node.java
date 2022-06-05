package practice7;

public class Node implements INode {
    public Node left;
    public Node right;
    public Integer data;
    public Integer getData() {return data; }
    public Node(Integer data) {
        this.data = data;
    }
}
