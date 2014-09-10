import java.util.ArrayList;
import java.util.List;

public class Clause {
	List<SubClause> subClauses;

	public Clause() {
		this.subClauses = new ArrayList<SubClause>();
	}

	public void addClause(SubClause sc) {
		subClauses.add(sc);
	}

	public void addAllClauses(List<SubClause> listSubClause) {
		subClauses.addAll(listSubClause);
	}

	@Override
	public String toString() {
		String cnfClause = "";
		boolean firstIteration = true;
		for (SubClause cs : subClauses) {
			if (firstIteration) {
				cnfClause += cs.assignment;
				firstIteration = false;
			} else {
				cnfClause += " or " + cs.assignment;
			}
		}

		return "[" + cnfClause + "]";
	}
}
