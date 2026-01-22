package proyecto;

import spark.Request;
import spark.Response;

public class AuthorService {

    private static AuthorREST authorRest = new AuthorREST();

    public static Object getAll(Request req, Response res) {
        res.type("application/json");
        res.status(200);
        return authorRest.getAll();
    }

    public static Object getById(Request req, Response res) {
        res.type("application/json");
        Author a = authorRest.getById(req.params("id"));
        if (a != null) {
            res.status(200);
            return a;
        } else {
            res.status(404);
            return new ErrorMessage("Author not found");
        }
    }

    public static Object create(Request req, Response res) {
        res.type("application/json");
        String name = req.queryParams("name");
        String nationality = req.queryParams("nationality");

        if (name == null || name.trim().isEmpty()) {
            res.status(400);
            return new ErrorMessage("Name is required");
        }

        if (authorRest.findByName(name) != null) {
            res.status(409); // Conflict
            return new ErrorMessage("Author already exists");
        }

        Author created = authorRest.create(new Author(name, nationality));
        res.status(201);
        return created;
    }

    public static Object update(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");
        String name = req.queryParams("name");
        String nationality = req.queryParams("nationality");

        Author existing = authorRest.getById(id);
        if (existing == null) {
            res.status(404);
            return new ErrorMessage("Author not found");
        }

        // Si cambia el nombre, comprobar que no exista ya otro autor con ese nombre
        if (name != null && !name.equals(existing.getName())) {
            Author duplicate = authorRest.findByName(name);
            if (duplicate != null) {
                res.status(409);
                return new ErrorMessage("Author name already taken by another author");
            }
        }

        // Mantener valores antiguos si los nuevos son nulos
        String newName = (name != null) ? name : existing.getName();
        String newNat = (nationality != null) ? nationality : existing.getNationality();

        Author updated = authorRest.update(id, new Author(newName, newNat));
        res.status(200);
        return updated;
    }


    public static Object deleteAuthor(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");

        if (authorRest.getById(id) == null) {
            res.status(404);
            return new ErrorMessage("Author not found");
        }

        boolean deleted = authorRest.delete(id);
        if (deleted) {
            res.status(200);
            return new ErrorMessage("Author deleted successfully");
        } else {
            res.status(500);
            return new ErrorMessage("Error deleting author");
        }
    }
}