import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogicSATPLResolution {
	List<Clause> KB = new LinkedList<Clause>();
	int count = 0, count1 = 0;
	int sizeofKB = 0;

	public boolean isResolved(List<Clause> clauses) {
		// add new clauses to knowledge base
		int KBsize = 0;
		
		if (KB.size() != 0) {
			for (KBsize = 0; KBsize < clauses.size(); KBsize++)
				if (!compareClauseList(KB, clauses.get(KBsize))) {
					KB.add(clauses.get(KBsize));
					break;
				}
			if (KBsize == clauses.size())
				return true;
		}
		for (KBsize = 0; KBsize < clauses.size(); KBsize++)
			if (!compareClauseList(KB, clauses.get(KBsize)))
				KB.add(clauses.get(KBsize));
		
		
		/*List<Clause> clause1 = new LinkedList<Clause>();
		for (int i = 0; i < KB.size(); i++) {
			Clause c = KB.get(i);
			clause1.add(removeTautologies(c));
		}
		KB.clear();
		KB.addAll(clause1);*/
		
		/*for(int x=0;x<KB.size();x++)
			System.out.println(KB.get(x).toString());*/

		// check if any 2 clauses are compliments of each other
		for (int i = 0; i < KB.size() - 1; i++) {
			int length1 = KB.get(i).subClauses.size();
			if (length1 == 1) {
				SubClause c = KB.get(i).subClauses.get(0);
				for (int j = i + 1; j < KB.size(); j++) {
					int length2 = KB.get(j).subClauses.size();
					if (length2 == 1) {
						SubClause c1 = KB.get(j).subClauses.get(0);
						if (c.guest == c1.guest && c.table == c1.table
								&& c.negation != c1.negation)
							return false;
					}
				}
			}
		}

		// perform resolution
		List<Clause> clause = new LinkedList<Clause>();
		for (int i = 0; i < KB.size() - 1; i++) {
			Clause c = KB.get(i);
			for (int j = i + 1; j < KB.size(); j++) {
				clause.addAll(returnPairofClause(c, KB.get(j)));
			}
		}
		sizeofKB = KB.size();

		boolean flag1 = isResolved(clause);
		return flag1;
	}

	private boolean compareClauseList(List<Clause> clause1, Clause clause) {
		for (int i = 0; i < clause1.size(); i++) {
			Clause c = clause1.get(i);
			if (CompareClauses(c, clause))
				return true;
		}
		return false;
	}

	private boolean CompareClauses(Clause c, Clause clause) {
		int foundSubClauseMatching = 0;
		if (c.subClauses.size() == clause.subClauses.size()) {
			for (int i = 0; i < c.subClauses.size(); i++) {
				SubClause sc = c.subClauses.get(i);
				for (int j = 0; j < clause.subClauses.size(); j++) {
					if (sc.assignment
							.equals(clause.subClauses.get(j).assignment)) {
						foundSubClauseMatching++;
						break;
					}
				}
			}
			if (foundSubClauseMatching == c.subClauses.size())
				return true;
		}
		return false;
	}

	public List<Clause> returnPairofClause(Clause c1, Clause c2) {
		List<SubClause> listSubClauses1 = c1.subClauses;
		List<SubClause> listSubClauses2 = c2.subClauses;
		List<Clause> clause = new ArrayList<Clause>();
		for (int i = 0; i < listSubClauses1.size(); i++) {
			SubClause sc = FindNegation(listSubClauses1.get(i), listSubClauses2);
			if (sc != null) {
				Clause clause1 = new Clause();
				clause1.addAllClauses(listSubClauses1);
				clause1.addAllClauses(listSubClauses2);
				clause1.subClauses.remove(sc);
				clause1.subClauses.remove(listSubClauses1.get(i));
				clause1 = RemoveDuplicates(clause1);
				clause.add(clause1);
			}
		}
		return clause;
	}

	private Clause RemoveDuplicates(Clause clause) {
		for (int i = 0; i < clause.subClauses.size(); i++) {
			SubClause sc = clause.subClauses.get(i);
			for (int j = 0; j < clause.subClauses.size(); j++) {
				if (i != j
						&& sc.assignment
								.equals(clause.subClauses.get(j).assignment)) {
					clause.subClauses.remove(sc);
					break;
				}
			}
		}
		return clause;
	}

	private SubClause FindNegation(SubClause sc, List<SubClause> listSubClauses) {
		for (SubClause subClause : listSubClauses) {
			if (sc.guest == subClause.guest && sc.table == subClause.table
					&& sc.negation != subClause.negation) {
				return subClause;
			}
		}
		return null;
	}
	
	public Clause removeTautologies(Clause clause) {
		List<SubClause> subClause = clause.subClauses;
		List<SubClause>	retSc = new ArrayList<SubClause>();
		for(int i=0;i<subClause.size();i++) {
			boolean isNegation = false;
			SubClause sc = subClause.get(i);
			for(int j=0;j<subClause.size();j++) {
				if(sc.guest == subClause.get(j).guest && sc.table == subClause.get(j).table && sc.negation != subClause.get(j).negation)
				{
					isNegation = true;
					break;
				}
				
			}
			if(!isNegation)
				retSc.add(sc);
		}
		clause.subClauses.clear();
		clause.subClauses.addAll(retSc);
		return clause;
	}
}
