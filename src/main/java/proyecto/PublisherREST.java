package proyecto;

import java.util.List;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class PublisherREST {
    // CAMBIA ESTO POR TU URL DE RESTDB PARA EDITORIALES
	private static final String URL = "https://libreria-7fe4.restdb.io/rest/publishers";
    private static final String APIKEY = "66ccab08d021d717cf55132daf54b05da9536";

    public List<Publisher> getAll() {
        return Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .asObject(new GenericType<List<Publisher>>(){}).getBody();
    }

    public Publisher getById(String id) {
        return Unirest.get(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .asObject(Publisher.class).getBody();
    }
    
    public Publisher findByName(String name) {
        String query = String.format("{\"name\":\"%s\"}", name);
        List<Publisher> list = Unirest.get(URL)
                .header("x-apikey", APIKEY)
                .queryString("q", query)
                .asObject(new GenericType<List<Publisher>>(){}).getBody();
        return list.isEmpty() ? null : list.get(0);
    }

    public Publisher create(Publisher p) {
        return Unirest.post(URL)
                .header("x-apikey", APIKEY)
                .header("Content-Type", "application/json")
                .body(p)
                .asObject(Publisher.class).getBody();
    }

    public Publisher update(String id, Publisher p) {
        return Unirest.put(URL + "/{id}")
                .header("x-apikey", APIKEY)
                .routeParam("id", id)
                .header("Content-Type", "application/json")
                .body(p)
                .asObject(Publisher.class).getBody();
    }

    public boolean delete(String id) {
        HttpResponse<String> response = Unirest.delete(URL + "/{id}")
            .header("x-apikey", APIKEY)
            .routeParam("id", id)
            .asString();
        return response.getStatus() == 200;
    }
}