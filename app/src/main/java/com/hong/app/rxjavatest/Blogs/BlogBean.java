package com.hong.app.rxjavatest.Blogs;

import android.os.Parcel;
import android.os.Parcelable;

import com.hong.app.rxjavatest.database.Blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BlogBean implements Parcelable {

    private String id;//"_id"
    private String createdAt;
    private String description;//"desc"
    private Date publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }


    public BlogBean() {

    }

    public BlogBean(Blog blog) {
        id = blog.blogID;
        publishedAt = blog.publishedAt;
        description = blog.description;
        type = blog.type;
        url = blog.url;
        who = blog.author;
    }

    protected BlogBean(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        description = in.readString();
        long tmpPublishedAt = in.readLong();
        publishedAt = tmpPublishedAt != -1 ? new Date(tmpPublishedAt) : null;
        source = in.readString();
        type = in.readString();
        url = in.readString();
        used = in.readByte() != 0x00;
        who = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(description);
        dest.writeLong(publishedAt != null ? publishedAt.getTime() : -1L);
        dest.writeString(source);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeByte((byte) (used ? 0x01 : 0x00));
        dest.writeString(who);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BlogBean> CREATOR = new Parcelable.Creator<BlogBean>() {
        @Override
        public BlogBean createFromParcel(Parcel in) {
            return new BlogBean(in);
        }

        @Override
        public BlogBean[] newArray(int size) {
            return new BlogBean[size];
        }
    };


    public static List<BlogBean> convertBlogList(List<Blog> blogs) {

        List<BlogBean> blogBeanList = new ArrayList<>();

        for (int i = 0; i < blogs.size(); i++) {
            blogBeanList.add(new BlogBean(blogs.get(i)));
        }

        return blogBeanList;
    }
}
