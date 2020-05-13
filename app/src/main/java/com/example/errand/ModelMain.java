package com.example.errand;

public class ModelMain {

    private int image;
    private String text_banner, text_banner_desc1, text_banner_desc2,text_pod, text_our_serv, text_serv_ran, text_12_per;

    public ModelMain(int image, String text_banner, String text_banner_desc1, String text_banner_desc2, String text_pod, String text_our_serv, String text_serv_ran, String text_12_per) {
        this.image = image;
        this.text_banner = text_banner;
        this.text_banner_desc1 = text_banner_desc1;
        this.text_banner_desc2 = text_banner_desc2;
        this.text_pod = text_pod;
        this.text_our_serv = text_our_serv;
        this.text_serv_ran = text_serv_ran;
        this.text_12_per = text_12_per;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText_banner() {
        return text_banner;
    }

    public void setText_banner(String text_banner) {
        this.text_banner = text_banner;
    }

    public String getText_banner_desc1() {
        return text_banner_desc1;
    }

    public void setText_banner_desc1(String text_banner_desc1) {
        this.text_banner_desc1 = text_banner_desc1;
    }

    public String getText_banner_desc2() {
        return text_banner_desc2;
    }

    public void setText_banner_desc2(String text_banner_desc2) {
        this.text_banner_desc2 = text_banner_desc2;
    }

    public String getText_pod() {
        return text_pod;
    }

    public void setText_pod(String text_pod) {
        this.text_pod = text_pod;
    }

    public String getText_our_serv() {
        return text_our_serv;
    }

    public void setText_our_serv(String text_our_serv) {
        this.text_our_serv = text_our_serv;
    }

    public String getText_serv_ran() {
        return text_serv_ran;
    }

    public void setText_serv_ran(String text_serv_ran) {
        this.text_serv_ran = text_serv_ran;
    }

    public String getText_12_per() {
        return text_12_per;
    }

    public void setText_12_per(String text_12_per) {
        this.text_12_per = text_12_per;
    }
}
