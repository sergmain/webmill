package org.riverock.webmill.admin.bean;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import java.io.Serializable;

/**
 * User: SergeMaslyukov
 * Date: 24.02.2007
 * Time: 18:23:03
 */
public class UploadedFileBean implements Serializable {
    private UploadedFile uploadedFile;


    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
