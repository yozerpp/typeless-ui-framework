package me.yusuf.tauf;

@UiMetadata(getCssClassNames = "base-class")
public class Entity1 {
    public Entity1(){

    }
    @UiMetadata(getCssClassNames = "extra-class", getContainerId = 1)
     Entity2 entity2;
}
