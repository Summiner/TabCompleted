package rs.jamie.tabcompleted.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComponentListSerializer implements ValueSerialiser<Component[]> {
    @Override
    public Class<Component[]> getTargetClass() {
        return Component[].class;
    }

    @Override
    public Component[] deserialise(FlexibleType flexibleType) {
        try {
            List<FlexibleType> list = flexibleType.getList();
            List<Component> components = new ArrayList<>();
            list.forEach((entry) -> {
                try {
                    components.add(MiniMessage.miniMessage().deserialize(entry.getString()));
                } catch (BadValueException ignored) {
                }
            });
            return components.toArray(new Component[0]);
        } catch (BadValueException e) {
            return new Component[0];
        }
    }

    @Override
    public Object serialise(Component[] value, Decomposer decomposer) {
        List<Object> objects = new ArrayList<>();
        for (Component component : value) {
            objects.add(MiniMessage.miniMessage().serialize(component));
        }
        return objects.toArray();
    }
}