package com.capillary.Compression;
public class Node {
    int value;
    int frequency;
    Node left;
    Node right;
    boolean isLeafNode;

    public Node(int value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        left = null;
        right = null;
        isLeafNode = true;
    }

    public Node(Node leftNode, Node rightNode) {
        this.value = -1;
        this.frequency = leftNode.frequency + rightNode.frequency;
        this.isLeafNode = false;
        this.left = leftNode;
        this.right = rightNode;
    }

}
