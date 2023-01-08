import java.io.IOException;
import java.util.Arrays;

public class ArrayStoredCharacterCount implements CharacterCount<Integer> {
    
    int[] countArray;
    InputStream inputStream;

    public ArrayStoredCharacterCount(InputStream inputStream){
        countArray=new int[257];
        this.inputStream=inputStream;
    }

    public Integer[] calculateCharacterCount() throws IOException{
        int n;
        while((n=inputStream.getBits(8))!=-1){
            // System.out.println("array "+n);
            countArray[n]++;
        }
        Integer[] res=Arrays.stream( countArray ).boxed().toArray( Integer[]::new );
        return res;
    }
}
