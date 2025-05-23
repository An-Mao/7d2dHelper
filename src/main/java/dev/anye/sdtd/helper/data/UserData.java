package dev.anye.sdtd.helper.data;

import java.util.HashMap;

public class UserData {
    public String userid = "";
    public String platformid = "";
    public HashMap<String, Boolean> reward = new HashMap<>();
    public int recordItemLimit = 0;
    public boolean canExtractSaveItem = false;
    public long raffleTime = 0;
}
