package dsl.exemple;

public class Crouching implements HeroState {

    public HeroState up() {
        return this;
    }

    public HeroState down() {
        return this;
    }

    public HeroState release() {
        return new Standing();
    }

    public HeroState next() {
        return this;
    }
}
