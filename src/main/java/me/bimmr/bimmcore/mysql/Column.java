package me.bimmr.bimmcore.mysql;


/**
 * A Column for MySQLManager
 */
public class Column {
    private Integer  length;
    private DataType type;
    private String   name;

    public Column(String name, DataType type, Integer length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public Column(String name, DataType type, int length) {
        this.name = name;
        this.type = type;
        this.length = Integer.valueOf(length);
    }

    /**
     * @return the type
     */
    public DataType getDataType() {
        return type;
    }

    /**
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
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
