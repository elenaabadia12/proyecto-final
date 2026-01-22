package proyecto;

import java.util.List;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class BookREST {
    // CAMBIA ESTO POR TU URL DE RESTDB PARA LIBROS
	private static final String URL = "https://libreria-7fe4.restdb.io/rest/books";
    private static final String APIKEY = "66ccab08d021d717cf55132daf54b05da9536";

    public List<Book> getAll() {
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .asObject(new GenericType<List<Book>>(){}).getBody();
    }

    public Book getById(String id) {
        return Unirest.get(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .asObject(Book.class).getBody();
    }

    // Búsqueda por Autor (Relación)
    public List<Book> findByAuthor(String authorId) {
        String query = String.format("{\"authorId\":\"%s\"}", authorId);
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Book>>(){}).getBody();
    }

    // Búsqueda por Editorial (Relación)
    public List<Book> findByPublisher(String publisherId) {
        String query = String.format("{\"publisherId\":\"%s\"}", publisherId);
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Book>>(){}).getBody();
    }

    // Búsqueda por Temática (Filtro)
    public List<Book> findByTheme(String theme) {
        String query = String.format("{\"theme\":\"%s\"}", theme);
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Book>>(){}).getBody();
    }
    
    // Validar título duplicado (para el PUT)
    public Book findByTitle(String title) {
        String query = String.format("{\"title\":\"%s\"}", title);
        List<Book> list = Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Book>>(){}).getBody();
        return list.isEmpty() ? null : list.get(0);
    }

    public Book create(Book b) {
        return Unirest.post(URL)
                .header("x-apikey", APIKEY)
                .header("Content-Type", "application/json")
                .body(b)
                .asObject(Book.class).getBody();
    }

    public Book update(String id, Book b) {
        return Unirest.put(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .header("Content-Type", "application/json")
                .body(b)
                .asObject(Book.class).getBody();
    }

    public boolean delete(String id) {
        HttpResponse<String> response = Unirest.delete(URL + "/{id}")
            .header("x-apikey", APIKEY)
            .routeParam("id", id)
            .asString();
        return response.getStatus() == 200;
    }
}