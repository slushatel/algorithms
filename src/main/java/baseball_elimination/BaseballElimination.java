package baseball_elimination;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final Map<String, Integer> teams;
    private final String[] teamsArray;
    private final int[] w, l, r;
    private final int[][] g;
    private final int n;

    private final Set<String> isEliminated;
    private final Map<String, List<String>> eliminatingSet;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        teams = new HashMap<>(n);
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        teamsArray = new String[n];
        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teams.put(team, i);
            teamsArray[i] = team;
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }

        isEliminated = new HashSet<>();
        eliminatingSet = new HashMap<>();
        for (int i = 0; i < n; i++) {
            checkEliminating(teamsArray[i]);
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teamsArray);
    }

    private void checkTeams(String... teamList) {
        for (Object team : teamList) {
            if (!teams.keySet().contains(team)) throw new IllegalArgumentException();
        }
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeams(team);
        return w[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeams(team);
        return l[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeams(team);
        return r[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeams(team1, team2);
        return g[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeams(team);
        return isEliminated.contains(team);
    }

    private void checkEliminating(String team) {
        int teamIndex = teams.get(team);
        FlowNetwork fn = new FlowNetwork((n - 1) * (n - 2) / 2 + n + 1);
        int curInd = 1;
        int startTeamsIndex = (n - 1) * (n - 2) / 2 + 1;
        final int LAST_ITEM = (n - 1) * (n - 2) / 2 + n;
        int maxPossibleFlow = 0;
        for (int i = 0; i < n - 1; i++) {
            if (i == teamIndex) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == teamIndex) continue;
                fn.addEdge(new FlowEdge(0, curInd, g[i][j]));
                maxPossibleFlow += g[i][j];
                fn.addEdge(new FlowEdge(curInd, startTeamsIndex + i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(curInd, startTeamsIndex + j, Double.POSITIVE_INFINITY));
                curInd++;
            }
            int residualWins = w[teamIndex] + r[teamIndex] - w[i];
            if (residualWins < 0) {
                isEliminated.add(team);
                List<String> list = eliminatingSet.getOrDefault(team, new LinkedList<>());
                list.add(teamsArray[i]);
                eliminatingSet.put(team, list);
                return;
            }
            fn.addEdge(new FlowEdge(startTeamsIndex + i, LAST_ITEM, residualWins));
        }
        FordFulkerson ff = new FordFulkerson(fn, 0, LAST_ITEM);

        if (ff.value() < maxPossibleFlow) {
            isEliminated.add(team);
            for (int i = 0; i < n - 1; i++) {
                int teamInd = i < teamIndex ? i : i + 1;
                if (ff.inCut(startTeamsIndex + i)) {
                    List<String> list = eliminatingSet.getOrDefault(team, new LinkedList<>());
                    list.add(teamsArray[teamInd]);
                    eliminatingSet.put(team, list);
                }
            }
        }
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeams(team);
        return eliminatingSet.get(team);
    }

//    public static void main(String[] args) {
//        checkFile("D:\\work\\algorithms\\src\\main\\java\\teams4.txt");
//        checkFile("D:\\work\\algorithms\\src\\main\\java\\teams5.txt");
//    }

//    private static void checkFile(String file) {
//        BaseballElimination division = new BaseballElimination(file);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            } else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
//        System.out.println();
//    }
}
