package local.minecraftfrontend.demo;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = "frontend_demo", dist = Dist.CLIENT)
public final class DemoMod {
    public DemoMod(IEventBus bus) {
        var open = new KeyMapping("key.frontend_demo.open", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F8, "key.categories.frontend_demo");
        bus.addListener((RegisterKeyMappingsEvent event) -> event.register(open));
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            var client = Minecraft.getInstance();
            while (open.consumeClick()) {
                if (client.player != null && client.screen == null) client.setScreen(DemoScreen.create());
            }
        });
    }
}
