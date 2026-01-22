package proyecto;

import spark.Request;
import spark.Response;

public class PublisherService {

    private static PublisherREST publisherRest = new PublisherREST();

    public static Object getAll(Request req, Response res) {
        res.type("application/json");
        res.status(200);
        return publisherRest.getAll();
    }

    public static Object getById(Request req, Response res) {
        res.type("application/json");
        Publisher p = publisherRest.getById(req.params("id"));
        if (p != null) {
            res.status(200);
            return p;
        } else {
            res.status(404);
            return new ErrorMessage("Publisher not found");
        }
    }

    public static Object create(Request req, Response res) {
        res.type("application/json");
        String name = req.queryParams("name");
        String website = req.queryParams("website");

        if (name == null || name.trim().isEmpty()) {
            res.status(400);
            return new ErrorMessage("Name is required");
        }

        if (publisherRest.findByName(name) != null) {
            res.status(409);
            return new ErrorMessage("Publisher already exists");
        }

        Publisher created = publisherRest.create(new Publisher(name, website));
        res.status(201);
        return created;
    }

    public static Object update(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");
        String name = req.queryParams("name");
        String website = req.queryParams("website");

        Publisher existing = publisherRest.getById(id);
        if (existing == null) {
            res.status(404);
            return new ErrorMessage("Publisher not found");
        }

        if (name != null && !name.equals(existing.getName())) {
            Publisher duplicate = publisherRest.findByName(name);
            if (duplicate != null) {
                res.status(409);
                return new ErrorMessage("Publisher name already taken");
            }
        }

        String newName = (name != null) ? name : existing.getName();
        String newWeb = (website != null) ? website : existing.getWebsite();

        Publisher updated = publisherRest.update(id, new Publisher(newName, newWeb));
        res.status(200);
        return updated;
    }

    public static Object deletePublisher(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");

        if (publisherRest.getById(id) == null) {
            res.status(404);
            return new ErrorMessage("Publisher not found");
        }

        boolean deleted = publisherRest.delete(id);
        if (deleted) {
            res.status(200);
            return new ErrorMessage("Publisher deleted successfully");
        } else {
            res.status(500);
            return new ErrorMessage("Error deleting publisher");
        }
    }
}