/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.particle;

import biomesoplenty.api.sound.BOPSounds;
import biomesoplenty.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class DripParticleBOP extends SingleQuadParticle
{
    private final Fluid type;
    protected boolean isGlowing;

    DripParticleBOP(ClientLevel p_106051_, double p_106052_, double p_106053_, double p_106054_, Fluid p_106055_, TextureAtlasSprite sprite)
    {
        super(p_106051_, p_106052_, p_106053_, p_106054_, sprite);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.type = p_106055_;
    }

    protected Fluid getType() {
        return this.type;
    }

    @Override
    public SingleQuadParticle.Layer getLayer()
    {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public int getLightColor(float p_106065_) {
        return this.isGlowing ? 240 : super.getLightColor(p_106065_);
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed)
        {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed)
            {
                this.xd *= (double)0.98F;
                this.yd *= (double)0.98F;
                this.zd *= (double)0.98F;
                BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
                FluidState fluidstate = this.level.getFluidState(blockpos);
                if (fluidstate.getType() == this.type && this.y < (double)((float)blockpos.getY() + fluidstate.getHeight(this.level, blockpos)))
                {
                    this.remove();
                }
            }
        }
    }

    protected void preMoveUpdate()
    {
        if (this.lifetime-- <= 0)
        {
            this.remove();
        }
    }

    protected void postMoveUpdate() {}

    static class DripHangParticle extends DripParticleBOP {
        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel p_106085_, double p_106086_, double p_106087_, double p_106088_, Fluid p_106089_, ParticleOptions p_106090_, TextureAtlasSprite sprite) {
            super(p_106085_, p_106086_, p_106087_, p_106088_, p_106089_, sprite);
            this.fallingParticle = p_106090_;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }

        }

        protected void postMoveUpdate() {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }
    }

    static class DripLandParticle extends DripParticleBOP
    {
        DripLandParticle(ClientLevel p_106102_, double p_106103_, double p_106104_, double p_106105_, Fluid p_106106_, TextureAtlasSprite sprite)
        {
            super(p_106102_, p_106103_, p_106104_, p_106105_, p_106106_, sprite);
            this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        }
    }

    static class FallAndLandParticle extends DripParticleBOP.FallingParticle
    {
        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel p_106116_, double p_106117_, double p_106118_, double p_106119_, Fluid p_106120_, ParticleOptions p_106121_, TextureAtlasSprite sprite)
        {
            super(p_106116_, p_106117_, p_106118_, p_106119_, p_106120_, sprite);
            this.landParticle = p_106121_;
        }

        protected void postMoveUpdate()
        {
            if (this.onGround)
            {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    static class FallingParticle extends DripParticleBOP
    {
        FallingParticle(ClientLevel p_106132_, double p_106133_, double p_106134_, double p_106135_, Fluid p_106136_, TextureAtlasSprite sprite)
        {
            this(p_106132_, p_106133_, p_106134_, p_106135_, p_106136_, (int)(64.0D / (Math.random() * 0.8D + 0.2D)), sprite);
        }

        FallingParticle(ClientLevel p_172022_, double p_172023_, double p_172024_, double p_172025_, Fluid p_172026_, int p_172027_, TextureAtlasSprite sprite)
        {
            super(p_172022_, p_172023_, p_172024_, p_172025_, p_172026_, sprite);
            this.lifetime = p_172027_;
        }

        protected void postMoveUpdate()
        {
            if (this.onGround)
            {
                this.remove();
            }

        }
    }

    static class BloodFallAndLandParticle extends DripParticleBOP.FallAndLandParticle
    {
        BloodFallAndLandParticle(ClientLevel p_106146_, double p_106147_, double p_106148_, double p_106149_, Fluid p_106150_, ParticleOptions p_106151_, TextureAtlasSprite sprite) {
            super(p_106146_, p_106147_, p_106148_, p_106149_, p_106150_, p_106151_, sprite);
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                float f = Mth.randomBetween(this.random, 0.3F, 1.0F);
                this.level.playLocalSound(this.x, this.y, this.z, BOPSounds.FLESH_TENDON_DRIP, SoundSource.BLOCKS, f, 1.0F, false);
            }
        }
    }

    public static class BloodFallProvider implements ParticleProvider<SimpleParticleType>
    {
        protected final SpriteSet sprite;

        public BloodFallProvider(SpriteSet p_106184_)
        {
            this.sprite = p_106184_;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double xo, double yo, double zo, double xd, double yd, double zd, RandomSource random)
        {
            DripParticleBOP.DripHangParticle dripparticle = new DripParticleBOP.DripHangParticle(level, xo, yo, zo, Fluids.EMPTY, ModParticles.LANDING_BLOOD, this.sprite.get(random));
            dripparticle.gravity = 0.01F;
            dripparticle.setColor(0.443F, 0.141F, 0.149F);
            return dripparticle;
        }
    }

    public static class BloodHangProvider implements ParticleProvider<SimpleParticleType>
    {
        protected final SpriteSet sprite;

        public BloodHangProvider(SpriteSet p_106184_)
        {
            this.sprite = p_106184_;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double xo, double yo, double zo, double xd, double yd, double zd, RandomSource random)
        {
            DripParticleBOP.DripHangParticle dripparticle$driphangparticle = new DripParticleBOP.DripHangParticle(level, xo, yo, zo, Fluids.EMPTY, ModParticles.FALLING_BLOOD, this.sprite.get(random));
            dripparticle$driphangparticle.gravity *= 0.01F;
            dripparticle$driphangparticle.lifetime = 100;
            dripparticle$driphangparticle.setColor(0.443F, 0.141F, 0.149F);
            return dripparticle$driphangparticle;
        }
    }

    public static class BloodLandProvider implements ParticleProvider<SimpleParticleType>
    {
        protected final SpriteSet sprite;

        public BloodLandProvider(SpriteSet p_106205_) {
            this.sprite = p_106205_;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double xo, double yo, double zo, double xd, double yd, double zd, RandomSource random)
        {
            DripParticleBOP dripparticle = new DripParticleBOP.DripLandParticle(level, xo, yo, zo, Fluids.EMPTY, this.sprite.get(random));
            dripparticle.lifetime = (int)(128.0D / (Math.random() * 0.8D + 0.2D));
            dripparticle.setColor(0.443F, 0.141F, 0.149F);
            return dripparticle;
        }
    }
}