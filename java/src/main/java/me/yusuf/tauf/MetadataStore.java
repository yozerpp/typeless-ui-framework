package me.yusuf.tauf;

import me.yusuf.utils.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MetadataStore {
    private final Set<Class<?>> entityClasses;
    public MetadataStore(String fullPackageName, Class<?>... extraClasses){
        this.entityClasses = getClasses(fullPackageName, extraClasses);
    }
    public MetadataStore(Set<Class<?>> classes){
        this.entityClasses = Collections.unmodifiableSet(classes);
    }
    private final Map<String, UiMetadata> cache = new ConcurrentHashMap<>();
    private final Set<String> accessed = Collections.synchronizedSet(new HashSet<>());
    public UiMetadata getMetadata(String className){
        return getMetadata(className, null);
    }
    public UiMetadata getMetadata(String className, String fieldName){
        UiMetadata cached;
        String key = className + (fieldName!=null?"-" + fieldName:"");
        if ((cached = cache.get(key))!=null) return cached;
        Optional opt= entityClasses.stream().filter(cls-> cls.getSimpleName().equals(className)).findAny();
        if (!opt.isPresent()) return null;
        Class<?> fromClass = (Class<?>) opt.get();
        opt= Arrays.stream(fromClass.getDeclaredFields()).filter(field -> field.getName().equals(fieldName)).findAny();
        if (!opt.isPresent()) return null;
        Field field = (Field) opt.get();
        Class<?> toClass = field.getType();
        if (!accessed.add(key)) return cache.get(toClass.getSimpleName());
        UiMetadata fieldMetadata = field.getAnnotation(UiMetadata.class);
        UiMetadata classMetadata;
        if (fieldMetadata==null){
            classMetadata= field.getType().getAnnotation(UiMetadata.class);
            cache.put(toClass.getSimpleName(),classMetadata);
            return classMetadata;
        }
        else if ((classMetadata=field.getType().getAnnotation(UiMetadata.class))!=null) {
            UiMetadata merged = UiMetadataUtils.merge(fieldMetadata,classMetadata);
            cache.put(key, merged);
            return merged;
        }
        else {
            cache.put(key, fieldMetadata);
            return fieldMetadata;
        }
    }

    /**
     *
     * @param pacakgeName
     * @param extraClasses
     * @return returns metadata for each individual class and field. Key format is '${className}' or '${className}-${fieldName}'
     */
    public static Map<String,UiMetadata> getAllMetadatas(String pacakgeName, Class<?>... extraClasses){
        final Set<Class<?>> classes =  getClasses(pacakgeName, extraClasses);
        Map<String, UiMetadata> metadatas= classes.parallelStream().map(cls-> Arrays.stream(cls.getDeclaredFields()).filter(f->f.isAnnotationPresent(UiMetadata.class)).collect(()->new HashMap<String, UiMetadata>(),(map,field)-> {
            UiMetadata metadata;
            if ((metadata=field.getAnnotation(UiMetadata.class))!=null){
                UiMetadata classMetadata;
                if ((classMetadata = field.getType().getAnnotation(UiMetadata.class))!=null) metadata = UiMetadataUtils.merge(metadata,classMetadata);
            } else metadata = field.getType().getAnnotation(UiMetadata.class);
            map.put(field.getDeclaringClass().getSimpleName() + "-" + field.getName() + ":"+ ReflectionUtils.typeToString(field.getGenericType()),metadata);
                }, Map::putAll))
                .reduce(new HashMap<String, UiMetadata>(),(acc, map)->{
                    acc.putAll(map);
                    return acc;
                });
        metadatas.putAll(classes.stream().filter(cls->cls.isAnnotationPresent(UiMetadata.class)).collect(Collectors.toMap(Class::getSimpleName, cls->cls.getAnnotation(UiMetadata.class))));
        return metadatas;
    }
    private static Set<Class<?>> getClasses(String packageName, Class<?>... extraClasses){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            File lPackage = new File(loader.getResource(packageName.replace('.', '/')).toURI());
            Set<Class<?>> packageClasses= Arrays.stream(lPackage.list()).map(classFile->packageName+'.'+classFile.replace(".class","")).map(clsName->{
                try {
                    return Class.forName(clsName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());
            Collections.addAll(packageClasses,extraClasses);
            return Collections.unmodifiableSet(packageClasses);
        } catch (URISyntaxException e){RuntimeException ex= new RuntimeException("package " + packageName + " can't be accessed", e); ex.addSuppressed(e);ex.setStackTrace(e.getStackTrace());
            throw ex;}
    }
}
