package org.riverock.webmill.portal.bean;

import org.riverock.interfaces.portal.bean.Article;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Blob;

/**
 * User: SergeMaslyukov
 * Date: 19.11.2006
 * Time: 1:06:52
 * <p/>
 * $Id$
 */
@Entity
@Table(name="WM_PORTLET_ARTICLE")
@TableGenerator(
    name="TABLE_PORTLET_ARTICLE",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portlet_article",
    allocationSize = 1,
    initialValue = 1
)
public class ArticleBean implements Article, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTLET_ARTICLE")
    @Column(name="ID_SITE_CTX_ARTICLE")
    private Long articleId;

    @Column(name="ID_USER")
    private Long userId;

    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId;

    @Column(name="DATE_POST")
    private Date postDate;

    @Column(name="ARTICLE_CODE")
    private String articleCode;

    @Column(name="NAME_ARTICLE")
    private String articleName;

    @Column(name="IS_PLAIN_HTML")
    private boolean isPlain;

    @Column(name="IS_DELETED")
    private boolean isDeleted;

    @Column(name="ARTICLE_BLOB")
    private Blob articleBlob;

    @Transient
    private String articleData;


    public ArticleBean() {
    }

    public ArticleBean(Article article) {
        this.articleId = article.getArticleId();
        this.siteLanguageId = article.getSiteLanguageId();
        this.postDate = article.getPostDate();
        this.articleCode = article.getArticleCode();
        this.articleName = article.getArticleName();
        this.isPlain = article.isPlain();
        this.articleData = article.getArticleData();
        this.userId = article.getUserId();
        this.isDeleted = article.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Blob getArticleBlob() {
        return articleBlob;
    }

    public void setArticleBlob(Blob articleBlob) {
        this.articleBlob = articleBlob;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        if (postDate==null) {
            this.postDate=null;
            return;
        }
        this.postDate = new Date(postDate.getTime());
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleData() {
        return articleData;
    }

    public void setArticleData(String articleData) {
        this.articleData = articleData;
    }

    public boolean isPlain() {
        return isPlain;
    }

    public void setPlain(boolean plain) {
        isPlain = plain;
    }
}
