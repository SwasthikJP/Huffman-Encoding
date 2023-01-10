
public class PreorderHeaderInfoReader implements IHeaderInfoReader {

    @Override
    public Node readHeaderInfo(InputStream inputStream) {

        if (inputStream.getBit() == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        return new Node(inputStream.getBits(9), 0);
    }

}
