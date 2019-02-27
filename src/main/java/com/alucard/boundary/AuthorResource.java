package com.alucard.boundary;

import com.alucard.domain.Author;
import com.alucard.domain.Book;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {

  private final Book book;

  public AuthorResource(Book book) {
    this.book = book;
  }

  @GET
  public Author get() {
    return book.getAuthor();
  }

}
