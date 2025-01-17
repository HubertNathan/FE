package events;

import java.util.HashMap;
import java.util.Map;

public enum ServiceLocator {
    INSTANCE;
    private Map<Class<?>,Class<?>> services = new HashMap<>();
    public <T> void registerService(Class<T> service, Class<? extends T> provider){
        services.put(service,provider);
    }
    public <T> T getService(Class<T> type){
        Class<?> provider = services.get(type);
        try {
            return type.cast(provider.getConstructor().newInstance());
        } catch (Exception e){
            throw new IllegalArgumentException("Service not available");
        }
    }
}
