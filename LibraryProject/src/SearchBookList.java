public class SearchBookList {

        private String ISBN;
        private String bookName;
        private String authorName;
        private String pageSize;
        private boolean bookState;


        public String getISBN() {
            return ISBN;
        }

        public String getBookName() {
            return bookName;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getPageSize() {
            return pageSize;
        }

        public boolean isBookState() {
            return bookState;
        }

        public void setISBN(String ISBN) {
            this.ISBN = ISBN;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public void setBookState(boolean bookState) {
            this.bookState = bookState;
        }
    }

