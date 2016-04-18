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
    public  Date publishedAt;

    @Column
    public String type;

    @Column
    public  String url;

    @Column
    public String author;

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
    }

    public static boolean isBlogCollected(String blogID) {
        Blog blog = SQLite.select()
                .from(Blog.class)
                .where(com.hong.app.rxjavatest.database.Blog_Table.blogID.eq(blogID))
                .querySingle();

        return blog != null;
    }

    @Nullable
    public static Blog getBlogById(String blogID) {
        Blog blog = SQLite.select()
                .from(Blog.class)
                .where(com.hong.app.rxjavatest.database.Blog_Table.blogID.is(blogID))
                .querySingle();

        return blog;
    }

    public static List<Blog> getAllFavouriteBlogs() {
        return SQLite.select()
                .from(Blog.class)
                .orderBy(com.hong.app.rxjavatest.database.Blog_Table.publishedAt, false)
                .queryList();
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
}
