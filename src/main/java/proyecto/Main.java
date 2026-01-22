package proyecto;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Puerto predeterminado 8080 (necesario para Railway)
        port(8080); 

        // Configuración CORS (Para que se pueda llamar desde cualquier sitio)
        options("/*", (req, res) -> {
            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null)
                res.header("Access-Control-Allow-Headers", headers);
            String method = req.headers("Access-Control-Request-Method");
            if (method != null)
                res.header("Access-Control-Allow-Methods", method);
            return "OK";
        });
        before((req, res) -> res.header("Access-Control-Allow-Origin", "*"));

        // Transformador a JSON
        JsonTransformer json = new JsonTransformer();


        // RUTAS AUTORES
        get("/authors", AuthorService::getAll, json);
        get("/authors/:id", AuthorService::getById, json);
        post("/authors", AuthorService::create, json);
        put("/authors/:id", AuthorService::update, json);
        
        // CORRECCIÓN AQUÍ:
        delete("/authors/:id", AuthorService::deleteAuthor, json); 

        // RUTAS EDITORIALES
        get("/publishers", PublisherService::getAll, json);
        get("/publishers/:id", PublisherService::getById, json);
        post("/publishers", PublisherService::create, json);
        put("/publishers/:id", PublisherService::update, json);
        
        // CORRECCIÓN AQUÍ:
        delete("/publishers/:id", PublisherService::deletePublisher, json);

        // RUTAS LIBROS
        get("/books", BookService::getAll, json);
        get("/books/:id", BookService::getById, json);
        post("/books", BookService::create, json);
        put("/books/:id", BookService::update, json);
        
        // CORRECCIÓN AQUÍ:
        delete("/books/:id", BookService::deleteBook, json);
    }
}