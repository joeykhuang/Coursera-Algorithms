/**
 * @author : Joey Huang
 * @since : 12/29/21, Wed
 **/

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class BaseballElimination {
    private final int n;
    private final HashMap<String, Integer> teams;
    private final HashMap<Integer, String> teamsRev;
    private final HashMap<Integer, int[]> stats;
    private final int[][] schedule;
    private final HashMap<String, Iterable<String>> elimination;

    private void checkValidity(String team) {
        if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
    }

    private FlowNetwork createFlowNetwork(String team) {
        int teamMatchups = 0;
        int teamID = this.teams.get(team);
        int maxWin = this.wins(team) + this.remaining(team);
        ArrayList<int[]> matchups = new ArrayList<>();
        for (int i = 0; i < this.n - 1; i++) {
            for (int j = i + 1; j < this.n; j++) {
                if (i != j && i != teamID && j != teamID && this.schedule[i][j] != 0) {
                    teamMatchups++;
                    matchups.add(new int[]{i, j});
                }
            }
        }
        FlowNetwork f = new FlowNetwork(teamMatchups + this.n + 2);

        // add game flow edges
        for (int i = 0; i < teamMatchups; i++) {
            int x = matchups.get(i)[0];
            int y = matchups.get(i)[1];
            f.addEdge(new FlowEdge(0, i + 1, this.schedule[x][y]));
            f.addEdge(new FlowEdge(i + 1, teamMatchups + x + 1, Double.POSITIVE_INFINITY));
            f.addEdge(new FlowEdge(i + 1, teamMatchups + y + 1, Double.POSITIVE_INFINITY));
        }

        int t = teamMatchups + this.n + 1;
        for (int i = 0; i < this.n; i++) {
            if (i == teamID) continue;
            f.addEdge(new FlowEdge(teamMatchups + i + 1, t, maxWin - this.stats.get(i)[0]));
        }

        return f;
    }

    private Iterable<String> trivialElimination(String team) {
        int winMax = this.wins(team) + this.remaining(team);
        ArrayList<String> subset = new ArrayList<>();
        for (int i = 0; i < this.n; i++){
            if (this.stats.get(i)[0] > winMax) {
                subset.add(this.teamsRev.get(i));
            }
        }
        return subset.isEmpty() ? null : subset;
    }

    private void generateEliminations() {
        for (int i = 0; i < this.n; i ++) {
            String team = this.teamsRev.get(i);
            Iterable<String> trivialElimResults = this.trivialElimination(team);
            if (trivialElimResults != null) {
                this.elimination.put(team, trivialElimResults);
                continue;
            }
            FlowNetwork f = this.createFlowNetwork(team);
            int begin = f.V() - this.n - 1;
            FordFulkerson ff = new FordFulkerson(f, 0, f.V() - 1);
            ArrayList<String> subset = new ArrayList<>();
            for (int j = 0; j < this.n; j++) {
                if (j != i && ff.inCut(begin + j)) {
                    subset.add(this.teamsRev.get(j));
                }
            }
            this.elimination.put(team, subset.isEmpty() ? null : subset);
        }
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In file = new In(filename);
        this.n = file.readInt();
        this.teams = new LinkedHashMap<>();
        this.teamsRev = new HashMap<>();
        this.stats = new HashMap<>();
        this.schedule = new int[this.n][this.n];
        this.elimination = new HashMap<>();

        for (int i = 0; i < this.n; i++) {
            String teamName = file.readString();
            this.teams.put(teamName, i);
            this.teamsRev.put(i, teamName);
            this.stats.put(i, new int[]{file.readInt(), file.readInt(), file.readInt()});

            for (int j = 0; j < this.n; j++) {
                this.schedule[i][j] = file.readInt();
            }
        }

        this.generateEliminations();
    }

    // number of teams
    public int numberOfTeams() {
        return this.n;
    }

    // all teams
    public Iterable<String> teams() {
        return this.teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        this.checkValidity(team);
        return this.stats.get(this.teams.get(team))[0];
    }

    // number of losses for given team
    public int losses(String team) {
        this.checkValidity(team);
        return this.stats.get(this.teams.get(team))[1];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        this.checkValidity(team);
        return this.stats.get(this.teams.get(team))[2];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        this.checkValidity(team1);
        this.checkValidity(team2);
        int team1Index = this.teams.get(team1);
        int team2Index = this.teams.get(team2);
        return this.schedule[team1Index][team2Index];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        this.checkValidity(team);
        return this.elimination.get(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        this.checkValidity(team);
        return this.elimination.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
