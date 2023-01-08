import java.io.FileInputStream;
import java.io.IOException;

public class InputStream {
    
    int buffer;
    int bufferSize;
    FileInputStream fileInputStream;

    public InputStream(String fileName) throws IOException{
        fileInputStream=new FileInputStream(fileName);
        bufferSize=0;
        buffer=0;
        loadBuffer();
    }

    public void loadBuffer() throws IOException{
        if(bufferSize==0){
           buffer=fileInputStream.read();
           bufferSize=8;
        }
    }

    public int getBit() throws IOException{
        if(buffer==-1){
            return -1;
        }
    //    System.out.println("getbit buffer "+buffer);
        int bit=buffer>>7;
        bufferSize--;
        buffer=(buffer<<1)&255;

        if(bufferSize==0){
        loadBuffer();  
        }
        // System.out.println("hi -- "+bit);
        return bit;
    }

    public int getBits(int length) throws IOException{
        int tempByte=0;
        while(length!=0){
            int bit=getBit();
            if(bit==-1){
                return -1;
            }
            tempByte=(tempByte<<1)+bit;
            length--;
        }
    //    System.out.println("getbit buffer "+tempByte);

        return tempByte;
    }
}
