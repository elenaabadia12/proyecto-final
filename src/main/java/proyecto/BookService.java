package proyecto;

import spark.Request;
import spark.Response;

public class BookService {

    private static BookREST bookRest = new BookREST();
    private static AuthorREST authorRest = new AuthorREST();
    private static PublisherREST publisherRest = new PublisherREST();

    public static Object getAll(Request req, Response res) {
        res.type("application/json"); 
        res.status(200);
        
        //FILTRO POR TEMÁTICA
        String theme = req.queryParams("theme");
        if(theme != null && !theme.isEmpty()) {
            return bookRest.findByTheme(theme);
        }
        
        //FILTRO POR NOMBRE DE AUTOR
        String authorName = req.queryParams("author");
        if (authorName != null && !authorName.isEmpty()) {
            // Buscamos al autor por nombre para sacar su ID
            Author a = authorRest.findByName(authorName);
            if (a != null) {
                return bookRest.findByAuthor(a.getId());
            } else {
                // Si no existe, devolvemos lista vacía
                return new java.util.ArrayList<>();
            }
        }
        
        // FILTRO POR NOMBRE DE EDITORIAL
        String publisherName = req.queryParams("publisher");
        if (publisherName != null && !publisherName.isEmpty()) {
        	// Buscamos editorial por nombre para sacar su ID
            Publisher p = publisherRest.findByName(publisherName);
            if (p != null) {
                return bookRest.findByPublisher(p.getId());
            } else {
                return new java.util.ArrayList<>();
            }
        }
        
        // Si no hay filtros devolver todo
        return bookRest.getAll();
    }

    public static Object getById(Request req, Response res) {
        res.type("application/json"); 
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
        res.type("application/json"); 
        
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
        res.type("application/json"); 
        String id = req.params("id");
        
        Book currentBook = bookRest.getById(id);
        if (currentBook == null) {
            res.status(404);
            return new ErrorMessage("Book not found");
        }

        String title = req.queryParams("title");
        String theme = req.queryParams("theme");
        
       
        Book possibleDuplicate = bookRest.findByTitle(title);
        if (possibleDuplicate != null && !possibleDuplicate.getId().equals(id)) {
             res.status(409); 
             return new ErrorMessage("Title already exists in another book");
        }

        
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