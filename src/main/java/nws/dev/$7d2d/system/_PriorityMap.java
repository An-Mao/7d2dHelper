package nws.dev.$7d2d.system;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class _PriorityMap<K,V> {
    private final Map<K, PriorityQueue<V>> map = new HashMap<>();
    private final Comparator<V> comparator;

    public _PriorityMap(Comparator<V> comparator) {
        this.comparator = comparator;
    }
    public void put(K key,V value){
        if (!map.containsKey(key)) map.put(key,new PriorityQueue<>(comparator));
        map.get(key).add(value);
    }
    public PriorityQueue<V> get(K key){
        return map.get(key);
    }
}
