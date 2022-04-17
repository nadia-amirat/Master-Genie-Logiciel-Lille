package fil.univ.drive.web.dto;

public class ArticleEntry {

        Long id;
        int qty;
        String name;

    public ArticleEntry(Long id, int qty, String name) {
        this.id = id;
        this.qty = qty;
        this.name = name;
    }

    public void setId(Long id) {
            this.id = id;
            this.name = name;
        }
        public int getQty() {
            return qty;
        }
        public void setQty(int qty) {
            this.qty = qty;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
