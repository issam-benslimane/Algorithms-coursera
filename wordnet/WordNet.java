/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import java.util.HashMap;

public class WordNet {
    private Digraph graph;
    private SAP sap;
    private HashMap<String, SET<Integer>> nounIdsMap = new HashMap<>();
    private HashMap<Integer, String> synsetIdMap = new HashMap<>();


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validateArgument(synsets);
        validateArgument(hypernyms);
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        int nvertex = 0;
        while (synsetsIn.hasNextLine()) {
            String[] line = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String synset = line[1];
            synsetIdMap.put(id, synset);
            nvertex++;
            for (String noun : synset.split(" ")) {
                SET<Integer> ids = nounIdsMap.get(noun);
                if (ids == null) ids = new SET<>();
                ids.add(id);
                nounIdsMap.put(noun, ids);
            }
        }
        graph = new Digraph(nvertex);
        validateGraph();
        sap = new SAP(graph);
        while (hypernymsIn.hasNextLine()) {
            String[] line = hypernymsIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(id, Integer.parseInt(line[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounIdsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validateArgument(word);
        return nounIdsMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        SET<Integer> idsA = nounIdsMap.get(nounA);
        SET<Integer> idsB = nounIdsMap.get(nounB);
        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        SET<Integer> idsA = nounIdsMap.get(nounA);
        SET<Integer> idsB = nounIdsMap.get(nounB);
        int shortest = sap.ancestor(idsA, idsB);
        if (shortest == -1) return null;
        return synsetIdMap.get(shortest);
    }

    private void validateGraph() {
        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle()) throw new IllegalArgumentException("");
    }

    private void validateArgument(String arg) {
        if (arg == null) throw new IllegalArgumentException("");
    }

    private void validateNoun(String noun) {
        validateArgument(noun);
        if (!isNoun(noun)) throw new IllegalArgumentException("");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wn.sap("tomcat", "Babylonian_weeping_willow"));
    }
}
