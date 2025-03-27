package unoeste.fipp.playmysongsbackend.entities;

public class Musica {
    private String titulo, artista, estilo, file;
    public Musica(String titulo, String artista, String estilo, String file) {
        this.titulo = titulo;
        this.artista = artista;
        this.estilo = estilo;
        this.file = file;
    }

    public String getArtista() {
        return artista;
    }

    public String getEstilo() {
        return estilo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFile() {
        return file;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
