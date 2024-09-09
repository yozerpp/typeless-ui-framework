package me.yusuf.tauf;

import me.yusuf.utils.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class AppTest{
    private MetadataStore store;
    @Before
    public void setUp(){
        Set<Class<?>> cls = new HashSet<Class<?>>();
        cls.add(Entity2.class);cls.add(Entity1.class);
        this.store = new MetadataStore(cls);
    }
    @Test
    public void testMerge(){
        UiMetadata old = Entity2.class.getAnnotation(UiMetadata.class);
        UiMetadata expected = new UiMetadataHolder(old.getTag(), StringUtils.concatEachString(new String[]{"extra-class"},old.getCssClassNames()),new int[]{1},old.getContainerOrder(),old.getOrder());
        Assert.assertEquals(expected,store.getMetadata(Entity1.class.getSimpleName(),Entity2.class.getSimpleName().toLowerCase()));
    }
}
