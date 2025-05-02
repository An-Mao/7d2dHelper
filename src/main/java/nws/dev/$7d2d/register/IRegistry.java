package nws.dev.$7d2d.register;

import java.util.Optional;

public interface IRegistry<T extends IRegistrable> {
    void register(T object);
    Optional<T> getObject(String name);
}
