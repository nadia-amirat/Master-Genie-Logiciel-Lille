package dsl.exemple;

public class Context {
    private HeroState heroState = new Crouching();

    public void test(){
        System.out.println("my state is "+this.heroState.getClass());
        this.heroState = this.heroState.release();
        System.out.println("my state should be Standing and actually is "+this.heroState.getClass());
        this.heroState = this.heroState.down();
        System.out.println("my state should be croushing and actually is "+this.heroState.getClass());
    }


}
