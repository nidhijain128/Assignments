public class SubClause {
	String assignment;
	int guest;
	int table;
	boolean negation;

	public SubClause(int guest, int table, boolean negation) {
		this.guest = guest;
		this.table = table;
		this.negation = negation;
		if (negation)
			this.assignment = "!X" + guest + table;
		else
			this.assignment = "X" + guest + table;
	}
}
