import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Chart
{
	private HashMap<Coord, LinkedList<Tree>>	_map;
	
	public Chart()
	{
		this._map = new HashMap<Coord, LinkedList<Tree>>();
	}
	
	public void addTree(int row, int col, Tree t)
	{
		Coord				c;
		LinkedList<Tree>	l;
		
		for (Entry<Coord, LinkedList<Tree>> e : this._map.entrySet())
			System.out.println(e.getKey());
		c = new Coord(row, col);
		if (this._map.containsKey(c))
		{
			System.out.println("Adding a complementary key");
			this._map.get(c).add(t);
			Collections.sort(this._map.get(c), Collections.reverseOrder());
		}
		else
		{
			System.out.println("New key list");
			l = new LinkedList<Tree>();
			l.add(t);
			this._map.put(c, l);
		}
	}
	
	public LinkedList<Tree> getTrees(int row, int col)
	{
		return (this._map.get(new Coord(row, col)));
	}
	
	private class Coord
	{
		private int		_row;
		private int		_col;
		
		public Coord(int row, int col)
		{
			this._row = row;
			this._col = col;
		}
		
		@Override
		public boolean equals(Object o)
		{
			Coord	c;
			
			if (o == null || !(o instanceof Coord))
				return (false);
			c = (Coord)o;
			return (this._row == c._row && this._col == c._col);
		}
		
		public String toString()
		{
			return ("(" + this._row + ", " + this._col +")");
		}
	}
}
