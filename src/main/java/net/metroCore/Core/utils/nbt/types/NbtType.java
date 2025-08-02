package net.metroCore.Core.utils.nbt.types;

public enum NbtType {
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    BYTE_ARRAY,
    INT_ARRAY,
    LONG_ARRAY,
    LIST,
    COMPOUND,
    END;

    public static NbtType fromId(int id) {
        return switch (id) {
            case 1 -> BYTE;
            case 2 -> SHORT;
            case 3 -> INT;
            case 4 -> LONG;
            case 5 -> FLOAT;
            case 6 -> DOUBLE;
            case 7 -> BYTE_ARRAY;
            case 8 -> STRING;
            case 9 -> LIST;
            case 10 -> COMPOUND;
            case 11 -> INT_ARRAY;
            case 12 -> LONG_ARRAY;
            default -> END;
        };
    }

    public int getId() {
        return switch (this) {
            case BYTE -> 1;
            case SHORT -> 2;
            case INT -> 3;
            case LONG -> 4;
            case FLOAT -> 5;
            case DOUBLE -> 6;
            case BYTE_ARRAY -> 7;
            case STRING -> 8;
            case LIST -> 9;
            case COMPOUND -> 10;
            case INT_ARRAY -> 11;
            case LONG_ARRAY -> 12;
            case END -> 0;
        };
    }
}
