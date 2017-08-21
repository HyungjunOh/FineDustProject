package com.hyungjun212naver.finedustproject.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hyung on 2017-08-17.
 */

public class GpsToAddr {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public class Meta {

        @SerializedName("total_count")
        @Expose
        private Integer totalCount;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

    }

    public class Document {

        @SerializedName("region_type")
        @Expose
        private String regionType;
        @SerializedName("address_name")
        @Expose
        private String addressName;
        @SerializedName("region_1depth_name")
        @Expose
        private String region1depthName;
        @SerializedName("region_2depth_name")
        @Expose
        private String region2depthName;
        @SerializedName("region_3depth_name")
        @Expose
        private String region3depthName;
        @SerializedName("region_4depth_name")
        @Expose
        private String region4depthName;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("x")
        @Expose
        private Double x;
        @SerializedName("y")
        @Expose
        private Double y;

        public String getRegionType() {
            return regionType;
        }

        public void setRegionType(String regionType) {
            this.regionType = regionType;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public String getRegion1depthName() {
            return region1depthName;
        }

        public void setRegion1depthName(String region1depthName) {
            this.region1depthName = region1depthName;
        }

        public String getRegion2depthName() {
            return region2depthName;
        }

        public void setRegion2depthName(String region2depthName) {
            this.region2depthName = region2depthName;
        }

        public String getRegion3depthName() {
            return region3depthName;
        }

        public void setRegion3depthName(String region3depthName) {
            this.region3depthName = region3depthName;
        }

        public String getRegion4depthName() {
            return region4depthName;
        }

        public void setRegion4depthName(String region4depthName) {
            this.region4depthName = region4depthName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

    }
}