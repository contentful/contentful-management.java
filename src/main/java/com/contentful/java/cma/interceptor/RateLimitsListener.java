package com.contentful.java.cma.interceptor;

import com.contentful.java.cma.model.RateLimits;

/**
 * Interface to get informed about rate limits.
 */
public interface RateLimitsListener {
  void onRateLimitHeaderReceived(RateLimits rateLimits);
}
