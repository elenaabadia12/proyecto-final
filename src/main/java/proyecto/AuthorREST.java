package proyecto;

import java.util.List;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public class AuthorREST {
    // CAMBIA ESTO POR TU URL DE RESTDB PARA AUTORES
	private static final String URL = "https://libreria-7fe4.restdb.io/rest/authors";
    private static final String APIKEY = "66ccab08d021d717cf55132daf54b05da9536";

    public List<Author> getAll() {
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .asObject(new GenericType<List<Author>>(){})
                .getBody();
    }

    public Author getById(String id) {
        return Unirest.get(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .asObject(Author.class)
                .getBody();
    }

    public Author findByName(String name) {
        String query = String.format("{\"name\":\"%s\"}", name);
        List<Author> list = Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Author>>(){})
                .getBody();
        return list.isEmpty() ? null : list.get(0);
    }

    public Author create(Author a) {
        return Unirest.post(URL)
                .header("x-apikey", APIKEY)
                .header("Content-Type", "application/json")
                .body(a)
                .asObject(Author.class)
                .getBody();
    }

    public Author update(String id, Author a) {
        return Unirest.put(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .header("Content-Type", "application/json")
                .body(a)
                .asObject(Author.class)
                .getBody();
    }

    // Corrección: Delete devuelve status
    public boolean delete(String id) {
        HttpResponse<String> response = Unirest.delete(URL + "/{id}")
            .header("x-apikey", APIKEY)
            .routeParam("id", id)
            .asString();
        return response.getStatus() == 200 || response.getStatus() == 204;
    }
}