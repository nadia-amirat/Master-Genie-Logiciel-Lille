package fil.univ.drive;

import com.fasterxml.jackson.databind.ObjectMapper;
import fil.univ.drive.service.user.Role;
import fil.univ.drive.web.ConnectionController;
import lombok.SneakyThrows;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class APIClient {
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    public MvcResult getConnectionPage(Cookie cookie) {
        return sendRequest(get("/connect").cookie(cookie), status().isOk());
    }

    @SneakyThrows
    public MvcResult connect(Cookie cookie, String role) {
        return sendRequest(post("/connect")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(List.of(
                                new BasicNameValuePair("role", role.toUpperCase(Locale.ROOT))))))
                        .cookie(cookie),
                status().is3xxRedirection()
        );
    }

    public MvcResult getExpiredArticles() throws Exception {
        return sendRequest(get("/stock/expired").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .cookie(new Cookie(ConnectionController.COOKIE_KEY, Role.ADMIN.name())));
    }
    public MvcResult getAvailableArticles() throws Exception {
        return sendRequest(get("/stock/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie(new Cookie(ConnectionController.COOKIE_KEY, Role.CLIENT.name())));
    }
    public MvcResult disconnect(Cookie cookie) {
        return sendRequest(get("/disconnect").cookie(cookie), status().is3xxRedirection());
    }

    @SneakyThrows
    public MvcResult addStock(Long articleId, Long quantity) {
        return sendRequest(post("/stock/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie(new Cookie(ConnectionController.COOKIE_KEY, Role.ADMIN.name()))
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("articleId", articleId.toString()),
                        new BasicNameValuePair("quantity", quantity.toString())
                )))));
    }

    @SneakyThrows
    public MvcResult returnArticleFromOrder(Long orderId, Long articleId) {
        return sendRequest(get(String.format("/order/%s/return/%s", orderId, articleId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie(new Cookie(ConnectionController.COOKIE_KEY, Role.EMPLOYEE.name())));
    }

    @SneakyThrows
    public MvcResult sendRequest(MockHttpServletRequestBuilder operation, ResultMatcher expectedResult) {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
        return this.mockMvc.perform(
                        operation
                                .characterEncoding("utf-8"))
                .andExpect(expectedResult)
                .andReturn();
    }

    public MvcResult sendRequest(MockHttpServletRequestBuilder operation)
            throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
        return this.mockMvc.perform(
                        operation
                                .characterEncoding("utf-8"))
                .andReturn();
    }

}
