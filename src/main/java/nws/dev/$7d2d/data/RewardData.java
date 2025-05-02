package nws.dev.$7d2d.data;

public class RewardData {
    /**
     * type. 奖励类型 0:增加积分 1:给予物品 2:扣除积分
     * count. 奖励数量
     * name. 物品名称
     * quality. 物品品质
     */
    public int type;
    public int count;
    public String name;
    public int quality;
    public RewardData(int type, int count, String name, int quality) {
        this.type = type;
        this.count = count;
        this.name = name;
        this.quality = quality;
    }
    public RewardData(int type, int count){
        this.type = type;
        this.count = count;
        this.name = "";
        this.quality = 0;
    }
}
