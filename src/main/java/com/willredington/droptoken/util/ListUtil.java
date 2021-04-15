package com.willredington.droptoken.util;

import java.util.List;

public class ListUtil {

  private ListUtil() {}

  public static <T> boolean allItemsMatch(List<T> items) {
    if (!items.isEmpty()) {

      T firstItem = items.get(0);
      return items.stream().allMatch(val -> val.equals(firstItem));
    }

    return true;
  }
}
