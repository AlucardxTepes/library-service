package com.alucard.boundary;

import com.alucard.domain.Book;
import com.alucard.domain.Bookshelf;
import com.alucard.domain.Library;
import com.alucard.domain.Loan;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sub REST resource implementation for loans.
 */
@RequestScoped
public class LoanResource {

  @Inject
  private Bookshelf bookshelf;
  @Inject
  private Library library;
  @Inject
  private Logger logger;

  private String isbn;

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response loans() {
    logger.log(Level.INFO, "Getting loans for book with ISBN {0}.", isbn);
    Book book = bookshelf.findByISBN(isbn);
    return Response.ok(book.getLoans()).build();
  }

  @GET
  @Path("/{loanId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response loan(@PathParam("loanId") String loanId) {
    Loan loan = library.loanInfo(loanId);
    return Response.ok(loan).build();
  }

  @DELETE
  @Path("/{loanId}")
  public Response returnBook(@PathParam("loanId") String loanId) {
    library.returnBook(isbn, loanId);
    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response lendBook(Loan loan) {
    library.lendBook(isbn, loan);
    URI location = UriBuilder.fromResource(BookResource.class)
        .path("/{isbn}/loans/{loanId}")
        .resolveTemplate("isbn", isbn)
        .resolveTemplate("loanId", loan.getId())
        .build();
    return Response.created(location).build();
  }
}
