package com.callhippo.bueno.callhippo.model;

public class numberArray {
    private String number;
    private String _id;
    private  String deleteStatus;

    public numberArray(String number, String _id, String deleteStatus) {
        this.number = number;
        this._id = _id;
        this.deleteStatus = deleteStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
