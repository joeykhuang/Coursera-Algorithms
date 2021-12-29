import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author : Joey Huang
 * @since : 12/24/21, Fri
 **/

public class WordNet {

    private Digraph g;
    private HashMap<String, Set<Integer>> map;
    private HashMap<Integer, String> rmap;

    private void checkNull(Object o){
        if (o == null) throw new IllegalArgumentException();
    }

    private void buildSysnets(String synsets){
        In synsetIn = new In(synsets);
        while (synsetIn.hasNextLine()){
            String[] line = synsetIn.readLine().split(",");
            if (line.length < 2) continue;
            int index = Integer.parseInt(line[0]);
            this.rmap.put(index, line[1]);
            String[] words = line[1].split("\\s++");
            for (String s : words) {
                if (!this.map.containsKey(s)){
                    this.map.put(s, new HashSet<>());
                }
                this.map.get(s).add(index);
            }
        }
    }

    private void buildHypernyms(String hypernyms){
        In hypernymIn = new In(hypernyms);
        while (hypernymIn.hasNextLine()){
            String[] line = hypernymIn.readLine().split(",");
            if (line.length < 2) continue;
            int index = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++){
                this.g.addEdge(index, Integer.parseInt(line[i]));
            }
        }
    }
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkNull(synsets);
        checkNull(hypernyms);
        this.rmap = new HashMap<>();
        this.map = new HashMap<>();
        this.buildSysnets(synsets);
        this.g = new Digraph(this.rmap.size());
        this.buildHypernyms(hypernyms);

        if (this.hasCycles()) throw new IllegalArgumentException();
    }

    private boolean hasCycles() {
        return false;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet("synsets3.txt", "hypernyms3InvalidCycle.txt");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return this.map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP s = new SAP(this.g);
        Iterable<Integer> a = this.map.get(nounA);
        Iterable<Integer> b = this.map.get(nounB);
        return s.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP s = new SAP(this.g);
        Iterable<Integer> a = this.map.get(nounA);
        Iterable<Integer> b = this.map.get(nounB);
        int ancestor = s.ancestor(a, b);
        return this.rmap.get(ancestor);
    }
}
