package io.duygu.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thesis {
    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("meta")
    @Expose
    private String meta;
    @SerializedName("tr")
    @Expose
    private String tr;
    @SerializedName("tezNo")
    @Expose
    private Integer tezNo;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public Integer getTezNo() {
        return tezNo;
    }

    public void setTezNo(Integer tezNo) {
        this.tezNo = tezNo;
    }

}
