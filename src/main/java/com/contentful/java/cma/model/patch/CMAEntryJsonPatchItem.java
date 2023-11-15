package com.contentful.java.cma.model.patch;

/**
 * @see <a href="https://jsonpatch.com/">JSON Patch</a>
 */
public class CMAEntryJsonPatchItem {
    private JsonPatchOperator op;
    private String path;
    private Object value;

    public CMAEntryJsonPatchItem() {
    }

    public CMAEntryJsonPatchItem(JsonPatchOperator op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public JsonPatchOperator getOp() {
        return op;
    }

    public void setOp(JsonPatchOperator op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
