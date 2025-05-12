package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.core.json._JsonConfig;

import java.util.HashMap;

public class LanguageConfig extends _JsonConfig<HashMap<String,String>> {
    public LanguageConfig(String filePath) {
        super(filePath, """
                {
                    "usual.command.error.bind":"您已绑定账号",
                    "usual.command.error.not_bind":"请先绑定账号",
                    "usual.command.error.target_not_bind":"对方未绑定账号",
                    "usual.command.error.not_online":"未找到玩家，请确认玩家是否在线。",
                    "usual.command.error.target_not_online":"未找到玩家，请确认玩家是否在线。",
                    "usual.command.error.point_insufficient":"积分不足",
                    "agree_save.command.error.args_number":"指令格式错误，正确格式：同意跟档 qq",
                    "agree_save.command.success":"已同意跟档申请",
                    "agree_unban.command.error.args_number":"指令格式错误，正确格式：同意解封 qq",
                    "agree_unban.command.error.not_found":"未找到此封禁玩家",
                    "agree_unban.command.error.agree":"您已同意此玩家的解封申请",
                    "agree_unban.command.error.agreed":"您已同意过此玩家的解封申请",
                    "agree_unban.command.error.net_error":"解封失败，网络异常",
                    "agree_unban.command.success":"解封成功",
                    "agree_unban.command.error.self":"您不能同意自己的解封申请",
                    "apply_white.command.error.args_number":"指令格式错误，正确格式：申请白名单 白名单名称",
                    "apply_white.command.error.not_met":"白名单添加失败，你未满足白名单要求：\\n需要等级：%s\\n需要积分：%s\\n%s",
                    "apply_white.command.error.net":"白名单添加失败，网络异常。",
                    "apply_white.command.success":"白名单添加成功",
                    "apply_white.command.error.not_found":"未找到此白名单，请确认此白名单是否存在",
                    "bind_server.command.error.not_found":"指定服务器名称不存在，请重试。",
                    "bind_server.command.success":"绑定成功",
                    "buy_save_item_num.command.error.args_number":"指令格式错误，正确格式：购买跟档物品数量 数量",
                    "buy_save_item_num.command.error.limit":"您的跟档物品数量已达到上限",
                    "buy_save_item_num.command.error.data":"数据错误，请重新尝试",
                    "buy_save_item_num.command.success":"已增加%s个跟档物品数量，当前数量%s",
                    "buy_save_item_num.command.error.net":"添加失败，请重新尝试",
                    "check_save_item.command.error.args_number":"指令格式错误，正确格式：查看跟档物品 qq",
                    "check_save_item.command.error.target_not_have_item":"对方没有跟档物品",
                    "check_save_item.command.target_items":"对方跟档物品:%s",
                    "find_item.command.error.args_number":"指令格式错误，正确格式：物品信息 物品名称",
                    "find_item.command.error.empty":"物品名称不能为空",
                    "find_white.command.error.too_more":"结果数量过多，请提供更多字词并重新查找",
                    "find_white.command.error.not_found":"未找到包含此物品的白名单，请检查是否有错误。",
                    "find_white.command.success":"包含类似物品的白名单:%s",
                    "receive_gift.command.error.args_number":"指令格式错误，正确格式：领取礼包 礼包名称",
                    "receive_gift.command.error.has":"您已领取过【%s】礼包",
                    "request_info.command.error.args_number":"指令格式错误，正确格式：查信息 玩家名称",
                    "request_white.command.error.empty":"指令格式错误，正确格式：查询白名单 白名单名称",
                    "request_white.command.error.not_found":"未找到此白名单，请确认此白名单是否存在",
                    "bind.command.error.not_found":"未找到玩家，请确认您的昵称（%s）与游戏昵称是否一致，注意大小写，并且是否在线",
                    "bind.command.success.step.1":"绑定已就绪，请在游戏内发送【绑定账号】来完成绑定。",
                    "bind.command.success.step.done":"绑定成功",
                    "clear_server.command.start":"即将清理服务器",
                    "close_net.command.close":"即将关闭网关",
                    "close_net.command.warn":"您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令",
                    "close_net.command.error.is_restart":"重启进程运行中，请勿执行此指令",
                    "event_list.command.tip":"-----活动列表-----\\n%s",
                    "get_new_player_gift.command.error.has":"您已领取过新手礼包",
                    "kick_self.command.success":"强制下线成功",
                    "kick_self.command.error.net":"强制下线失败，请稍后再试",
                    "kill_self.command.success":"自杀成功",
                    "kill_self.command.error.net":"自杀失败，请稍后再试",
                    "raffle.command.error.cooldown":"冷却中",
                    "raffle.command.error.net":"抽奖失败",
                    "raffle.command.success":"花费%s积分，抽奖结果：\\n%s",
                    "reload_config.command.start":"即将重新加载配置",
                    "reload_game_file.command.start":"开始重新读取游戏文件",
                    "reload_game_file.command.done":"重新读取游戏文件完成",
                    "request_save_item.command.error.has":"您已申请过跟档，如果想要修改，请先提取物品。",
                    "request_save_item.command.error.server":"服务器状态异常，请稍后再试",
                    "request_save_item.command.error.net":"获取服务器状态异常，请稍后再试",
                    "request_save_item.command.step.1":"请确认是否已将物品上的模组卸下，您可以跟档%s个物品，如果确认无误请再次发送此指令。",
                    "request_save_item.command.error.online":"请离线后再试",
                    "request_save_item.command.error.bag":"获取背包物品失败，请重试",
                    "request_save_item.command.error.bag.more":"背包物品过多，请清理后再试",
                    "request_save_item.command.error.bag.empty":"背包物品为空，请检查",
                    "request_save_item.command.success":"物品记录完成，成功:%s ，失败:%s。",
                    "request_unban.command.error.has":"您已申请过解封，当前已有%s个群员为您同意解封",
                    "request_unban.command.error.not_found":"未查询到您的封禁记录",
                    "restart_server.command.error.running":"重启进程运行中，请勿执行此指令",
                    "restart_server.command.start":"即将完全重启服务器",
                    "restart_server.command.step.1":"您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令",
                    "run_kit.command.error.running":"重启进程运行中，请勿执行此指令",
                    "run_kit.command.start":"即将运行kit",
                    "run_kit.command.step.1":"您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令",
                    "send_save_item.command.error.need_check":"无法提取物品，请等待服主核验。",
                    "send_save_item.command.error.not_item":"没有需要提取的物品",
                    "send_save_item.command.success":"成功提取%s个物品",
                    "sign_help.command.tip":"签到时您可以@一位玩家来绑定。如果对方当日签到并且您在线，则可以额外获得一次签到奖励。若其当日未签到，您将无法获取下次签到奖励",
                    "start_server.command.error.running":"重启进程运行中，请勿执行此指令",
                    "start_server.command.start":"即将启动服务器",
                    "start_server.command.step.1":"您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令"
                }
                """, new TypeToken<>(){});
    }
    public String getLang(String key){
        if (getDatas().containsKey(key)) return getDatas().get(key);
        return key;
    }
}
