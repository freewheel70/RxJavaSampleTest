package com.hong.app.freegank.blogs;

import android.os.Parcel;
import android.os.Parcelable;

import com.hong.app.freegank.database.Blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class BlogBean implements Parcelable {

    private String id;//"_id"
    private String createdAt;
    private String description;//"desc"
    private Date publishedAt;
    private String type;
    private String url;
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

    public String getDescription() {
        return description;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getWho() {
        return who;
    }


    public BlogBean() {

    }

    public BlogBean(String createdAt, String description, String id, Date publishedAt, String type, String url, String who) {
        this.createdAt = createdAt;
        this.description = description;
        this.id = id;
        this.publishedAt = publishedAt;
        this.type = type;
        this.url = url;
        this.who = who;
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
        type = in.readString();
        url = in.readString();
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
        dest.writeString(type);
        dest.writeString(url);
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
