import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {
    
    public static void main(String[] args) throws IOException {
        InputStream inputStream=new InputStream("test.txt");
        ArrayStoredCharacterCount arrayStoredCharacterCount=new ArrayStoredCharacterCount(inputStream);
        Integer[] ar=arrayStoredCharacterCount.calculateCharacterCount();
        for(int i=0;i<ar.length;i++){
            if(ar[i]!=0){
                System.out.println((char)i+" : "+ar[i]);
            }
        }
        HeapBasedOptimalTree heapBasedOptimalTree=new HeapBasedOptimalTree(ar);
        heapBasedOptimalTree.buildOptimalTree();

        PreorderBasedAssignCode preorderBasedAssignCode=new PreorderBasedAssignCode(heapBasedOptimalTree.getOptimalTree());
       String[] ls= preorderBasedAssignCode.getCodeList();

       for(int i=0;i<ls.length;i++){
        if(ls[i]!=null){
            System.out.println("value "+(char)i+" : "+ls[i]);
        }
       }


    
        // System.out.println(inputStream.getBits(8));
        // System.out.println((206<<1)&255);
    }
}
