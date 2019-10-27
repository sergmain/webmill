package org.riverock.portlet.featured_article;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * User: SergeMaslyukov
 * Date: 14.10.2007
 * Time: 14:00:17
 * $Id$
 */
@Entity
@Table(name = "WM_PORTLET_F_ARTICLE")
@TableGenerator(
    name = "TABLE_PORTLET_FEATURE_ARTICLE",
    table = "WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portlet_feature_article",
    allocationSize = 1,
    initialValue = 1
)
public class FeaturedArticle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_PORTLET_FEATURE_ARTICLE")
    @Column(name = "ID_ARTICLE")
    private Long articleId;

    @Column(name = "ID_SITE")
    private Long siteId;

    @Column(name = "THEME")
    private String theme;

    @Lob
    @Column(name = "ARTICLE_TEXT")
    private String articleText;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "CREATE_DATE")
    private Date createDate;


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
