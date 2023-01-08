public interface OptimalTree {
   void buildOptimalTree(); // used in encoding
   void buildOptimalTree(InputStream fileStream);  // used in decoding
   Node getOptimalTree();
}
