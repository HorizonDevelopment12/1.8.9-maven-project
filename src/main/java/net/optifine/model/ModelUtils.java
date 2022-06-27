package net.optifine.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils
{

    public static IBakedModel duplicateModel(IBakedModel model)
    {
        List list = duplicateQuadList(model.getGeneralQuads());
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        List list1 = new ArrayList();

        for (EnumFacing enumfacing : aenumfacing) {
            List list2 = model.getFaceQuads(enumfacing);
            List list3 = duplicateQuadList(list2);
            list1.add(list3);
        }

        return new SimpleBakedModel(list, list1, model.isAmbientOcclusion(), model.isGui3d(), model.getParticleTexture(), model.getItemCameraTransforms());
    }

    public static List duplicateQuadList(List lists)
    {
        List list = new ArrayList();

        for (Object e : lists)
        {
            BakedQuad bakedquad = (BakedQuad) e;
            BakedQuad bakedquad1 = duplicateQuad(bakedquad);
            list.add(bakedquad1);
        }

        return list;
    }

    public static BakedQuad duplicateQuad(BakedQuad quad)
    {
        return new BakedQuad(quad.getVertexData().clone(), quad.getTintIndex(), quad.getFace(), quad.getSprite());
    }
}
