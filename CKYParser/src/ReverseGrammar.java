import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * A reversed grammar.
 * @author Antoine LAFOUASSE
 */
public class ReverseGrammar extends Grammar0 {
	private HashMap<RHS, LinkedList<RewrRuleProb>> _map;
	
	/**
	 * initialise the container that actually stores the grammar
	 */
	protected void init() {
		this._map = new HashMap<RHS, LinkedList<RewrRuleProb>>();
	}
	
	/**
	 * create a new grammar with the given axiom
	 * @param axiom: the axiom of the grammar
	 */
	public ReverseGrammar(Symbol axiom) {
		super(axiom);
		this.init();
	}
	
	/**
	 * add a new rewriting rule to the grammar
	 * @param r: the rewriting rule to add to the grammar
	 */
	public void addRule(RewrRuleProb r)
	{
		LinkedList<RewrRuleProb>	lst;
		RHS					rhs;

		rhs = r.getRHS();
		if (!this._map.containsKey(rhs))
		{
			lst = new LinkedList<RewrRuleProb>();
			lst.add(r);
			this._map.put(rhs, lst);
		}
		else if (!this._map.get(rhs).contains(r))
		{
			this._map.get(rhs).add(r);
			// fait en sorte que les productions sont toujours ordonnees 
			// par ordre decroissant de probabilites
			Collections.sort(this._map.get(rhs),
					Collections.reverseOrder());
		}
		else
			throw new RuntimeException("Duplicate rules in the grammar");
				
	}
	
	public void addRule(String new_rule)
	{
		addRule(new RewrRuleProb(new_rule));
	}

	public LinkedList<RewrRuleProb> getRules(RHS rhs)
	{
		return this._map.get(rhs);
	}

}
