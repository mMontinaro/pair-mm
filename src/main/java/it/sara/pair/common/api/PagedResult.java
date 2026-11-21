package it.sara.pair.common.api;

import java.util.List;

public record PagedResult<T>(long total, List<T> data) {}
