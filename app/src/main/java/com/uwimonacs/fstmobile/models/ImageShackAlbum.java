package com.uwimonacs.fstmobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akinyele on 7/28/2017.
 */

    public class ImageShackAlbum {

    /**
     * success : true
     * process_time : 17
     * result : {"id":"J1Zl","title":"C5","description":"Chemistry Lecture Theater 5","creation_date":1501168813,"public":true,"password_protected":false,"is_owner":false,"is_following":false,"owner":{"username":"Akinyele","avatar":{"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0},"membership":"trial","featured_photographer":false,"allow_following":null},"limit":20,"offset":0,"sort":"custom","order":"desc","total":1,"special":false,"images":[{"id":"pmqOaXrCj","server":922,"bucket":1294,"filename":"qOaXrC.jpg","direct_link":"imagizer.imageshack.com/img922/1294/qOaXrC.jpg","original_filename":"c5.jpg","title":"","album":{"id":"J1Zl","title":"C5","public":true},"creation_date":1501169187,"public":true,"hidden":false,"filesize":164,"width":960,"height":1280,"likes":0,"liked":false,"is_owner":false,"owner":{"username":"Akinyele","avatar":{"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0},"membership":"trial","featured_photographer":false},"adult_content":false,"sort_order":0}]}
     */

//    private boolean success;
//    private int process_time;
    private ResultType result;

//

    public ResultType getResult() {
        return result;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    public static class ResultType {
        /**
         * id : J1Zl
         * title : C5
         * description : Chemistry Lecture Theater 5
         * creation_date : 1501168813
         * public : true
         * password_protected : false
         * is_owner : false
         * is_following : false
         * owner : {"username":"Akinyele","avatar":{"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0},"membership":"trial","featured_photographer":false,"allow_following":null}
         * limit : 20
         * offset : 0
         * sort : custom
         * order : desc
         * total : 1
         * special : false
         * images : [{"id":"pmqOaXrCj","server":922,"bucket":1294,"filename":"qOaXrC.jpg","direct_link":"imagizer.imageshack.com/img922/1294/qOaXrC.jpg","original_filename":"c5.jpg","title":"","album":{"id":"J1Zl","title":"C5","public":true},"creation_date":1501169187,"public":true,"hidden":false,"filesize":164,"width":960,"height":1280,"likes":0,"liked":false,"is_owner":false,"owner":{"username":"Akinyele","avatar":{"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0},"membership":"trial","featured_photographer":false},"adult_content":false,"sort_order":0}]
         */

        private String id;
        private String title;
        private String description;
        private int creation_date;
        @SerializedName("public")
        private boolean publicX;
        private boolean password_protected;
        private boolean is_owner;
        private boolean is_following;
        private OwnerType owner;
        private int limit;
        private int offset;
        private String sort;
        private String order;
        private int total;
        private boolean special;
        private List<ImagesType> images;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getCreation_date() {
            return creation_date;
        }

        public void setCreation_date(int creation_date) {
            this.creation_date = creation_date;
        }

        public boolean isPublicX() {
            return publicX;
        }

        public void setPublicX(boolean publicX) {
            this.publicX = publicX;
        }

        public boolean isPassword_protected() {
            return password_protected;
        }

        public void setPassword_protected(boolean password_protected) {
            this.password_protected = password_protected;
        }

        public boolean isIs_owner() {
            return is_owner;
        }

        public void setIs_owner(boolean is_owner) {
            this.is_owner = is_owner;
        }

        public boolean isIs_following() {
            return is_following;
        }

        public void setIs_following(boolean is_following) {
            this.is_following = is_following;
        }

        public OwnerType getOwner() {
            return owner;
        }

        public void setOwner(OwnerType owner) {
            this.owner = owner;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public boolean isSpecial() {
            return special;
        }

        public void setSpecial(boolean special) {
            this.special = special;
        }

        public List<ImagesType> getImages() {
            return images;
        }

        public void setImages(List<ImagesType> images) {
            this.images = images;
        }

        public static class OwnerType {
            /**
             * username : Akinyele
             * avatar : {"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0}
             * membership : trial
             * featured_photographer : false
             * allow_following : null
             */

            private String username;
            private AvatarType avatar;
            private String membership;
            private boolean featured_photographer;
            private Object allow_following;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public AvatarType getAvatar() {
                return avatar;
            }

            public void setAvatar(AvatarType avatar) {
                this.avatar = avatar;
            }

            public String getMembership() {
                return membership;
            }

            public void setMembership(String membership) {
                this.membership = membership;
            }

            public boolean isFeatured_photographer() {
                return featured_photographer;
            }

            public void setFeatured_photographer(boolean featured_photographer) {
                this.featured_photographer = featured_photographer;
            }

            public Object getAllow_following() {
                return allow_following;
            }

            public void setAllow_following(Object allow_following) {
                this.allow_following = allow_following;
            }

            public static class AvatarType {
                /**
                 * id :
                 * filename :
                 * server : 0
                 * cropped : false
                 * x_pos : 0
                 * y_pos : 0
                 * x_length : 0
                 * y_length : 0
                 */

                private String id;
                private String filename;
                private int server;
                private boolean cropped;
                private int x_pos;
                private int y_pos;
                private int x_length;
                private int y_length;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public int getServer() {
                    return server;
                }

                public void setServer(int server) {
                    this.server = server;
                }

                public boolean isCropped() {
                    return cropped;
                }

                public void setCropped(boolean cropped) {
                    this.cropped = cropped;
                }

                public int getX_pos() {
                    return x_pos;
                }

                public void setX_pos(int x_pos) {
                    this.x_pos = x_pos;
                }

                public int getY_pos() {
                    return y_pos;
                }

                public void setY_pos(int y_pos) {
                    this.y_pos = y_pos;
                }

                public int getX_length() {
                    return x_length;
                }

                public void setX_length(int x_length) {
                    this.x_length = x_length;
                }

                public int getY_length() {
                    return y_length;
                }

                public void setY_length(int y_length) {
                    this.y_length = y_length;
                }
            }
        }

        public static class ImagesType {
            /**
             * id : pmqOaXrCj
             * server : 922
             * bucket : 1294
             * filename : qOaXrC.jpg
             * direct_link : imagizer.imageshack.com/img922/1294/qOaXrC.jpg
             * original_filename : c5.jpg
             * title :
             * album : {"id":"J1Zl","title":"C5","public":true}
             * creation_date : 1501169187
             * public : true
             * hidden : false
             * filesize : 164
             * width : 960
             * height : 1280
             * likes : 0
             * liked : false
             * is_owner : false
             * owner : {"username":"Akinyele","avatar":{"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0},"membership":"trial","featured_photographer":false}
             * adult_content : false
             * sort_order : 0
             */

            private String id;
            private int server;
            private int bucket;
            private String filename;
            private String direct_link;
            private String original_filename;
            private String title;
            private AlbumType album;
            private int creation_date;
            @SerializedName("public")
            private boolean publicX;
            private boolean hidden;
            private int filesize;
            private int width;
            private int height;
            private int likes;
            private boolean liked;
            private boolean is_owner;
            private OwnerTypeX owner;
            private boolean adult_content;
            private int sort_order;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getServer() {
                return server;
            }

            public void setServer(int server) {
                this.server = server;
            }

            public int getBucket() {
                return bucket;
            }

            public void setBucket(int bucket) {
                this.bucket = bucket;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getDirect_link() {
                return direct_link;
            }

            public void setDirect_link(String direct_link) {
                this.direct_link = direct_link;
            }

            public String getOriginal_filename() {
                return original_filename;
            }

            public void setOriginal_filename(String original_filename) {
                this.original_filename = original_filename;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public AlbumType getAlbum() {
                return album;
            }

            public void setAlbum(AlbumType album) {
                this.album = album;
            }

            public int getCreation_date() {
                return creation_date;
            }

            public void setCreation_date(int creation_date) {
                this.creation_date = creation_date;
            }

            public boolean isPublicX() {
                return publicX;
            }

            public void setPublicX(boolean publicX) {
                this.publicX = publicX;
            }

            public boolean isHidden() {
                return hidden;
            }

            public void setHidden(boolean hidden) {
                this.hidden = hidden;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public boolean isLiked() {
                return liked;
            }

            public void setLiked(boolean liked) {
                this.liked = liked;
            }

            public boolean isIs_owner() {
                return is_owner;
            }

            public void setIs_owner(boolean is_owner) {
                this.is_owner = is_owner;
            }

            public OwnerTypeX getOwner() {
                return owner;
            }

            public void setOwner(OwnerTypeX owner) {
                this.owner = owner;
            }

            public boolean isAdult_content() {
                return adult_content;
            }

            public void setAdult_content(boolean adult_content) {
                this.adult_content = adult_content;
            }

            public int getSort_order() {
                return sort_order;
            }

            public void setSort_order(int sort_order) {
                this.sort_order = sort_order;
            }

            public static class AlbumType {
                /**
                 * id : J1Zl
                 * title : C5
                 * public : true
                 */

                private String id;
                private String title;
                @SerializedName("public")
                private boolean publicX;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public boolean isPublicX() {
                    return publicX;
                }

                public void setPublicX(boolean publicX) {
                    this.publicX = publicX;
                }
            }

            public static class OwnerTypeX {
                /**
                 * username : Akinyele
                 * avatar : {"id":"","filename":"","server":0,"cropped":false,"x_pos":0,"y_pos":0,"x_length":0,"y_length":0}
                 * membership : trial
                 * featured_photographer : false
                 */

                private String username;
                private AvatarTypeX avatar;
                private String membership;
                private boolean featured_photographer;

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public AvatarTypeX getAvatar() {
                    return avatar;
                }

                public void setAvatar(AvatarTypeX avatar) {
                    this.avatar = avatar;
                }

                public String getMembership() {
                    return membership;
                }

                public void setMembership(String membership) {
                    this.membership = membership;
                }

                public boolean isFeatured_photographer() {
                    return featured_photographer;
                }

                public void setFeatured_photographer(boolean featured_photographer) {
                    this.featured_photographer = featured_photographer;
                }

                public static class AvatarTypeX {
                    /**
                     * id :
                     * filename :
                     * server : 0
                     * cropped : false
                     * x_pos : 0
                     * y_pos : 0
                     * x_length : 0
                     * y_length : 0
                     */

                    private String id;
                    private String filename;
                    private int server;
                    private boolean cropped;
                    private int x_pos;
                    private int y_pos;
                    private int x_length;
                    private int y_length;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getFilename() {
                        return filename;
                    }

                    public void setFilename(String filename) {
                        this.filename = filename;
                    }

                    public int getServer() {
                        return server;
                    }

                    public void setServer(int server) {
                        this.server = server;
                    }

                    public boolean isCropped() {
                        return cropped;
                    }

                    public void setCropped(boolean cropped) {
                        this.cropped = cropped;
                    }

                    public int getX_pos() {
                        return x_pos;
                    }

                    public void setX_pos(int x_pos) {
                        this.x_pos = x_pos;
                    }

                    public int getY_pos() {
                        return y_pos;
                    }

                    public void setY_pos(int y_pos) {
                        this.y_pos = y_pos;
                    }

                    public int getX_length() {
                        return x_length;
                    }

                    public void setX_length(int x_length) {
                        this.x_length = x_length;
                    }

                    public int getY_length() {
                        return y_length;
                    }

                    public void setY_length(int y_length) {
                        this.y_length = y_length;
                    }
                }
            }
        }
    }
}