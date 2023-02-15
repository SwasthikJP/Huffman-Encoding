package com.capillary.Compression.huffmanimplementation.huffmanutils;
public class Node {
   public Object value;
   public int frequency;
   public Node left;
   public Node right;
   public boolean isLeafNode;

    public Node(Object value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        left = null;
        right = null;
        isLeafNode = true;
    }

    public Node(Node leftNode, Node rightNode) {
        this.value = null;
        if(leftNode!=null && rightNode!=null){
            this.frequency = leftNode.frequency + rightNode.frequency;
        }else{
            this.frequency=0;
        }
        this.isLeafNode = false;
        this.left = leftNode;
        this.right = rightNode;
    }

}
