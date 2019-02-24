package com.alucard;

import com.alucard.domain.Book;
import com.alucard.domain.Bookshelf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("books")
@RequestScoped
public class BookResource {

  @Inject
  Bookshelf bookshelf;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response books() {
    return Response.ok(bookshelf.findAll()).build();
  }

  @GET
  @Path("/{isbn}")
  public Response get(@PathParam("isbn") String isbn) {
    Book book = bookshelf.findByISBN(isbn);
    return Response.ok(book).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(Book book) {
    if (bookshelf.exists(book.getIsbn())) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    bookshelf.create(book);
    URI location = UriBuilder.fromResource(BookResource.class)
        .path("/{isbn}")
        .resolveTemplate("isbn", book.getIsbn())
        .build();
    return Response.created(location).build();
  }

  @PUT
  @Path("/{isbn}")
  public Response update(@PathParam("isbn") String isbn, Book book) {
    bookshelf.update(isbn, book);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{isbn}")
  public Response delete(@PathParam("isbn") String isbn) {
    bookshelf.delete(isbn);
    return Response.noContent().build();
  }

}
