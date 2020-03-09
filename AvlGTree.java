package cmsc420.sortedMap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import org.w3c.dom.*;

import cmsc420.structure.City;

public class AvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	//		public static void main(String [] args) {
	//	
	//			SortedMap<String, Integer> map = new AvlGTree<>();
	//			map.put("hi", 5);
	//			map.put("hello", 10);
	//			map.put("namaste", 15);
	//			map.put("apple", 2);
	//					
	//			SortedMap<String, Integer> tmap = new TreeMap<>();
	//			tmap.put("hi", 5);
	//			tmap.put("hello", 10);
	//			tmap.put("namaste", 15);
	//			tmap.put("apple", 2);
	//					
	//			AvlGTree<String, Integer> map3 = new AvlGTree<>();
	//			map3.put("hi", 5);
	//			map3.put("hello", 10);
	//			map3.put("namaste", 15);
	//			map3.put("apple", 2);
	//					
	//			System.out.println("tree first key " + tmap.firstKey());
	//			System.out.println("tree last key " + tmap.lastKey());
	//			System.out.println("tree size " + tmap.size());
	//			System.out.println("avltree first key " + map.firstKey());
	//			System.out.println("avltree last key " + map.lastKey());
	//			System.out.println("avltree size " + map.size());
	//			System.out.println("AVLtree first key " + map3.firstKey());
	//			System.out.println("AVLtree last key " + map3.lastKey());
	//			System.out.println("AVLtree size " + map3.size());
	//			
	//		
	//
	//		System.out.println();
	//
	//		SortedMap<String, Integer> map2 = new AvlGTree<>();
	//		map2.put("hi", 5);
	//		map2.put("hello", 10);
	//		map2.put("namaste", 15);
	//		//map2.putAll(map);
	//		SortedMap<String, Integer> avlmap = new AvlGTree<>();
	//		avlmap = map2.subMap("acc", "jiok");
	//		//System.out.println("avl submap size: " + avlmap.size());
	//
	//		System.out.println("avl first key " +map2.firstKey());
	//		System.out.println("avl last key " + map2.lastKey());
	//		System.out.println("avl size " + map2.size());
	//		System.out.println("avl contains hello " + map2.containsKey("hello"));
	//		System.out.println("avl contains bye " + map2.containsKey("bye"));
	//		System.out.println("avl contains 5 " + map2.containsValue(5));
	//		System.out.println("avl contains 10 " + map2.containsValue(10));
	//		System.out.println("avl contains 15 " + map2.containsValue(15));
	//		System.out.println("avl get hello value " + map2.get("hello"));
	//
	//	}

	//    public static void main(String[]args) {
	//        TreeMap<String, Integer> treemap = new TreeMap<>();    
	//        AvlGTree<String, Integer> avlmap = new AvlGTree<>();  
	//        
	//        treemap.put("cmsc 232", 132);
	//        treemap.put("cmsc 131", 131);
	//        treemap.put("cmsc 420", 420);
	//        
	//        System.out.println(treemap.toString());
	//        
	//        avlmap.put("cmsc 132", 132);
	//        avlmap.put("cmsc 131", 131);
	//        avlmap.put("cmsc 420", 420);
	//        avlmap.put("cmsc 411", 411);
	//        avlmap.put("cmsc 216", 216);
	//        avlmap.put("cmsc 330", 330);
	//        avlmap.printAvlGTree();
	//        System.out.println(avlmap.toString());
	//    }

	private int g;
	private transient EntrySet entrySet = null;

	/**
	 * The comparator used to maintain order in this tree map, or
	 * null if it uses the natural ordering of its keys.
	 */
	private final Comparator<? super K> comparator;

	private Entry <K,V> root = null;

	/**
	 * The number of entries in the tree
	 */
	private int size = 0;

	/**
	 * The number of structural modifications to the tree.
	 */
	private int modCount;

	public AvlGTree() {
		comparator = null;
		root = null;
		/* Put Default max height difference value to be 1 */ 
		g = 1; 
	}

	public AvlGTree(final Comparator<? super K> c) {
		/* Initializes comparator based on the parameter given */
		comparator = c;
		root = null;
		/* Put Default max height difference value to be 1 */
		g = 1;
	}

	public AvlGTree(final int g) {
		comparator = null;
		root = null;
		/* Initializes the new g based on the parameter passed in */
		this.g = g;
	}

	public AvlGTree(final Comparator<? super K> comp, final int g) {
		/* Initializes comparator based on the parameter given */
		comparator = comp;
		root = null;
		/* Initializes the new g based on the parameter passed in */
		this.g = g;
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	public int size() {
		return size;
	}

	public int rootHeight() {
		return height(root);
	}

	/**
	 * @param n node in the Set
	 * @return the height of the node
	 */
	private int height(Entry<K,V> n) {
		if (n == null)
			return -1;
		return 1 + Math.max(height(n.left), height(n.right));
	}

	/**
	 * Removes all of the mappings from this map (optional operation). 
	 * The map will be empty after this call returns.
	 * @throws UnsupportedOperationException if the clear operation is not supported by this map
	 */
	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	/**
	 * Returns {@code true} if this map contains a mapping for the specified
	 * key.
	 *
	 * @param key key whose presence in this map is to be tested
	 * @return {@code true} if this map contains a mapping for the
	 *         specified key
	 * @throws ClassCastException if the specified key cannot be compared
	 *         with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *         and this map uses natural ordering, or its comparator
	 *         does not permit null keys
	 */
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	/**
	 * Returns {@code true} if this map maps one or more keys to the
	 * specified value.  More formally, returns {@code true} if and only if
	 * this map contains at least one mapping to a value {@code v} such
	 * that {@code (value==null ? v==null : value.equals(v))}.  This
	 * operation will probably require time linear in the map size for
	 * most implementations.
	 *
	 * @param value value whose presence in this map is to be tested
	 * @return {@code true} if a mapping to {@code value} exists;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean containsValue(Object value) {
		for (Entry<K,V> e = getFirstEntry(); e != null; e = successor(e))
			if (valEquals(value, e.value))
				return true;
		return false;
	}

	@Override
	public V get(Object key) {
		Entry<K,V> p = getEntry(key);
		return (p==null ? null : p.value);
	}

	public Comparator<? super K> comparator() {
		return comparator;
	}

	/**
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	@Override
	public K firstKey() {
		if(root == null)
			throw new NoSuchElementException();

		Entry<K,V> first = getFirstEntry();
		if (first.getKey() == null) {
			throw new NoSuchElementException();
		} else
			return first.getKey();
	}

	/**
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	@Override
	public K lastKey() {
		if(root == null)
			throw new NoSuchElementException();

		Entry<K,V> last = getLastEntry();
		if (last.getKey() == null) {
			throw new NoSuchElementException();
		} else
			return last.getKey();
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		super.putAll(map);
	}

	final Entry<K,V> getEntry(Object key) {
		if (comparator != null)
			return getEntryUsingComparator(key);
		if (key == null)
			throw new NullPointerException();
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		Entry<K,V> p = root;
		while (p != null) {
			int cmp = k.compareTo(p.key);
			if (cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}

	final Entry<K,V> getFirstEntry() {

		Entry<K,V> p = root;
		if (p != null)
			while (p.left != null)
				p = p.left;
		return p;
	}

	final Entry<K,V> getLastEntry() {

		Entry<K,V> p = root;
		if (p != null)
			while (p.right != null)
				p = p.right;
		return p;
	}

	//	@SuppressWarnings("unchecked")
	//	final int compare(Object k1, Object k2) {
	//		return comparator == null ? ((Comparable<? super K>) k1).compareTo((K) k2)
	//				: comparator.compare((K) k1, (K) k2);
	//	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		EntrySet es = entrySet;
		return (es != null) ? es : (entrySet = new EntrySet());
		//return new EntrySet();
	}

	//comparison for any object
	final static boolean valEquals(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}

	Entry<K,V> successor(Entry<K,V> t) {
		if (t == null)
			return null;
		else if (t.right != null) {
			Entry<K,V> p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {
			Entry<K,V> p = t.parent;
			Entry<K,V> ch = t;
			while (p != null && ch == p.right) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	private Entry<K,V>  predecessor(Entry<K,V> t) {
		if (t == null)
			return null;
		else if (t.left != null) {
			Entry<K,V> p = t.left;
			while (p.right != null)
				p = p.right;
			return p;
		} else {
			Entry<K,V> p = t.parent;
			Entry<K,V> ch = t;
			while (p != null && ch == p.left) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	final Entry<K,V> getEntryUsingComparator(Object key) {
		K k = (K) key;
		Comparator<? super K> cpr = comparator;
		Entry<K,V> p = root;

		if (cpr != null) {

			while (p != null) {
				int cmp = -1*cpr.compare(k, p.key);
				if (cmp < 0)
					p = p.left;
				else if (cmp > 0)
					p = p.right;
				else
					return p;
			}
		}
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return size == 0 ? true : false;
	}

	private void rebalance(Entry<K,V> n) {
		//System.out.println("print g: " + g);
		//TODO problem in balancing
		setBalance(n);
		//System.out.println("n & n.balance: " + n.getKey() + " " + n.balance);
		//		System.out.println("root val3: " + root.value + "node value: " + n.getKey());
		//		System.out.println("n.balance: " + n.balance);

		if (n.balance < -g) {
			if (height(n.left.left) >= height(n.left.right))
				n = rotateRight(n);
			else
				n = rotateLeftThenRight(n);

		} else if (n.balance > g) {
			if (height(n.right.right) >= height(n.right.left))
				n = rotateLeft(n);
			else
				n = rotateRightThenLeft(n);
		}

		//System.out.println("root val4: " + root.value + "node value: " + n.getKey());
		if (n.parent != null) {
			rebalance(n.parent);
		} else {
			root = n;
		}
	}

	@SuppressWarnings("unchecked")
	private Entry<K,V> rotateLeft(Entry<K,V> a) {

		Entry<K,V> b = a.right;
		b.parent = a.parent;

		a.right = b.left;

		if (a.right != null)
			a.right.parent = a;

		b.left = a;
		a.parent = b;

		if (b.parent != null) {
			if (b.parent.right == a) {
				b.parent.right = b;
			} else {
				b.parent.left = b;
			}
		}

		setBalance(a, b);

		return b;
	}

	@SuppressWarnings("unchecked")
	private Entry<K,V> rotateRight(Entry<K,V> a) {

		Entry<K,V> b = a.left;
		b.parent = a.parent;

		a.left = b.right;

		if (a.left != null)
			a.left.parent = a;

		b.right = a;
		a.parent = b;

		if (b.parent != null) {
			if (b.parent.right == a) {
				b.parent.right = b;
			} else {
				b.parent.left = b;
			}
		}

		setBalance(a, b);

		return b;
	}

	private Entry<K,V> rotateLeftThenRight(Entry<K,V> n) {
		n.left = rotateLeft(n.left);
		return rotateRight(n);
	}

	private Entry<K,V> rotateRightThenLeft(Entry<K,V> n) {
		n.right = rotateRight(n.right);
		return rotateLeft(n);
	}

	private void setBalance(Entry<K,V> n) {
		//System.out.println("height(n.right) " + height(n.right) + " height(n.left) " + height(n.left));
		n.balance = height(n.right) - height(n.left);
	}

	@SuppressWarnings("unchecked")
	private void setBalance(Entry<K,V>... nodes) {
		for (Entry<K,V> n : nodes) {
			//System.out.println("height(n.right) " + height(n.right) + " height(n.left) " + height(n.left));
			n.balance = height(n.right) - height(n.left);
		}
	}

	@Override
	public V put(K key, V value) {
		Entry<K,V> t = root;
		if (t == null) {
			// check for NullPointerException
			root = new Entry<K,V>(key, value, null);
			size = 1;
			modCount++;
			return null;
		}
		//		Entry<K,V> parent = root;
		//		parent.right = new Entry<K, V>(key, value, root);
		//		System.out.println("in put method: t's value " + t.getKey());
		int cmp;
		Entry<K,V> parent;
		// split comparator and comparable paths
		Comparator<? super K> cmpr = comparator();
		if (cmpr != null) {
			do {
				parent = t;
				//System.out.println("parent: " + parent.getKey());
				cmp = cmpr.compare(t.key, key);
				//System.out.println("compared: " + cmpr.compare(t.key, key));
				//System.out.println("t.right: " + t.right);
				//System.out.println("compare return: " + cmpr.compare(key, t.key));
				if (cmp < 0)
					t = t.left;
				else if (cmp > 0)
					t = t.right;
				else 
					return t.setValue(value);
				//System.out.println("comparator is not null");
				//System.out.println("t after t.right: " + t);
			} while (t != null);
		}
		else {
			parent = null;
			//cmp = 1;  //***************************************
			//System.out.println("comparator is null");
			if (key == null)
				throw new NullPointerException();
			Comparable<? super K> k = (Comparable<? super K>) key;
			do {
				parent = t;
				cmp = k.compareTo(t.key);
				if (cmp < 0)
					t = t.left;
				else if (cmp > 0)
					t = t.right;
				else
					return t.setValue(value);
			} while (t != null);
		}

		//System.out.println("parent key: " + parent.getKey());
		//System.out.println("root val1: " + root.value);
		Entry<K,V> e = new Entry<K,V>(key, value, parent);
		if (cmp < 0)
			parent.left = e; 
		else {
			parent.right = e;
			//System.out.println("parent right: " + parent.right.getKey());
		}
		//System.out.println("root val2: " + root.value);
		rebalance(parent);  
		//System.out.println("root val5: " + root.value);
		size++;
		modCount++;
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return new SubMap<K, V>();
	}

	public void printAvlGTree(Element output, Document results, AvlGTree<K,V> cities) {
		//System.out.println("root val: " + root.getKey());
		//System.out.println("root_val: " + root.right.key);
		printAvlGTree(root, output, results, cities);
	}

	private void printAvlGTree(Entry<K, V> n, Element outputNode, Document results, AvlGTree<K, V> cities) {
		if (n != null) {
			//System.out.println("get:" + cities.get(n.getKey()));
			City c = (City) cities.get(n.getKey()); 
			String coord = "(" + c.getLocalX() + "," + c.getLocalY() + ")";
			Element elem = results.createElement("node");
			elem.setAttribute("key", (String) n.getKey());
			elem.setAttribute("value", coord);
			outputNode.appendChild(elem);

			//System.out.println(n.getKey() + " " + n.getValue());
			if (n.right == null) {
				Element empty = results.createElement("emptyChild");
				elem.appendChild(empty);
				//System.out.println("reached here");
			} else
				printAvlGTree(n.right, elem, results, cities);

			if (n.left == null) {
				Element empty = results.createElement("emptyChild");
				elem.appendChild(empty);
			} else
				printAvlGTree(n.left, elem, results, cities);
		}
	}

	static class Entry <K,V> implements Map.Entry<K, V> {

		int height;
		int balance;
		Entry<K,V> left = null;
		Entry<K,V> right = null;
		Entry<K,V> parent;
		K key;
		V value;

		Entry(K key, V value){
			this.key = key;
			this.value = value;
			height = 0;
			balance = 0;
		}

		public Entry(K key, V value, Entry<K, V> parent) {
			this(key, value);
			this.parent = parent;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V val) {
			V oldValue = this.value;
			this.value = val;
			return oldValue;
		}

		//comparison
		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<?,?> e = (Map.Entry<?, ?>) o;

			return valEquals(key,e.getKey()) && valEquals(value, e.getValue());
		}

		//returns hashcode for each entry at the entry level
		public int hashCode() {

			int keyHash = (key==null ? 0 : key.hashCode());
			int valueHash = (value==null ? 0 : value.hashCode());
			return keyHash ^ valueHash;
		}

		//toString for each entry
		public String toString(){
			return key + "=" + value;
		}
	}

	abstract class PrivateEntryIterator<T> implements Iterator<T> {
		Entry<K,V> next;
		Entry<K,V> lastReturned;
		int expectedModCount;

		PrivateEntryIterator(Entry<K,V> first) {
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
		}

		public final boolean hasNext() {
			return next != null;
		}

		final Entry<K,V> nextEntry() {
			Entry<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = successor(e);
			lastReturned = e;
			return e;
		}   

		final Entry<K,V> prevEntry() {
			Entry<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = predecessor(e);
			lastReturned = e;
			return e;
		}

		public void  remove() {
			//			if (lastReturned == null)
			//				throw new IllegalStateException();
			//			if (modCount != expectedModCount)
			//				throw new ConcurrentModificationException();
			//			// deleted entries are replaced by their successors
			//			if (lastReturned.left != null && lastReturned.right != null)
			//				next = lastReturned;
			//			//deleteEntry(lastReturned);
			//			expectedModCount = modCount;
			//			lastReturned = null;
			throw new UnsupportedOperationException();
		}
	}

	final class  EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
		EntryIterator(Entry<K,V> first) {
			super(first);
		}

		public Map.Entry<K,V>  next() {
			return nextEntry();
		}
	}

	final class EntrySet extends AbstractSet<Map.Entry<K,V>> {

		public Iterator<Map.Entry<K,V>>  iterator() {
			return new EntryIterator(getFirstEntry());
		}

		public boolean  contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
			V value = entry.getValue();
			Entry<K,V> p = getEntry(entry.getKey());
			return p != null && valEquals(p.getValue(), value);
		}

		public boolean remove(Object o) {
			//			if (!(o instanceof Map.Entry))
			//				return false;
			//			Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
			//			V value = entry.getValue();
			//			Entry<K,V> p = getEntry(entry.getKey());
			//			if (p != null && valEquals(p.getValue(), value)) {
			//				//deleteEntry(p);
			//				return true;
			//			}
			//			return false;
			throw new UnsupportedOperationException();
		}

		public int  size() {
			return AvlGTree.this.size();
		}

		public void clear() {
			AvlGTree.this.clear();
		}
	}

	private class SubMap<K, V> extends AbstractMap<K,V> implements SortedMap<K,V> {

		private boolean fromStart = false, toEnd = false;
		private K fromKey, toKey;
//		private Object readResolve() {
//			return new AscendingSubMap<>(AvlGTree.this,fromStart, fromKey, true,toEnd, toKey, false);
//		}
		public Set<Map.Entry<K,V>> entrySet() { throw new InternalError(); }
		public K lastKey() { throw new InternalError(); }
		public K firstKey() { throw new InternalError(); }
		public SortedMap<K,V> subMap(K fromKey, K toKey) { throw new InternalError(); }
		public SortedMap<K,V> headMap(K toKey) { throw new InternalError(); }
		public SortedMap<K,V> tailMap(K fromKey) { throw new InternalError(); }
		public Comparator<? super K> comparator() { throw new InternalError(); }
	}
//
//	static final class AscendingSubMap<K,V> extends NavigableSubMap<K,V> {
//		private static final long serialVersionUID = 912986545866124060L;
//
//		AscendingSubMap(AvlGTree<K,V> m,
//				boolean fromStart, K lo, boolean loInclusive,
//				boolean toEnd,     K hi, boolean hiInclusive) {
//			super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
//		}
//
//		public Comparator<? super K> comparator() {
//			return m.comparator();
//		}
//
//		public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
//				K toKey,   boolean toInclusive) {
//			if (!inRange(fromKey, fromInclusive))
//				throw new IllegalArgumentException("fromKey out of range");
//			if (!inRange(toKey, toInclusive))
//				throw new IllegalArgumentException("toKey out of range");
//			return new AscendingSubMap<>(m, false, fromKey, fromInclusive, false, toKey,   toInclusive);
//		}
//
//		public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
//			if (!inRange(toKey, inclusive))
//				throw new IllegalArgumentException("toKey out of range");
//			return new AscendingSubMap<>(m, fromStart, lo,    loInclusive, false,     toKey, inclusive);
//		}
//
//		public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
//			if (!inRange(fromKey, inclusive))
//				throw new IllegalArgumentException("fromKey out of range");
//			return new AscendingSubMap<>(m,
//					false, fromKey, inclusive,
//					toEnd, hi,      hiInclusive);
//		}
//
//		public NavigableMap<K,V> descendingMap() {
//			NavigableMap<K,V> mv = descendingMapView;
//			return (mv != null) ? mv :
//				(descendingMapView =
//				new DescendingSubMap<>(m,
//						fromStart, lo, loInclusive,
//						toEnd,     hi, hiInclusive));
//		}
//
//		Iterator<K> keyIterator() {
//			return new SubMapKeyIterator(absLowest(), absHighFence());
//		}
//
//		Spliterator<K> keySpliterator() {
//			return new SubMapKeyIterator(absLowest(), absHighFence());
//		}
//
//		Iterator<K> descendingKeyIterator() {
//			return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
//		}
//
//		final class AscendingEntrySetView extends EntrySetView {
//			public Iterator<Map.Entry<K,V>> iterator() {
//				return new SubMapEntryIterator(absLowest(), absHighFence());
//			}
//		}
//
//		public Set<Map.Entry<K,V>> entrySet() {
//			EntrySetView es = entrySetView;
//			return (es != null) ? es : (entrySetView = new AscendingEntrySetView());
//		}
//
//		AvlGTree.Entry<K,V> subLowest()       { return absLowest(); }
//		AvlGTree.Entry<K,V> subHighest()      { return absHighest(); }
//		AvlGTree.Entry<K,V> subCeiling(K key) { return absCeiling(key); }
//		AvlGTree.Entry<K,V> subHigher(K key)  { return absHigher(key); }
//		AvlGTree.Entry<K,V> subFloor(K key)   { return absFloor(key); }
//		AvlGTree.Entry<K,V> subLower(K key)   { return absLower(key); }
	}


//	abstract static class NavigableSubMap<K,V> extends AbstractMap<K,V>
//	implements NavigableMap<K,V>, java.io.Serializable {
//		private static final long serialVersionUID = -2102997345730753016L;
//		/**
//		 * The backing map.
//		 */
//		final AvlGTree<K,V> m;
//
//		/**
//		 * Endpoints are represented as triples (fromStart, lo,
//		 * loInclusive) and (toEnd, hi, hiInclusive). If fromStart is
//		 * true, then the low (absolute) bound is the start of the
//		 * backing map, and the other values are ignored. Otherwise,
//		 * if loInclusive is true, lo is the inclusive bound, else lo
//		 * is the exclusive bound. Similarly for the upper bound.
//		 */
//		final K lo, hi;
//		final boolean fromStart, toEnd;
//		final boolean loInclusive, hiInclusive;
//
//		NavigableSubMap(AvlGTree<K,V> m,
//				boolean fromStart, K lo, boolean loInclusive,
//				boolean toEnd,     K hi, boolean hiInclusive) {
//			if (!fromStart && !toEnd) {
//				if (m.compare(lo, hi) > 0)
//					throw new IllegalArgumentException("fromKey > toKey");
//			} else {
//				if (!fromStart) // type check
//					m.compare(lo, lo);
//				if (!toEnd)
//					m.compare(hi, hi);
//			}
//
//			this.m = m;
//			this.fromStart = fromStart;
//			this.lo = lo;
//			this.loInclusive = loInclusive;
//			this.toEnd = toEnd;
//			this.hi = hi;
//			this.hiInclusive = hiInclusive;
//		}
//
//		// internal utilities
//
//		final boolean tooLow(Object key) {
//			if (!fromStart) {
//				int c = m.compare(key, lo);
//				if (c < 0 || (c == 0 && !loInclusive))
//					return true;
//			}
//			return false;
//		}
//
//		final boolean tooHigh(Object key) {
//			if (!toEnd) {
//				int c = m.compare(key, hi);
//				if (c > 0 || (c == 0 && !hiInclusive))
//					return true;
//			}
//			return false;
//		}
//
//		final boolean inRange(Object key) {
//			return !tooLow(key) && !tooHigh(key);
//		}
//
//		final boolean inClosedRange(Object key) {
//			return (fromStart || m.compare(key, lo) >= 0)
//					&& (toEnd || m.compare(hi, key) >= 0);
//		}
//
//		final boolean inRange(Object key, boolean inclusive) {
//			return inclusive ? inRange(key) : inClosedRange(key);
//		}
//
//		/*
//		 * Absolute versions of relation operations.
//		 * Subclasses map to these using like-named "sub"
//		 * versions that invert senses for descending maps
//		 */
//
//		final AvlGTree.Entry<K,V> absLowest() {
//			AvlGTree.Entry<K,V> e =
//					(fromStart ?  m.getFirstEntry() :
//						(loInclusive ? m.getCeilingEntry(lo) :
//							m.getHigherEntry(lo)));
//			return (e == null || tooHigh(e.key)) ? null : e;
//		}
//
//		final AvlGTree.Entry<K,V> absHighest() {
//			AvlGTree.Entry<K,V> e =
//					(toEnd ?  m.getLastEntry() :
//						(hiInclusive ?  m.getFloorEntry(hi) :
//							m.getLowerEntry(hi)));
//			return (e == null || tooLow(e.key)) ? null : e;
//		}
//
//		final AvlGTree.Entry<K,V> absCeiling(K key) {
//			if (tooLow(key))
//				return absLowest();
//			AvlGTree.Entry<K,V> e = m.getCeilingEntry(key);
//			return (e == null || tooHigh(e.key)) ? null : e;
//		}
//
//		final AvlGTree.Entry<K,V> absHigher(K key) {
//			if (tooLow(key))
//				return absLowest();
//			AvlGTree.Entry<K,V> e = m.getHigherEntry(key);
//			return (e == null || tooHigh(e.key)) ? null : e;
//		}
//
//		final AvlGTree.Entry<K,V> absFloor(K key) {
//			if (tooHigh(key))
//				return absHighest();
//			AvlGTree.Entry<K,V> e = m.getFloorEntry(key);
//			return (e == null || tooLow(e.key)) ? null : e;
//		}
//
//		final AvlGTree.Entry<K,V> absLower(K key) {
//			if (tooHigh(key))
//				return absHighest();
//			AvlGTree.Entry<K,V> e = m.getLowerEntry(key);
//			return (e == null || tooLow(e.key)) ? null : e;
//		}
//
//		/** Returns the absolute high fence for ascending traversal */
//		final AvlGTree.Entry<K,V> absHighFence() {
//			return (toEnd ? null : (hiInclusive ?
//					m.getHigherEntry(hi) :
//						m.getCeilingEntry(hi)));
//		}
//
//		/** Return the absolute low fence for descending traversal  */
//		final AvlGTree.Entry<K,V> absLowFence() {
//			return (fromStart ? null : (loInclusive ?
//					m.getLowerEntry(lo) :
//						m.getFloorEntry(lo)));
//		}
//
//		// Abstract methods defined in ascending vs descending classes
//		// These relay to the appropriate absolute versions
//
//		abstract AvlGTree.Entry<K,V> subLowest();
//		abstract AvlGTree.Entry<K,V> subHighest();
//		abstract AvlGTree.Entry<K,V> subCeiling(K key);
//		abstract AvlGTree.Entry<K,V> subHigher(K key);
//		abstract AvlGTree.Entry<K,V> subFloor(K key);
//		abstract AvlGTree.Entry<K,V> subLower(K key);
//
//		/** Returns ascending iterator from the perspective of this submap */
//		abstract Iterator<K> keyIterator();
//
//		abstract Spliterator<K> keySpliterator();
//
//		/** Returns descending iterator from the perspective of this submap */
//		abstract Iterator<K> descendingKeyIterator();
//
//		// public methods
//
//		public boolean isEmpty() {
//			return (fromStart && toEnd) ? m.isEmpty() : entrySet().isEmpty();
//		}
//
//		public int size() {
//			return (fromStart && toEnd) ? m.size() : entrySet().size();
//		}
//
//		public final boolean containsKey(Object key) {
//			return inRange(key) && m.containsKey(key);
//		}
//
//		public final V put(K key, V value) {
//			if (!inRange(key))
//				throw new IllegalArgumentException("key out of range");
//			return m.put(key, value);
//		}
//
//		public final V get(Object key) {
//			return !inRange(key) ? null :  m.get(key);
//		}
//
//		public final V remove(Object key) {
//			return !inRange(key) ? null : m.remove(key);
//		}
//
//		public final Map.Entry<K,V> ceilingEntry(K key) {
//			return exportEntry(subCeiling(key));
//		}
//
//		public final K ceilingKey(K key) {
//			return keyOrNull(subCeiling(key));
//		}
//
//		public final Map.Entry<K,V> higherEntry(K key) {
//			return exportEntry(subHigher(key));
//		}
//
//		public final K higherKey(K key) {
//			return keyOrNull(subHigher(key));
//		}
//
//		public final Map.Entry<K,V> floorEntry(K key) {
//			return exportEntry(subFloor(key));
//		}
//
//		public final K floorKey(K key) {
//			return keyOrNull(subFloor(key));
//		}
//
//		public final Map.Entry<K,V> lowerEntry(K key) {
//			return exportEntry(subLower(key));
//		}
//
//		public final K lowerKey(K key) {
//			return keyOrNull(subLower(key));
//		}
//
//		public final K firstKey() {
//			return key(subLowest());
//		}
//
//		public final K lastKey() {
//			return key(subHighest());
//		}
//
//		public final Map.Entry<K,V> firstEntry() {
//			return exportEntry(subLowest());
//		}
//
//		public final Map.Entry<K,V> lastEntry() {
//			return exportEntry(subHighest());
//		}
//
//		public final Map.Entry<K,V> pollFirstEntry() {
//			AvlGTree.Entry<K,V> e = subLowest();
//			Map.Entry<K,V> result = exportEntry(e);
//			if (e != null)
//				m.deleteEntry(e);
//			return result;
//		}
//
//		public final Map.Entry<K,V> pollLastEntry() {
//			AvlGTree.Entry<K,V> e = subHighest();
//			Map.Entry<K,V> result = exportEntry(e);
//			if (e != null)
//				m.deleteEntry(e);
//			return result;
//		}
//
//		// Views
//		transient NavigableMap<K,V> descendingMapView;
//		transient EntrySetView entrySetView;
//		transient KeySet<K> navigableKeySetView;
//
//		public final NavigableSet<K> navigableKeySet() {
//			KeySet<K> nksv = navigableKeySetView;
//			return (nksv != null) ? nksv :
//				(navigableKeySetView = new AvlGTree.KeySet<>(this));
//		}
//
//		public final Set<K> keySet() {
//			return navigableKeySet();
//		}
//
//		public NavigableSet<K> descendingKeySet() {
//			return descendingMap().navigableKeySet();
//		}
//
//		public final SortedMap<K,V> subMap(K fromKey, K toKey) {
//			return subMap(fromKey, true, toKey, false);
//		}
//
//		public final SortedMap<K,V> headMap(K toKey) {
//			return headMap(toKey, false);
//		}
//
//		public final SortedMap<K,V> tailMap(K fromKey) {
//			return tailMap(fromKey, true);
//		}
//
//		// View classes
//
//		abstract class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
//			private transient int size = -1, sizeModCount;
//
//			public int size() {
//				if (fromStart && toEnd)
//					return m.size();
//				if (size == -1 || sizeModCount != m.modCount) {
//					sizeModCount = m.modCount;
//					size = 0;
//					Iterator<?> i = iterator();
//					while (i.hasNext()) {
//						size++;
//						i.next();
//					}
//				}
//				return size;
//			}
//
//			public boolean isEmpty() {
//				AvlGTree.Entry<K,V> n = absLowest();
//				return n == null || tooHigh(n.key);
//			}
//
//			public boolean contains(Object o) {
//				if (!(o instanceof Map.Entry))
//					return false;
//				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
//				Object key = entry.getKey();
//				if (!inRange(key))
//					return false;
//				AvlGTree.Entry<?,?> node = m.getEntry(key);
//				return node != null &&
//						valEquals(node.getValue(), entry.getValue());
//			}
//
//			public boolean remove(Object o) {
//				if (!(o instanceof Map.Entry))
//					return false;
//				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
//				Object key = entry.getKey();
//				if (!inRange(key))
//					return false;
//				AvlGTree.Entry<K,V> node = m.getEntry(key);
//				if (node!=null && valEquals(node.getValue(),
//						entry.getValue())) {
//					m.deleteEntry(node);
//					return true;
//				}
//				return false;
//			}
//		}
//
//		/**
//		 * Iterators for SubMaps
//		 */
//		abstract class SubMapIterator<T> implements Iterator<T> {
//			AvlGTree.Entry<K,V> lastReturned;
//			AvlGTree.Entry<K,V> next;
//			final Object fenceKey;
//			int expectedModCount;
//
//			SubMapIterator(AvlGTree.Entry<K,V> first,
//					AvlGTree.Entry<K,V> fence) {
//				expectedModCount = m.modCount;
//				lastReturned = null;
//				next = first;
//				// fenceKey = fence == null ? UNBOUNDED : fence.key;
//			}
//
//			public final boolean hasNext() {
//				return next != null && next.key != fenceKey;
//			}
//
//			final AvlGTree.Entry<K,V> nextEntry() {
//				//            AvlGTree.Entry<K,V> e = next;
//				//            if (e == null || e.key == fenceKey)
//				//                throw new NoSuchElementException();
//				//            if (m.modCount != expectedModCount)
//				//                throw new ConcurrentModificationException();
//				//            next = successor(e);
//				//            lastReturned = e;
//				//            return e;
//			}
//
//			final AvlGTree.Entry<K,V> prevEntry() {
//				//            AvlGTree.Entry<K,V> e = next;
//				//            if (e == null || e.key == fenceKey)
//				//                throw new NoSuchElementException();
//				//            if (m.modCount != expectedModCount)
//				//                throw new ConcurrentModificationException();
//				//            next = predecessor(e);
//				//            lastReturned = e;
//				//            return e;
//			}
//
//			final void removeAscending() {
//				//            if (lastReturned == null)
//				//                throw new IllegalStateException();
//				//            if (m.modCount != expectedModCount)
//				//                throw new ConcurrentModificationException();
//				//            // deleted entries are replaced by their successors
//				//            if (lastReturned.left != null && lastReturned.right != null)
//				//                next = lastReturned;
//				//            m.deleteEntry(lastReturned);
//				//            lastReturned = null;
//				//            expectedModCount = m.modCount;
//			}
//
//			final void removeDescending() {
//				//            if (lastReturned == null)
//				//                throw new IllegalStateException();
//				//            if (m.modCount != expectedModCount)
//				//                throw new ConcurrentModificationException();
//				//            m.deleteEntry(lastReturned);
//				//            lastReturned = null;
//				//            expectedModCount = m.modCount;
//			}
//
//		}
//
//		final class SubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
//			SubMapEntryIterator(AvlGTree.Entry<K,V> first,
//					AvlGTree.Entry<K,V> fence) {
//				super(first, fence);
//			}
//			public Map.Entry<K,V> next() {
//				return nextEntry();
//			}
//			public void remove() {
//				removeAscending();
//			}
//		}
//
//		final class DescendingSubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
//			DescendingSubMapEntryIterator(AvlGTree.Entry<K,V> last,
//					AvlGTree.Entry<K,V> fence) {
//				super(last, fence);
//			}
//
//			public Map.Entry<K,V> next() {
//				return prevEntry();
//			}
//			public void remove() {
//				removeDescending();
//			}
//		}
//
//		// Implement minimal Spliterator as KeySpliterator backup
//		final class SubMapKeyIterator extends SubMapIterator<K>
//		implements Spliterator<K> {
//			SubMapKeyIterator(AvlGTree.Entry<K,V> first,
//					AvlGTree.Entry<K,V> fence) {
//				super(first, fence);
//			}
//			public K next() {
//				return nextEntry().key;
//			}
//			public void remove() {
//				removeAscending();
//			}
//			public Spliterator<K> trySplit() {
//				return null;
//			}
//			public void forEachRemaining(Consumer<? super K> action) {
//				while (hasNext())
//					action.accept(next());
//			}
//			public boolean tryAdvance(Consumer<? super K> action) {
//				if (hasNext()) {
//					action.accept(next());
//					return true;
//				}
//				return false;
//			}
//			public long estimateSize() {
//				return Long.MAX_VALUE;
//			}
//			public int characteristics() {
//				return Spliterator.DISTINCT | Spliterator.ORDERED |
//						Spliterator.SORTED;
//			}
//			public final Comparator<? super K>  getComparator() {
//				return NavigableSubMap.this.comparator();
//			}
//		}
//
//		final class DescendingSubMapKeyIterator extends SubMapIterator<K>
//		implements Spliterator<K> {
//			DescendingSubMapKeyIterator(AvlGTree.Entry<K,V> last,
//					AvlGTree.Entry<K,V> fence) {
//				super(last, fence);
//			}
//			public K next() {
//				return prevEntry().key;
//			}
//			public void remove() {
//				removeDescending();
//			}
//			public Spliterator<K> trySplit() {
//				return null;
//			}
//			public void forEachRemaining(Consumer<? super K> action) {
//				while (hasNext())
//					action.accept(next());
//			}
//			public boolean tryAdvance(Consumer<? super K> action) {
//				if (hasNext()) {
//					action.accept(next());
//					return true;
//				}
//				return false;
//			}
//			public long estimateSize() {
//				return Long.MAX_VALUE;
//			}
//			public int characteristics() {
//				return Spliterator.DISTINCT | Spliterator.ORDERED;
//			}
//		}
//
//	}

