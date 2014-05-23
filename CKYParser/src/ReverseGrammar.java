import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * A reversed grammar.
 * @author Antoine LAFOUASSE
 */
public class ReverseGrammar extends Grammar0 {
	private HashMap<String, LinkedList<RewrRuleProb>> _map;
	
	/**
	 * initialise the container that actually stores the grammar
	 */
	protected void init() {
		this._map = new HashMap<String, LinkedList<RewrRuleProb>>();
	}
	
	public ReverseGrammar(Symbol axiom) {
		super(axiom);
		this.init();
	}
	
	public void addRule(RewrRuleProb r)
	{
		LinkedList<RewrRuleProb>	lst;
		String						rhs;

		rhs = r.getRHS()[0];
		if (r.getRHS().length == 2)
			rhs += " " + r.getRHS()[1];
		if (!this._map.containsKey(rhs))
		{
			lst = new LinkedList<RewrRuleProb>();
			lst.add(r);
			this._map.put(rhs, lst);
		}
		else if (!this._map.get(rhs).contains(r))
		{
			this._map.get(rhs).add(r);
			Collections.sort(this._map.get(rhs),
					Collections.reverseOrder());
		}
		else
			throw new RuntimeException("Duplicate rules in the grammar");
				
	}
	
	public void addRule(String new_rule)
	{
		String		tab[];
		String		rhs[];
		
		tab = new_rule.split(" ");
		if (tab.length < 4 || tab.length > 5)
			throw new RuntimeException("Incorrect grammar rule");
		rhs = new String[tab.length == 5 ? 2 : 1];
		rhs[0] = tab[3];
		if (tab.length == 5)
			rhs[1] = tab[4];
		this.addRule(new RewrRuleProb(tab[1], rhs,
				Double.parseDouble(tab[0])));
	}

	public LinkedList<RewrRuleProb> getRules(String[] rhs)
	{
		String	k;
		
		k = rhs[0];
		if (rhs.length > 1)
			k += " " + rhs[1];
		return (this.getRules(k));
	}
	
	
	public LinkedList<RewrRuleProb> getRules(String rhs)
	{
		return (this._map.get(rhs));
	}
}
