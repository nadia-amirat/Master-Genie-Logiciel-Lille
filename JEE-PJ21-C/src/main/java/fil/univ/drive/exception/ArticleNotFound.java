package fil.univ.drive.exception;

import lombok.Getter;

@Getter
public class ArticleNotFound extends Throwable {
    public ArticleNotFound(String msg) {
        super(msg);
    }
}
