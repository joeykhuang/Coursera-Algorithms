/**
 * @author : Joey Huang
 * @since : 12/24/21, Fri
 **/

public class Outcast {

    private WordNet w;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.w = wordnet;
    }

    // see test client below
    public static void main(String[] args) {

    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = Integer.MIN_VALUE;
        String outcastNoun = "";
        for (String noun : nouns){
            int distSum = 0;
            for (String othernoun : nouns){
                if (noun != othernoun) distSum += this.w.distance(noun, othernoun);
            }
            if (distSum > maxDist) {
                maxDist = distSum;
                outcastNoun = noun;
            }
        }
        return outcastNoun;
    }
}
