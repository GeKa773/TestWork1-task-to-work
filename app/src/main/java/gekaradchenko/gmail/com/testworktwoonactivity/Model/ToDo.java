package gekaradchenko.gmail.com.testworktwoonactivity.Model;

public class ToDo {
    private int id;
    private String title;
    private String text;
    private String date;

    public ToDo() {
    }

    public ToDo(String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public ToDo(int id, String title, String text, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
