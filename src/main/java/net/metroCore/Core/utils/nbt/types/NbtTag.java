package net.metroCore.Core.utils.nbt.types;

public class NbtTag {

    private final String key;
    private final Object value;
    private final NbtType type;

    public NbtTag(String key, Object value, NbtType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public NbtType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> clazz) {
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("NBT tag value is not of type " + clazz.getSimpleName());
    }

    @Override
    public String toString() {
        return "NbtTag{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
