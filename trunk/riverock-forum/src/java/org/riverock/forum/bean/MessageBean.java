package org.riverock.forum.bean;

import org.riverock.common.tools.StringTools;

/**
 * @author SMaslyukov
 *         Date: 29.04.2005
 *         Time: 21:09:21
 *         $Id$
 */
public class MessageBean {
    private String content = null;
    private long messageId;
    private Long forumId;
    private Long topicId;
    private String editMessageUrl = null;

    private String forumName = null;
    private String topicName = null;

    private long iconId; 

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = StringTools.encodeXml(content);
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getForumId() {
        return forumId;
    }

    public String getEditMessageUrl() {
        return editMessageUrl;
    }

    public void setEditMessageUrl(String editMessageUrl) {
        this.editMessageUrl = editMessageUrl;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getIconId() {
        return iconId;
    }

    public void setIconId(long iconId) {
        this.iconId = iconId;
    }
}
