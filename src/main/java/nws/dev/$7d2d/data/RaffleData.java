package nws.dev.$7d2d.data;

import nws.dev.core.math._WeightRandom;

import java.util.HashMap;
import java.util.List;

public class RaffleData {
    public final boolean enable;
    public final int point;
    public final int cooldown;
    public final List<RaffleItem> items;
    private transient  final _WeightRandom<RewardData> random;
    public RaffleData(boolean enable, int point, int cooldown, List<RaffleItem> items) {
        this.enable = enable;
        this.point = point;
        this.cooldown = cooldown;
        this.items = items;
        HashMap<RewardData, Integer> map = new HashMap<>();
        items.forEach(i -> map.put(i.reward(), i.weight()));
        this.random = new _WeightRandom<>(map);
    }
    public record RaffleItem(RewardData reward,int weight) {}
    public _WeightRandom<RewardData> random() {return random;}
}
