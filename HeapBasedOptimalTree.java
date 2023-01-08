import java.util.Comparator;
import java.util.PriorityQueue;

public class HeapBasedOptimalTree implements OptimalTree {
    Node rootNode;
    Integer[] characterCount;

    public HeapBasedOptimalTree(Integer[] characterCount){
       rootNode=null;
       this.characterCount=characterCount;
    }
    

    public void combineSubTree(PriorityQueue<Node> pq){
        while(pq.size()!=1){
            Node a=pq.poll();
            Node b=pq.poll();
            pq.add(new Node(a,b));
        }
        rootNode=pq.poll();
        System.out.println(rootNode.frequency);
    }

    public void buildOptimalTree(){
        PriorityQueue<Node> pq=new PriorityQueue<>( 1, new Comparator<Node>() {
          @Override public int compare(Node a, Node b) {
            return Integer.compare(a.frequency, b.frequency);
          }
        });

        for(int i=0;i<characterCount.length;i++){
            if(characterCount[i]!=0){
                pq.add(new Node(i,characterCount[i]));
            }
        }
        combineSubTree(pq);
    }

    public void buildOptimalTree(InputStream inputStream){

    }

    public Node getOptimalTree(){
        if(rootNode==null){
            buildOptimalTree();
        }
        return rootNode;
    }
}
