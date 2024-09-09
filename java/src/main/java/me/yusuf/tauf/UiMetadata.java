package me.yusuf.tauf;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface UiMetadata {
    /** stupid property naming for jackson
     *
     */
    public String[] getTag() default "div";
    public String[] getCssClassNames() default {};
    public int[] getContainerId() default 0;
    public int[] getContainerOrder() default 0;
    public int[] getOrder() default 0;
}