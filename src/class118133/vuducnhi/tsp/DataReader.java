package class118133.vuducnhi.tsp;

public class DataReader {
    private int numVertices;
    DataReader(int numVertices) {
        this.numVertices = numVertices;
    }

    private String getFileName() {
        switch (numVertices) {

        }
        return "Abcdef";
    }

    public int[][] readCostFromDataFile() {
        String filename = getFileName();
        int[][] result = new int[numVertices][numVertices];

        return result;
    }
}
