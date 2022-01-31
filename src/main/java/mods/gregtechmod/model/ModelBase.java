package mods.gregtechmod.model;

import ic2.core.model.AbstractModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ModelBase extends AbstractModel {
    protected final Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();
    protected final ResourceLocation particle;
    protected final FaceBakery bakery = new FaceBakery();
    
    protected final boolean enableCache;
    private final Map<IBlockState, IBakedModel> cache = new ConcurrentHashMap<>();
    
    public ModelBase(ResourceLocation particle, List<Map<EnumFacing, ResourceLocation>> textures, boolean enableCache) {
        this(particle, textures.stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()), enableCache);
    }
    
    public ModelBase(ResourceLocation particle, Collection<ResourceLocation> textures, boolean enableCache) {
        this.particle = Objects.requireNonNull(particle);
        this.enableCache = enableCache;
        textures.forEach(loc -> this.sprites.put(loc, null));
        this.sprites.put(particle, null);
    }
    
    protected abstract IBakedModel generateModel(IBlockState rawState);
    
    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.sprites.keySet();
    }
    
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.sprites.entrySet()
                .forEach(entry -> entry.setValue(bakedTextureGetter.apply(entry.getKey())));
        return this;
    }
    
    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        IBakedModel model;
        if (this.enableCache) model = this.cache.computeIfAbsent(state, this::generateModel);
        else model = generateModel(state);
        
        return model.getQuads(state, side, rand);
    }

    @Override
    public void onReload() {}

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.sprites.get(this.particle);
    }
}
