package org.lslonina.shop.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {
    
    public InvalidDataExceptionMapper() {
    }

    @Override
    public Response toResponse(InvalidDataException exception) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
    }

}