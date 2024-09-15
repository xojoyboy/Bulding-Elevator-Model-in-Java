package view;

import scanerzus.Request;

/**
 * RequestListener is a functional interface that defines the method required
 * for a request listener.
 */
@FunctionalInterface
public interface RequestListener {
  void onRequest(Request request);
}
