package it.sara.pair.common.api;

/**
 * BaseResponse
 * @param status basis of all api responses
 * @param data payload, typed item
 */
public record BaseResponse<T>(BaseStatus baseStatus, T data) { }
