package me.bimmr.bimmcore.mysql;


/**
 * The type Column.
 */
public class Column {
    private Integer  length;
    private DataType type;
    private String   name;

    /**
     * Instantiates a new Column.
     *
     * @param name   the name
     * @param type   the type
     * @param length the length
     */
    public Column(String name, DataType type, Integer length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    /**
     * Instantiates a new Column.
     *
     * @param name   the name
     * @param type   the type
     * @param length the length
     */
    public Column(String name, DataType type, int length) {
        this.name = name;
        this.type = type;
        this.length = Integer.valueOf(length);
    }

    /**
     * Gets data type.
     *
     * @return the data type
     */
    public DataType getDataType() {
        return type;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (length == null) return name + " " + type.toString();
        return name + " " + type.toString() + "(" + length + ")";
    }

}
