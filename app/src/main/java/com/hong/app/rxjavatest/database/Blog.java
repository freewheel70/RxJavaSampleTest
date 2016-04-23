package com.hong.app.rxjavatest.database;

import android.support.annotation.Nullable;

import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
@ModelContainer
@Table(database = FreeGankDatabase.class)
public class Blog extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String blogID;

    @Column
    public String description;//"desc"

    @Column
    public Date publishedAt;

    @Column
    public String type;

    @Column
    public String url;

    @Column
    public String author;

    @Column
    public boolean isRemoved;

    @Column
    public boolean isSynced;

    public Blog() {
        super();
    }

    public Blog(BlogBean blogBean) {
        blogID = blogBean.getId();
        description = blogBean.getDescription();
        publishedAt = blogBean.getPublishedAt();
        type = blogBean.getType();
        url = blogBean.getUrl();
        author = blogBean.getWho();
        isRemoved = false;
        isSynced = false;
    }

    public static boolean isBlogCollected(String blogID) {
        Blog blog = SQLite.select()
                .from(Blog.class)
                .where(com.hong.app.rxjavatest.database.Blog_Table.blogID.eq(blogID))
                .querySingle();

        return blog != null && (!blog.isRemoved);
    }

    @Nullable
    public static Blog getBlogById(String blogID) {
        Blog blog = SQLite.select()
                .from(Blog.class)
                .where(com.hong.app.rxjavatest.database.Blog_Table.blogID.is(blogID))
                .querySingle();

        return blog;
    }

    public static List<Blog> getAllNonRemovedFavouriteBlogs() {
        return SQLite.select()
                .from(Blog.class)
                .where(Blog_Table.isRemoved.eq(false))
                .orderBy(com.hong.app.rxjavatest.database.Blog_Table.publishedAt, false)
                .queryList();
    }

    public static List<Blog> getAllNonSyncedFavouriteBlogs() {
        return SQLite.select()
                .from(Blog.class)
                .where(Blog_Table.isSynced.eq(false))
                .orderBy(com.hong.app.rxjavatest.database.Blog_Table.publishedAt, false)
                .queryList();
    }


    public static void createBlogFromJsonObject(JSONObject jsonObject) throws JSONException {

        String blogID = jsonObject.getString("blogID");
        Blog blog = getBlogById(blogID);
        if (blog == null) {
            blog = new Blog();
            blog.blogID = blogID;
            blog.author = jsonObject.getString("author");
            blog.url = jsonObject.getString("url");
            blog.type = jsonObject.optString("type");
            blog.description = jsonObject.getString("description");
            blog.publishedAt = new Date(jsonObject.getLong("publishedAt"));
            blog.isRemoved = jsonObject.getBoolean("isRemoved");
            blog.isSynced = true;
        } else {
            blog.isRemoved = jsonObject.getBoolean("isRemoved");
        }

        blog.save();
    }


    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("blogID", blogID);
        jsonObject.put("description", description);
        jsonObject.put("publishedAt", publishedAt.getTime());
        jsonObject.put("type", type);
        jsonObject.put("author", author);
        jsonObject.put("isRemoved", isRemoved);
        return jsonObject;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "author='" + author + '\'' +
                ", id=" + id +
                ", blogID='" + blogID + '\'' +
                ", description='" + description + '\'' +
                ", publishedAt=" + publishedAt +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static void deleteAll() {
        SQLite.delete().from(Blog.class).execute();
    }
}
