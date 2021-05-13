package co.domi.androids13;

import java.util.Date;

public class Post {

    public String id;
    public String comentario;
    public long fecha;
    public String idfoto;

    public Post() {
    }

    public Post(String id, String comentario, String idfoto) {
        this.id = id;
        this.comentario = comentario;
        this.fecha = new Date().getTime();
        this.idfoto = idfoto;
    }
}
