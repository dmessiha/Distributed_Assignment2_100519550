import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CodingType {
    TYPE_BASE64_ENCODE(0),
    TYPE_BASE64_DECODE(1),
    TYPE_MD5_ENCODE(2),
    TYPE_MD5_DECODE(3),
    TYPE_SHA1_HASH(4);

    private static final Map<Integer, CodingType> TYPE_CODINGTYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(CodingType::getType, Function.identity()));

    private final int type;

    CodingType(final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static CodingType fromValue(final int value) {
        return TYPE_CODINGTYPE_MAP.get(value);
    }
}
