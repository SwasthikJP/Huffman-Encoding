package com.capillary.zipper.wordbasedhuffman.database;

public class TreeNode {
    String hash;
    boolean isLeafNode;

    public TreeNode(String hash,Boolean isLeafNode){
        this.hash=hash;
        this.isLeafNode=isLeafNode;
    }
}
