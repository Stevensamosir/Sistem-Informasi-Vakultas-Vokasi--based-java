package uas;

public class Berita {
    private int id;
    private String judul;
    private String foto;
    private String konten;
    private String tanggal;

    public Berita(int id, String judul, String foto, String konten, String tanggal) {
        this.id = id;
        this.judul = judul;
        this.foto = foto;
        this.konten = konten;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getFoto() {
        return foto;
    }

    public String getKonten() {
        return konten;
    }

    public String getTanggal() {
        return tanggal;
    }
}
