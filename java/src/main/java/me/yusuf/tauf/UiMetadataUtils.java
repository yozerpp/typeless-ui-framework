package me.yusuf.tauf;

import me.yusuf.utils.ArrayUtils;
import me.yusuf.utils.StringUtils;

import java.util.Arrays;
import java.util.Objects;

public class UiMetadataUtils {
    public static UiMetadata merge(UiMetadata fieldMetadata, UiMetadata classMetadata) {
            if (Objects.deepEquals(fieldMetadata,classMetadata)) return null;
            return new UiMetadataHolder(fieldMetadata.getTag()!=null? fieldMetadata.getTag() : classMetadata.getTag(), StringUtils.concatEachString(fieldMetadata.getCssClassNames(),classMetadata.getCssClassNames()), ArrayUtils.coalesce(fieldMetadata.getContainerId(),classMetadata.getContainerId()), ArrayUtils.coalesce(fieldMetadata.getContainerOrder(), classMetadata.getContainerOrder()), ArrayUtils.coalesce(fieldMetadata.getOrder(), classMetadata.getOrder()));
    }
    public static UiMetadata getContainerMetadata(UiMetadata metadata){
        if (metadata.getTag().length<2) throw new RuntimeException("metadata doesnt have a container.");
        return new UiMetadataHolder(Arrays.copyOfRange(metadata.getTag(),1,metadata.getTag().length),metadata.getCssClassNames().length>0?Arrays.copyOfRange(metadata.getCssClassNames(),1,metadata.getCssClassNames().length):null,metadata.getContainerId().length>0?Arrays.copyOfRange(metadata.getContainerId(),1,metadata.getContainerId().length):null,metadata.getContainerOrder().length>0?Arrays.copyOfRange(metadata.getContainerOrder(), 1, metadata.getContainerOrder().length):null,metadata.getOrder().length>0?Arrays.copyOfRange(metadata.getOrder(),1,metadata.getOrder().length):null);
    }

}

