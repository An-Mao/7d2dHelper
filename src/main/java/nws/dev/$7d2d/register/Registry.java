package nws.dev.$7d2d.register;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<T extends IRegistrable> implements IRegistry<T>{
    private final Map<String,T> registeredObjects = new HashMap<>();
    @Override
    public void register(T object) {
        String name = object.getRegisterName();
        if (registeredObjects.containsKey(name)) throw new IllegalArgumentException("Object with name '" + name +"' already registered");
        registeredObjects.put(name,object);
    }

    @Override
    public Optional<T> getObject(String name) {
        return Optional.ofNullable(registeredObjects.get(name));
    }
}
