package com.uwimonacs.fstmobile.services;

import java.util.List;

/**
 * Created by Akinyele on 6/13/2017.
 */

public class driveFolder  {


    /**
     * kind : drive#childList
     * etag : "Wy0is8iiMpSKxWISeR6BX7Y4p60/o1BVbOISEM6CnS2H4zVppKzmdRg"
     * selfLink : https://www.googleapis.com/drive/v2/files/0Bz_AuWvwHHsqVGZRQlpJWnJUbkE/children
     * items : [{"kind":"drive#childReference","id":"0Bz_AuWvwHHsqbzVVLVVtX09Md3c","selfLink":"https://www.googleapis.com/drive/v2/files/0Bz_AuWvwHHsqVGZRQlpJWnJUbkE/children/0Bz_AuWvwHHsqbzVVLVVtX09Md3c","childLink":"https://www.googleapis.com/drive/v2/files/0Bz_AuWvwHHsqbzVVLVVtX09Md3c"}]
     */

    private String kind;
    private String etag;
    private String selfLink;
    private List<ItemsBean> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * kind : drive#childReference
         * id : 0Bz_AuWvwHHsqbzVVLVVtX09Md3c
         * selfLink : https://www.googleapis.com/drive/v2/files/0Bz_AuWvwHHsqVGZRQlpJWnJUbkE/children/0Bz_AuWvwHHsqbzVVLVVtX09Md3c
         * childLink : https://www.googleapis.com/drive/v2/files/0Bz_AuWvwHHsqbzVVLVVtX09Md3c
         */

        private String kind;
        private String id;
        private String selfLink;
        private String childLink;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSelfLink() {
            return selfLink;
        }

        public void setSelfLink(String selfLink) {
            this.selfLink = selfLink;
        }

        public String getChildLink() {
            return childLink;
        }

        public void setChildLink(String childLink) {
            this.childLink = childLink;
        }
    }
}
