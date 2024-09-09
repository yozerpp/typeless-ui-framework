package me.yusuf.tauf;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

class UiMetadataHolder implements java.io.Serializable ,UiMetadata{
    private final String[] tag;
    private final String[] cssClassNames;
    private final int[] containerId;
    private final int[] containerOrder;
    private final int[] order;
    public UiMetadataHolder(String[] tag, String[] cssClassNames, int[] containerId, int[] containerOrder, int[] order) {
        this.tag= tag!=null && tag.length!=0?tag: new String[]{"div"};
        this.order = order!=null && order.length!=0?order:new int[]{0};
        this.containerId=containerId!=null&& containerId.length!=0?containerId:new int[]{0};
        this.containerOrder=containerOrder!=null && containerOrder.length!=0?containerOrder:new int[]{0};
        this.cssClassNames=cssClassNames!=null && cssClassNames.length!=0?cssClassNames:new String[0];
    }

    @Override
    public String[] getTag() {
        return tag;
    }

    @Override
    public String[] getCssClassNames() {
        return cssClassNames;
    }

    @Override
    public int[] getContainerId() {
        return containerId;
    }

    @Override
    public int[] getContainerOrder() {
        return containerOrder;
    }

    @Override
    public int[] getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UiMetadataHolder that = (UiMetadataHolder) object;
        return Objects.deepEquals(tag, that.tag) && Objects.deepEquals(cssClassNames, that.cssClassNames) && Objects.deepEquals(containerId, that.containerId) && Objects.deepEquals(containerOrder, that.containerOrder) && Objects.deepEquals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(tag), Arrays.hashCode(cssClassNames), Arrays.hashCode(containerId), Arrays.hashCode(containerOrder), Arrays.hashCode(order));
    }

    @Override
    public String toString() {
        return "UiMetadataHolder{" +
                "tag=" + Arrays.toString(tag) +
                ", cssClassNames=" + Arrays.toString(cssClassNames) +
                ", containerId=" + Arrays.toString(containerId) +
                ", containerOrder=" + Arrays.toString(containerOrder) +
                ", order=" + Arrays.toString(order) +
                '}';
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return UiMetadata.class;
    }
}
