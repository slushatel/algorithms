import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
	final Map<String, Integer> teams;
	final int[] w, l, r;
	final int[][] g;
	final int n;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		n = in.readInt();
		teams = new HashMap<>(n);
		w = new int[n];
		l = new int[n];
		r = new int[n];
		g = new int[n][n];
		for (int i = 0; i < n; i++) {
			teams.put(in.readString(), i);
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < n; j++) {
				g[i][j] = in.readInt();
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return n;
	}

	// all teams
	public Iterable<String> teams() {
		return teams.keySet();
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

		FlowNetwork fn = new FlowNetwork()
		return true;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		checkTeams(team);
		return new ArrayList<>();
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination("C:\\work\\algorithms\\src\\main\\java\\teams4.txt");
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
