package nws.dev.$7d2d;

public class TestCode {

    public static void test() {

        String test = """
                {"self_id":1977970939,"user_id":751898988,"time":1742775258,"message_id":922417665,"message_seq":87,"message_type":"private","sender":{"user_id":751898988,"nickname":"猫","card":""},"raw_message":"1","font":14,"sub_type":"friend","message":[{"type":"text","data":{"text":"1"}}],"message_format":"array","post_type":"message"}
                """;

        //{"self_id":1977970939,"user_id":751898988,"time":1744287769,"message_id":1599724187,"message_seq":58117,"message_type":"group","sender":{"user_id":751898988,"nickname":"猫","card":"","role":"owner","title":""},"raw_message":"查询物品 &#91;神话&#93;小小的M60 4☆","font":14,"sub_type":"normal","message":[{"type":"text","data":{"text":"查询物品 [神话]小小的M60 4☆"}}],"message_format":"array","post_type":"message","group_id":164447436}
        //{"self_id":1977970939,"user_id":751898988,"time":1742775258,"message_id":922417665,"message_seq":87,"message_type":"private","sender":{"user_id":751898988,"nickname":"猫","card":""},"raw_message":"1","font":14,"sub_type":"friend","message":[{"type":"text","data":{"text":"1"}}],"message_format":"array","post_type":"message"}
        //{"self_id":1977970939,"user_id":751898988,"time":1741425113,"message_id":1407333403,"message_seq":58072,"message_type":"group","sender":{"user_id":751898988,"nickname":"猫","card":"","role":"owner","title":""},"raw_message":"签到[CQ:at,qq=1977970939,name=nekowq]","font":14,"sub_type":"normal","message":[{"type":"text","data":{"text":"签到"}},{"type":"at","data":{"qq":"1977970939","name":"nekowq"}}],"message_format":"array","post_type":"message","group_id":164447436}
        //{"self_id":1977970939,"user_id":751898988,"time":1742775258,"message_id":922417665,"message_seq":87,"message_type":"private","sender":{"user_id":751898988,"nickname":"猫","card":""},"raw_message":"帮助","font":14,"sub_type":"friend","message":[{"type":"text","data":{"text":"帮助"}}],"message_format":"array","post_type":"message"}


        //Gson gson = new Gson();
        //QQData.Message message = gson.fromJson(test, QQData.Message.class);
        //_Log.info(message.message_type);

        //"76561198405695513"
        /*
        PlayerInfoData playerInfoData = KitNet.getBagItems("76561198405695513");
        _Log.info(playerInfoData.entityid());
        _Log.info("数量："+ playerInfoData.bag().size());

         */

        /*
        StringBuilder sb = new StringBuilder();
        _File.getFiles(DataTable.UserDir, ".json").forEach(p->{
            _Log.debug(p.getFileName().toString());
            UserConfig config = new UserConfig(p.getFileName().toString());
        });


         */

        //GameInfo gameInfo = new GameInfo("F:/SteamLibrary/steamapps/common/7 Days To Die");
        //_Log.info(gameInfo.getItemInfo("celestialSpear"));
        /*
        _Log.info("已读取物品数量："+GameInfo.I.getItems().size());
        _Log.info("已读取配方数量："+GameInfo.I.getRecipes().size());
        _Log.info("已读取语言数量："+GameInfo.I.getLanguage().size());
        _Log.info("test："+GameInfo.I.findItem("神罚"));


        _Log.info(String.valueOf("ammoBundle44MagnumBulletBall".length()));

        Font font = new Font("Arial", Font.PLAIN, 12);
        _Image image = new _Image(256,512,1);
        image.drawRect(0,0,256,512, Color.WHITE);
        image.drawText("测试内容",DataTable.font, 0, 15, Color.BLACK);
        image.save(DataTable.Dir + "\\test.png");
         */

        //_Log.info(DrawConfig.I.wrapText(List.of("测试标题","测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试","测试000","[ff0000]测试","测[00ff00]试")).toString());
        //_Log.info(QQExCommand.convertPath("D:\\AM\\AMDev\\java\\7d2dHelper\\cache/image/9debe13955470fa4556dd2d1e6a0f3df.png"));

        //String file = QQExCommand.convertPath("D:\\\\AM\\AMDev\\java\\7d2dHelper\\cache\\image\\41355748fbd031c7427821757f9a6c6c.png");

        String data = """
                <effect_group>
                                <requirement name="CVarCompare" cvar="nwsVoidRift" operation="Equals" value="<i>" />
                                <triggered_effect trigger="onSelfBuffUpdate" action="RemovePrefabFromEntity" prefab="spear_steel_Prefab" parent_transform="Head"/>
                                <triggered_effect trigger="onSelfBuffUpdate" action="AttachPrefabToEntity" prefab="@:Other/Items/Weapons/Melee/Spear/spear_steel_Prefab.prefab" parent_transform="Head" local_offset="0,<y>,0" local_rotation="180,0,0" local_scale="9,9,9"/>
                            </effect_group>
                """;

        //_Log.debug(QQExCommand.convertPathToUri(DataTable.ImageCache+"/"+ _Byte.getMD5("testt")+".png"));
        //QQHelper.easySendGroupMsg("164447436", "测试");
        //QQHelper.sendMsg(Urls.qqSendGroupMsg,data);
        //_Log.debug(List.of("1 \\n 2".split("\\\\n")).toString());
        //"""
        //                将此线程标记为守护线程或非守护线程。当所有启动的非守护线程都终止时，关闭序列开始。
        //
        //                虚拟线程的守护状态始终为 true，且无法通过此方法将其更改为 false。
        //
        //                必须在线程启动之前调用此方法。未指定线程终止后此方法的行为。
        //                """
        //_Log.debug(String.valueOf(DrawConfig.I.findLineEndIndex("将此线程标记为守护线程或[ff0000]非守护线程。当所有启动的[ffff00]非守护线程都终止时，关闭序列开始。",256)));
        //DrawConfig.I.createImage(List.of("将此线程标记为守护线程或[ff0000]非守护线程。当所有启动的非守护线程都终止时，关闭序列开始。","虚拟线程[ffff00]的守护状态始终为 true，[ff00ff]且无法通过此方法将其更改为 false。")).save(DataTable.ImageCache+"/test.png");
        /*
        float a = 60;
        for (int i = 1;i < 99;i++){
            System.out.println(data.replace("<i>",String.valueOf(i)).replace("<y>",String.valueOf(a)));

            a-=0.5f;
        }

         */
        $7DTD._Log.debug(String.format("测试数：%s",1));
    }
}
