package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TableDetails {

    @JsonIgnore
    private Long id;
    
    private String tableName;

    public TableDetails(Long id, String tableName) {
        super();
        this.id = id;
        this.tableName = tableName;
    }

    public TableDetails() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * When transmitting ID to the client we need a data type that is supported
     * (long is not supported in JavaScript)
     *
     * @return String representation if {@link #id}
     */
    public String getIdString() {
        return id == null ? null : id.toString();
    }

    public void setIdString(String idString) {
        // no-op
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        } else {
            return id.intValue();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == null) {
            return false;
        }
        if (!(obj instanceof TableDetails)) {
            return false;
        }

        if (id.equals(((TableDetails) obj).id)) {
            return true;
        }
        return false;
    }
}
