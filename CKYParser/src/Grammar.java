import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * A reversed grammar.
 * @author Antoine LAFOUASSE
 */
public class Grammar
{
	private final HashMap<LinkedList<Symbol>, LinkedList<RewritingRule>> _map;
	
	public Grammar()
	{
		this._map =
				new HashMap<LinkedList<Symbol>, LinkedList<RewritingRule>>();
	}
	
	public void addRule(RewritingRule r)
	{
		LinkedList<RewritingRule>	lst;

		if (this._map.containsKey(r.getRHS()))
		{
			lst = new LinkedList<RewritingRule>();
			lst.add(r);
			this._map.put(r.getRHS(), lst);
		}
		else if (!this._map.get(r.getRHS()).contains(r))
		{
			this._map.get(r.getRHS()).add(r);
			Collections.sort(this._map.get(r.getRHS()));
		}
		else
			throw new RuntimeException("Duplicate rules in the grammar");
				
	}
	
	public void addRule(String line)
	{
		String				tab[];
		LinkedList<Symbol>	rhs;
		
		rhs = new LinkedList<Symbol>();
		tab = line.split(" ");
		if (tab.length < 4)
			throw new RuntimeException("Incorrect grammar rule");
		for (int i = 3; i < tab.length; i++)
			rhs.add(new Symbol(tab[i]));
		addRule(new RewritingRule(new Symbol(tab[1]),
				rhs,
				Double.parseDouble(tab[0])));
	}
}
