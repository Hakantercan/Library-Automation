public class MyBookList {
    private String ISBN;
    private String lastDeliverDate;
    private String bookName;
    private String authorName;
    private String pageSize;
    private boolean bookState;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getLastDeliverDate() {
        return lastDeliverDate;
    }

    public void setLastDeliverDate(String lastDeliverDate) {
        this.lastDeliverDate = lastDeliverDate;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isBookState() {
        return bookState;
    }

    public void setBookState(boolean bookState) {
        this.bookState = bookState;
    }
}
