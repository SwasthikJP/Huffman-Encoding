public class PreorderBasedAssignCode implements AssignCode {
    
    String[] codeList;
    Node rootNode;

    public PreorderBasedAssignCode(){
        rootNode=null;
        codeList=null;
    }

    public PreorderBasedAssignCode(Node node){
            rootNode=node;
            codeList=null;
    }

    public void PreOrder(Node root,String code){
        if(root==null){
            return;
        }
        if(root.isLeafNode){
            codeList[root.value]=code;
            System.out.println((char)root.value+" : "+code);
        }else{
            PreOrder(root.left, code+"0");
            PreOrder(root.right, code+"1");
        }
    }

    public void nodeTraversal(){
        codeList=new String[257];
        PreOrder(rootNode,"");
    }

    public String[] getCodeList(){
        if(codeList==null){
            nodeTraversal();
        }
        return codeList;
    }


}
