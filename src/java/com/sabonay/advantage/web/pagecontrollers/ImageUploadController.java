/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.common.utils.StringUtil;
import com.sabonay.modules.imageutils.ImageResource;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.File;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author crash
 */
@SessionScoped
@Named(value = "imageUploadController")
public class ImageUploadController implements Serializable {

    UploadedFile file;
    private String escapeSignature, signatureUrl, directorSignature = "";
    private String signatureImagePath = "";
    private static final String PIC_NAME = "shc_director_signature";
    private boolean rendered = false;
    private static final String BEAN_NAME = "imageUploadController";
    

    @PostConstruct
    private void init() {
        signatureImagePath = System.getProperty("com.sun.aas.instanceRoot")
                + File.separator + "docroot" + File.separator + "SHC_RES" + File.separator;
        directorSignature = signatureImagePath + PIC_NAME + ".png";
        directorSignature = StringUtil.ecapeBackSlash(directorSignature);
        signatureUrl = "http://" + "localhost:8080/" + "SHC_RES" + "/";
        signatureUrl = signatureUrl + PIC_NAME + ".png";
        escapeSignature = signatureUrl;
    }

    public String handleSignatureUpload(FileUploadEvent fue) {
        System.out.println(">>in the upload method<<");
        try {
            file = fue.getFile();
            String uploadedFile = ImageResource.saveJPGImage(file.getContents(), signatureImagePath, PIC_NAME);
            signatureUrl = signatureUrl + PIC_NAME + ".png";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageUploadController getInstance() {
        ImageUploadController uploadController = (ImageUploadController) JsfUtil.getManagedBean(BEAN_NAME);
        return uploadController;
    }

    public void renderFileUpload() {
        rendered = true;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getEscapeSignature() {
        return escapeSignature;
    }

    public void setEscapeSignature(String escapeSignature) {
        this.escapeSignature = escapeSignature;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getSignatureImagePath() {
        return signatureImagePath;
    }

    public void setSignatureImagePath(String signatureImagePath) {
        this.signatureImagePath = signatureImagePath;
    }

    public String getDirectorSignature() {
        return directorSignature;
    }

    public void setDirectorSignature(String directorSignature) {
        this.directorSignature = directorSignature;
    }
    
    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
}
