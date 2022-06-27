package net.optifine.player;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;

public class CapeImageBuffer extends ImageBufferDownload
{
    private AbstractClientPlayer player;
    private final ResourceLocation resourceLocation;

    public CapeImageBuffer(AbstractClientPlayer player, ResourceLocation resourceLocation)
    {
        this.player = player;
        this.resourceLocation = resourceLocation;
    }

    public BufferedImage parseUserSkin(BufferedImage imageRaw)
    {
        return CapeUtils.parseCape(imageRaw);
    }

    public void skinAvailable()
    {
        if (this.player != null)
        {
            this.player.setLocationOfCape(this.resourceLocation);
        }

        this.cleanup();
    }

    public void cleanup()
    {
        this.player = null;
    }

}
