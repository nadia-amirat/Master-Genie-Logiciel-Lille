package dsl.exemple;

public class Standing implements HeroState {

    public HeroState up() {
        return  new Jumping();
    }

    public HeroState down() {
        return  new Crouching();
    }

    public HeroState release() {
        return this;
    }

    public HeroState next() {
        return this;
    }
}
