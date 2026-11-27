package bj.orientation.web;

import bj.orientation.ocr.OcrIndisponibleException;
import bj.orientation.web.dto.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Traduit les erreurs en enveloppe d'erreur standard ASIN. Aucun détail technique (classe
 * d'exception, trace) n'est renvoyé au client : les erreurs inattendues sont journalisées côté
 * serveur et le client reçoit un message générique.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse onValidation(MethodArgumentNotValidException exception) {
    return ApiErrorResponse.of(
        "VALIDATION_ERROR",
        "Requête invalide : série requise et notes valides (0 à 20) obligatoires.",
        HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler(OcrIndisponibleException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ApiErrorResponse onOcrIndisponible(OcrIndisponibleException exception) {
    return ApiErrorResponse.of(
        "OCR_INDISPONIBLE", exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  public ApiErrorResponse onFichierTropGros(MaxUploadSizeExceededException exception) {
    return ApiErrorResponse.of(
        "FICHIER_TROP_GROS",
        "Fichier trop volumineux (max 15 Mo). Réduis la taille de la photo ou saisis tes notes à la main.",
        HttpStatus.PAYLOAD_TOO_LARGE.value());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse onArgumentInvalide(IllegalArgumentException exception) {
    return ApiErrorResponse.of(
        "SERIE_INVALIDE", "Série inconnue.", HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MissingServletRequestPartException.class,
    MultipartException.class,
    MethodArgumentTypeMismatchException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse onRequeteIllisible(Exception exception) {
    return ApiErrorResponse.of(
        "REQUETE_INVALIDE", "Requête mal formée.", HttpStatus.BAD_REQUEST.value());
  }

  /**
   * Filet de sécurité : toute autre erreur devient un 500 générique. Les exceptions Spring qui
   * portent déjà leur propre statut HTTP (404, 405, 415…) sont relancées telles quelles pour être
   * traitées par Spring.
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorResponse onErreurInattendue(RuntimeException exception) {
    if (exception instanceof ErrorResponse) {
      throw exception;
    }
    LOG.error("Erreur inattendue lors du traitement de la requête", exception);
    return ApiErrorResponse.of(
        "ERREUR_INTERNE",
        "Une erreur interne est survenue. Réessaie plus tard.",
        HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}
