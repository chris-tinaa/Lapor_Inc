package com.example.laporinc.reportdetail;

public class Comment {

    private String identitas, waktu, isiKomentar;

    public Comment(String identitas, String waktu, String isiKomentar) {
        this.identitas= identitas;
        this.waktu = waktu;
        this.isiKomentar = isiKomentar;
    }

    public Comment() {
    }

    public String getIdentitas() {
        return identitas;
    }

    public void setIdentitas(String identitas) {
        this.identitas = identitas;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }
}

