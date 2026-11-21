package it.sara.pair.common.api;

import java.util.List;

/**
 * PagedResponse
 * @param status basis of all api responses
 * @param total total number of matching rows
 * @param data typed list of items, payload
 */
public record PagedResponse<T>(BaseStatus status, long total, List<T> data) {}
