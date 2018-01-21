package ch.bfh.eadj.persistence.enumeration;

import ch.bfh.eadj.persistence.entity.Book;

public enum BookBinding {

    HARDCOVER, PAPERBACK, EBOOK, UNKNOWN;

    public static BookBinding getBinding(String value) {
        try {
            return BookBinding.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BookBinding.UNKNOWN;
        }
    }
}
