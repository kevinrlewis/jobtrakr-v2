package com.kevinrlewis.util;

import com.kevinrlewis.domain.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;

@Log4j2
public class ResponseUtil {

    private ResponseUtil() {}

    public static void config(DeferredResult<ResponseEntity<?>> deferredResult){
        deferredResult.onCompletion(() -> log.debug("Request Complete"));
        deferredResult.onTimeout(() -> deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Request Accepted"))));
        deferredResult.onError((Throwable t) -> deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), t.getMessage()))));
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}