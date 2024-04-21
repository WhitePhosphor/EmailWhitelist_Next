package org.pykefox.p4.emailwhitelist;

import org.bukkit.plugin.java.JavaPlugin;

public final class EmailWhitelist extends JavaPlugin {

    @Override
    public void onEnable() {
        // 启动↑
        System.out.println("EmailWhitelist" + this);
        // 轮询
        // 主逻辑有空再写
    }

    @Override
    public void onDisable() {
        System.out.println("See you next time.\n" +
                "Shutting down......");
    }
}
