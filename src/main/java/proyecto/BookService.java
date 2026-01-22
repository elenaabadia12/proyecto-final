package proyecto;

import spark.Request;
import spark.Response;

public class BookService {

    private static BookREST bookRest = new BookREST();
    private static AuthorREST authorRest = new AuthorREST();
    private static PublisherREST publisherRest = new PublisherREST();

    public static Object getAll(Request req, Response res) {
        res.type("application/json"); // Corrección
        res.status(200);
        
        // Filtros opcionales por query params (ej: /books?theme=Terror)
        String theme = req.queryParams("theme");
        if(theme != null && !theme.isEmpty()) {
            return bookRest.findByTheme(theme);
        }
        
        return bookRest.getAll();
    }

    public static Object getById(Request req, Response res) {
        res.type("application/json"); // Corrección
        Book b = bookRest.getById(req.params("id"));
        if (b != null) {
            res.status(200);
            return b;
        } else {
            res.status(404);
            return new ErrorMessage("Book not found");
        }
    }

    public static Object create(Request req, Response res) {
        res.type("application/json"); // Corrección
        
        String title = req.queryParams("title");
        String theme = req.queryParams("theme");
        String authorName = req.queryParams("author");
        String publisherName = req.queryParams("publisher");

        // 1. Validar que existan el Autor y la Editorial
        Author auth = authorRest.findByName(authorName);
        if (auth == null) {
            res.status(400);
            return new ErrorMessage("Author does not exist. Create Author first.");
        }

        Publisher pub = publisherRest.findByName(publisherName);
        if (pub == null) {
            res.status(400);
            return new ErrorMessage("Publisher does not exist. Create Publisher first.");
        }

        // 2. Crear Libro
        Book newBook = new Book(title, theme, auth.getId(), pub.getId());
        Book created = bookRest.create(newBook);

        if (created != null) {
            res.status(201);
            return created;
        } else {
            res.status(500);
            return new ErrorMessage("Error creating book");
        }
    }

    public static Object update(Request req, Response res) {
        res.type("application/json"); // Corrección
        String id = req.params("id");
        
        Book currentBook = bookRest.getById(id);
        if (currentBook == null) {
            res.status(404);
            return new ErrorMessage("Book not found");
        }

        String title = req.queryParams("title");
        String theme = req.queryParams("theme");
        
        // CORRECCIÓN PUT: Validar si el título ya existe en OTRO libro
        Book possibleDuplicate = bookRest.findByTitle(title);
        if (possibleDuplicate != null && !possibleDuplicate.getId().equals(id)) {
             res.status(409); // Conflict
             return new ErrorMessage("Title already exists in another book");
        }

        // Mantenemos los IDs anteriores si no se pasan nuevos, o lógica para actualizarlos
        // Para simplificar, asumimos que solo se edita titulo y tema, o se recuperan los objetos
        String authorId = currentBook.getAuthorId(); 
        String publisherId = currentBook.getPublisherId();

        Book updated = bookRest.update(id, new Book(title, theme, authorId, publisherId));
        res.status(200);
        return updated;
    }

    public static Object deleteBook(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");

        if (bookRest.getById(id) == null) {
            res.status(404);
            return new ErrorMessage("Book not found");
        }

        boolean deleted = bookRest.delete(id);
        if (deleted) {
            res.status(200);
            return new ErrorMessage("Book deleted successfully");
        } else {
            res.status(500);
            return new ErrorMessage("Error deleting book");
        }
    }
}